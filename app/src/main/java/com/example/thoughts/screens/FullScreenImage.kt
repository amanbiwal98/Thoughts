package com.example.thoughts.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter

@Composable
fun FullScreenImage(navController: NavHostController, imageUrl: String) {
    Box (
        modifier = Modifier.fillMaxSize()
            .background(Color.Black)
            .wrapContentSize()
    ){
        Image(painter = rememberAsyncImagePainter(model = imageUrl), contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit)
    }
}