package com.example.logistica.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.logistica.data.repository.DeliveryRepository
import com.example.logistica.screens.*
import com.example.logistica.viewmodel.DeliveryViewModel
import com.example.logistica.viewmodel.LoginViewModel
import com.example.logistica.viewmodel.appViewModelFactory

@Composable
fun AppNavigation(
    repository: DeliveryRepository,
    abrirMapa: (String) -> Unit
) {

    val navController = rememberNavController()
    val factory = remember(repository) { appViewModelFactory(repository) }
    val deliveryViewModel: DeliveryViewModel = viewModel(factory = factory)

    // Argumento "id" que usan varias rutas
    val argumentoId = listOf(
        navArgument("id") { type = NavType.IntType }
    )

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
                        popUpTo("entregas") { inclusive = true }
                    }
                }
            )
        }

        // DETALLE DE ENTREGA
        composable(
            route = "detalle/{id}",
            arguments = argumentoId
        ) { backStackEntry ->

            val id = backStackEntry.arguments?.getInt("id") ?: 0
            val estado by deliveryViewModel.estado.collectAsStateWithLifecycle()
            val entrega = estado.entregas.find { it.id == id }

            // Si Android cerró la app (por ejemplo, al ir a Maps), recarga la lista
            LaunchedEffect(id) {
                if (entrega == null) {
                    deliveryViewModel.cargarEntregas()
                }
            }

            DeliveryDetailScreen(
                entrega = entrega,
                cargando = estado.cargando,
                onBack = {
                    navController.popBackStack()
                },
                onMap = { direccion ->
                    abrirMapa(direccion)
                },
                onUpdate = {
                    navController.navigate("estado/$id")
                },
                onIncident = {
                    navController.navigate("incidencia/$id")
                }
            )
        }

        // ACTUALIZAR ESTADO
        composable(
            route = "estado/{id}",
            arguments = argumentoId
        ) { backStackEntry ->

            val id = backStackEntry.arguments?.getInt("id") ?: 0
            val estado by deliveryViewModel.estado.collectAsStateWithLifecycle()
            val entrega = estado.entregas.find { it.id == id }

            LaunchedEffect(Unit) {
                deliveryViewModel.limpiarErrorAccion()
            }

            UpdateStatusScreen(
                entrega = entrega,
                guardando = estado.guardando,
                error = estado.errorAccion,
                onBack = {
                    navController.popBackStack()
                },
                onSave = { nuevoEstado ->
                    deliveryViewModel.actualizarEstado(id, nuevoEstado) {
                        // Al guardar, regresa al detalle
                        navController.popBackStack()
                    }
                },
                onIncident = {
                    navController.navigate("incidencia/$id")
                }
            )
        }

        // REGISTRAR INCIDENCIA
        composable(
            route = "incidencia/{id}",
            arguments = argumentoId
        ) { backStackEntry ->

            val id = backStackEntry.arguments?.getInt("id") ?: 0
            val estado by deliveryViewModel.estado.collectAsStateWithLifecycle()
            val entrega = estado.entregas.find { it.id == id }

            LaunchedEffect(Unit) {
                deliveryViewModel.limpiarErrorAccion()
            }

            IncidentScreen(
                entrega = entrega,
                guardando = estado.guardando,
                error = estado.errorAccion,
                onBack = {
                    navController.popBackStack()
                },
                onSave = { motivo ->
                    deliveryViewModel.registrarIncidencia(id, motivo) {
                        navController.navigate("incidenciaGuardada") {
                            // Quita detalle/estado/incidencia del historial
                            popUpTo("entregas")
                        }
                    }
                }
            )
        }

        // INCIDENCIA REGISTRADA
        composable("incidenciaGuardada") {
            val estado by deliveryViewModel.estado.collectAsStateWithLifecycle()
            val incidencia = estado.ultimaIncidencia

            // "2026-11-13T10:15:00-06:00" -> "10:15"
            val hora = incidencia?.estado_fecha_hora
                ?.takeIf { it.length >= 16 }
                ?.substring(11, 16)
                ?: "--:--"

            IncidentSavedScreen(
                motivo = estado.motivoIncidencia ?: "",
                pedido = if (incidencia != null) "Pedido #${incidencia.pedido.id}" else "",
                hora = hora,
                onHome = {
                    navController.navigate("entregas") {
                        popUpTo("entregas") { inclusive = true }
                    }
                }
            )
        }

        // SIN ENTREGAS PENDIENTES
        composable("sinEntregas") {
            EmptyDeliveriesScreen(
                onRetry = {
                    navController.navigate("entregas") {
                        popUpTo("sinEntregas") { inclusive = true }
                    }
                }
            )
        }
    }
}