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
    const val SEGUIMIENTO = "seguimiento/{pacienteId}/{adminId}"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Route.LOGIN) {

        composable(
            route = Route.SEGUIMIENTO,
            arguments = listOf(
                navArgument("pacienteId") { type = NavType.StringType },
                navArgument("adminId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val pacienteId = backStackEntry.arguments?.getString("pacienteId") ?: ""
            val adminId = backStackEntry.arguments?.getString("adminId") ?: ""

            PantallaSeguimiento(
                pacienteId = pacienteId,
                usuarioAdminLogueado = adminId,
                irAtras = { navController.popBackStack() }
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
                irSeguimiento = { paciente -> navController.navigate("seguimiento/$paciente/$usuario") }
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
            TraduccionManual(irHome = { navController.popBackStack() })
        }

        composable(Route.AJUSTES) {
            PantallaAjustes(irHome = { navController.popBackStack() })
        }
    }
}