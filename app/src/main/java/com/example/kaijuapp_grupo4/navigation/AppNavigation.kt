package com.example.kaijuapp_grupo4.navigation

import androidx.compose.material3.Text
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
import com.example.kaijuapp_grupo4.viewmodel.MainViewModel

@Composable
fun AppNavigation(
    mainViewModel: MainViewModel = viewModel(),
    inventarioViewModel: InventarioViewModel = viewModel()
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController, mainViewModel = mainViewModel)
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
            PerfilScreen(navController = navController, mainViewModel = mainViewModel)
        }
    }
}