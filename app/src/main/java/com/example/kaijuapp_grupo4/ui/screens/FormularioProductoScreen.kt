package com.example.kaijuapp_grupo4.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.kaijuapp_grupo4.navigation.Screen
import com.example.kaijuapp_grupo4.ui.components.rememberSelectorImagen
import com.example.kaijuapp_grupo4.viewmodel.InventarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioProductoScreen(
    navController: NavController,
    inventarioViewModel: InventarioViewModel
) {
    val formState by inventarioViewModel.formState.collectAsState()

    val selectorImagen = rememberSelectorImagen(prefijoArchivo = "producto") { uri ->
        inventarioViewModel.onImagenChange(uri)
    }

    DisposableEffect(Unit) {
        onDispose { inventarioViewModel.limpiarFormulario() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Producto Kaiju") },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Ingrese los datos del nuevo producto:",
                style = MaterialTheme.typography.titleMedium
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (formState.imagenUri != null) {
                        AsyncImage(
                            model = formState.imagenUri,
                            contentDescription = "Foto del producto",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.CameraAlt, contentDescription = "Sin foto")
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Sin foto seleccionada", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = selectorImagen.abrirGaleria,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Galería")
                }
                OutlinedButton(
                    onClick = selectorImagen.abrirCamara,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Cámara")
                }
            }

            CampoFormulario(
                valor = formState.nombre,
                onValorChange = inventarioViewModel::onNombreChange,
                etiqueta = "Nombre del Producto *",
                error = formState.errores.nombre
            )

            CampoFormulario(
                valor = formState.categoria,
                onValorChange = inventarioViewModel::onCategoriaChange,
                etiqueta = "Categoría (Opcional)",
                error = null
            )

            CampoFormulario(
                valor = formState.precio,
                onValorChange = inventarioViewModel::onPrecioChange,
                etiqueta = "Precio ($) *",
                error = formState.errores.precio,
                tipoTeclado = KeyboardType.Decimal
            )

            CampoFormulario(
                valor = formState.cantidad,
                onValorChange = inventarioViewModel::onCantidadChange,
                etiqueta = "Cantidad Inicial en Stock *",
                error = formState.errores.cantidad,
                tipoTeclado = KeyboardType.Number
            )

            CampoFormulario(
                valor = formState.stockMinimo,
                onValorChange = inventarioViewModel::onStockMinimoChange,
                etiqueta = "Stock Mínimo (alerta) *",
                error = formState.errores.stockMinimo,
                tipoTeclado = KeyboardType.Number,
                accionTeclado = ImeAction.Done
            )

            Button(
                onClick = {
                    if (inventarioViewModel.validarFormulario()) {
                        navController.navigate(Screen.Inventario.route) {
                            popUpTo(Screen.Home.route)
                            launchSingleTop = true
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Guardar Producto")
            }
        }
    }
}

@Composable
private fun CampoFormulario(
    valor: String,
    onValorChange: (String) -> Unit,
    etiqueta: String,
    error: String?,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    accionTeclado: ImeAction = ImeAction.Next
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(etiqueta) },
        singleLine = true,
        isError = error != null,
        trailingIcon = {
            if (error != null) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = "Error",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        },
        supportingText = {
            if (error != null) {
                Text(text = error)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = tipoTeclado, imeAction = accionTeclado),
        colors = OutlinedTextFieldDefaults.colors(
            errorContainerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.25f)
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

