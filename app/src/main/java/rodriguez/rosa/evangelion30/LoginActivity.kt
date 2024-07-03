package rodriguez.rosa.evangelion30

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import rodriguez.rosa.evangelion30.Controladores.ControladorLogIn
import rodriguez.rosa.evangelion30.Controladores.ControladorRegistrarUsuario
import rodriguez.rosa.evangelion30.Modelo.ModeloLogIn
import rodriguez.rosa.evangelion30.util.NotificacionesUsuario
import rodriguez.rosa.evangelion30.util.Subscriptor

class LoginActivity : AppCompatActivity(), Subscriptor {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val signUp: TextView = findViewById(R.id.signUp)
        val logIn: Button = findViewById(R.id.logInButton)
        val forgotPassword: TextView = findViewById(R.id.forgotPassword)

        val editTextEmail: EditText = findViewById(R.id.email)
        val editTextPassword: EditText = findViewById(R.id.password)

        signUp.setOnClickListener{
            val intent = Intent(this, SignUp::class.java)
            this.startActivity(intent)
        }

        logIn.setOnClickListener {

            //subscribir la vista al modelo
            ModeloLogIn.getInstance().addSubscriber(this)

            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (isAnyFieldBlank(email, password)) {
                Toast.makeText(
                    this, "Favor de llenar los campos de manera correcta",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }
            if (isInvalidEmail(email)) {
                Toast.makeText(
                    this, "Favor de ingresar un correo válido",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            ControladorLogIn.getInstance().iniciarSesion(
                editTextEmail.text.toString(),
                editTextPassword.text.toString()
            )
        }

        forgotPassword.setOnClickListener{
            val intent = Intent(this, RecoverPassword::class.java)
            this.startActivity(intent)
        }

        if( Firebase.auth.currentUser != null ) {
            Firebase.auth.signOut() //Cerramos sesion

            Toast.makeText(
                this,
                "Sesion cerrada con exito",
                Toast.LENGTH_LONG
            ).show()

        }

    }

    private fun isAnyFieldBlank(email: String, password: String): Boolean {
        return email.isBlank() || password.isBlank()
    }

    private fun isInvalidEmail(email: String): Boolean {
        val emailPattern = Regex("^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$")

        return !emailPattern.matches(email)
    }

    override fun notificar(datos: NotificacionesUsuario) {

        if(datos == NotificacionesUsuario.LOGIN_CORRECTO) {
            Toast.makeText(
                this, "LogIn correcto", Toast.LENGTH_SHORT
            ).show()
            val intent: Intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)


        } else if (datos == NotificacionesUsuario.LOGIN_INCORRECTO) {
            Toast.makeText(
                this, "¡LogIn incorrecto, verificar correo o contraseña!", Toast.LENGTH_SHORT
            ).show()
        } else {
            throw RuntimeException("ENVIAMOS DATOS INCORRECTOS")
        }

    }

}