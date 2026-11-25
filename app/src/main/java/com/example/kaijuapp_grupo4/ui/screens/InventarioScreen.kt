package com.example.kaijuapp_grupo4.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.kaijuapp_grupo4.model.Producto
import com.example.kaijuapp_grupo4.navigation.Screen
import com.example.kaijuapp_grupo4.ui.components.formatearPrecio
import com.example.kaijuapp_grupo4.ui.theme.obtenerWindowSizeClass
import com.example.kaijuapp_grupo4.viewmodel.InventarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventarioScreen(
    navController: NavController,
    inventarioViewModel: InventarioViewModel
) {
    val productos by inventarioViewModel.productos.collectAsState()
    val backendOnline by inventarioViewModel.backendOnline.collectAsState()
    val mensajeSincronizacion by inventarioViewModel.mensajeSincronizacion.collectAsState()
    val sucursalAfiliada by inventarioViewModel.sucursalAfiliada.collectAsState()
    val columnas = if (obtenerWindowSizeClass().widthSizeClass == WindowWidthSizeClass.Compact) 1 else 2

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inventario - Sucursal $sucursalAfiliada") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.FormularioProducto.route) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Producto")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (backendOnline) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (backendOnline) "Backend: Online / Sincronizado" else "Backend: Offline (Datos locales Room)",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            if (!mensajeSincronizacion.isNullOrBlank()) {
                                Text(
                                    text = mensajeSincronizacion!!,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                        Button(
                            onClick = { inventarioViewModel.sincronizarConBackend() }
                        ) {
                            Text("Actualizar (POST)")
                        }
                    }
                }
            }

            Button(
                onClick = { navController.navigate(Screen.InventarioExtra.route) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            ) {
                Text("Ver Inventario Multi-Sucursal (Extra)")
            }

            Text(
                text = "Productos en Stock (${productos.size}):",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (productos.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Aún no hay productos registrados.\nPresiona + para agregar el primero.",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(columnas),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(productos, key = { it.id }) { producto ->
                        ItemProductoCard(
                            producto = producto,
                            onEditar = { productoEditando = producto },
                            onEliminar = { inventarioViewModel.eliminarProducto(producto.id) },
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ItemProductoCard(
    producto: Producto,
    onEditar: () -> Unit,
    onEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val esStockBajo = producto.cantidadDisponible <= producto.stockMinimo
    var expandedMenu by remember { mutableStateOf(false) }

    val colorFondo by animateColorAsState(
        targetValue = if (esStockBajo) {
            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        animationSpec = tween(durationMillis = 400),
        label = "colorStock"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorFondo)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                if (esStockBajo) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Stock Bajo",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = " Stock Bajo",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Categoría: ${producto.categoria}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Precio: ${formatearPrecio(producto.precio)}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Disponible: ${producto.cantidadDisponible} unidades",
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (esStockBajo) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (!producto.imagenUri.isNullOrBlank()) {
                        AsyncImage(
                            model = producto.imagenUri,
                            contentDescription = "Miniatura producto",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = "Sin foto",
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
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
}

@Composable
fun DialogoEditarProducto(
    producto: Producto,
    onDismiss: () -> Unit,
    onGuardar: (Producto) -> Unit
) {
    var nombre by remember { mutableStateOf(producto.nombre) }
    var categoria by remember { mutableStateOf(producto.categoria) }
    var precioStr by remember { mutableStateOf(producto.precio.toString()) }
    var cantidadStr by remember { mutableStateOf(producto.cantidadDisponible.toString()) }
    var stockMinStr by remember { mutableStateOf(producto.stockMinimo.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Producto") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = categoria,
                    onValueChange = { categoria = it },
                    label = { Text("Categoría") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = precioStr,
                    onValueChange = { precioStr = it },
                    label = { Text("Precio") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = cantidadStr,
                    onValueChange = { cantidadStr = it },
                    label = { Text("Cantidad Disponible") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = stockMinStr,
                    onValueChange = { stockMinStr = it },
                    label = { Text("Stock Mínimo") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val precio = precioStr.toDoubleOrNull() ?: producto.precio
                    val cantidad = cantidadStr.toIntOrNull() ?: producto.cantidadDisponible
                    val stockMin = stockMinStr.toIntOrNull() ?: producto.stockMinimo
                    val productoActualizado = producto.copy(
                        nombre = nombre.trim(),
                        categoria = categoria.trim(),
                        precio = precio,
                        cantidadDisponible = cantidad,
                        stockMinimo = stockMin
                    )
                    onGuardar(productoActualizado)
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
