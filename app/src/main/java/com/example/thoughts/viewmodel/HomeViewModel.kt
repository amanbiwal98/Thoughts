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

class HomeViewModel : ViewModel() {
    private val db = FirebaseDatabase.getInstance() // Realtime DataBase
    val thoughts = db.getReference("thoughts")

    private var _thoughtsAndUsers = MutableLiveData<List<Pair<ThoughtModel, UserModel>>>()
    val thoughtsAndUsers: LiveData<List<Pair<ThoughtModel, UserModel>>> = _thoughtsAndUsers

    init {
        fetchThoughtsAndUsers {
            _thoughtsAndUsers.value = it
        }
    }

    fun fetchThoughtsAndUsers(onResult: (List<Pair<ThoughtModel, UserModel>>) -> Unit){
        thoughts.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableListOf<Pair<ThoughtModel, UserModel>>()
                for (thoughtsSnapShot in snapshot.children){
                    val thought = thoughtsSnapShot.getValue(ThoughtModel::class.java)
                    thought.let {
                        fetchUsersFromThoughs(it!!){
                            user->
                            result.add(0, it to user)

                            if (result.size == snapshot.childrenCount.toInt()){
                                onResult(result)
                            }
                        }
                    }
                }
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