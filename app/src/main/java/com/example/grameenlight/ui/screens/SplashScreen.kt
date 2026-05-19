package com.example.grameenlight.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.grameenlight.ui.theme.SurfaceNocturnal
import com.example.grameenlight.ui.theme.GrameenLightTheme
import com.example.grameenlight.ui.theme.PrimaryBlue
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate("onboarding") {
            popUpTo("splash") { inclusive = true }
        }
    }

    SplashContent()
}

@Composable
fun SplashContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color.White, PrimaryBlue.copy(alpha = 0.1f)))),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.Default.Lightbulb,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = PrimaryBlue
            )
            Text(
                "GRAMEEN LIGHT",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = SurfaceNocturnal
            )
            Text(
                "Smart Streetlight Audit",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(32.dp))
            CircularProgressIndicator(color = PrimaryBlue)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    GrameenLightTheme {
        SplashContent()
    }
}
