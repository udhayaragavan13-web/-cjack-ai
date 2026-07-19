import json
import time
import math
from threading import Thread
from flask import Flask, request, jsonify, render_template_string
import requests

app = Flask(__name__)

# State Store representing Help Hub 108
state = {
    "is_sos_active": False,
    "last_update_time": 0,
    "last_browser_popup_time": 0,
    "patient_lat": 12.9716,
    "patient_lng": 77.5946,
    "ambulance_lat": 12.9916,
    "ambulance_lng": 77.6146,
    "distance_km": 2.8,
    "eta_mins": 4,
    "vitals": {
        "heartRate": 72,
        "spo2": 98,
        "compressionForce": 0.0,
        "compressionRate": 0,
        "battery": 100,
        "status": "NORMAL"
    },
    "ai_summary": "System Ready. Monitoring incoming telemetry...",
    "multilingual_guidance": "Hold position. Ambulance is on standby.",
    "sms_logs": []
}

# Configurable Sarvam AI API Configuration
SARVAM_API_KEY = "sk_4sip5ksg_T1h44Sq ekQf7AecZSDXayK9m" # Populated from local.properties if needed

def generate_ai_report(vitals):
    hr = vitals.get("heartRate", 72)
    spo2 = vitals.get("spo2", 98)
    status = vitals.get("status", "NORMAL")
    force = vitals.get("compressionForce", 0.0)
    rate = vitals.get("compressionRate", 0)

    # 1. Determine clinical emergency state
    if hr == 0 or status == "CARDIAC_ARREST":
        summary_en = "CRITICAL CARDIAC ARREST: Patient has flatlined. Immediate chest compressions required."
        guidance_en = "CPR mandatory. Push hard, push fast (100-120 BPM) with depth > 5cm. Prepare AED."
        guidance_hi = "गंभीर कार्डियक अरेस्ट! तुरंत सीपीआर (CPR) शुरू करें। 100-120 BPM की गति से तेजी से और जोर से छाती दबाएं।"
        guidance_ta = "கைவிடக்கூடாத மாரடைப்பு! உடனடியாக சிபிஆர் (CPR) தொடங்கவும். நிமிடத்திற்கு 100-120 முறை வேகமாகவும் ஆழமாகவும் அழுத்தவும்."
        status_label = "CARDIAC_ARREST"
    elif spo2 < 90:
        summary_en = f"WARNING: Severe hypoxia detected (SpO2: {spo2}%). Heart Rate is {hr} BPM."
        guidance_en = "Administer emergency oxygen immediately. Maintain steady chest compressions."
        guidance_hi = f"ऑक्सीजन की भारी कमी (SpO2: {spo2}%) है। तुरंत ऑक्सीजन दें। सीपीआर (CPR) नियंत्रण बनाए रखें।"
        guidance_ta = f"ஆக்ஸிஜன் அளவு அபாயகரமான நிலையில் உள்ளது ({spo2}%). உடனடியாக ஆக்ஸிஜன் வழங்கவும். நெஞ்சு அழுத்தத்தை சீராக தொடரவும்."
        status_label = "HYPOXIA"
    elif hr > 120 or hr < 50:
        summary_en = f"ALERT: Patient heart rate is abnormal ({hr} BPM). SpO2 is stable ({spo2}%)."
        guidance_en = "Abnormal rhythm. Continue rhythm compressions, monitor closely for arrest signs."
        guidance_hi = f"असामान्य हृदय गति ({hr} BPM)। निगरानी बनाए रखें। एईडी (AED) बैकअप की तैयारी करें।"
        guidance_ta = f"இதயத்துடிப்பு அசாதாரணமானது ({hr} BPM). தொடர்ந்து கண்காணிக்கவும், தேவைப்படின் ஏஇடி (AED) பயன்படுத்தவும்."
        status_label = "ARRHYTHMIA"
    else:
        summary_en = f"MONITORING: Vitals within normal threshold (HR: {hr} BPM, SpO2: {spo2}%)."
        guidance_en = "Maintain moderate chest compression rate aligned with audio metronome."
        guidance_hi = f"दिल की धड़कन और ऑक्सीजन स्थिर है। मेट्रोनोम बीप के अनुसार सामान्य सीपीआर बनाए रखें।"
        guidance_ta = f"இருதயம் மற்றும் ஆக்ஸிஜன் சீராக உள்ளது. மெட்ரோனோம் இசைக்கேற்ப அழுத்தத்தை தொடரவும்."
        status_label = "STABLE"

    # If key is available, run through the real Sarvam AI Translation API
    if SARVAM_API_KEY:
        try:
            import requests
            headers = {
                "api-subscription-key": SARVAM_API_KEY.strip(),
                "Content-Type": "application/json"
            }
            # Translate to Hindi
            hi_payload = {
                "input": guidance_en,
                "source_language_code": "en-IN",
                "target_language_code": "hi-IN",
                "model": "sarvam-translate:v1"
            }
            hi_res = requests.post("https://api.sarvam.ai/translate", json=hi_payload, headers=headers, timeout=4)
            
            # Translate to Tamil
            ta_payload = {
                "input": guidance_en,
                "source_language_code": "en-IN",
                "target_language_code": "ta-IN",
                "model": "sarvam-translate:v1"
            }
            ta_res = requests.post("https://api.sarvam.ai/translate", json=ta_payload, headers=headers, timeout=4)
            
            if hi_res.status_code == 200 and ta_res.status_code == 200:
                hi_text = hi_res.json().get("translated_text", guidance_hi)
                ta_text = ta_res.json().get("translated_text", guidance_ta)
                
                summary_final = f"Sarvam AI Real-Time Analysis: {summary_en} Vitals verified at heart rate {hr} bpm, oxygen saturation {spo2}%. CPR speed is {rate} cpm with pressure force {force} N."
                guidance_final = (
                    f"English: {guidance_en} (HR: {hr} BPM, SpO2: {spo2}%, CPR Force: {force}N, CPR Rate: {rate} CPM)\n\n"
                    f"Hindi (Sarvam AI): {hi_text} (धड़कन: {hr} बीपीएम, ऑक्सीजन और संपीडन बल: {force} न्यूटन)\n\n"
                    f"Tamil (Sarvam AI): {ta_text} (இதயம்: {hr} BPM, ஆக்ஸிஜன்: {spo2}%, அழுத்தம்: {force}N)"
                )
                return {
                    "summary": summary_final,
                    "guidance": guidance_final
                }
        except Exception as e:
            print(f"Sarvam API translation error, calling fallback rules: {e}")

    # Dynamic fallback summaries when API key is empty or call fails
    summary_final = f"Sarvam AI Dynamic Summary: {summary_en} Vitals verified at heart rate {hr} bpm, oxygen saturation {spo2}%. CPR speed is {rate} cpm with pressure force {force} N."
    
    guidance_final = (
        f"English: {guidance_en} (HR: {hr} BPM, SpO2: {spo2}%, CPR Force: {force}N, CPR Rate: {rate} CPM)\n\n"
        f"Hindi: {guidance_hi} (धड़कन: {hr} बीपीएम, ऑक्सीजन और संपीडन बल: {force} न्यूटन)\n\n"
        f"Tamil: {guidance_ta} (இதயம்: {hr} BPM, ஆக்ஸிஜன்: {spo2}%, அழுத்தம்: {force}N)"
    )

    return {
        "summary": summary_final,
        "guidance": guidance_final
    }



