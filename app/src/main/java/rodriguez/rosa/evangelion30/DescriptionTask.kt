package rodriguez.rosa.evangelion30

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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

//Cargar los datos de la task seleccionada
        val nombre = intent.getStringExtra("titulo")
        val descripcion = intent.getStringExtra("descripcion")
        val fecha = intent.getStringExtra("fecha")
        val categoria = intent.getStringExtra("categoria")
        val prioridad = intent.getIntExtra("prioridad", 1)
        val estado = intent.getBooleanExtra("terminado", false)

        var tituloTask : TextView = findViewById(R.id.tituloTask)
        var descripcionTask : TextView = findViewById(R.id.descripcionTask)
        var fechaTask : TextView = findViewById(R.id.fechaTask)
        var prioridadTask: TextView = findViewById(R.id.prioridadTask)
        var categoriaTask: TextView = findViewById(R.id.categoriaTask)
        var estadoTask: TextView = findViewById(R.id.estadoTerminadoTask)

        tituloTask.setText(nombre)
        descripcionTask.setText(descripcion)
        fechaTask.setText(fecha)
        prioridadTask.setText(prioridad.toString())
        categoriaTask.setText(categoria)
        if(estado == false){
            estadoTask.setText("No terminada")
        }else{
            estadoTask.setText("Terminada")
        }


        //Listeners para los botones de la activity (actualizar, eliminar y terminar tarea)
        val deleteButton: Button = findViewById(R.id.deleteButton)
        val updateButton: Button = findViewById(R.id.updateButton)
        val terminarButton : Button = findViewById(R.id.terminarTaskButton)

        deleteButton.setOnClickListener {
            mostrarAlertaEliminarTarea()
        }

        updateButton.setOnClickListener {
            val intent = Intent(this, Edit_Task_Activity::class.java)
            intent.putExtra("titulo", tituloTask.text)
            intent.putExtra("descripcion", descripcionTask.text)
            intent.putExtra("fecha", fechaTask.text)
            this.startActivity(intent)

        }

        terminarButton.setOnClickListener {

            val intent: Intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)

        }

    }

    //Función para mostrar la alerta para confirmar la eliminación de la task
    private fun mostrarAlertaEliminarTarea() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar Tarea")
        builder.setMessage("¿Deseas eliminar la tarea?")

        builder.setPositiveButton("Sí") { dialog, which ->

            Toast.makeText(applicationContext, "Tarea eliminada", Toast.LENGTH_SHORT).show()
            val intent: Intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)

        }

        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }

}