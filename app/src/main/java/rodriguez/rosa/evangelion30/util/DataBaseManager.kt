package rodriguez.rosa.evangelion30.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

object DataBaseManager {

        val firebaseDatabase = FirebaseDatabase.getInstance()
        val databaseReference = firebaseDatabase.getReference()

}