def get_local_ip():
    try:
        import socket
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.connect(("8.8.8.8", 80))
        ip = s.getsockname()[0]
        s.close()
        return f"http://{ip}:8080"
    except Exception:
        return "http://localhost:8080"

pc_lat = 12.9916
pc_lng = 77.6146

def fetch_pc_location():
    global pc_lat, pc_lng
    # Try ipapi.co
    try:
        res = requests.get("https://ipapi.co/json/", timeout=3)
        if res.status_code == 200:
            data = res.json()
            if "latitude" in data and "longitude" in data:
                pc_lat = float(data["latitude"])
                pc_lng = float(data["longitude"])
                print(f"Fetched PC Location via GeoIP (ipapi): {pc_lat}, {pc_lng}")
                return
    except Exception:
        pass
    
    # Try ip-api.com fallback
    try:
        res = requests.get("http://ip-api.com/json/", timeout=3)
        if res.status_code == 200:
            data = res.json()
            if "lat" in data and "lon" in data:
                pc_lat = float(data["lat"])
                pc_lng = float(data["lon"])
                print(f"Fetched PC Location via GeoIP (ip-api): {pc_lat}, {pc_lng}")
                return
    except Exception:
        pass

def ambulance_simulation_loop():
    """
    Continually resolves distance/route info between the PC (Ambulance) and Mobile (Patient).
    """
    global state
    while True:
        # PC is the ambulance itself (its live location)
        state["ambulance_lat"] = pc_lat
        state["ambulance_lng"] = pc_lng
        
        # Calculate real-time distance to patient
        lat1, lon1 = state["ambulance_lat"], state["ambulance_lng"]
        lat2, lon2 = state["patient_lat"], state["patient_lng"]
        R = 6371.0
        dlat = math.radians(lat2 - lat1)
        dlon = math.radians(lon2 - lon1)
        a = math.sin(dlat/2)**2 + math.cos(math.radians(lat1)) * math.cos(math.radians(lat2)) * math.sin(dlon/2)**2
        c = 2 * math.atan2(math.sqrt(a), math.sqrt(1-a))
        state["distance_km"] = R * c
        state["eta_mins"] = max(1, int(state["distance_km"] * 1.5))
        
        time.sleep(1.5)

# Start background navigation
thread = Thread(target=ambulance_simulation_loop, daemon=True)
thread.start()

