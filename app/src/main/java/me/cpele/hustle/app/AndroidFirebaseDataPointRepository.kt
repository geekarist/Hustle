//package me.cpele.hustle.app
//
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//import me.cpele.hustle.domain.DataPointRepository
//
//class AndroidFirebaseDataPointRepository(
//    private val firebaseLogin: FirebaseLogin
//) : DataPointRepository {
//
//    private val db by lazy {
//        FirebaseFirestore.getInstance()
//    }
//
//    override suspend fun insert(elapsedMillis: Long) {
//        firebaseLogin.complete().await()
//
//        val currentUserEmail = FirebaseAuth.getInstance().currentUser?.email
//            ?: throw IllegalStateException("User should be logged in at this point")
//
//        val dataPoint = mapOf(
//            "user" to currentUserEmail,
//            "time" to elapsedMillis
//        )
//        db.collection("dataPoints")
//            .add(dataPoint)
//            .addOnFailureListener { throw it }
//    }
//}