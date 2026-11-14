
package com.example.logistica.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.logistica.data.repository.DeliveryRepository
import com.example.logistica.viewmodel.LoginViewModel
import com.example.logistica.viewmodel.appViewModelFactory
import com.example.logistica.viewmodel.DeliveryViewModel

import com.example.logistica.screens.*


@Composable
fun AppNavigation(
    repository: DeliveryRepository,
    abrirMapa: () -> Unit
) {

    val navController = rememberNavController()
    val factory = remember(repository) { appViewModelFactory(repository) }
    val deliveryViewModel: DeliveryViewModel = viewModel(factory = factory)

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
            val loginViewModel: LoginViewModel = viewModel(factory = factory)
            val estado by loginViewModel.estado.collectAsStateWithLifecycle()

            LaunchedEffect(estado.sesionIniciada) {
                if (estado.sesionIniciada) {
                    loginViewModel.sesionNavegada()
                    navController.navigate("entregas") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }

            LoginScreen(
                cargando = estado.cargando,
                mensajeError = estado.error,
                onLogin = { email, password ->
                    loginViewModel.iniciarSesion(email, password)
                }
            )
        }

        // LISTADO DE ENTREGAS
        composable("entregas") {
            val estado by deliveryViewModel.estado.collectAsStateWithLifecycle()

            LaunchedEffect(Unit) {
                deliveryViewModel.cargarEntregas()
            }

            DeliveriesScreen(
                nombre = deliveryViewModel.nombreUsuario(),
                entregas = estado.entregas,
                cargando = estado.cargando,
                error = estado.error,
                onSincronizar = {
                    deliveryViewModel.cargarEntregas()
                },
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