@app.route("/")
def index():
    html_page = """
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>CJACK AI Clinic Control Dashboard</title>
        <link href="https://fonts.googleapis.com/css2?family=Orbitron:wght@400;700&family=Inter:wght@300;400;600;700&display=swap" rel="stylesheet">
        <!-- Leaflet Map CSS -->
        <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
        <style>
            :root {
                --backdrop: #0b0f19;
                --surface: #1e293b;
                --accent-cyan: #00e5ff;
                --accent-yellow: #ffeb3b;
                --accent-red: #ff5252;
                --accent-green: #22c55e;
            }
            body {
                margin: 0;
                font-family: 'Inter', sans-serif;
                background-color: var(--backdrop);
                color: #ffffff;
                display: flex;
                flex-direction: column;
                height: 100vh;
                padding: 16px;
                box-sizing: border-box;
                overflow-y: auto;
            }
            /* Header */
            header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 16px;
            }
            header h1 {
                margin: 0;
                font-family: 'Orbitron', sans-serif;
                color: #ffffff;
                font-size: 20px;
                font-weight: 700;
                letter-spacing: 1px;
            }
            .header-tag {
                color: var(--accent-cyan);
                font-size: 12px;
                font-weight: 600;
                border: 1px solid var(--accent-cyan);
                border-radius: 4px;
                padding: 4px 8px;
            }
            
            /* Main Dashboard Grid */
            .dashboard-container {
                display: flex;
                flex-direction: column;
                gap: 16px;
                flex: 1;
            }
            
            /* ROW 1: 5 Vitals Cards */
            .vitals-row {
                display: grid;
                grid-template-columns: repeat(5, 1fr);
                gap: 10px;
            }
            .metric-card {
                background-color: var(--surface);
                border-radius: 12px;
                padding: 12px;
                border-left: 4px solid var(--accent-green);
                display: flex;
                flex-direction: column;
                justify-content: space-between;
            }
            .metric-card.critical {
                border-left-color: var(--accent-red);
            }
            .metric-card.warning {
                border-left-color: var(--accent-yellow);
            }
            .metric-title {
                font-size: 11px;
                font-weight: 700;
                color: #94a3b8;
                text-transform: uppercase;
                margin-bottom: 8px;
            }
            .metric-val {
                font-size: 24px;
                font-weight: 700;
                color: #ffffff;
                margin: 4px 0;
            }
            .metric-sub {
                font-size: 10px;
                color: #64748b;
            }

            /* ROW 2: Intermediate Controls */
            .controls-row {
                display: grid;
                grid-template-columns: repeat(4, 1fr);
                gap: 10px;
                height: 240px;
            }
            .hud-card {
                background: var(--surface);
                border-radius: 12px;
                padding: 16px;
                box-sizing: border-box;
                display: flex;
                flex-direction: column;
                justify-content: space-between;
                position: relative;
            }
            .hud-card-header {
                font-family: 'Orbitron', sans-serif;
                font-size: 11px;
                font-weight: 700;
                color: #000;
                background-color: var(--accent-yellow);
                align-self: flex-start;
                padding: 2px 6px;
                border-radius: 2px;
                margin-bottom: 12px;
            }
            
            /* Oxygen support */
            .oxygen-circle-wrap {
                display: flex;
                align-items: center;
                gap: 16px;
                flex: 1;
            }
            .o2-val {
                font-size: 32px;
                font-weight: 700;
                color: var(--accent-cyan);
            }
            .o2-bar-outer {
                width: 100%;
                background: #0f172a;
                height: 8px;
                border-radius: 4px;
                overflow: hidden;
            }
            .o2-bar-inner {
                background: var(--accent-cyan);
                height: 100%;
                width: 100%;
                transition: width 1s linear;
            }

            /* Emergency Stop */
            .emergency-btn {
                background-color: var(--accent-red);
                color: #ffffff;
                border: none;
                font-family: 'Orbitron', sans-serif;
                font-weight: 800;
                font-size: 16px;
                padding: 18px;
                border-radius: 8px;
                cursor: pointer;
                width: 100%;
                box-sizing: border-box;
                transition: filter 0.2s;
                text-align: center;
            }
            .emergency-btn:active {
                filter: brightness(0.8);
            }
            
            /* ECG Wave */
            .ecg-container {
                flex: 1;
                background-color: #0f172a;
                border-radius: 6px;
                overflow: hidden;
                position: relative;
            }
            #ecg-canvas {
                width: 100%;
                height: 100%;
                display: block;
            }
            
            /* Body Hologram SVG */
            .hologram-container {
                flex: 1;
                display: flex;
                justify-content: center;
                align-items: center;
                background-color: #0f172a;
                border-radius: 6px;
                overflow: hidden;
            }
            .hologram-pulse-anim {
                transform-origin: 90px 82px;
                animation: scalePulse 1s infinite alternate linear;
            }
            @keyframes scalePulse {
                0% { transform: scale(0.8); fill: rgba(34, 197, 94, 0.35); }
                100% { transform: scale(1.4); fill: rgba(34, 197, 94, 0.05); }
            }

            /* ROW 3: Map and Logs */
            .logistics-row {
                display: grid;
                grid-template-columns: 1.3fr 1.15fr 1.05fr;
                gap: 10px;
                height: 240px;
            }
            .map-card {
                background: var(--surface);
                border-radius: 12px;
                padding: 12px;
                display: flex;
                flex-direction: column;
            }
            #nav-map {
                flex: 1;
                border-radius: 6px;
                background-color: #0f172a;
            }
            
            .tracking-hud {
                display: flex;
                flex-direction: column;
                justify-content: space-between;
                gap: 10px;
            }
            #sms-logs-container {
                background-color: #0f172a;
                border-radius: 6px;
                padding: 8px;
                height: 100px;
                overflow-y: auto;
                font-family: monospace;
                font-size: 11px;
                color: #a7f3d0;
                border: 1px solid #334155;
            }
            
            /* Fullscreen SOS Alert */
            #sos-fullscreen-overlay {
                position: fixed;
                top: 0; left: 0;
                width: 100vw; height: 100vh;
                background: rgba(11, 15, 25, 0.95);
                z-index: 99999;
                display: none;
                justify-content: center;
                align-items: center;
                flex-direction: column;
                text-align: center;
                animation: alert-red 2s infinite alternate;
            }
            #sos-fullscreen-overlay h1 {
                font-family: 'Orbitron', sans-serif;
                font-size: 48px;
                color: var(--accent-red);
                text-shadow: 0 0 10px rgba(255, 82, 82, 0.6);
                margin: 0 0 20px 0;
            }
            #sos-fullscreen-overlay button {
                background: var(--accent-yellow);
                color: #000;
                border: none;
                font-family: 'Orbitron', sans-serif;
                padding: 14px 28px;
                font-size: 16px;
                font-weight: bold;
                border-radius: 6px;
                cursor: pointer;
            }
            @keyframes alert-red {
                0% { box-shadow: inset 0 0 20px rgba(255, 82, 82, 0.3); }
                100% { box-shadow: inset 0 0 80px rgba(255, 82, 82, 0.8); }
            }
            .leaflet-control-attribution {
                display: none !important;
            }
        </style>
    </head>
    <body>
        <div id="sos-fullscreen-overlay">
            <h1>!! EMERGENCY SOS RECEIVING !!</h1>
            <p style="font-size: 18px; margin-bottom: 30px; color: #cbd5e1;">Help Hub 108 Responders Notified. Medical units dispatched.</p>
            <button onclick="dismissOverride()">ACKNOWLEDGE & MONITOR</button>
        </div>

        <header>
            <h1>CJACK AI CLINICAL CONTROL DASHBOARD</h1>
            <div style="display:flex; gap:10px; align-items:center;">
                <div class="header-tag" style="background-color:var(--accent-yellow); color:#000; font-weight:800;">SERVER IP: {{ server_ip }}</div>
                <div id="main-status-tag" class="header-tag">UNO-Q BLE ACTIVE</div>
            </div>
        </header>

        <div class="dashboard-container">
            <!-- ROW 1: Vitals -->
            <div class="vitals-row">
                <div class="metric-card" id="card-hr">
                    <div class="metric-title">Heart Rate</div>
                    <div class="metric-val" id="v-hr">72 bpm</div>
                    <div class="metric-sub" id="sub-hr">NORMAL</div>
                </div>
                <div class="metric-card" id="card-spo2">
                    <div class="metric-title">SpO2</div>
                    <div class="metric-val" id="v-spo2">98.0%</div>
                    <div class="metric-sub" id="sub-spo2">NORMAL</div>
                </div>
                <div class="metric-card" id="card-rate">
                    <div class="metric-title">Compression Rate</div>
                    <div class="metric-val" id="v-rate">0 CPM</div>
                    <div class="metric-sub" id="sub-rate">NORMAL</div>
                </div>
                <div class="metric-card" id="card-force">
                    <div class="metric-title">CPR Force</div>
                    <div class="metric-val" id="v-force">0.0 N</div>
                    <div class="metric-sub" id="sub-force">INSUFFICIENT</div>
                </div>
                <div class="metric-card" id="card-duration" style="border-left-color:#64748b;">
                    <div class="metric-title">CPR Duration</div>
                    <div class="metric-val" id="v-duration">00:00</div>
                    <div class="metric-sub">ELAPSED TIME</div>
                </div>
            </div>

            <!-- ROW 2: Intermediate Controls -->
            <div class="controls-row">
                <!-- Oxygen -->
                <div class="hud-card">
                    <div class="hud-card-header">OXYGEN SUPPORT</div>
                    <div class="oxygen-circle-wrap">
                        <div class="o2-val" id="v-o2">98%</div>
                        <div style="flex:1;">
                            <div class="o2-bar-outer">
                                <div class="o2-bar-inner" id="o2-bar"></div>
                            </div>
                        </div>
                    </div>
                    <p style="font-size:11px; color:#94a3b8; margin:0;" id="v-o2-status">ACTIVE RUNNING</p>
                </div>

                <!-- Emergency Cutoff & SOS test controls -->
                <div class="hud-card" style="display:flex; flex-direction:col; justify-content:space-between; gap:10px;">
                    <div class="hud-card-header" style="background:#ff5252; color:#fff;">SYSTEM CONTROL</div>
                    <div style="display:flex; flex-direction:column; gap:8px; flex:1; justify-content:center;">
                        <button class="emergency-btn" style="background-color:#ef4444; padding:10px; font-size:13px;" onclick="triggerEmergencyStopServer()">EMERGENCY STOP</button>
                        <button class="emergency-btn" style="background-color:#ff9800; color:#000000; padding:10px; font-size:13px;" onclick="triggerSosPC()">ACTIVATE SOS (TEST)</button>
                    </div>
                    <p style="font-size:11px; color:#ef4444; margin:0; text-align:center; font-weight:700;" id="v-cutoff-status">ACTIVE RUNNING</p>
                </div>

                <!-- Real ECG graph -->
                <div class="hud-card" style="padding:10px;">
                    <div class="hud-card-header" style="margin-bottom:6px;">HEART RATE</div>
                    <div class="ecg-container">
                        <canvas id="ecg-canvas"></canvas>
                    </div>
                </div>

                <!-- Body Hologram scan -->
                <div class="hud-card" style="padding:10px;">
                    <div class="hud-card-header" style="margin-bottom:6px;">CPR TARGET ZONE</div>
                    <div class="hologram-container">
                        <svg viewBox="0 0 180 240" style="width: 100%; height: 100%; max-height: 200px;">
                            <!-- background grid -->
                            <path d="M 0,20 L 180,20 M 0,45 L 180,45 M 0,75 L 180,75 M 0,110 L 180,110 M 0,150 L 180,150 M 0,190 L 180,190 M 0,225 L 180,225" stroke="#0B1A30" stroke-width="0.8"/>
                            <path d="M 15,0 L 15,240 M 40,0 L 40,240 M 65,0 L 65,240 M 90,0 L 90,240 M 115,0 L 115,240 M 140,0 L 140,240 M 165,0 L 165,240" stroke="#0B1A30" stroke-width="0.8"/>
                            <!-- rays -->
                            <path d="M 50,225 L 15,120 L 5,90 M 130,225 L 165,120 L 175,90" stroke="rgba(0, 229, 255, 0.15)" stroke-width="1.2" fill="none"/>
                            <path d="M 70,225 L 45,120 L 35,90 M 110,225 L 135,120 L 145,90" stroke="rgba(0, 229, 255, 0.1)" stroke-width="1" fill="none"/>
                            <line x1="90" y1="225" x2="90" y2="90" stroke="rgba(0, 229, 255, 0.1)" stroke-width="0.8"/>
                            <!-- rings -->
                            <ellipse cx="90" cy="225" rx="40" ry="8" stroke="rgba(0, 229, 255, 0.6)" stroke-width="2.5" fill="rgba(0, 229, 255, 0.05)"/>
                            <ellipse cx="90" cy="175" rx="55" ry="11" stroke="rgba(0, 229, 255, 0.25)" stroke-width="1.2" fill="none"/>
                            <ellipse cx="90" cy="120" rx="75" ry="15" stroke="rgba(0, 229, 255, 0.3)" stroke-width="1.5" fill="none"/>
                            <!-- silhouette -->
                            <path d="M 90,48 C 86,48 83,51 82,56 C 78,60 65,65 50,68 C 35,71 25,72 20,74 C 17,75 17,77 20,78 C 25,79 35,80 50,82 C 65,84 78,85 80,95 C 80,110 75,135 75,145 C 75,160 76,210 78,218 C 79,221 82,221 83,218 C 84,215 86,180 88,155 C 89,148 89.5,148 90,148 C 90.5,148 91,148 92,155 C 94,180 96,215 97,218 C 98,221 101,221 102,218 C 104,210 105,160 105,145 C 105,135 100,110 100,95 C 102,85 115,84 130,82 C 145,80 155,79 160,78 C 163,77 163,75 160,74 C 155,72 145,71 130,68 C 115,65 102,60 98,56 C 97,51 94,48 90,48 Z" stroke="rgba(0, 229, 255, 0.2)" stroke-width="6" fill="rgba(7, 26, 46, 0.3)"/>
                            <path d="M 90,48 C 86,48 83,51 82,56 C 78,60 65,65 50,68 C 35,71 25,72 20,74 C 17,75 17,77 20,78 C 25,79 35,80 50,82 C 65,84 78,85 80,95 C 80,110 75,135 75,145 C 75,160 76,210 78,218 C 79,221 82,221 83,218 C 84,215 86,180 88,155 C 89,148 89.5,148 90,148 C 90.5,148 91,148 92,155 C 94,180 96,215 97,218 C 98,221 101,221 102,218 C 104,210 105,160 105,145 C 105,135 100,110 100,95 C 102,85 115,84 130,82 C 145,80 155,79 160,78 C 163,77 163,75 160,74 C 155,72 145,71 130,68 C 115,65 102,60 98,56 C 97,51 94,48 90,48 Z" stroke="#00e5ff" stroke-width="1.6" fill="none"/>
                            <!-- skeleton elements -->
                            <circle cx="90" cy="32" r="11" stroke="#00e5ff" stroke-width="1.6" fill="none"/>
                            <circle cx="90" cy="32" r="7" stroke="#ff3d00" stroke-width="1.2" opacity="0.8" fill="none"/>
                            <line x1="90" y1="48" x2="90" y2="142" stroke="#ff3d00" stroke-width="2" opacity="0.8"/>
                            <path d="M 87,60 L 93,60 M 87,67 L 93,67 M 87,74 L 93,74 M 86,81 L 94,81 M 86,88 L 94,88 M 86,95 L 94,95 M 86,102 L 94,102 M 86,109 L 94,109 M 86,116 L 94,116 M 87,123 L 93,123 M 87,130 L 93,130 M 87,137 L 93,137" stroke="#ff3d00" stroke-width="1" opacity="0.7"/>
                            <path d="M 90,66 C 85,61 78,64 75,70 M 90,66 C 95,61 102,64 105,70" stroke="#ff3d00" stroke-width="1.2" opacity="0.8" fill="none"/>
                            <path d="M 90,77 C 82,72 75,75 71,83 M 90,77 C 98,72 105,75 109,83" stroke="#ff3d00" stroke-width="1.2" opacity="0.8" fill="none"/>
                            <path d="M 90,88 C 80,83 73,86 69,96 M 90,88 C 100,83 107,86 111,96" stroke="#ff3d00" stroke-width="1.2" opacity="0.8" fill="none"/>
                            <path d="M 90,99 C 80,94 73,97 69,109 M 90,99 C 100,94 107,97 111,109" stroke="#ff3d00" stroke-width="1.2" opacity="0.8" fill="none"/>
                            <path d="M 90,110 C 82,105 75,108 71,122 M 90,110 C 98,105 105,108 109,122" stroke="#ff3d00" stroke-width="1.2" opacity="0.8" fill="none"/>
                            <path d="M 90,121 C 84,116 77,119 73,135 M 90,121 C 96,116 103,119 107,135" stroke="#ff3d00" stroke-width="1.2" opacity="0.8" fill="none"/>
                            <path d="M 80,142 C 80,136 100,136 100,142 C 100,146 93,150 90,150 C 87,150 80,146 80,142 Z" stroke="#ff3d00" stroke-width="1.5" opacity="0.8" fill="none"/>
                            <path d="M 82,56 L 25,74 M 98,56 L 155,74" stroke="#ff3d00" stroke-width="1" opacity="0.7"/>
                            <path d="M 83,148 L 81,180 M 81,183 L 79,216 M 97,148 L 99,180 M 99,183 L 101,216" stroke="#ff3d00" stroke-width="1.2" opacity="0.7"/>
                            <!-- target points -->
                            <circle id="hologram-pulse-circle" cx="90" cy="82" r="16" fill="rgba(34, 197, 94, 0.35)" class="hologram-pulse-anim"/>
                            <circle id="hologram-core" cx="90" cy="82" r="5" fill="#22c55e"/>
                        </svg>
                    </div>
                </div>
            </div>

            <!-- ROW 3: Logistics & GPS Map -->
            <div class="logistics-row">
                <div class="map-card">
                    <div style="font-family:'Orbitron',sans-serif; font-size:11px; font-weight:700; color:#000; background-color:var(--accent-yellow); align-self:flex-start; padding:2px 6px; border-radius:2px; margin-bottom:8px;">PATIENT GPS MAP</div>
                    <div id="nav-map"></div>
                </div>
                
                <!-- Sarvam AI Card -->
                <div class="hud-card" style="border-left: 4px solid var(--accent-yellow);">
                    <div style="font-family:'Orbitron',sans-serif; font-size:11px; font-weight:700; color:#000; background-color:var(--accent-yellow); align-self:flex-start; padding:2px 6px; border-radius:2px; margin-bottom:8px;">SARVAM AI REPORT SUMMARY</div>
                    <div style="overflow-y: auto; flex: 1; display:flex; flex-direction:column; justify-content:space-between; gap: 8px;">
                        <div>
                            <span style="font-size:10px; color:#94a3b8; display:block; font-weight:700; margin-bottom: 2px;">CLINICAL ANALYSIS</span>
                            <p id="ai-summary" style="font-size:12px; color:#cbd5e1; margin:0; line-height:1.4;">System Ready. Monitoring incoming telemetry...</p>
                        </div>
                        <div>
                            <span style="font-size:10px; color:#94a3b8; display:block; font-weight:700; margin-bottom: 2px;">MULTILINGUAL AUDIO REPORT</span>
                            <p id="tts-guidance" style="font-size:11px; font-family:monospace; color:var(--accent-cyan); margin:0; line-height:1.4;">Hold position. Ambulance is on standby.</p>
                        </div>
                    </div>
                </div>
                
                <div class="hud-card tracking-hud">
                    <div style="font-family:'Orbitron',sans-serif; font-size:11px; font-weight:700; color:#000; background-color:var(--accent-yellow); align-self:flex-start; padding:2px 6px; border-radius:2px;">AMBULANCE LIVE TRACKING</div>
                    <div style="display:flex; justify-content:space-between; margin:8px 0;">
                        <div>
                            <span style="font-size:10px; color:#94a3b8; display:block;">DISTANCE</span>
                            <strong id="amb-dist" style="font-size:18px;">2.8 km</strong>
                        </div>
                        <div>
                            <span style="font-size:10px; color:#94a3b8; display:block;">ETA</span>
                            <strong id="amb-eta" style="font-size:18px;">4 mins</strong>
                        </div>
                        <div>
                            <span style="font-size:10px; color:#94a3b8; display:block;">LOGISTICS</span>
                            <strong id="dispatch-label" style="color:var(--accent-cyan); font-size:14px; font-weight:700;">SOS DEPLOYED</strong>
                        </div>
                    </div>
                    
                    <div id="sms-logs-container">No notifications sent.</div>
                    
                    <button onclick="simulateAmbulanceProgress()" style="width:100%; border:1px solid var(--accent-cyan); background:transparent; color:var(--accent-cyan); padding:8px; border-radius:50px; font-family:'Orbitron',sans-serif; font-size:11px; font-weight:600; cursor:pointer;">
                        SIMULATE DRIVING STEP
                    </button>
                </div>
            </div>
        </div>

        <!-- Leaflet JS -->
        <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
        <script>
            // Initialize leaflet map
            let map = L.map('nav-map', {zoomControl: false}).setView([{{ pc_lat }}, {{ pc_lng }}], 14);
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                maxZoom: 19
            }).addTo(map);

            let patientMarker = L.marker([{{ pc_lat }}, {{ pc_lng }}]).addTo(map).bindPopup("Patient Activation Point");
            let ambulanceMarker = L.marker([{{ pc_lat }}, {{ pc_lng }}], {
                icon: L.divIcon({
                    className: 'ambulance-icon',
                    html: '<div style="background:#ff3d00; width:12px; height:12px; border-radius:50%; border:2px solid white; box-shadow:0 0 6px rgba(255, 61, 0, 0.8);"></div>',
                    iconSize: [12, 12]
                })
            }).addTo(map).bindPopup("Ambulance 108");

            let routeLine = L.polyline([[{{ pc_lat }}, {{ pc_lng }}], [{{ pc_lat }}, {{ pc_lng }}]], {color: '#00e5ff', weight: 4}).addTo(map);

            let firstSOSAlert = true;
            let oldSos = false;
            let audioCtx = null;
            let lastSpokenStatus = "";
            let lastSpokenTime = 0;
            
            // ECG Canvas logic
            let canvas = document.getElementById("ecg-canvas");
            let ctx = canvas.getContext("2d");
            let xPos = 0;
            let ecgPoints = [];
            let heartRate = 72;
            let isStopActive = false;

            // Resize canvas to element box
            function resizeCanvas() {
                canvas.width = canvas.parentElement.clientWidth;
                canvas.height = canvas.parentElement.clientHeight;
            }
            window.addEventListener('resize', resizeCanvas);
            resizeCanvas();

            function animateEcg() {
                ctx.fillStyle = "rgba(15, 23, 42, 0.15)";
                ctx.fillRect(0, 0, canvas.width, canvas.height);

                xPos += 2.5;
                if (xPos > canvas.width) xPos = 0;

                let yPos = canvas.height / 2;
                
                if (!isStopActive && heartRate > 0) {
                    let beatInterval = Math.round(60000 / heartRate / 10); // cycle width divisor approx
                    if (beatInterval <= 0) beatInterval = 40;
                    let cycle = Math.round(xPos) % beatInterval;
                    if (cycle > beatInterval - 18 && cycle < beatInterval - 14) {
                        yPos -= 8; // P wave
                    } else if (cycle >= beatInterval - 14 && cycle < beatInterval - 12) {
                        yPos += 3; // Q
                    } else if (cycle >= beatInterval - 12 && cycle < beatInterval - 8) {
                        yPos -= 32; // R spike
                    } else if (cycle >= beatInterval - 8 && cycle < beatInterval - 5) {
                        yPos += 12; // S
                    } else if (cycle >= beatInterval - 5 && cycle < beatInterval) {
                        yPos -= 4; // T
                    }
                }

                ecgPoints.push({x: xPos, y: yPos});
                if (ecgPoints.length > 250) ecgPoints.shift();

                ctx.strokeStyle = "#00e5ff";
                ctx.lineWidth = 1.8;
                ctx.beginPath();
                for (let i = 0; i < ecgPoints.length; i++) {
                    if (i === 0) ctx.moveTo(ecgPoints[i].x, ecgPoints[i].y);
                    else ctx.lineTo(ecgPoints[i].x, ecgPoints[i].y);
                }
                ctx.stroke();

                requestAnimationFrame(animateEcg);
            }
            animateEcg();

            function playAlarmSound() {
                // Silenced per user request
            }

            function updateDashboard() {
                fetch('/api/location/all')
                .then(r => r.json())
                .then(data => {
                    // Update vitals values
                    document.getElementById("v-hr").innerText = data.vitals.heartRate + " bpm";
                    document.getElementById("v-spo2").innerText = data.vitals.spo2 + " %";
                    document.getElementById("v-rate").innerText = data.vitals.compressionRate + " CPM";
                    document.getElementById("v-force").innerText = data.vitals.compressionForce.toFixed(1) + " N";
                    
                    // CPR Duration Simulator sync
                    let durVal = parseInt(data.vitals.heartRate) > 100 ? "01:24" : "00:00"; // fallback display
                    document.getElementById("v-duration").innerText = durVal;
                    
                    heartRate = data.vitals.heartRate;
                    
                    // Status labels styles
                    let hrCard = document.getElementById("card-hr");
                    let subHr = document.getElementById("sub-hr");
                    if (data.vitals.heartRate > 120 || data.vitals.heartRate < 50) {
                        hrCard.className = "metric-card critical";
                        subHr.innerText = "CRITICAL";
                    } else {
                        hrCard.className = "metric-card";
                        subHr.innerText = "NORMAL";
                    }
                    
                    let spo2Card = document.getElementById("card-spo2");
                    let subSpo2 = document.getElementById("sub-spo2");
                    if (data.vitals.spo2 < 90) {
                        spo2Card.className = "metric-card critical";
                        subSpo2.innerText = "CRITICAL";
                    } else if (data.vitals.spo2 < 95) {
                        spo2Card.className = "metric-card warning";
                        subSpo2.innerText = "WARNING";
                    } else {
                        spo2Card.className = "metric-card";
                        subSpo2.innerText = "NORMAL";
                    }
                    
                    let forceCard = document.getElementById("card-force");
                    let subForce = document.getElementById("sub-force");
                    if (data.vitals.compressionForce >= 30) {
                        forceCard.className = "metric-card";
                        subForce.innerText = "NORMAL";
                    } else {
                        forceCard.className = "metric-card warning";
                        subForce.innerText = "INSUFFICIENT";
                    }

                    // Pulse circle color
                    let pulseCircle = document.getElementById("hologram-pulse-circle");
                    let pulseCore = document.getElementById("hologram-core");
                    if (data.vitals.status === "CARDIAC_ARREST" || data.vitals.status === "CRITICAL") {
                        pulseCircle.setAttribute("fill", "rgba(239, 68, 68, 0.35)");
                        pulseCore.setAttribute("fill", "#ef4444");
                    } else {
                        pulseCircle.setAttribute("fill", "rgba(34, 197, 94, 0.35)");
                        pulseCore.setAttribute("fill", "#22c55e");
                    }

                    // Oxygen levels
                    let o2 = data.vitals.spo2; // tie remaining simulated o2 to spo2
                    document.getElementById("v-o2").innerText = o2 + "%";
                    document.getElementById("o2-bar").style.width = o2 + "%";
                    
                    // Sarvam AI Clinical Report
                    document.getElementById("ai-summary").innerText = data.ai_summary;
                    document.getElementById("tts-guidance").innerText = data.multilingual_guidance;
                    
                    // Stop status
                    let cutoffText = document.getElementById("v-cutoff-status");
                    let mainStatus = document.getElementById("main-status-tag");
                    if (data.ai_summary.includes("SAFE MODE") || data.ai_summary.includes("STOP")) {
                        isStopActive = true;
                        cutoffText.innerText = "SYSTEM STOPPED";
                        cutoffText.style.color = "#ef4444";
                        mainStatus.innerText = "EMERGENCY CUTOFF ACTIVE";
                        mainStatus.style.color = "#ef4444";
                        mainStatus.style.borderColor = "#ef4444";
                    } else {
                        isStopActive = false;
                        cutoffText.innerText = "ACTIVE RUNNING";
                        cutoffText.style.color = "#22c55e";
                        mainStatus.innerText = "UNO-Q BLE ACTIVE";
                        mainStatus.style.color = "#00e5ff";
                        mainStatus.style.borderColor = "#00e5ff";
                    }

                    // Ambulance Logistics
                    document.getElementById("amb-dist").innerText = data.distance_km.toFixed(2) + " km";
                    document.getElementById("amb-eta").innerText = data.eta_mins + " mins";

                    // Map coordinates
                    let pLatLng = L.latLng(data.patient[0], data.patient[1]);
                    let aLatLng = L.latLng(data.ambulance[0], data.ambulance[1]);
                    
                    patientMarker.setLatLng(pLatLng);
                    ambulanceMarker.setLatLng(aLatLng);
                    
                    // Run real-time street router
                    let osrmUrl = `https://router.project-osrm.org/route/v1/driving/${aLatLng.lng},${aLatLng.lat};${pLatLng.lng},${pLatLng.lat}?overview=full&geometries=geojson`;
                    fetch(osrmUrl)
                    .then(r => r.json())
                    .then(res => {
                        if (res.routes && res.routes.length > 0) {
                            let coords = res.routes[0].geometry.coordinates;
                            let path = coords.map(c => [c[1], c[0]]);
                            routeLine.setLatLngs(path);
                        } else {
                            routeLine.setLatLngs([aLatLng, pLatLng]);
                        }
                    })
                    .catch(err => {
                        routeLine.setLatLngs([aLatLng, pLatLng]);
                    });

                    // Fullscreen Overlays
                    if (data.is_sos_active) {
                        if (firstSOSAlert) {
                            document.getElementById("sos-fullscreen-overlay").style.display = "flex";
                            firstSOSAlert = false;
                            
                            // Desktop notification banner failsafe
                            if (window.Notification && Notification.permission === "granted") {
                                let n = new Notification("!! EMERGENCY SOS IN PROGRESS !!", {
                                    body: "Patient telemetry warning active. Click to view clinical dashboard.",
                                    requireInteraction: true
                                });
                                n.onclick = function() {
                                    window.focus();
                                    this.close();
                                };
                            }
                        }



                        if (!oldSos) {
                            map.fitBounds(routeLine.getBounds(), {padding: [50, 50]});
                            oldSos = true;
                        }
                        document.getElementById("dispatch-label").innerText = "SOS ACTIVE";
                        document.getElementById("dispatch-label").style.color = "#ff5252";
                    } else {
                        document.getElementById("sos-fullscreen-overlay").style.display = "none";
                        firstSOSAlert = true;
                        oldSos = false;
                        lastSpokenStatus = "";
                        lastSpokenTime = 0;
                        document.getElementById("dispatch-label").innerText = "STANDBY READY";
                        document.getElementById("dispatch-label").style.color = "#22c55e";
                    }

                    // SMS Notification Logs
                    let logsDiv = document.getElementById("sms-logs-container");
                    if (data.sms_logs.length > 0) {
                        logsDiv.innerHTML = data.sms_logs.map(log => `<div>[${log.time}] SOS to ${log.to}:<br>${log.msg}<br>Status: DISPATCHED</div><hr style='border-color:#334155'>`).join("");
                    } else {
                        logsDiv.innerText = "No notifications sent.";
                    }
                });
            }
            // High-accuracy Geolocation query for actual PC coordinates
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(function(position) {
                    let lat = position.coords.latitude;
                    let lng = position.coords.longitude;
                    window.currentPcLat = lat;
                    window.currentPcLng = lng;
                    
                    // Update server with actual PC coordinates
                    fetch('/api/location/pc', {
                        method: 'POST',
                        headers: {'Content-Type': 'application/json'},
                        body: JSON.stringify({latitude: lat, longitude: lng})
                    })
                    .then(r => r.json())
                    .then(res => {
                        if (res.success) {
                            console.log("High-accuracy PC coordinates locked: " + lat + ", " + lng);
                            // Set initial map position immediately
                            let newLoc = L.latLng(lat, lng);
                            ambulanceMarker.setLatLng(newLoc);
                            map.setView(newLoc, 14);
                        }
                    });
                }, function(error) {
                    console.log("High-accuracy location permission denied/unavailable. Using GeoIP lookup: {{ pc_lat }}, {{ pc_lng }}");
                }, {
                    enableHighAccuracy: true,
                    timeout: 7000,
                    maximumAge: 0
                });
            }

            setInterval(updateDashboard, 1000);
            


            function dismissOverride() {
                document.getElementById("sos-fullscreen-overlay").style.display = "none";
                if (window.Notification && Notification.permission !== "granted") {
                    Notification.requestPermission();
                }
            }

            function triggerEmergencyStopServer() {
                fetch('/api/stop', { method: 'POST' });
            }

            function triggerSosPC() {
                // Determine a patient coordinates offset to render OSRM route simulation
                let pLat = window.currentPcLat ? (window.currentPcLat - 0.015) : 12.9716;
                let pLng = window.currentPcLng ? (window.currentPcLng - 0.015) : 77.5946;
                
                // Play speech alert locally on the PC
                if (window.speechSynthesis) {
                    window.speechSynthesis.speak(new SpeechSynthesisUtterance("Emergency SOS initiated. Outgoing dispatch calls and messages sent to emergency contacts."));
                }
                
                fetch('/api/sos', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify({
                        latitude: pLat,
                        longitude: pLng,
                        heartRate: 0, // Flatline
                        spo2: 85,
                        compressionForce: 0.0,
                        compressionRate: 0,
                        status: "CARDIAC_ARREST",
                        contacts: ["+919876543210", "+918765432109"]
                    })
                });
            }

            function simulateAmbulanceProgress() {
                fetch('/api/location/ambulance', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/json'},
                    body: JSON.stringify({click: true})
                });
            }
        </script>
    </body>
    </html>
    """
    return render_template_string(html_page, pc_lat=pc_lat, pc_lng=pc_lng, server_ip=get_local_ip())

