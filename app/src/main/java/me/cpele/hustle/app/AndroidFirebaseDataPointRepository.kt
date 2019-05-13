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

        firebaseLogin.ensure()

        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
            ?: throw IllegalStateException("User should be logged in at this point")

        return suspendCoroutine { continuation ->

            db.collection("dataPoints")
                .get()
                .addOnSuccessListener { snapshot ->
                    val value: List<Long> = snapshot
                        .filter { it["user"] == currentUserEmail }
                        .filter { it["duration"] is Long }
                        .map {
                            it.getLong("duration")
                                ?: throw IllegalStateException("Field 'duration' should be Long and not null")
                        }
                    continuation.resume(value)
                }.addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        }
    }

    override suspend fun insert(elapsedMillis: Long) {

        firebaseLogin.ensure()

        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
            ?: throw IllegalStateException("User should be logged in at this point")

        return suspendCoroutine { continuation ->

            val dataPoint = mapOf(
                "user" to currentUserEmail,
                "duration" to elapsedMillis,
                "time" to System.currentTimeMillis()
            )
            db.collection("dataPoints")
                .add(dataPoint)
                .addOnSuccessListener { continuation.resumeWith(Result.success(Unit)) }
                .addOnFailureListener { continuation.resumeWithException(it) }
        }
    }
}