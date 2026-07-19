/*
====================================================
CJACK Prototype
Arduino UNO Q

Sensors:
- Pulse Sensor Amped  -> A2
- AD8232 ECG          -> A0
- FSR                 -> A1

Output:
JSON over Serial @9600

Required Library:
PulseSensor Playground
====================================================
*/

#include <PulseSensorPlayground.h>

// -------------------- Pins --------------------
const int PULSE_PIN = A2;
const int ECG_PIN   = A0;
const int FSR_PIN   = A1;

const int LO_PLUS  = 10;
const int LO_MINUS = 11;

// -------------------- Pulse Sensor --------------------
PulseSensorPlayground pulseSensor;

int BPM = 0;

// -------------------- Setup --------------------
void setup() {

  Serial.begin(9600);

  pinMode(LO_PLUS, INPUT);
  pinMode(LO_MINUS, INPUT);

  pulseSensor.analogInput(PULSE_PIN);

  // Change this if needed
  pulseSensor.setThreshold(550);

  if (!pulseSensor.begin()) {
    Serial.println("{\"error\":\"Pulse Sensor not found\"}");
  }

  Serial.println("CJACK Started");
}

// -------------------- Loop --------------------
void loop() {

  // ---------- Heart Rate ----------
  BPM = pulseSensor.getBeatsPerMinute();

  if (pulseSensor.sawStartOfBeat()) {
    // Beat detected
  }

  // ---------- ECG ----------
  int ecg;

  if (digitalRead(LO_PLUS) || digitalRead(LO_MINUS))
    ecg = -1;
  else
    ecg = analogRead(ECG_PIN);

  // ---------- FSR ----------
  int force = analogRead(FSR_PIN);

  // ---------- Pulse Raw ----------
  int pulseRaw = analogRead(PULSE_PIN);

  // ---------- SpO2 ----------
  // Pulse Sensor cannot measure oxygen saturation
  int spo2 = 0;

  // ---------- Status ----------
  String status = "NORMAL";

  if (BPM == 0)
    status = "NO_PULSE";
  else if (BPM < 60)
    status = "LOW_HR";
  else if (BPM > 120)
    status = "HIGH_HR";

  if (force > 700)
    status = "CPR_ACTIVE";

  // ---------- JSON ----------
  Serial.print("{");

  Serial.print("\"heartRate\":");
  Serial.print(BPM);

  Serial.print(",\"pulse\":");
  Serial.print(pulseRaw);

  Serial.print(",\"spo2\":");
  Serial.print(spo2);

  Serial.print(",\"ecg\":");
  Serial.print(ecg);

  Serial.print(",\"force\":");
  Serial.print(force);

  Serial.print(",\"status\":\"");
  Serial.print(status);

  Serial.println("\"}");

  delay(100);
}
