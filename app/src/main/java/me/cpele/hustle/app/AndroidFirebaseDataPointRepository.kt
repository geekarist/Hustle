package me.cpele.hustle.app

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import me.cpele.hustle.domain.DataPointRepository
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AndroidFirebaseDataPointRepository(
    private val firebaseLogin: FirebaseLogin
) : DataPointRepository {

    private val db by lazy {
        FirebaseFirestore.getInstance()
    }

    override suspend fun findAll(): List<Long> {

        firebaseLogin.complete().await()

        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
            ?: throw IllegalStateException("User should be logged in at this point")

        return suspendCoroutine { continuation ->

            db.collection("dataPoints")
                .get()
                .addOnSuccessListener { snapshot ->
                    val value: List<Long> = snapshot
                        .filter { it["user"] == currentUserEmail }
                        .filter { it["time"] is Long }
                        .map {
                            it.getLong("time")
                                ?: throw IllegalStateException("Field 'time' should be Long and not null")
                        }
                    continuation.resume(value)
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun insert(elapsedMillis: Long) {
        firebaseLogin.complete().await()

        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
            ?: throw IllegalStateException("User should be logged in at this point")

        val dataPoint = mapOf(
            "user" to currentUserEmail,
            "time" to elapsedMillis
        )
        db.collection("dataPoints")
            .add(dataPoint)
            .addOnFailureListener { throw it }
    }
}