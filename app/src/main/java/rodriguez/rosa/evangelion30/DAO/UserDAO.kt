package rodriguez.rosa.evangelion30.DAO

import android.util.Log
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import rodriguez.rosa.evangelion30.util.NotificacionesUsuario
import rodriguez.rosa.evangelion30.util.Subscriptor
import rodriguez.rosa.evangelion30.util.Topics

class UserDAO {

    private var subscriptores: HashMap<String, ArrayList<Subscriptor>> = hashMapOf(
        Topics.REGISTRO.toString() to ArrayList(),
        Topics.LOGIN.toString() to ArrayList(),
        Topics.LOGOUT.toString() to ArrayList()
    )

    companion object {

        val instance: UserDAO = UserDAO()

        fun addSubcriber(sub: Subscriptor, topic: Topics) {

            if (!instance.subscriptores.containsKey(topic.toString())) {
                throw RuntimeException("EL TOPIC NO EXISTE")
            }

            instance.subscriptores.get(topic.toString())!!.add(sub)
        }

    }

    fun logInUser(email: String, password: String) {
        val TAG: String = "LOGIN"
        val auth = Firebase.auth

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { task ->
                notificar(NotificacionesUsuario.LOGIN_CORRECTO, Topics.LOGIN)
            }.addOnFailureListener { e ->
                Log.e(TAG, e.stackTraceToString())
                notificar(NotificacionesUsuario.LOGIN_INCORRECTO, Topics.LOGIN)
            }

    }

    fun createUser(email: String, password: String) {
        val TAG: String = "SingIn"
        val auth = Firebase.auth

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { task ->
                notificar(NotificacionesUsuario.REGISTRO_CORRECTO, Topics.REGISTRO)
            }.addOnFailureListener { e ->
                Log.e(TAG, e.stackTraceToString())
                notificar(NotificacionesUsuario.REGISTRO_INCORRECTO, Topics.REGISTRO)
            }

    }

    fun logOutUser() {
        Firebase.auth.signOut()
        notificar(NotificacionesUsuario.LOG_OUT, Topics.LOGOUT)
    }

    private fun notificar(data: NotificacionesUsuario, topic: Topics): Unit {
        for (sub in this.subscriptores.get(topic.toString())!!) {
            sub.notificar(data)
        }
    }

}