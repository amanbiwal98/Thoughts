package com.example.thoughts.screens

import android.widget.Toast
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.thoughts.R
import com.example.thoughts.navigation.Routes
import com.example.thoughts.viewmodel.AuthViewModel

@Composable
fun Login(navController: NavController){

    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState()
    val error by authViewModel.error.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(firebaseUser) {
        if (firebaseUser!=null){
            navController.navigate(Routes.BottomNav.routes){
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }

    error?.let {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
    }

    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Login", style = TextStyle(
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp)
        )
        Spacer(modifier = Modifier.padding(1.dp))
        Image(painter = painterResource(id = R.drawable.man), contentDescription = "person",
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
                .clickable {

                }, contentScale = ContentScale.Crop)

        Spacer(modifier = Modifier.padding(25.dp))
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
        Spacer(modifier = Modifier.padding(10.dp))

        ElevatedButton(onClick = {
            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(context, "Please enter all details", Toast.LENGTH_LONG).show()
            }else{
                authViewModel.login(email, password, context)
            }

        }, modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors()) {
            Text(text = "Login", style = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp)
            )
        }

        Spacer(modifier = Modifier.padding(10.dp))

        TextButton(onClick = {
            navController.navigate(Routes.Register.routes){
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "New User? Please Register", style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginView(){
//    Login()
}