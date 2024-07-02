package rodriguez.rosa.evangelion30.DAO

import AuthManager
import android.util.Log
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import rodriguez.rosa.evangelion30.dominio.Task
import rodriguez.rosa.evangelion30.util.NotificacionesUsuario
import rodriguez.rosa.evangelion30.util.Subscriptor
import rodriguez.rosa.evangelion30.util.SubscriptorProxy
import rodriguez.rosa.evangelion30.util.Topics
import org.threeten.bp.LocalDate
import com.jakewharton.threetenabp.AndroidThreeTen

class UserDAO {

    private var subscriptores: HashMap<String, ArrayList<SubscriptorProxy>> = hashMapOf(
        Topics.REGISTRO.toString() to ArrayList(),
        Topics.LOGIN.toString() to ArrayList(),
        Topics.LOGOUT.toString() to ArrayList(),
        Topics.RESET_PASSWORD.toString() to ArrayList()
    )

    companion object {

        private var instance: UserDAO? = null


        fun getInstances(): UserDAO {
            if(instance == null){
                instance = UserDAO()
            }

            return instance!!
        }


    }

    fun addSubcriber(sub: SubscriptorProxy, topic: Topics) {

        if (!instance!!.subscriptores.containsKey(topic.toString())) {
            throw RuntimeException("EL TOPIC NO EXISTE")
        }

        if(!instance!!.subscriptores.get(topic.toString())!!.contains(sub)){
            instance!!.subscriptores.get(topic.toString())!!.add(sub)
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

    fun resetPassword(email: String) {
        val TAG: String = "reset password"
        val auth = Firebase.auth

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener{ task ->

                notificar(NotificacionesUsuario.RESET_PASSWORD_CORRECTO, Topics.RESET_PASSWORD)
            }.addOnFailureListener { e ->
                Log.e(TAG, e.stackTraceToString())
                notificar(NotificacionesUsuario.RESET_PASSWORD_INCORRECTO, Topics.RESET_PASSWORD)
            }
    }

    fun logOutUser() {
        Firebase.auth.signOut()
        notificar(NotificacionesUsuario.LOG_OUT, Topics.LOGOUT)
    }

    private fun notificar(data: NotificacionesUsuario, topic: Topics): Unit {
        for (sub in this.subscriptores.get(topic.toString())!!) {
            sub.notificar(data, topic)        }
    }

    fun addTask(title: String, description: String, category: String, priority: Int){
        var userID: String? = AuthManager.currentUserId

            val task = Task(
                titulo = title,
                descripcion = description,
                fecha = LocalDate.now().toString(),  // Example date format
                categoria = category,    // Example category
                prioridad = priority,         // Example priority level
                terminado = false      // Task is not yet completed
            )

        if (userID!=null){
            val database = FirebaseDatabase.getInstance()
            val tasksRef = database.getReference("User").child(userID).child("Tasks")

            // Use push() to generate a unique key for the task
            val newTaskRef = tasksRef.push()

            // Set the value of the new task node
            newTaskRef.setValue(task)  .addOnSuccessListener {
                // Task added successfully
                Log.d("FirebaseDatabase", "Task added successfully")
            }
                .addOnFailureListener { e ->
                    // Handle any errors
                    Log.e("FirebaseDatabase", "Error adding task", e)
                }
        } else {
            Log.e("FirebaseDatabase", "User ID is null")
        }

    }


}