package com.example.thoughts.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thoughts.model.ThoughtModel
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.storage
import java.util.UUID

class AddThoughtViewModel : ViewModel() {
    private val db = FirebaseDatabase.getInstance()
    val userRef = db.getReference("thoughts")

    private val storageRef = Firebase.storage.reference
    private val imageRef = storageRef.child("thoughtsisPosted/${UUID.randomUUID()}.jpg")
    // create a child in storage of name user which save images as unique UUID.jpg(ex - 0bd938a4-7447-44a0-9fad-80ff55cf48d2.jpg) name

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> = _isPosted

    fun saveImage(
        thought: String,
        userId: String,
        imageUri: Uri
    ) {
        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveData(thought, userId,  it.toString())
            }
        }
    }

    fun saveData(
        thought: String,
        userId: String,
        imageUri: String
    ) {
        val thoughtData = ThoughtModel(thought, imageUri, userId, System.currentTimeMillis().toString())

        userRef.child(userRef.push().key!!).setValue(thoughtData)
            .addOnSuccessListener {
                _isPosted.postValue(true)
            }
            .addOnFailureListener {
                _isPosted.postValue(false)
            }
    }

}