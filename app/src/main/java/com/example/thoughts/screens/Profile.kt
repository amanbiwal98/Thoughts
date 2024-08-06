package com.example.thoughts.screens

import android.content.pm.PackageManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.thoughts.R
import com.example.thoughts.Utils.SharedPref
import com.example.thoughts.item_view.ThoughtItem
import com.example.thoughts.model.UserModel
import com.example.thoughts.navigation.Routes
import com.example.thoughts.viewmodel.AuthViewModel
import com.example.thoughts.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import okhttp3.Route

@Composable
fun Profile(navController: NavHostController){
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)

    val userViewModel: UserViewModel = viewModel()
    val thoughts by userViewModel.thoughts.observeAsState(null)

    val followersList by userViewModel.followerList.observeAsState(null)
    val followingList by userViewModel.followingList.observeAsState(null)

    var currentUserId = ""

    if (FirebaseAuth.getInstance().currentUser != null) {
        currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    }

    if (currentUserId!=""){
        userViewModel.getFollowers(currentUserId)
        userViewModel.getFollowing(currentUserId)
    }

    val user = UserModel(
        name = SharedPref.getName(context),
        username = SharedPref.getuserName(context),
        imageUri = SharedPref.getImage(context)
    )

    if (firebaseUser!=null)
        userViewModel.fetchThoughts(firebaseUser!!.uid)

    LaunchedEffect(firebaseUser) {
        if (firebaseUser == null){
            navController.navigate(Routes.Login.routes){
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }

    LazyColumn() {
        item {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {

                val (profile, logo, userName,
                    bio, followers, following,
                    button) = createRefs()


                Text(text = SharedPref.getName(context), style = TextStyle(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp
                ), modifier = Modifier.constrainAs(profile) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start, margin = 14.dp)
                    end.linkTo(logo.start, margin = 16.dp)//
                    width = Dimension.fillToConstraints
                })


                Image(painter = rememberAsyncImagePainter(model = SharedPref.getImage(context)), contentDescription = "close",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .constrainAs(logo) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        }
                )

                Text(text = SharedPref.getuserName(context), style = TextStyle(
                    fontSize = 16.sp
                ), modifier = Modifier.constrainAs(userName) {
                    top.linkTo(profile.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 14.dp)
                    end.linkTo(parent.end, margin = 14.dp)//
                    width = Dimension.fillToConstraints
                })

                Text(text = SharedPref.getBio(context), style = TextStyle(
                    fontSize = 16.sp
                ), modifier = Modifier.constrainAs(bio) {
                    top.linkTo(userName.bottom, margin = 10.dp)
                    start.linkTo(parent.start, margin = 14.dp)
                    end.linkTo(parent.end, margin = 134.dp)
                    width = Dimension.fillToConstraints
                })

                Text(text = "${followersList?.size} Followers || ${followingList?.size} Following", style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    fontSize = 15.sp
                ), modifier = Modifier.constrainAs(followers) {
                    top.linkTo(bio.bottom, margin = 8.dp)
                    start.linkTo(parent.start, margin = 14.dp)
                })

                ElevatedButton(onClick = { authViewModel.logout() },
                    modifier = Modifier
                        .constrainAs(button) {
                            top.linkTo(followers.bottom, margin = 16.dp)
                            start.linkTo(parent.start, margin = 14.dp)
                        }
                        .width(140.dp),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text(text = "Logout")
                }

            }
            Divider(color = Color.LightGray, thickness = 1.dp)
        }

        items(thoughts?: emptyList()){pair->
            ThoughtItem(thought = pair, users = user, navController = navController, userId = SharedPref.getuserName(context))
        }
    }
}

