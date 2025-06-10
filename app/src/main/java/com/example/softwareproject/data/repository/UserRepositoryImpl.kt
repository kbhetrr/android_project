package com.example.softwareproject.data.repository

import com.example.softwareproject.data.entity.User
import com.example.softwareproject.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val fireBaseStore : FirebaseFirestore
) : UserRepository {
    override suspend fun getUserInfo(userId: Int): User {
        val doc = fireBaseStore.collection("user").document(userId.toString()).get().await()
        return doc.toObject(User::class.java) ?:throw Exception("User not found")
    }
}