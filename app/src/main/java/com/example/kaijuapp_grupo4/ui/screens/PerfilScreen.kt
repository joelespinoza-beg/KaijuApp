package com.example.kaijuapp_grupo4.ui.screens

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kaijuapp_grupo4.ui.components.ImagenInteligente
import com.example.kaijuapp_grupo4.ui.components.rememberSelectorImagen
import com.example.kaijuapp_grupo4.ui.theme.VerdeExito
import com.example.kaijuapp_grupo4.viewmodel.PerfilViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    navController: NavController,
    perfilViewModel: PerfilViewModel
) {
    val fotoPerfil by perfilViewModel.fotoPerfil.collectAsState()
    val alertasActivas by perfilViewModel.alertasActivas.collectAsState()
    var mostrarMensaje by remember { mutableStateOf(false) }

    val selectorImagen = rememberSelectorImagen(prefijoArchivo = "perfil") { uri ->
        perfilViewModel.guardarFoto(uri)
    }

    val colorBoton by animateColorAsState(
        targetValue = if (alertasActivas) VerdeExito else MaterialTheme.colorScheme.primary,
        animationSpec = tween(durationMillis = 500),
        label = "colorAlertas"
    )

    LaunchedEffect(mostrarMensaje) {
        if (mostrarMensaje) {
            delay(2000)
            mostrarMensaje = false
        }
    }

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
            ImagenInteligente(imageUri = fotoPerfil)

            Text(
                text = "Administrador Kaiju",
                style = MaterialTheme.typography.titleLarge
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = selectorImagen.abrirGaleria) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Galería")
                }
                OutlinedButton(onClick = selectorImagen.abrirCamara) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Cámara")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    perfilViewModel.alternarAlertas()
                    mostrarMensaje = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorBoton),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (alertasActivas) "Alertas de Stock: ACTIVADAS" else "Activar Alertas de Stock")
            }

            AnimatedVisibility(visible = mostrarMensaje) {
                Text(
                    text = "¡Preferencia guardada exitosamente!",
                    color = VerdeExito,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

