package com.example.thoughts.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.thoughts.Utils.SharedPref
import com.example.thoughts.item_view.ThoughtItem
import com.example.thoughts.navigation.Routes
import com.example.thoughts.viewmodel.AuthViewModel
import com.example.thoughts.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth


@Composable
fun OtherUsersProfile(navController: NavHostController, uid: String) {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)

    val userViewModel: UserViewModel = viewModel()
    val thoughts by userViewModel.thoughts.observeAsState(null)
    val users by userViewModel.users.observeAsState(null)
    val followersList by userViewModel.followerList.observeAsState(null)
    val followingList by userViewModel.followingList.observeAsState(null)


    userViewModel.fetchThoughts(uid)
    userViewModel.fetchUser(uid)
    userViewModel.getFollowers(uid)
    userViewModel.getFollowing(uid)

    var currentUserId = ""

    if (FirebaseAuth.getInstance().currentUser != null) {
        currentUserId = FirebaseAuth.getInstance().currentUser!!.uid
    }

    LaunchedEffect(firebaseUser) {
        if (firebaseUser == null) {
            navController.navigate(Routes.Login.routes) {
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

                val (
                    profile, logo, userName,
                    bio, followers, following,
                    button,
                ) = createRefs()


                Text(text = users!!.name, style = TextStyle(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp
                ), modifier = Modifier.constrainAs(profile) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start, margin = 14.dp)
                    end.linkTo(logo.start, margin = 16.dp)
                    width = Dimension.fillToConstraints
                })


                Image(painter = rememberAsyncImagePainter(model = users!!.imageUri),
                    contentDescription = "user image",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .constrainAs(logo) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        }
                )

                Text(text = users!!.username, style = TextStyle(
                    fontSize = 16.sp
                ), modifier = Modifier.constrainAs(userName) {
                    top.linkTo(profile.bottom, margin = 4.dp)
                    start.linkTo(parent.start, margin = 14.dp)
                    end.linkTo(parent.end, margin = 14.dp)//
                    width = Dimension.fillToConstraints
                })

                Text(text = users!!.bio, style = TextStyle(
                    fontSize = 16.sp
                ),  modifier = Modifier.constrainAs(bio) {
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

                ElevatedButton(onClick = {
                    if (currentUserId != "") {
                        if(followersList != null && followersList!!.isNotEmpty() && followersList!!.contains(currentUserId)){
                            userViewModel.unfollowUsers(uid, currentUserId)
                        }else{
                            userViewModel.followUsers(uid, currentUserId)
                        }
                    }
                },
                    modifier = Modifier.constrainAs(button) {
                        top.linkTo(followers.bottom, margin = 16.dp)
                        start.linkTo(parent.start, margin = 14.dp)
                    }
                        .width(140.dp),
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text(
                        text = if (followersList != null && followersList!!.isNotEmpty() && followersList!!.contains(
                                currentUserId
                            )
                        ) "Following" else "Follow"
                    )
                }


            }
            Divider(color = Color.LightGray, thickness = 1.dp)
        }

        if (thoughts != null && users != null) {
            items(thoughts ?: emptyList()) { pair ->
                ThoughtItem(
                    thought = pair,
                    users = users!!,
                    navController = navController,
                    userId = SharedPref.getuserName(context)
                )
            }
        }
    }
}