package rodriguez.rosa.evangelion30

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import rodriguez.rosa.evangelion30.Controladores.ControladorLogIn
import rodriguez.rosa.evangelion30.Controladores.ControladorRestaurarPassword
import rodriguez.rosa.evangelion30.Modelo.ModeloRestaurarPassword
import rodriguez.rosa.evangelion30.util.NotificacionesUsuario
import rodriguez.rosa.evangelion30.util.Subscriptor

class RecoverPassword : AppCompatActivity(), Subscriptor {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recover_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val email: EditText = findViewById(R.id.email)

        //Listener para el botón de restaurar contraseña
        val recoverPasswordButton: Button = findViewById(R.id.recoverPasswordButton)
        recoverPasswordButton.setOnClickListener {

            //subscribir vista al modelo
            ModeloRestaurarPassword.getInstance().addSubscriber(this)

            val emailText = email.text.toString()

            if(isInvalidEmail(emailText)){
                Toast.makeText(
                    this, "Favor de ingresar un correo válido",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            ControladorRestaurarPassword.getInstance().restaurarPassword(
                email.text.toString()
            )
        }

    }

    private fun isInvalidEmail(email: String): Boolean {
        val emailPattern = Regex("^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$")

        return !emailPattern.matches(email)
    }

    override fun notificar(datos: NotificacionesUsuario) {
        if(datos == NotificacionesUsuario.RESET_PASSWORD_CORRECTO) {
            Toast.makeText(
                this, "Restauración de contraseña correcto", Toast.LENGTH_SHORT
            ).show()
            val intent: Intent = Intent(this, LoginActivity::class.java)
            this.startActivity(intent)


        } else if (datos == NotificacionesUsuario.RESET_PASSWORD_INCORRECTO) {

        } else {
            throw RuntimeException("ENVIAMOS DATOS INCORRECTOS")
        }
    }

}