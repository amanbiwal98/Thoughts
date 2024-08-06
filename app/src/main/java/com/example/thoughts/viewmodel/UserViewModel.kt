package com.example.thoughts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.thoughts.model.ThoughtModel
import com.example.thoughts.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class UserViewModel : ViewModel() {
    private val db = FirebaseDatabase.getInstance()
    val thoughtRef = db.getReference("thoughts")
    val userRef = db.getReference("user")

    private val _thoughts = MutableLiveData(listOf<ThoughtModel>())
    val thoughts : LiveData<List<ThoughtModel>> get() = _thoughts

    private val _followerList = MutableLiveData(listOf<String>())
    val followerList : LiveData<List<String>> get() = _followerList

    private val _followingList = MutableLiveData(listOf<String>())
    val followingList : LiveData<List<String>> get() = _followingList

    private val _users = MutableLiveData(UserModel())
    val users : LiveData<UserModel> get() = _users

    fun fetchUser(uid: String) {
        userRef.child(uid).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                _users.postValue(user)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun fetchThoughts(uId:String){
        thoughtRef.orderByChild("userId").equalTo(uId).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val thoughtList = snapshot.children.mapNotNull {
                    it.getValue((ThoughtModel::class.java))
                }
                _thoughts.postValue(thoughtList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    val fireStoreDb = Firebase.firestore
    fun followUsers(userId: String, currentUserId: String){
        val ref = fireStoreDb.collection("following").document(currentUserId)
        val followerRef = fireStoreDb.collection("followers").document(userId)

        ref.update("followingIds", FieldValue.arrayUnion(userId))
        followerRef.update("followersIds", FieldValue.arrayUnion(currentUserId))
    }

    fun unfollowUsers(userId: String, currentUserId: String){
        val ref = fireStoreDb.collection("following").document(currentUserId)
        val followerRef = fireStoreDb.collection("followers").document(userId)

        ref.update("followingIds", FieldValue.arrayRemove(userId))
        followerRef.update("followersIds", FieldValue.arrayRemove(currentUserId))
    }

    fun getFollowers(userId: String){
        fireStoreDb.collection("followers").document(userId)
            .addSnapshotListener{value, error ->
                val followerIds = value?.get("followersIds") as? List<String>?: listOf()
                _followerList.postValue(followerIds)
            }
    }

    fun getFollowing(userId: String){
        fireStoreDb.collection("following").document(userId)
            .addSnapshotListener{value, error ->
                val followingIds = value?.get("followingIds") as? List<String>?: listOf()
                _followingList.postValue(followingIds)
            }
    }

}