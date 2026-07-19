package com.cjack.ai.data.remote.provider

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProviderFactory @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    fun getProvider(isSimulationMode: Boolean): TelemetryProvider {
        return if (isSimulationMode) {
            MockProvider()
        } else {
            BleProvider(context)
        }
    }
}
