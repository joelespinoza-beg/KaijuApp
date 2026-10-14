package com.example.kaijuapp_grupo4.ui.screens

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kaijuapp_grupo4.model.Producto
import com.example.kaijuapp_grupo4.navigation.Screen
import com.example.kaijuapp_grupo4.viewmodel.InventarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenVentaScreen(
    navController: NavController,
    inventarioViewModel: InventarioViewModel
) {
    val productos by inventarioViewModel.productos.collectAsState()

    var productoSeleccionado by remember { mutableStateOf<Producto?>(null) }
    var cantidadTexto by remember { mutableStateOf("1") }
    var expanded by remember { mutableStateOf(false) }
    var errorMensaje by remember { mutableStateOf<String?>(null) }
    var ventaExitosa by remember { mutableStateOf(false) }

    val cantidadInt = cantidadTexto.toIntOrNull() ?: 0
    val totalVenta = (productoSeleccionado?.precio ?: 0.0) * cantidadInt

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
                                productoSeleccionado = producto
                                expanded = false
                                errorMensaje = null
                                ventaExitosa = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = cantidadTexto,
                onValueChange = {
                    cantidadTexto = it
                    errorMensaje = null
                    ventaExitosa = false
                },
                label = { Text("Cantidad a vender") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            productoSeleccionado?.let { prod ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Resumen de la Operación",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Producto: ${prod.nombre}")
                        Text(text = "Precio Unitario: $${prod.precio}")
                        Text(text = "Stock Actual: ${prod.cantidadDisponible} unidades")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Total a Cobrar: $$totalVenta",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            errorMensaje?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }

            if (ventaExitosa) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50)
                    )
                    Text(
                        text = " ¡Venta realizada y stock actualizado!",
                        color = Color(0xFF4CAF50),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Button(
                onClick = {
                    val prod = productoSeleccionado
                    if (prod == null) {
                        errorMensaje = "Por favor seleccione un producto"
                    } else if (cantidadInt <= 0) {
                        errorMensaje = "Ingrese una cantidad válida mayor a 0"
                    } else if (cantidadInt > prod.cantidadDisponible) {
                        errorMensaje = "Stock insuficiente (Disponible: ${prod.cantidadDisponible})"
                    } else {
                        val exito = inventarioViewModel.registrarVenta(prod.id, cantidadInt)
                        if (exito) {
                            ventaExitosa = true
                            errorMensaje = null
                            productoSeleccionado = prod.copy(
                                cantidadDisponible = prod.cantidadDisponible - cantidadInt
                            )
                        } else {
                            errorMensaje = "Error al procesar la venta"
                        }
                    }
                },
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