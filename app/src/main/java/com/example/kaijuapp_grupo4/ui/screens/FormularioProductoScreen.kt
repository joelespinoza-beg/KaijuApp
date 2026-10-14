package com.example.kaijuapp_grupo4.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kaijuapp_grupo4.navigation.Screen
import com.example.kaijuapp_grupo4.viewmodel.InventarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioProductoScreen(
    navController: NavController,
    inventarioViewModel: InventarioViewModel
) {
    val formState by inventarioViewModel.formState.collectAsState()

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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Ingrese los datos del nuevo producto:",
                style = MaterialTheme.typography.titleMedium
            )

            OutlinedTextField(
                value = formState.nombre,
                onValueChange = inventarioViewModel::onNombreChange,
                label = { Text("Nombre del Producto") },
                isError = formState.errores.nombre != null,
                supportingText = {
                    formState.errores.nombre?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = formState.categoria,
                onValueChange = inventarioViewModel::onCategoriaChange,
                label = { Text("Categoría (ej. Ropa, Coleccionables, Accesorios)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = formState.precio,
                onValueChange = inventarioViewModel::onPrecioChange,
                label = { Text("Precio ($)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = formState.errores.precio != null,
                supportingText = {
                    formState.errores.precio?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = formState.cantidad,
                onValueChange = inventarioViewModel::onCantidadChange,
                label = { Text("Cantidad Inicial en Stock") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = formState.errores.cantidad != null,
                supportingText = {
                    formState.errores.cantidad?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (inventarioViewModel.validarFormulario()) {
                        navController.navigate(Screen.Inventario.route) {
                            popUpTo(Screen.Home.route)
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
