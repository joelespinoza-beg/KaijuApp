package com.example.kaijuapp_grupo4.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kaijuapp_grupo4.navigation.Screen
import com.example.kaijuapp_grupo4.ui.components.formatearPrecio
import com.example.kaijuapp_grupo4.ui.theme.VerdeExito
import com.example.kaijuapp_grupo4.viewmodel.InventarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenVentaScreen(
    navController: NavController,
    inventarioViewModel: InventarioViewModel
) {
    val productos by inventarioViewModel.productos.collectAsState()
    val ventaState by inventarioViewModel.ventaState.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    // El producto se busca por id en la lista actual, asi siempre muestra el stock real
    val productoSeleccionado = productos.find { it.id == ventaState.productoId }
    val cantidad = ventaState.cantidad.toIntOrNull() ?: 0
    val totalVenta = (productoSeleccionado?.precio ?: 0.0) * cantidad

    DisposableEffect(Unit) {
        onDispose { inventarioViewModel.reiniciarVenta() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Venta Kaiju") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Seleccione el producto a vender:",
                style = MaterialTheme.typography.titleMedium
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = productoSeleccionado?.nombre ?: "Seleccionar Producto",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    productos.forEach { producto ->
                        DropdownMenuItem(
                            text = { Text("${producto.nombre} (Disp: ${producto.cantidadDisponible})") },
                            onClick = {
                                inventarioViewModel.seleccionarProductoVenta(producto.id)
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = ventaState.cantidad,
                onValueChange = inventarioViewModel::onCantidadVentaChange,
                label = { Text("Cantidad a vender") },
                singleLine = true,
                isError = ventaState.error != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            productoSeleccionado?.let { prod ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Resumen de la Operación",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Producto: ${prod.nombre}")
                        Text(text = "Precio Unitario: ${formatearPrecio(prod.precio)}")
                        Text(text = "Stock Actual: ${prod.cantidadDisponible} unidades")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Total a Cobrar: ${formatearPrecio(totalVenta)}",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            AnimatedVisibility(visible = ventaState.error != null) {
                Text(
                    text = ventaState.error ?: "",
                    color = MaterialTheme.colorScheme.error
                )
            }

            AnimatedVisibility(visible = ventaState.exito) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = VerdeExito
                    )
                    Text(
                        text = " ¡Venta realizada y stock actualizado!",
                        color = VerdeExito,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Button(
                onClick = inventarioViewModel::confirmarVenta,
                modifier = Modifier.fillMaxWidth(),
                enabled = productoSeleccionado != null
            ) {
                Text("Confirmar Venta")
            }

            Button(
                onClick = { navController.navigate(Screen.Inventario.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ir al Inventario")
            }
        }
    }
}

