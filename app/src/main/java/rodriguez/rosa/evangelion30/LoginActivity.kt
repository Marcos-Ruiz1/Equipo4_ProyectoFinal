package rodriguez.rosa.evangelion30

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {
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

        signUp.setOnClickListener{
            val intent = Intent(this, SignUp::class.java)
            this.startActivity(intent)
        }

        logIn.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }

        forgotPassword.setOnClickListener{
            val intent = Intent(this, RecoverPassword::class.java)
            this.startActivity(intent)
        }

    }
}