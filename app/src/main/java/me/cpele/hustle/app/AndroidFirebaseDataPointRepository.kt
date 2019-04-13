package me.cpele.hustle.app

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import me.cpele.hustle.domain.DataPointRepository

class AndroidFirebaseDataPointRepository : DataPointRepository {

    private val db by lazy {
        FirebaseFirestore.getInstance()
    }

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override suspend fun insert(elapsedMillis: Long) {
        val dataPoint = mapOf(
            "user" to firebaseAuth.currentUser?.email,
            "time" to elapsedMillis
        )
        db.collection("dataPoints")
            .add(dataPoint)
            .addOnFailureListener { throw it }
    }
}