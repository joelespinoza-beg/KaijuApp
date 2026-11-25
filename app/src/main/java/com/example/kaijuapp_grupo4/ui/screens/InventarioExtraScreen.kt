package com.example.kaijuapp_grupo4.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kaijuapp_grupo4.model.Producto
import com.example.kaijuapp_grupo4.model.Sucursal
import com.example.kaijuapp_grupo4.ui.theme.AzulLlegadas
import com.example.kaijuapp_grupo4.ui.theme.RojoMerma
import com.example.kaijuapp_grupo4.ui.theme.RojoSuaveFondo
import com.example.kaijuapp_grupo4.ui.theme.RojoSuaveTexto
import com.example.kaijuapp_grupo4.ui.theme.VerdeAzulado
import com.example.kaijuapp_grupo4.ui.theme.VerdeVentas
import com.example.kaijuapp_grupo4.viewmodel.InventarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventarioExtraScreen(
    navController: NavController,
    inventarioViewModel: InventarioViewModel = viewModel()
) {
    val todosLosProductos by inventarioViewModel.todosLosProductos.collectAsState()
    val backendOnline by inventarioViewModel.backendOnline.collectAsState()
    val tiemposSucursales by inventarioViewModel.tiemposSucursales.collectAsState()

    var productoEditando by remember { mutableStateOf<Producto?>(null) }

    if (productoEditando != null) {
        DialogoEditarProducto(
            producto = productoEditando!!,
            onDismiss = { productoEditando = null },
            onGuardar = { actualizado ->
                inventarioViewModel.actualizarProducto(actualizado)
                productoEditando = null
            }
        )
    }

    val sucursalesActualizadas = remember(todosLosProductos, tiemposSucursales) {
        listOf(
            Sucursal(
                id = "san_bernardo",
                nombre = "San Bernardo",
                direccion = "Av. América 450, San Bernardo",
                diasSinCambios = 0,
                tiempoTranscurridoFormateado = tiemposSucursales["san_bernardo"] ?: "Hace 15 minutos",
                cambioLlegados = todosLosProductos.filter { it.sucursal.equals("San Bernardo", ignoreCase = true) || it.sucursal.equals("san_bernardo", ignoreCase = true) }.size,
                cambioVendidos = 0,
                cambioMerma = 0,
                productos = todosLosProductos.filter { it.sucursal.equals("San Bernardo", ignoreCase = true) || it.sucursal.equals("san_bernardo", ignoreCase = true) }
            ),
            Sucursal(
                id = "central",
                nombre = "Central",
                direccion = "Av. Libertador B. O'Higgins 1200, Santiago",
                diasSinCambios = 0,
                tiempoTranscurridoFormateado = tiemposSucursales["central"] ?: "Hace 1 hora",
                cambioLlegados = todosLosProductos.filter { it.sucursal.equals("Central", ignoreCase = true) || it.sucursal.equals("central", ignoreCase = true) }.size,
                cambioVendidos = 0,
                cambioMerma = 0,
                productos = todosLosProductos.filter { it.sucursal.equals("Central", ignoreCase = true) || it.sucursal.equals("central", ignoreCase = true) }
            ),
            Sucursal(
                id = "norte",
                nombre = "Norte",
                direccion = "Av. Independencia 3400, Conchalí",
                diasSinCambios = 0,
                tiempoTranscurridoFormateado = tiemposSucursales["norte"] ?: "Hace 45 minutos",
                cambioLlegados = todosLosProductos.filter { it.sucursal.equals("Norte", ignoreCase = true) || it.sucursal.equals("norte", ignoreCase = true) }.size,
                cambioVendidos = 0,
                cambioMerma = 0,
                productos = todosLosProductos.filter { it.sucursal.equals("Norte", ignoreCase = true) || it.sucursal.equals("norte", ignoreCase = true) }
            ),
            Sucursal(
                id = "maipu",
                nombre = "Maipú",
                direccion = "Av. Pajaritos 2100, Maipú",
                diasSinCambios = 0,
                tiempoTranscurridoFormateado = tiemposSucursales["maipu"] ?: "Hace 2 días",
                cambioLlegados = todosLosProductos.filter { it.sucursal.equals("Maipú", ignoreCase = true) || it.sucursal.equals("maipu", ignoreCase = true) }.size,
                cambioVendidos = 0,
                cambioMerma = 0,
                productos = todosLosProductos.filter { it.sucursal.equals("Maipú", ignoreCase = true) || it.sucursal.equals("maipu", ignoreCase = true) }
            ),
            Sucursal(
                id = "persa_biobio",
                nombre = "Persa BioBío",
                direccion = "Placer 650, Franklin / BioBío",
                diasSinCambios = 0,
                tiempoTranscurridoFormateado = tiemposSucursales["persa_biobio"] ?: "Hace 35 días",
                cambioLlegados = todosLosProductos.filter { it.sucursal.equals("Persa BioBío", ignoreCase = true) || it.sucursal.equals("persa_biobio", ignoreCase = true) }.size,
                cambioVendidos = 0,
                cambioMerma = 0,
                productos = todosLosProductos.filter { it.sucursal.equals("Persa BioBío", ignoreCase = true) || it.sucursal.equals("persa_biobio", ignoreCase = true) }
            )
        )
    }

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var textoBusqueda by remember { mutableStateOf("") }
    var mensajeSincronizacion by remember { mutableStateOf(false) }

    val sucursalActual = sucursalesActualizadas.getOrElse(selectedTabIndex) { sucursalesActualizadas[0] }

    val productosFiltrados = remember(sucursalActual, textoBusqueda) {
        if (textoBusqueda.isBlank()) {
            sucursalActual.productos
        } else {
            sucursalActual.productos.filter {
                it.nombre.contains(textoBusqueda, ignoreCase = true) ||
                        it.categoria.contains(textoBusqueda, ignoreCase = true)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inventario Extra Multi-Sucursal") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VerdeAzulado,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                edgePadding = 16.dp,
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ) {
                sucursalesActualizadas.forEachIndexed { index, sucursal ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedTabIndex = index
                            textoBusqueda = ""
                        },
                        text = {
                            Text(
                                text = "Sucursal ${sucursal.nombre}",
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (!sucursalActual.estaDisponible) {
                            RojoSuaveFondo
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Sucursal ${sucursalActual.nombre}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = if (backendOnline) "Backend: Online / Sincronizado" else "Backend: Offline (Datos locales Room)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (backendOnline) MaterialTheme.colorScheme.primary else RojoSuaveTexto
                                )
                                Text(
                                    text = sucursalActual.direccion,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Última actualización: ${sucursalActual.tiempoTranscurridoFormateado}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            InsigniaCambio(
                                texto = "+${sucursalActual.cambioLlegados} llegados",
                                colorFondo = AzulLlegadas.copy(alpha = 0.15f),
                                colorTexto = AzulLlegadas
                            )
                            InsigniaCambio(
                                texto = "-${sucursalActual.cambioVendidos} vendidos",
                                colorFondo = VerdeVentas.copy(alpha = 0.15f),
                                colorTexto = VerdeVentas
                            )
                            InsigniaCambio(
                                texto = "-${sucursalActual.cambioMerma} merma",
                                colorFondo = RojoMerma.copy(alpha = 0.15f),
                                colorTexto = RojoMerma
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                inventarioViewModel.sincronizarConBackend()
                                inventarioViewModel.actualizarTiempoSucursal(sucursalActual.id, "Hace 1 minuto")
                                mensajeSincronizacion = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = VerdeAzulado),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                            Spacer(Modifier.width(6.dp))
                            Text("Actualizar ahora (Sincronizar y POST)")
                        }

                        AnimatedVisibility(visible = mensajeSincronizacion) {
                            Text(
                                text = "¡Sucursal sincronizada exitosamente con backend!",
                                color = VerdeVentas,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = textoBusqueda,
                    onValueChange = { textoBusqueda = it },
                    placeholder = { Text("Buscar producto en ${sucursalActual.nombre}...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Inventario de ${sucursalActual.nombre} (${productosFiltrados.size} productos):",
                    style = MaterialTheme.typography.titleSmall
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(productosFiltrados, key = { it.id }) { producto ->
                        ItemProductoExtraCard(
                            producto = producto,
                            onEditar = { productoEditando = producto },
                            onEliminar = { inventarioViewModel.eliminarProducto(producto.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InsigniaCambio(texto: String, colorFondo: Color, colorTexto: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(colorFondo)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = texto,
            color = colorTexto,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ItemProductoExtraCard(
    producto: Producto,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    var expandedMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Categoría: ${producto.categoria} | Sucursal: ${producto.sucursal}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${producto.cantidadDisponible} unidades",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Box {
                IconButton(onClick = { expandedMenu = true }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Opciones")
                }
                DropdownMenu(
                    expanded = expandedMenu,
                    onDismissRequest = { expandedMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Editar") },
                        onClick = {
                            expandedMenu = false
                            onEditar()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Eliminar") },
                        onClick = {
                            expandedMenu = false
                            onEliminar()
                        }
                    )
                }
            }
        }
    }
}
