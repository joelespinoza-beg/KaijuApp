package com.example.kaijuapp_grupo4.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Inventario : Screen("inventario")
    data object FormularioProducto : Screen("formulario_producto")
    data object ResumenVenta : Screen("resumen_venta")
    data object Perfil : Screen("perfil")
}