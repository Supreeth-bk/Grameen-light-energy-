package com.example.grameenlight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.grameenlight.model.Pole
import com.example.grameenlight.model.PoleStatus
import com.example.grameenlight.ui.screens.MainScreen
import com.example.grameenlight.ui.screens.MainScreenContent
import com.example.grameenlight.ui.screens.OnboardingScreen
import com.example.grameenlight.ui.screens.SplashScreen
import com.example.grameenlight.ui.theme.GrameenLightTheme
import com.example.grameenlight.viewmodel.MainViewModel
import com.example.grameenlight.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Ensure we handle the application cast safely or expect it to be GrameenLightApplication
            val app = application as GrameenLightApplication
            val viewModel: MainViewModel = viewModel(factory = MainViewModelFactory(app.repository))
            
            GrameenLightTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "splash") {
                    composable("splash") { SplashScreen(navController) }
                    composable("onboarding") { OnboardingScreen(navController) }
                    composable("login") { com.example.grameenlight.ui.screens.LoginScreen(navController) }
                    composable("main") { MainScreen(viewModel) }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GrameenLightTheme {
        // Fix: Use MainScreenContent with mock data to allow the Preview to render 
        // without needing a real ViewModel/Repository.
        MainScreenContent(
            poles = listOf(
                Pole("P101", "Market Road", 100f, 500f, PoleStatus.WORKING),
                Pole("P102", "Bus Stand", 400f, 500f, PoleStatus.FUSED)
            ),
            searchQuery = "",
            energySaved = 125.0f,
            personalImpact = 31.0f,
            complaints = emptyList(),
            isTechnicianMode = false,
            onSearchQueryChange = {},
            onReportStatus = { _, _ -> },
            onToggleDarkMode = {},
            onToggleTechnicianMode = {},
            onResetData = {}
        )
    }
}
