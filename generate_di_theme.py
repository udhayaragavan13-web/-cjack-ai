import os
from pathlib import Path

def write_file(path, content):
    p = Path(path)
    p.parent.mkdir(parents=True, exist_ok=True)
    p.write_text(content, encoding='utf-8')

base = Path(r"c:\dcj1\app\src\main\java\com\cjack\ai")

# 1. Application Class
write_file(base / "CjackApplication.kt", """package com.cjack.ai

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CjackApplication : Application()
""")

# 2. DI Module
write_file(base / "di/AppModule.kt", """package com.cjack.ai.di

import android.content.Context
import androidx.room.Room
import com.cjack.ai.data.local.CjackDatabase
import com.cjack.ai.data.local.dao.CprSessionDao
import com.cjack.ai.data.remote.FirebaseService
import com.cjack.ai.data.remote.provider.ProviderFactory
import com.cjack.ai.domain.engine.RuleEngine
import com.cjack.ai.domain.engine.VoiceQueueManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CjackDatabase {
        return Room.databaseBuilder(
            context,
            CjackDatabase::class.java,
            "cjack_db"
        ).build()
    }

    @Provides
    fun provideCprSessionDao(db: CjackDatabase): CprSessionDao = db.cprSessionDao

    @Provides
    @Singleton
    fun provideFirebaseService(): FirebaseService = FirebaseService()

    @Provides
    @Singleton
    fun provideProviderFactory(): ProviderFactory = ProviderFactory()

    @Provides
    @Singleton
    fun provideRuleEngine(): RuleEngine = RuleEngine()

    @Provides
    @Singleton
    fun provideVoiceQueueManager(@ApplicationContext context: Context): VoiceQueueManager {
        return VoiceQueueManager(context)
    }
}
""")

# 3. Theme
write_file(base / "presentation/theme/Color.kt", """package com.cjack.ai.presentation.theme

import androidx.compose.ui.graphics.Color

val DarkBackground = Color(0xFF101216)
val CardGlass = Color(0x22FFFFFF)
val NeonGreen = Color(0xFF00FF7F)
val NeonYellow = Color(0xFFFFEA00)
val NeonRed = Color(0xFFFF3131)
val AccentBlue = Color(0xFF00BFFF)
val TextPrimary = Color.White
val TextSecondary = Color(0xFF9E9E9E)
""")

write_file(base / "presentation/theme/Theme.kt", """package com.cjack.ai.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AccentBlue,
    secondary = NeonGreen,
    tertiary = NeonYellow,
    background = DarkBackground,
    surface = CardGlass,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = NeonRed
)

@Composable
fun CJACKAITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
""")

# 4. Navigation Routes
write_file(base / "presentation/navigation/Screen.kt", """package com.cjack.ai.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Validation : Screen("validation")
    object Dashboard : Screen("dashboard")
    object Ambulance : Screen("ambulance")
    object History : Screen("history")
    object Settings : Screen("settings")
}
""")

# 5. MainActivity
write_file(base / "MainActivity.kt", """package com.cjack.ai

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
""")

# 6. Basic Placeholders for Screens (to allow MainActivity to compile)
for screen, name in [
    ("splash", "SplashScreen"),
    ("validation", "ValidationScreen"),
    ("dashboard", "DashboardScreen"),
    ("ambulance", "AmbulanceScreen"),
    ("history", "HistoryScreen"),
    ("settings", "SettingsScreen")
]:
    write_file(base / f"presentation/screens/{screen}/{name}.kt", f"""package com.cjack.ai.presentation.screens.{screen}

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun {name}(navController: NavController) {{
    Text("Placeholder for {name}")
}}
""")

print("DI, Theme, MainActivity and Navigation generated.")
