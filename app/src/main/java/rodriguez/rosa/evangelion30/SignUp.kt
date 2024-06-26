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
import rodriguez.rosa.evangelion30.Controladores.ControladorRegistrarUsuario
import rodriguez.rosa.evangelion30.DAO.UserDAO
import rodriguez.rosa.evangelion30.Modelo.ModeloRegistarUsuario
import rodriguez.rosa.evangelion30.util.NotificacionesUsuario
import rodriguez.rosa.evangelion30.util.Subscriptor
import rodriguez.rosa.evangelion30.util.Topics

class SignUp : AppCompatActivity(), Subscriptor {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Listener para el botón de registro
        val signUpButton: Button       = findViewById(R.id.signUpButton)
        val editTextEmail: EditText    = findViewById(R.id.emailRegistro)
        val editTextPassword: EditText = findViewById(R.id.passwordRegistro)



        signUpButton.setOnClickListener {

            //Subscribir la vista al modelo
            ModeloRegistarUsuario.getInstance().addSubscriber(this)

            val password = editTextPassword.text.toString()
            val email = editTextEmail.text.toString()

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

            ControladorRegistrarUsuario.getInstance().crearUsuario(email, password)
        }

    }

    private fun isAnyFieldBlank(email: String, password: String): Boolean {
        return email.isBlank() || password.isBlank()
    }

    private fun isInvalidEmail(email: String): Boolean {
        val emailPattern = Regex("^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$")

        return !emailPattern.matches(email)
    }

    override fun notificar(data: NotificacionesUsuario) {
            if (data == NotificacionesUsuario.REGISTRO_CORRECTO) {
                Toast.makeText(
                    this, "Registro correcto", Toast.LENGTH_SHORT
                ).show()
                val intent: Intent = Intent(this, LoginActivity::class.java)
                this.startActivity(intent)
            } else if (data == NotificacionesUsuario.REGISTRO_INCORRECTO) {
                Toast.makeText(
                    this, "Hubo un error al registrar su usuario", Toast.LENGTH_LONG
                ).show()
            }
    }

}