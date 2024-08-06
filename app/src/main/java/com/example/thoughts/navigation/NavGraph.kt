package com.example.thoughts.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.thoughts.screens.AddThoughts
import com.example.thoughts.screens.BottomNav
import com.example.thoughts.screens.FullScreenImage
import com.example.thoughts.screens.Home
import com.example.thoughts.screens.Login
import com.example.thoughts.screens.Notification
import com.example.thoughts.screens.OtherUsersProfile
import com.example.thoughts.screens.Profile
import com.example.thoughts.screens.Register
import com.example.thoughts.screens.Search
import com.example.thoughts.screens.Splash

@Composable
fun NavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = Routes.Splash.routes
    ) {
        composable(Routes.Splash.routes){
            Splash(navController)
        }
        composable(Routes.BottomNav.routes){
            BottomNav(navController)
        }
        composable(Routes.AddThoughts.routes){
            AddThoughts(navController)
        }
        composable(Routes.Home.routes){
            Home(navController)
        }
        composable(Routes.Notification.routes){
            Notification()
        }
        composable(Routes.Profile.routes){
            Profile(navController)
        }
        composable(Routes.Search.routes){
            Search(navController)
        }
        composable(Routes.Login.routes){
            Login(navController)
        }
        composable(Routes.Register.routes){
            Register(navController)
        }
        composable(Routes.OtherUsers.routes){
            val data = it.arguments!!.getString("data")
            OtherUsersProfile(navController, data!!)
        }
        composable(Routes.FullImage.routes){
            val imageUrl = it.arguments!!.getString("imageUrl")
            FullScreenImage(navController, imageUrl!!)
        }
    }
}