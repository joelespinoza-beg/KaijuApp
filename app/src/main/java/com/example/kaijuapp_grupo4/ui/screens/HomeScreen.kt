package com.example.kaijuapp_grupo4.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kaijuapp_grupo4.navigation.Screen
import com.example.kaijuapp_grupo4.ui.theme.obtenerWindowSizeClass
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val esCompacto = obtenerWindowSizeClass().widthSizeClass == WindowWidthSizeClass.Compact

    val opcionesMenu = listOf(
        Triple(Icons.AutoMirrored.Filled.List, "Inventario de Productos", Screen.Inventario),
        Triple(Icons.Default.Add, "Registrar Producto", Screen.FormularioProducto),
        Triple(Icons.Default.ShoppingCart, "Registrar Venta", Screen.ResumenVenta),
        Triple(Icons.Default.Person, "Perfil de Usuario / Ajustes", Screen.Perfil)
    )

    val accionesPrincipales = listOf(
        "Ver Inventario" to Screen.Inventario,
        "Registrar Nuevo Producto" to Screen.FormularioProducto,
        "Registrar Venta" to Screen.ResumenVenta
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Menú Kaiju",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
                opcionesMenu.forEach { (icono, etiqueta, destino) ->
                    NavigationDrawerItem(
                        icon = { Icon(icono, contentDescription = etiqueta) },
                        label = { Text(etiqueta) },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            navController.navigate(destino.route)
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Gestión Kaiju") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "¡Bienvenido a Kaiju!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Sistema de Control de Stock",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Gestiona tus productos, evita quiebres de inventario y registra movimientos fácilmente.")
                    }
                }

                if (esCompacto) {
                    accionesPrincipales.forEach { (texto, destino) ->
                        Button(
                            onClick = { navController.navigate(destino.route) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(texto)
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        accionesPrincipales.forEach { (texto, destino) ->
                            Button(
                                onClick = { navController.navigate(destino.route) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(texto)
                            }
                        }
                    }
                }
            }
        }
    }
}

