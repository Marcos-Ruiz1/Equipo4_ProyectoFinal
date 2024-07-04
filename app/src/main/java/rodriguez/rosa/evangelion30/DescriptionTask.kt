package rodriguez.rosa.evangelion30

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import rodriguez.rosa.evangelion30.Controladores.ControladorEditTask
import rodriguez.rosa.evangelion30.dominio.Task
import rodriguez.rosa.evangelion30.util.DataBaseManager

class DescriptionTask : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_description_task)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Cargar los datos de la task seleccionada
        val id = intent.getStringExtra("id") ?: return showErrorAndFinish("Task ID is missing")
        val nombre = intent.getStringExtra("titulo") ?: return showErrorAndFinish("Title is missing")
        val descripcion = intent.getStringExtra("descripcion") ?: return showErrorAndFinish("Description is missing")
        val fecha = intent.getStringExtra("fecha") ?: return showErrorAndFinish("Date is missing")
        val categoria = intent.getStringExtra("categoria") ?: return showErrorAndFinish("Category is missing")
        val prioridad = intent.getIntExtra("prioridad", -1).takeIf { it != -1 } ?: return showErrorAndFinish("Priority is missing")
        val estado = intent.getBooleanExtra("terminado", false)

        val tituloTask: TextView = findViewById(R.id.tituloTask)
        val descripcionTask: TextView = findViewById(R.id.descripcionTask)
        val fechaTask: TextView = findViewById(R.id.fechaTask)
        val prioridadTask: TextView = findViewById(R.id.prioridadTask)
        val categoriaTask: TextView = findViewById(R.id.categoriaTask)
        val estadoTask: TextView = findViewById(R.id.estadoTerminadoTask)

        tituloTask.text = nombre
        descripcionTask.text = descripcion
        fechaTask.text = fecha
        prioridadTask.text = prioridad.toString()
        categoriaTask.text = categoria
        estadoTask.text = if (estado) "Terminada" else "No terminada"

        // Listeners para los botones de la activity (actualizar, eliminar y terminar tarea)
        val deleteButton: Button = findViewById(R.id.deleteButton)
        val updateButton: Button = findViewById(R.id.updateButton)
        val terminarButton: Button = findViewById(R.id.terminarTaskButton)

        if ( estado ) {

            terminarButton.isEnabled = false
            terminarButton.setBackgroundColor( resources.getColor(R.color.gray, null) )

        }

        deleteButton.setOnClickListener {
            mostrarAlertaEliminarTarea(id)

        }

        updateButton.setOnClickListener {
            val intent = Intent(this, Edit_Task_Activity::class.java).apply {
                putExtra("id", id)
                putExtra("titulo", tituloTask.text.toString())
                putExtra("descripcion", descripcionTask.text.toString())
                putExtra("fecha", fechaTask.text.toString())
                putExtra("categoria", categoriaTask.text.toString())
            }
            startActivity(intent)
        }

        terminarButton.setOnClickListener {
            val task = Task(
                id = id,
                titulo = tituloTask.text.toString(),
                descripcion = descripcionTask.text.toString(),
                fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                categoria = categoriaTask.text.toString(),
                prioridad = prioridad,
                terminado = true
            )
            ControladorEditTask.getInstance().editTask(task)

//            val mainActivity: Intent = Intent(this, MainActivity::class.java)
//            this.startActivity(mainActivity)


            Toast.makeText(
                this,
                "Tarea terminada con exito",
                Toast.LENGTH_SHORT
            ).show()

            this.finish()
        }
    }

    private fun mostrarAlertaEliminarTarea(id: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar Tarea")
        builder.setMessage("¿Deseas eliminar la tarea?")

        builder.setPositiveButton("Sí") { _, _ ->
            AuthManager.currentUserId?.let {
                DataBaseManager.databaseReference.child("User").child(
                    it
                ).child("Tasks").child(id).removeValue()
            }
            Toast.makeText(applicationContext, "Tarea eliminada", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun showErrorAndFinish(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        finish()
    }

    private fun getFecha(fecha: String): Edit_Task_Activity.fechas {
        val values = fecha.split("/")
        return Edit_Task_Activity.fechas(values[0].toInt(), values[1].toInt(), values[2].toInt())
    }



}