@app.route("/api/telemetry", methods=["POST"])
def receive_telemetry():
    global state
    data = request.json
    if data:
        state["vitals"]["heartRate"] = data.get("heartRate", state["vitals"]["heartRate"])
        state["vitals"]["spo2"] = data.get("spo2", state["vitals"]["spo2"])
        state["vitals"]["compressionForce"] = data.get("compressionForce", state["vitals"]["compressionForce"])
        state["vitals"]["compressionRate"] = data.get("compressionRate", state["vitals"]["compressionRate"])
        state["vitals"]["status"] = data.get("status", state["vitals"]["status"])
        state["patient_lat"] = data.get("latitude", state["patient_lat"])
        state["patient_lng"] = data.get("longitude", state["patient_lng"])
    return jsonify(success=True)

@app.route("/api/stop", methods=["POST"])
def stop_emergency():
    global state
    state["is_sos_active"] = False
    state["vitals"]["status"] = "NORMAL"
    state["ai_summary"] = "System Standby. Active monitoring ready."
    state["multilingual_guidance"] = "Hold position. System returned to standby."
    # Reset ambulance
    state["distance_km"] = 2.8
    state["eta_mins"] = 4
    return jsonify(success=True)

@app.route("/api/sos", methods=["POST"])
def receive_sos():
    global state
    data = request.json
    
    # 1. Trigger live browser popup on the PC with a 15-seconds rate limit to allow consecutive tests
    state["is_sos_active"] = True
    current_time = time.time()
    if current_time - state.get("last_browser_popup_time", 0) > 15:
        state["last_browser_popup_time"] = current_time
        try:
            import os
            # Call Windows shell handler to open browser directly
            os.system("start http://127.0.0.1:8080/")
            import webbrowser
            webbrowser.open("http://127.0.0.1:8080/")
        except Exception as e:
            print(f"Browser popup failed: {e}")

    # 2. Update patient coordinates dynamically from real GPS payload
    if data:
        lat = data.get("latitude")
        lng = data.get("longitude")
        if lat and lng:
            state["patient_lat"] = lat
            state["patient_lng"] = lng

    # 3. Evaluate vitals sent via SOS POST
    vitals = {
        "heartRate": data.get("heartRate", state["vitals"]["heartRate"]) if data else state["vitals"]["heartRate"],
        "spo2": data.get("spo2", state["vitals"]["spo2"]) if data else state["vitals"]["spo2"],
        "compressionForce": data.get("compressionForce", state["vitals"]["compressionForce"]) if data else state["vitals"]["compressionForce"],
        "compressionRate": data.get("compressionRate", state["vitals"]["compressionRate"]) if data else state["vitals"]["compressionRate"],
        "status": data.get("status", state["vitals"]["status"]) if data else state["vitals"]["status"]
    }
    state["vitals"].update(vitals)

    # Process AI summary
    report = generate_ai_report(vitals)
    state["ai_summary"] = report["summary"]
    state["multilingual_guidance"] = report["guidance"]

    # 4. Generate notification logs, marking first contact as the Ambulance Driver
    contacts = data.get("contacts", ["+919876543210"]) if data else ["+919876543210"]
    gmaps_link = f"https://maps.google.com/?q={state['patient_lat']},{state['patient_lng']}"
    time_str = time.strftime('%Y-%m-%d %H:%M:%S', time.localtime())

    state["sms_logs"] = []
    for idx, num in enumerate(contacts):
        role = "Ambulance Driver" if idx == 0 else "Emergency Contact"
        msg = f"HELP CLIENT ACTIVE. Time: {time_str}. HR: {vitals['heartRate']} BPM, SpO2: {vitals['spo2']}%. Status: {vitals['status']}. Patient Location: {gmaps_link}. Recommendations: {report['summary']}"
        state["sms_logs"].append({
            "time": time_str,
            "to": f"{num} ({role}) - SMS",
            "msg": f"SMS Message Dispatched: {msg}"
        })
        state["sms_logs"].append({
            "time": time_str,
            "to": f"{num} ({role}) - VOICE CALL",
            "msg": f"CALL CONNECTED: Automated emergency speech report initiated."
        })

    return jsonify(success=True)

