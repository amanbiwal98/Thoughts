package com.example.thoughts.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.thoughts.R
import com.example.thoughts.navigation.Routes
import com.example.thoughts.viewmodel.AuthViewModel

@Composable
fun Register(navController: NavController){
    var name by remember {
        mutableStateOf("")
    }
    var userName by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var bio by remember {
        mutableStateOf("")
    }
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)

    val context = LocalContext.current

    val permissionRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
        Manifest.permission.READ_MEDIA_IMAGES
    }else{
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
    
    LaunchedEffect(firebaseUser) {
        if (firebaseUser!=null){
            navController.navigate(Routes.BottomNav.routes){
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }

    val laauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            uri: Uri? ->  
            imageUri = uri
        }

    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
            isGranted: Boolean ->
            if (isGranted){
                Toast.makeText(context, "Permission Granted Successfully", Toast.LENGTH_LONG).show()
            } else{
                Toast.makeText(context, "Permission Denied", Toast.LENGTH_LONG).show()
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Register", style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp)
        )
        Spacer(modifier = Modifier.padding(1.dp))
        Image(painter = if (imageUri == null ){
            painterResource(id = R.drawable.man)
        } else rememberAsyncImagePainter(model = imageUri), contentDescription = "person",
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .clickable {
                    val isGranted = ContextCompat.checkSelfPermission(
                        context, permissionRequest
                    ) == PackageManager.PERMISSION_GRANTED

                    if (isGranted) {
                        laauncher.launch("image/*")
                    } else {
                        permissionLauncher.launch(permissionRequest)
                    }
                }, contentScale = ContentScale.Crop)

        Spacer(modifier = Modifier.padding(25.dp))
        OutlinedTextField(
            value = name, onValueChange = {name = it}, label = {
                Text(text = "Name")
            }, keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ), singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(1.dp))
        OutlinedTextField(
            value = userName, onValueChange = {userName = it}, label = {
                Text(text = "UserName")
            }, keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ), singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(1.dp))
        OutlinedTextField(
            value = email, onValueChange = {email = it}, label = {
                Text(text = "Email")
            }, keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ), singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(1.dp))
        OutlinedTextField(
            value = password, onValueChange = {password = it}, label = {
                Text(text = "Password")
            }, keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ), singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(1.dp))
        OutlinedTextField(
            value = bio, onValueChange = {bio = it}, label = {
                Text(text = "Bio")
            }, keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ), singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.padding(10.dp))

        ElevatedButton(onClick = {
                 if (name.isEmpty() || email.isEmpty() || password.isEmpty() || userName.isEmpty() || bio.isEmpty() || imageUri == null){
                     Toast.makeText(context, "Please enter all detais", Toast.LENGTH_LONG).show()
                 }else{
                     authViewModel.register(name, userName, email, password, bio, imageUri!!, context)
                 }
        }, modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors()) {
            Text(text = "Register", style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp)
            )
        }

        Spacer(modifier = Modifier.padding(10.dp))

        TextButton(onClick = {
              navController.navigate(Routes.Login.routes){
                  popUpTo(navController.graph.startDestinationId)
                  launchSingleTop = true
              }
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Existing User? Please Login", style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterView(){
//    Register()
}