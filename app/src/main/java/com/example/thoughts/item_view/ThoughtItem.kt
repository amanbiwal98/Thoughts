package com.example.thoughts.item_view

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.thoughts.R
import com.example.thoughts.Utils.SharedPref
import com.example.thoughts.model.ThoughtModel
import com.example.thoughts.model.UserModel
import com.example.thoughts.navigation.Routes

@Composable
fun ThoughtItem(
    thought: ThoughtModel,
    users: UserModel,
    navController: NavHostController,
    userId: String
) {
    val context = LocalContext.current

    Column {
        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            val(userImage, userName, date, time, title, image ) =createRefs()

            Image(painter = rememberAsyncImagePainter(model = users.imageUri), contentDescription = "user image",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .constrainAs(userImage) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
            )

            Text(text = users.username, style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            ), modifier = Modifier.constrainAs(userName) {
                top.linkTo(userImage.top)
                start.linkTo(userImage.end, margin = 14.dp)
                end.linkTo(parent.end, margin = 14.dp)
                width = Dimension.fillToConstraints
            })

            Text(text = thought.thought, style = TextStyle(
                fontSize = 14.sp
            ), modifier = Modifier.constrainAs(title) {
                top.linkTo(userName.bottom, margin = 2.dp)
                start.linkTo(userName.start)
                end.linkTo(parent.end, margin = 14.dp)
                width = Dimension.fillToConstraints
            })

            if (thought.image !=""){
                Card(modifier = Modifier.constrainAs(image){
                    top.linkTo(title.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                    Image(painter = rememberAsyncImagePainter(model = thought.image), contentDescription = "example",
                        modifier = Modifier
                            .clickable {
                                val encodedUrl = Uri.encode(thought.image)
                                val route = Routes.FullImage.routes.replace("{imageUrl}", encodedUrl)
                                navController.navigate(route)
                            }
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop)
                }
            }
        }

        Divider(color = Color.LightGray, thickness = 1.dp)
    }

}

@Preview(showBackground = true)
@Composable
fun ShowThoughtItem(){
//    ThoughtItem()
}