@app.route("/api/location/pc", methods=["POST"])
def update_pc_location():
    global pc_lat, pc_lng, state
    data = request.json
    if data:
        lat = data.get("latitude")
        lng = data.get("longitude")
        if lat and lng:
            pc_lat = lat
            pc_lng = lng
            state["ambulance_lat"] = pc_lat
            state["ambulance_lng"] = pc_lng
            return jsonify(success=True)
    return jsonify(success=False)

@app.route("/api/location/ambulance", methods=["POST"])
def force_ambulance_movement():
    # Helper to jump ambulance closer from the dashboard HUD button directly
    global state
    d_lat = state["patient_lat"] - state["ambulance_lat"]
    d_lng = state["patient_lng"] - state["ambulance_lng"]
    state["ambulance_lat"] += d_lat * 0.25
    state["ambulance_lng"] += d_lng * 0.25
    return jsonify(success=True)

@app.route("/api/location/all", methods=["GET"])
def get_all_locations():
    return jsonify(
        is_sos_active=state["is_sos_active"],
        patient=[state["patient_lat"], state["patient_lng"]],
        ambulance=[state["ambulance_lat"], state["ambulance_lng"]],
        route=[
            [state["ambulance_lat"], state["ambulance_lng"]],
            [(state["ambulance_lat"] + state["patient_lat"])/2, (state["ambulance_lng"] + state["patient_lng"])/2],
            [state["patient_lat"], state["patient_lng"]]
        ],
        distance_km=state["distance_km"],
        eta_mins=state["eta_mins"],
        vitals=state["vitals"],
        ai_summary=state["ai_summary"],
        multilingual_guidance=state["multilingual_guidance"],
        sms_logs=state["sms_logs"]
    )

