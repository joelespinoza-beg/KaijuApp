package com.example.kaijuapp_grupo4.ui.components

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import java.io.File

class SelectorImagen(
    val abrirGaleria: () -> Unit,
    val abrirCamara: () -> Unit
)

@Composable
fun rememberSelectorImagen(
    prefijoArchivo: String,
    onImagenSeleccionada: (Uri) -> Unit
): SelectorImagen {
    val context = LocalContext.current
    val alSeleccionar by rememberUpdatedState(onImagenSeleccionada)

    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val copia = copiarAAlmacenamientoInterno(context, uri, prefijoArchivo)
            if (copia != null) alSeleccionar(copia)
            else Toast.makeText(context, "No se pudo cargar la imagen", Toast.LENGTH_SHORT).show()
        }
    }

    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            val uri = guardarBitmap(context, bitmap, prefijoArchivo)
            if (uri != null) alSeleccionar(uri)
            else Toast.makeText(context, "No se pudo guardar la foto", Toast.LENGTH_SHORT).show()
        }
    }

    val permisoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            try {
                camaraLauncher.launch(null)
            } catch (_: Exception) {
                Toast.makeText(context, "Cámara no disponible. Abriendo galería...", Toast.LENGTH_SHORT).show()
                galeriaLauncher.launch("image/*")
            }
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    return SelectorImagen(
        abrirGaleria = { galeriaLauncher.launch("image/*") },
        abrirCamara = {
            if (esEmulador()) {
                Toast.makeText(context, "El emulador no soporta cámara. Abriendo galería...", Toast.LENGTH_SHORT).show()
                galeriaLauncher.launch("image/*")
            } else {
                permisoLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    )
}

private fun esEmulador(): Boolean =
    Build.FINGERPRINT.startsWith("generic") ||
            Build.MODEL.contains("google_sdk") ||
            Build.MODEL.contains("Emulator") ||
            Build.MODEL.contains("Android SDK built for x86") ||
            Build.HARDWARE.contains("goldfish") ||
            Build.HARDWARE.contains("ranchu") ||
            Build.PRODUCT.contains("sdk")

private fun copiarAAlmacenamientoInterno(context: Context, origen: Uri, prefijo: String): Uri? {
    return try {
        val entrada = context.contentResolver.openInputStream(origen) ?: return null
        val archivo = File(context.filesDir, "${prefijo}_${System.currentTimeMillis()}.jpg")
        entrada.use { input -> archivo.outputStream().use { salida -> input.copyTo(salida) } }
        Uri.fromFile(archivo)
    } catch (_: Exception) {
        null
    }
}

private fun guardarBitmap(context: Context, bitmap: Bitmap, prefijo: String): Uri? {
    return try {
        val archivo = File(context.filesDir, "${prefijo}_${System.currentTimeMillis()}.jpg")
        archivo.outputStream().use { salida -> bitmap.compress(Bitmap.CompressFormat.JPEG, 90, salida) }
        Uri.fromFile(archivo)
    } catch (_: Exception) {
        null
    }
}