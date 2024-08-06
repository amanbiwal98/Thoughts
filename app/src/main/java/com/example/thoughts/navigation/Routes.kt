package com.example.thoughts.navigation

sealed class Routes (val routes: String){
    object Home: Routes("home")
    object AddThoughts: Routes("add_Thoughts")
    object BottomNav: Routes("bottom_nav")
    object Notification: Routes("notification")
    object Profile: Routes("profile")
    object Search: Routes("search")
    object Splash: Routes("splash")
    object Login: Routes("login")
    object Register: Routes("register")
    object OtherUsers: Routes("register/{data}")
    object FullImage: Routes("fullImage/{imageUrl}")
}