package rodriguez.rosa.evangelion30

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import rodriguez.rosa.evangelion30.Controladores.Controlador
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
            Controlador.iniciarSesion(
                editTextEmail.text.toString(),
                editTextPassword.text.toString()
            )
        }

        forgotPassword.setOnClickListener{
            val intent = Intent(this, RecoverPassword::class.java)
            this.startActivity(intent)
        }

    }

    override fun notificar(datos: NotificacionesUsuario) {

        if(datos == NotificacionesUsuario.LOGIN_CORRECTO) {



        } else if (datos == NotificacionesUsuario.LOGIN_INCORRECTO) {

        } else {
            throw RuntimeException("ENVIAMOS DATOS INCORRECTOS")
        }

    }

}