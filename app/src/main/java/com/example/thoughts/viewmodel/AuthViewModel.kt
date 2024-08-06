package com.example.thoughts.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thoughts.Utils.SharedPref
import com.example.thoughts.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.util.UUID

class AuthViewModel : ViewModel() {
    val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance() //Get instance of Realtime DataBase
    val userRef = db.getReference("user") // Get Reference of this and create a path in db as "user"

    private val _firebaseUser = MutableLiveData<FirebaseUser?>()
    val firebaseUser: LiveData<FirebaseUser?> = _firebaseUser

    private val storageRef = Firebase.storage.reference //Get Reference of Storage
    private val imageRef = storageRef.child("users/${UUID.randomUUID()}.jpg")
    // create a child in storage of name user which save images as unique UUID.jpg(ex - 0bd938a4-7447-44a0-9fad-80ff55cf48d2.jpg) name

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error


    init {
        _firebaseUser.value = auth.currentUser
    }

    fun login(email: String, password: String, context: Context) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _firebaseUser.postValue(auth.currentUser)
                    //after this we will get user data from realtime database
                    getData(auth.currentUser!!.uid, context)
                } else {
                    _error.postValue(it.exception!!.message)
                }
            }
    }

    private fun getData(uid: String, context: Context) {
        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //Called when data is fetched. Converts the snapshot to UserModel and stores the data.
                val userData = snapshot.getValue(UserModel::class.java)
                SharedPref.storeData(userData!!.name, userData.email, userData.username, userData.bio, userData.imageUri, context)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun register(
        name: String,
        username: String,
        email: String,
        password: String,
        bio: String,
        imageUri: Uri,
        context: Context
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _firebaseUser.postValue(auth.currentUser)
                    saveImage(name, username, email, password, bio, imageUri, auth.currentUser?.uid, context)
                } else {
                    _error.postValue(it.exception!!.message)
                }
            }
    }

    private fun saveImage(
        name: String,
        username: String,
        email: String,
        password: String,
        bio: String,
        imageUri: Uri,
        uid: String?,
        context: Context
    ) {
        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveData(name, username, email, password, bio, it.toString(), uid, context)
            }
        }
    }

    private fun saveData(
        name: String,
        username: String,
        email: String,
        password: String,
        bio: String,
        imageUri: String,
        uid: String?,
        context: Context
    ) {
        val firestoreDb = Firebase.firestore // Firestore DataBase
        val followersRef = firestoreDb.collection("followers").document(uid!!)
        val followingRef = firestoreDb.collection("following").document(uid!!)

        followingRef.set(mapOf("followingIds" to listOf<String>()))
        followersRef.set(mapOf("followersIds" to listOf<String>()))


        val userData = UserModel(name, username, email, password, bio, imageUri, uid!!)

        userRef.child(uid!!).setValue(userData)
            .addOnSuccessListener {
                SharedPref.storeData(name, email, username, bio, imageUri, context)
            }
            .addOnFailureListener {

            }
    }

    fun logout(){
        auth.signOut()
        _firebaseUser.postValue(null)
    }
}