package com.example.kaijuapp_grupo4.ui.theme

import android.net.wifi.hotspot2.pps.HomeSp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun HomeScreen() {
    Scaffold(
        topBar = {
            TopAppBar(tittle = { Text("Mi App Kotlin")})
        }
    ) {
        innerPading ->
        Column(
            modifier = Modifier
                .padding(innerPading)
                .fillMaxSize()
                .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
        ) { }
    }
}