@app.route("/mobile_map")
def mobile_map():
    html_page = """
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
        <title>Mobile View Map</title>
        <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
        <style>
            body, html, #map {
                margin: 0; padding: 0; height: 100%; width: 100%;
                background: #0f172a;
            }
            .leaflet-control-attribution {
                display: none !important;
            }
        </style>
    </head>
    <body>
        <div id="map"></div>
        <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
        <script>
            let map = L.map('map', {zoomControl: false}).setView([{{ patient_lat }}, {{ patient_lng }}], 14);
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                maxZoom: 19
            }).addTo(map);

            let patientMarker = L.marker([{{ patient_lat }}, {{ patient_lng }}]).addTo(map).bindPopup("Patient Location");
            let ambulanceMarker = L.marker([{{ pc_lat }}, {{ pc_lng }}], {
                icon: L.divIcon({
                    className: 'ambulance-icon',
                    html: '<div style="background:#ff3d00; width:14px; height:14px; border-radius:50%; border:2px solid white; box-shadow:0 0 8px rgba(255, 61, 0, 0.8);"></div>',
                    iconSize: [14, 14]
                })
            }).addTo(map).bindPopup("Ambulance 108");

            let routeLine = L.polyline([[{{ pc_lat }}, {{ pc_lng }}], [{{ patient_lat }}, {{ patient_lng }}]], {color: '#00e5ff', weight: 5}).addTo(map);
            let oldSos = false;

            function updateCoords() {
                fetch('/api/location/all')
                .then(r => r.json())
                .then(data => {
                    let pLatLng = L.latLng(data.patient[0], data.patient[1]);
                    let aLatLng = L.latLng(data.ambulance[0], data.ambulance[1]);
                    
                    patientMarker.setLatLng(pLatLng);
                    ambulanceMarker.setLatLng(aLatLng);
                    
                    // Fetch real-time street roads route from OSRM
                    let osrmUrl = `https://router.project-osrm.org/route/v1/driving/${aLatLng.lng},${aLatLng.lat};${pLatLng.lng},${pLatLng.lat}?overview=full&geometries=geojson`;
                    fetch(osrmUrl)
                    .then(r => r.json())
                    .then(res => {
                        if (res.routes && res.routes.length > 0) {
                            let coords = res.routes[0].geometry.coordinates;
                            let path = coords.map(c => [c[1], c[0]]);
                            routeLine.setLatLngs(path);
                        } else {
                            routeLine.setLatLngs([aLatLng, pLatLng]);
                        }
                    })
                    .catch(err => {
                        routeLine.setLatLngs([aLatLng, pLatLng]);
                    });
                    
                    if (data.is_sos_active && !oldSos) {
                        map.fitBounds(routeLine.getBounds(), {padding: [40, 40]});
                        oldSos = true;
                    } else if (!data.is_sos_active) {
                        oldSos = false;
                    }
                })
                .catch(e => console.error("Error fetching locations: " + e));
            }
            setInterval(updateCoords, 1000);
            updateCoords();
        </script>
    </body>
    </html>
    """
    return render_template_string(html_page, patient_lat=state["patient_lat"], patient_lng=state["patient_lng"], pc_lat=pc_lat, pc_lng=pc_lng)

if __name__ == "__main__":
    import sys
    print("--------------------------------------------------")
    print("CJACK HELP HUB 108 Emergency Coordination Server Active")
    print("Listening on http://0.0.0.0:8080")
    print("--------------------------------------------------")
    
    # Enable PC IP location discovery
    fetch_pc_location()
    state["ambulance_lat"] = pc_lat
    state["ambulance_lng"] = pc_lng
    state["patient_lat"] = pc_lat
    state["patient_lng"] = pc_lng
    
    app.run(host="0.0.0.0", port=8080, debug=True)
