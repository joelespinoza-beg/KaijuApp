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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
    val productosLocales by inventarioViewModel.productos.collectAsState()
    val backendOnline by inventarioViewModel.backendOnline.collectAsState()
    val tiemposSucursales by inventarioViewModel.tiemposSucursales.collectAsState()

    var sucursales by remember {
        mutableStateOf(
            listOf(
                Sucursal(
                    id = "san_bernardo",
                    nombre = "San Bernardo",
                    direccion = "Av. América 450, San Bernardo",
                    diasSinCambios = 0,
                    tiempoTranscurridoFormateado = "Hace 15 minutos",
                    cambioLlegados = 3,
                    cambioVendidos = 5,
                    cambioMerma = 1,
                    productos = listOf(
                        Producto("sb-1", "Polera Kaiju Godzilla", "Ropa", 15990.0, 15, 5, sucursal = "San Bernardo"),
                        Producto("sb-2", "Figura Ghidorah Articulada", "Coleccionables", 34990.0, 4, 2, sucursal = "San Bernardo"),
                        Producto("sb-3", "Manga Kaiju No. 8 Vol. 1", "Libros", 8990.0, 20, 5, sucursal = "San Bernardo")
                    )
                ),
                Sucursal(
                    id = "central",
                    nombre = "Central",
                    direccion = "Av. Libertador B. O'Higgins 1200, Santiago",
                    diasSinCambios = 1,
                    tiempoTranscurridoFormateado = "Hace 1 hora",
                    cambioLlegados = 10,
                    cambioVendidos = 12,
                    cambioMerma = 0,
                    productos = listOf(
                        Producto("ct-1", "Polera Kaiju Classic", "Ropa", 15990.0, 25, 5, sucursal = "Central"),
                        Producto("ct-2", "Figura Godzilla 15cm", "Coleccionables", 29990.0, 8, 5, sucursal = "Central")
                    )
                ),
                Sucursal(
                    id = "norte",
                    nombre = "Norte",
                    direccion = "Av. Independencia 3400, Conchalí",
                    diasSinCambios = 0,
                    tiempoTranscurridoFormateado = "Hace 45 minutos",
                    cambioLlegados = 5,
                    cambioVendidos = 3,
                    cambioMerma = 0,
                    productos = listOf(
                        Producto("nt-1", "Taza Logo Kaiju", "Accesorios", 7990.0, 18, 5, sucursal = "Norte")
                    )
                ),
                Sucursal(
                    id = "maipu",
                    nombre = "Maipú",
                    direccion = "Av. Pajaritos 2100, Maipú",
                    diasSinCambios = 2,
                    tiempoTranscurridoFormateado = "Hace 2 días",
                    cambioLlegados = 0,
                    cambioVendidos = 8,
                    cambioMerma = 0,
                    productos = listOf(
                        Producto("mp-1", "Taza Logo Kaiju Edición Limitada", "Accesorios", 7990.0, 12, 4, sucursal = "Maipú"),
                        Producto("mp-2", "Figura Mechagodzilla 20cm", "Coleccionables", 39990.0, 2, 3, sucursal = "Maipú"),
                        Producto("mp-3", "Polera Mothra Vintage", "Ropa", 16990.0, 8, 3, sucursal = "Maipú")
                    )
                ),
                Sucursal(
                    id = "persa_biobio",
                    nombre = "Persa BioBío",
                    direccion = "Placer 650, Franklin / BioBío",
                    diasSinCambios = 35,
                    tiempoTranscurridoFormateado = "Hace 35 días",
                    cambioLlegados = 0,
                    cambioVendidos = 0,
                    cambioMerma = 2,
                    productos = listOf(
                        Producto("bb-1", "Poster Kaiju Retro 60s", "Decoración", 4990.0, 25, 5, sucursal = "Persa BioBío"),
                        Producto("bb-2", "Llavero Rodan Metálico", "Accesorios", 3990.0, 30, 10, sucursal = "Persa BioBío"),
                        Producto("bb-3", "Figura Godzilla Shin 18cm", "Coleccionables", 28990.0, 5, 2, sucursal = "Persa BioBío")
                    )
                )
            )
        )
    }

    val sucursalesActualizadas = remember(sucursales, productosLocales, tiemposSucursales) {
        sucursales.map { suc ->
            val tiempoActualizado = tiemposSucursales[suc.id] ?: suc.tiempoTranscurridoFormateado
            if (suc.id == "san_bernardo") {
                val base = listOf(
                    Producto("sb-1", "Polera Kaiju Godzilla", "Ropa", 15990.0, 15, 5, sucursal = "San Bernardo"),
                    Producto("sb-2", "Figura Ghidorah Articulada", "Coleccionables", 34990.0, 4, 2, sucursal = "San Bernardo"),
                    Producto("sb-3", "Manga Kaiju No. 8 Vol. 1", "Libros", 8990.0, 20, 5, sucursal = "San Bernardo")
                )
                suc.copy(productos = base + productosLocales, tiempoTranscurridoFormateado = tiempoActualizado)
            } else {
                suc.copy(tiempoTranscurridoFormateado = tiempoActualizado)
            }
        }
    }

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var textoBusqueda by remember { mutableStateOf("") }
    var mensajeSincronizacion by remember { mutableStateOf(false) }

    val sucursalActual = sucursalesActualizadas[selectedTabIndex]

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
                sucursales.forEachIndexed { index, sucursal ->
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

                            if (!sucursalActual.estaDisponible) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(RojoSuaveTexto.copy(alpha = 0.15f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "No disponible",
                                        color = RojoSuaveTexto,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
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

                if (!sucursalActual.estaDisponible) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(RojoSuaveFondo)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = RojoSuaveTexto
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Sucursal sin movimientos > 31 días. Su inventario completo permanece totalmente visible a continuación.",
                            color = RojoSuaveTexto,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Text(
                    text = "Inventario de ${sucursalActual.nombre} (${productosFiltrados.size} productos):",
                    style = MaterialTheme.typography.titleSmall
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(productosFiltrados, key = { it.id }) { producto ->
                        ItemProductoExtraCard(producto = producto)
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
private fun ItemProductoExtraCard(producto: Producto) {
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
            Column {
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
            }
            Text(
                text = "${producto.cantidadDisponible} unidades",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
