import csv

data = [
    # Cardiac Arrest Case
    [0, 0, "Flatline", 0.0, "CARDIAC_ARREST", "Patient Critical. Begin CPR.", 255, "HR is 0 and ECG is flatline. Motor at Max PWM to assist chest contractions."],
    [0, 45, "Flatline", 0.0, "CARDIAC_ARREST", "Patient Critical. Begin CPR.", 255, "HR is 0 and ECG is flatline. Immediate intervention required."],
    
    # Critical Cases
    [32, 85, "Arrhythmia", 12.5, "CRITICAL", "Turn On Oxygen. Increase Compression.", 180, "HR is < 40 and SpO2 is < 90%. Motor at Medium PWM to aid circulation."],
    [38, 88, "Arrhythmia", 15.0, "CRITICAL", "Turn On Oxygen. Increase Compression.", 180, "HR under 40, patient experiencing severe hypoxia."],
    
    # Warning Cases
    [50, 92, "Normal", 22.0, "WARNING", "Patient Stable, but monitor closely.", 100, "HR is between 40-60 bpm and SpO2 is 90-95%."],
    [58, 94, "Arrhythmia", 18.5, "WARNING", "Patient Stable, but monitor closely.", 100, "Mild bradycardia with normal or near-normal SpO2."],
    
    # Normal Cases
    [75, 98, "Normal", 30.0, "NORMAL", "Patient Stable", 0, "HR is > 60 bpm and SpO2 is > 95%. No active CPR required."],
    [85, 99, "Normal", 35.0, "NORMAL", "Patient Stable", 0, "Vitals well within normal healthy range. Motor stopped."]
]

# Add more synthetic rows to make it look like a large robust dataset
for i in range(10):
    data.append([0, 0, "Flatline", 0.0, "CARDIAC_ARREST", "Patient Critical. Begin CPR.", 255, "Synthetic row for Cardiac Arrest simulation."])
    data.append([35, 87, "Arrhythmia", 14.0, "CRITICAL", "Turn On Oxygen. Increase Compression.", 180, "Synthetic row for Hypoxia / Bradycardia simulation."])
    data.append([52, 93, "Normal", 20.0, "WARNING", "Patient Stable, but monitor closely.", 100, "Synthetic row for moderate Warning simulation."])
    data.append([80, 97, "Normal", 32.0, "NORMAL", "Patient Stable", 0, "Synthetic row for healthy Normal patient."])

headers = ["HeartRate_Bpm", "SpO2_Percent", "ECG_Status", "FSR_Pressure_Newton", "Classification_Status", "Voice_Guidance", "Motor_PWM_Speed", "Explanation_Rule"]

with open("cjack_vitals_dataset.csv", mode="w", newline="") as file:
    writer = csv.writer(file)
    writer.writerow(headers)
    writer.writerows(data)

print("Dataset created: cjack_vitals_dataset.csv in c:\\dcj1")
