package com.example.kaijuapp_grupo4.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kaijuapp_grupo4.navigation.Screen
import com.example.kaijuapp_grupo4.viewmodel.InventarioViewModel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.core.content.FileProvider
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioProductoScreen(
    navController: NavController,
    inventarioViewModel: InventarioViewModel
) {
    val formState by inventarioViewModel.formState.collectAsState()
    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        inventarioViewModel.onImagenChange(uri)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            val file = File(context.cacheDir, "prod_${System.currentTimeMillis()}.jpg")
            file.outputStream().use { out ->
                bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, out)
            }
            inventarioViewModel.onImagenChange(Uri.fromFile(file))
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            try {
                cameraLauncher.launch(null)
            } catch (_: Exception) {
                Toast.makeText(context, "Cámara no disponible. Abriendo galería...", Toast.LENGTH_SHORT).show()
                galleryLauncher.launch("image/*")
            }
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Ingrese los datos del nuevo producto:",
                style = MaterialTheme.typography.titleMedium
            )

            // Previsualización de la foto del producto
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

            // Botones para elegir Galería o Cámara
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Galería")
                }
                OutlinedButton(
                    onClick = {
                        cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Cámara")
                }
            }

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
