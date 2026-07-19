package com.cjack.ai.data.remote

import com.cjack.ai.data.model.Telemetry
import javax.inject.Inject

class FirebaseService @Inject constructor() {
    fun uploadTelemetry(telemetry: Telemetry, aiStatus: String) {
        // Stub: Realtime Database upload once per second
        // database.reference.child("patients").child(patientId).setValue(...)
    }
}
