package com.example.authapp.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.authapp.repository.AuthRepository
import com.example.authapp.remote.RetrofitClient
import com.example.authapp.viewmodel.AuthViewModel
import com.example.authapp.remote.AuthApi
import com.example.authapp.ui.screen.HomeScreen
import com.example.authapp.ui.screen.LoginScreen
import com.example.authapp.ui.screen.RegisterScreen
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import com.example.authapp.util.TokenManager


@Composable
fun AuthApp() {
    val context = LocalContext.current
    val authApi = RetrofitClient.instance.create(AuthApi::class.java)
    val tokenManager = TokenManager
    val authRepository = AuthRepository(authApi, context)
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory(authRepository))

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                viewModel = authViewModel,
                onAuthenticated = { navController.navigate("home") },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                viewModel = authViewModel,
                onRegisterSuccess = { navController.navigate("login") },
                onNavigateToLogin = { navController.navigate("login") }
            )
        }
        composable("home") {
            HomeScreen(
                viewModel = authViewModel,
                onLogout = { navController.navigate("login") }
            )
        }
    }
}