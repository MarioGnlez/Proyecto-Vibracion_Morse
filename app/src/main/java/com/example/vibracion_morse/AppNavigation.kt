package com.example.vibracion_morse

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vibracion_morse.ventanas.*

object Route {
    const val LOGIN = "login"
    const val HOME = "home/{usuario}"
    const val MANUAL = "manual"
    const val AJUSTES = "ajustes"
    const val CHAT_INDIVIDUAL = "chat/{miUsuario}/{otroUsuario}"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Route.LOGIN) {

        composable(
            route = Route.CHAT_INDIVIDUAL,
            arguments = listOf(
                navArgument("miUsuario") { type = NavType.StringType },
                navArgument("otroUsuario") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val miUsuario = backStackEntry.arguments?.getString("miUsuario") ?: ""
            val otroUsuario = backStackEntry.arguments?.getString("otroUsuario") ?: ""

            PantallaChat(
                miUsuario = miUsuario,
                otroUsuario = otroUsuario,
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Route.HOME,
            arguments = listOf(navArgument("usuario") { type = NavType.StringType })
        ) { backStackEntry ->
            val usuario = backStackEntry.arguments?.getString("usuario") ?: ""
            Home(
                usuarioLogueado = usuario,
                irTraductorManual = { navController.navigate(Route.MANUAL) },
                irAjustes = { navController.navigate(Route.AJUSTES) },
                irChat = { contacto ->
                    navController.navigate("chat/$usuario/$contacto")
                }
            )
        }

        composable(Route.LOGIN) {
            PantallaLogin(onLoginSuccess = { usuarioLogueado ->
                navController.navigate("home/$usuarioLogueado") {
                    popUpTo(Route.LOGIN) { inclusive = true }
                }
            })
        }

        composable(Route.MANUAL) {
            TraduccionManual(
                irHome = {
                    navController.popBackStack()
                }
            )
        }

        composable(Route.AJUSTES) {
            PantallaAjustes(
                irHome = {
                    navController.popBackStack()
                }
            )
        }
    }
}