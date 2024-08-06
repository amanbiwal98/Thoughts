package com.example.thoughts.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.thoughts.R
import com.example.thoughts.item_view.ThoughtItem
import com.example.thoughts.viewmodel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Home(navController: NavHostController){
    val context = LocalContext.current
    val homeViewModel: HomeViewModel = viewModel()
    val thoughtsAndUsers by homeViewModel.thoughtsAndUsers.observeAsState(null)

    LazyColumn {
        item {
            ConstraintLayout(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
            ) {
                val (logo) = createRefs()
                Image(painter = painterResource(id = R.drawable.logo), contentDescription = "logo",
                    modifier = Modifier
                        .size(80.dp)
                        .constrainAs(logo) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )
            }
        }
        items(thoughtsAndUsers ?: emptyList()){pairs ->
            ThoughtItem(
                thought = pairs.first,
                users = pairs.second,
                navController = navController,
                FirebaseAuth.getInstance().currentUser!!.uid
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowHome(){
//    Home()
}