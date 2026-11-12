
package com.example.logistica.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.logistica.screens.*

@Composable
fun AppNavigation(
    abrirMapa: () -> Unit
) {

    val navController = rememberNavController()

    var estadoPedido by remember {
        mutableStateOf("Pendiente")
    }

    var motivoIncidencia by remember {
        mutableStateOf("Cliente ausente")
    }

    var notasIncidencia by remember {
        mutableStateOf("")
    }

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        // INICIO DE SESIÓN
        composable("login") {
            LoginScreen(
                onLogin = {
                    navController.navigate("entregas") {
                        popUpTo("login") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // LISTADO DE ENTREGAS
        composable("entregas") {
            DeliveriesScreen(
                onOpen = { id ->
                    navController.navigate("detalle/$id")
                },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("entregas") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // DETALLE DE ENTREGA
        composable(
            route = "detalle/{id}",
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                }
            )
        ) {
            DeliveryDetailScreen(
                onBack = {
                    navController.popBackStack()
                },
                onMap = abrirMapa,
                onUpdate = {
                    navController.navigate("estado")
                },
                onIncident = {
                    navController.navigate("incidencia")
                }
            )
        }

        // ACTUALIZAR ESTADO
        composable("estado") {
            UpdateStatusScreen(
                estadoActual = estadoPedido,
                onBack = {
                    navController.popBackStack()
                },
                onSave = { nuevoEstado ->
                    estadoPedido = nuevoEstado
                    navController.popBackStack()
                },
                onIncident = {
                    navController.navigate("incidencia")
                }
            )
        }

        // REGISTRAR INCIDENCIA
        composable("incidencia") {
            IncidentScreen(
                onBack = {
                    navController.popBackStack()
                },
                onSave = { motivo, notas ->
                    motivoIncidencia = motivo
                    notasIncidencia = notas

                    navController.navigate("incidenciaGuardada")
                }
            )
        }

        // INCIDENCIA REGISTRADA
        composable("incidenciaGuardada") {
            IncidentSavedScreen(
                motivo = motivoIncidencia,
                onHome = {
                    navController.navigate("entregas") {
                        popUpTo("entregas") {
                            inclusive = true
                        }
                    }
                },
                onDetail = {
                    navController.navigate("detalle/1")
                }
            )
        }

        // SIN ENTREGAS PENDIENTES
        composable("sinEntregas") {
            EmptyDeliveriesScreen(
                onRetry = {
                    navController.navigate("entregas") {
                        popUpTo("sinEntregas") {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}
