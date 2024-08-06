package com.example.thoughts.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thoughts.Utils.SharedPref
import com.example.thoughts.model.ThoughtModel
import com.example.thoughts.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.storage
import java.util.UUID

class SearchViewModel : ViewModel() {
    private val db = FirebaseDatabase.getInstance()
    val users = db.getReference("user")
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    private var _users = MutableLiveData<List<UserModel>>()
    val userList: LiveData<List<UserModel>> = _users

    init {
        fetchUsers {
            _users.value = it.filterNot { it.uid == currentUserId }
        }
    }

    fun fetchUsers(onResult: (List<UserModel>) -> Unit){
        users.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableListOf<UserModel>()
                for (thoughtsSnapShot in snapshot.children){
                    val users = thoughtsSnapShot.getValue(UserModel::class.java)
                    result.add(users!!)
                }
                onResult(result)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun fetchUsersFromThoughs(thought: ThoughtModel, onResult:(UserModel)->Unit){
        db.getReference("user").child(thought.userId)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserModel::class.java)
                    user?.let(onResult)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}