package rodriguez.rosa.evangelion30.DAO

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import rodriguez.rosa.evangelion30.util.NotificacionesUsuario
import rodriguez.rosa.evangelion30.util.Subscriptor

class UserDAO {

    private var subscriptores: ArrayList<Subscriptor> = ArrayList()

    companion object {

        val instance: UserDAO = UserDAO()

        fun addSubcriber(sub: Subscriptor) {
            instance.subscriptores.add(sub)
        }

    }

    fun logInUser(email: String, password: String) {
        val TAG: String = "LOGIN"
        val auth = Firebase.auth

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { task ->
                notificar(NotificacionesUsuario.LOGIN_CORRECTO)
            }.addOnFailureListener { e ->
                Log.e(TAG, e.stackTraceToString())
                notificar(NotificacionesUsuario.LOGIN_INCORRECTO)
            }

    }

    fun createUser(email: String, password: String) {
        val TAG: String = "SingIn"
        val auth = Firebase.auth

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { task ->
                notificar(NotificacionesUsuario.REGISTRO_CORRECTO)
            }.addOnFailureListener { e ->
                Log.e(TAG, e.stackTraceToString())
                notificar(NotificacionesUsuario.REGISTRO_INCORRECTO)
            }

    }

    fun logOutUser() {
        Firebase.auth.signOut()
    }

    private fun notificar(data: NotificacionesUsuario): Unit {
        for (sub in this.subscriptores) {
            sub.notificar(data)
        }
    }

}