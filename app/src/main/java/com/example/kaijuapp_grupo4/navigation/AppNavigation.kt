package com.example.kaijuapp_grupo4.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kaijuapp_grupo4.ui.screens.FormularioProductoScreen
import com.example.kaijuapp_grupo4.ui.screens.HomeScreen
import com.example.kaijuapp_grupo4.ui.screens.InventarioScreen
import com.example.kaijuapp_grupo4.ui.screens.PerfilScreen
import com.example.kaijuapp_grupo4.ui.screens.ResumenVentaScreen
import com.example.kaijuapp_grupo4.viewmodel.InventarioViewModel
import com.example.kaijuapp_grupo4.viewmodel.PerfilViewModel

@Composable
fun AppNavigation(
    inventarioViewModel: InventarioViewModel = viewModel(),
    perfilViewModel: PerfilViewModel = viewModel()
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(350)) +
                    fadeIn(tween(350))
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(350)) +
                    fadeOut(tween(350))
        },
        popEnterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(350)) +
                    fadeIn(tween(350))
        },
        popExitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(350)) +
                    fadeOut(tween(350))
        }
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Screen.Inventario.route) {
            InventarioScreen(navController = navController, inventarioViewModel = inventarioViewModel)
        }
        composable(route = Screen.FormularioProducto.route) {
            FormularioProductoScreen(navController = navController, inventarioViewModel = inventarioViewModel)
        }
        composable(route = Screen.ResumenVenta.route) {
            ResumenVentaScreen(navController = navController, inventarioViewModel = inventarioViewModel)
        }
        composable(route = Screen.Perfil.route) {
            PerfilScreen(navController = navController, perfilViewModel = perfilViewModel)
        }
    }
}

