package com.example.kaijuapp_grupo4.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kaijuapp_grupo4.ui.components.ImagenInteligente
import com.example.kaijuapp_grupo4.viewmodel.MainViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var modoAlertasActivo by remember { mutableStateOf(false) }
    var mostrarMensajeConfirmacion by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            val file = File(context.cacheDir, "camara_${System.currentTimeMillis()}.jpg")
            file.outputStream().use { out ->
                bitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, out)
            }
            imageUri = Uri.fromFile(file)
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            try {
                cameraLauncher.launch(null)
            } catch (_: Exception) {
                android.widget.Toast.makeText(context, "Cámara no disponible. Abriendo galería...", android.widget.Toast.LENGTH_SHORT).show()
                galleryLauncher.launch("image/*")
            }
        } else {
            android.widget.Toast.makeText(context, "Permiso de cámara denegado", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    val colorBotonAnimado by animateColorAsState(
        targetValue = if (modoAlertasActivo) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary,
        animationSpec = tween(durationMillis = 500),
        label = "colorAnim"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil de Encargado") },
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ImagenInteligente(imageUri = imageUri)

            Text(
                text = "Administrador Kaiju",
                style = MaterialTheme.typography.titleLarge
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { galleryLauncher.launch("image/*") }) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Galería")
                }
                OutlinedButton(onClick = {
                    if (android.os.Build.FINGERPRINT.startsWith("generic")
                        || android.os.Build.MODEL.contains("google_sdk")
                        || android.os.Build.MODEL.contains("Emulator")
                        || android.os.Build.MODEL.contains("Android SDK built for x86")
                        || android.os.Build.HARDWARE.contains("goldfish")
                        || android.os.Build.HARDWARE.contains("ranchu")
                        || android.os.Build.PRODUCT.contains("sdk")) {
                        android.widget.Toast.makeText(context, "El emulador no soporta cámara. Abriendo galería...", android.widget.Toast.LENGTH_SHORT).show()
                        galleryLauncher.launch("image/*")
                    } else {
                        try {
                            cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                        } catch (_: Exception) {
                            android.widget.Toast.makeText(context, "Cámara no disponible. Abriendo galería...", android.widget.Toast.LENGTH_SHORT).show()
                            galleryLauncher.launch("image/*")
                        }
                    }
                }) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Cámara")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    modoAlertasActivo = !modoAlertasActivo
                    mostrarMensajeConfirmacion = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorBotonAnimado),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (modoAlertasActivo) "Alertas de Stock: ACTIVADAS" else "Activar Alertas de Stock")
            }

            AnimatedVisibility(visible = mostrarMensajeConfirmacion) {
                Text(
                    text = "¡Preferencia guardada exitosamente!",
                    color = Color(0xFF4CAF50),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}