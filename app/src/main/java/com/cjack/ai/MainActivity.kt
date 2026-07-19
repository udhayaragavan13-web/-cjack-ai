package com.cjack.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cjack.ai.presentation.navigation.Screen
import com.cjack.ai.presentation.screens.ambulance.AmbulanceScreen
import com.cjack.ai.presentation.screens.dashboard.DashboardScreen
import com.cjack.ai.presentation.screens.history.HistoryScreen
import com.cjack.ai.presentation.screens.settings.SettingsScreen
import com.cjack.ai.presentation.screens.splash.SplashScreen
import com.cjack.ai.presentation.screens.validation.ValidationScreen
import com.cjack.ai.presentation.theme.CJACKAITheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val requestPermissionLauncher = registerForActivityResult(
            androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions()
        ) { _ -> }
        
        requestPermissionLauncher.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.BLUETOOTH_SCAN,
                android.Manifest.permission.BLUETOOTH_CONNECT,
                android.Manifest.permission.SEND_SMS,
                android.Manifest.permission.CALL_PHONE
            )
        )

        setContent {
            CJACKAITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = Screen.Splash.route) {
                        composable(Screen.Splash.route) {
                            SplashScreen(navController)
                        }
                        composable(Screen.Validation.route) {
                            ValidationScreen(navController)
                        }
                        composable(Screen.Dashboard.route) {
                            DashboardScreen(navController)
                        }
                        composable(Screen.Ambulance.route) {
                            AmbulanceScreen(navController)
                        }
                        composable(Screen.History.route) {
                            HistoryScreen(navController)
                        }
                        composable(Screen.Settings.route) {
                            SettingsScreen(navController)
                        }
                    }
                }
            }
        }
    }
}
