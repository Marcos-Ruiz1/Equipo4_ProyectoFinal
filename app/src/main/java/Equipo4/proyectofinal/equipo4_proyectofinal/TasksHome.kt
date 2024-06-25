package Equipo4.proyectofinal.equipo4_proyectofinal

import Dominio.Task
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.GridView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TasksHome : AppCompatActivity() {

    //Adaptador y tareas
    private var adaptador: AdaptadorTasks? = null
    var tasks  = ArrayList<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tasks_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Botones
        val configuracionButton: Button = findViewById(R.id.configuracionButton)
        val agregarTaskButton: Button = findViewById(R.id.agregarTaskButton)
        val estadisticasButton: Button = findViewById(R.id.estadisticasButton)

        //Click listeners a los botones
        configuracionButton.setOnClickListener{
            val intent = Intent(this, Configuracion::class.java)
            this.startActivity(intent)
        }

        agregarTaskButton.setOnClickListener {
            val intent = Intent(this, AgregarTask::class.java)
            this.startActivity(intent)
        }

        estadisticasButton.setOnClickListener {
            val intent = Intent(this, Estadisticas::class.java)
            this.startActivity(intent)
        }



        //cargar objetos Task
        fillTasks()

        //Instancia del adaptador y establecerlo al GridView para que muestras las tasks
        adaptador = AdaptadorTasks(this, tasks)
        var tasksGridView: GridView = findViewById(R.id.tasksGridView)
        tasksGridView.adapter = adaptador

    }




    //Método que instancia objetos Task, que luego se mostrarán en pantalla con el GridView
    fun fillTasks(){

        tasks.add(Task("realizar informes", "tengo que hacer los informes que me pide el jefe tengo que hacer los informes que me pide el jefe tengo que hacer los informes que me pide el jefe tengo que hacer los informes que me pide el jefe tengo que hacer los informes que me pide el jefe tengo que hacer los informes que me pide el jefetengo que hacer los informes que me pide el jefetengo que hacer los informes que me pide el jefe tengo que hacer los informes que me pide el jefe tengo que hacer los informes que me pide el jefetengo que hacer los informes que me pide el jefe", "10/06/2024", "Educación", 10, false))
        tasks.add(Task("Presentación del proyecto", "Preparar y presentar el proyecto final del curso a los profesores y compañeros", "15/07/2024", "Escuela", 9, false))
        tasks.add(Task("Reunión con el cliente", "Reunirse con el cliente para discutir los requisitos y el progreso del proyecto", "20/07/2024", "Trabajo", 8, false))
        tasks.add(Task("Investigación de mercado", "Realizar una investigación de mercado para entender las necesidades del público objetivo", "25/07/2024", "Trabajo", 7, false))
        tasks.add(Task("Desarrollo de la app", "Implementar las funcionalidades principales de la aplicación móvil y realizar pruebas", "30/07/2024", "Tecnología", 9, false))
        tasks.add(Task("Documentación del código", "Escribir la documentación detallada del código fuente y las funcionalidades implementadas", "05/08/2024", "Tecnología", 8, false))
        tasks.add(Task("Lanzamiento del producto", "Preparar y lanzar el producto al mercado, asegurando que todas las funcionalidades estén operativas", "10/08/2024", "Marketing", 10, false))


    }



    //Clase privada creada para cargar en el GridView las tasks
    private class AdaptadorTasks: BaseAdapter{

        var tasks =  ArrayList<Task>()
        var contexto: Context? = null

        constructor(contexto: Context, tasks: ArrayList<Task>){
            this.tasks = tasks
            this.contexto = contexto
        }

        override fun getCount(): Int {
            return tasks.size
        }

        override fun getItem(position: Int): Any {
            return tasks[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        /**
         * Métoodo que se encarga de cargar la vista con la lista de tareas en el GridView
         */
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

            var task = tasks[position]
            var inflador = LayoutInflater.from(contexto)
            var vista = inflador.inflate(R.layout.task_view, null)

            var tituloTask: TextView = vista.findViewById(R.id.tituloTaskTextView)
            var descripcionTask: TextView = vista.findViewById(R.id.descripcionTaskTextView)
            var fechaTask: TextView = vista.findViewById(R.id.fechaTaskTextView)
            var terminarTurno: Button = vista.findViewById(R.id.terminarButton)
            var prioridadTask: TextView = vista.findViewById(R.id.prioridadTaskTextView)
            var categoriaTask: TextView = vista.findViewById(R.id.categoriaTaskTextView)

            tituloTask.setText(task.titulo)
            descripcionTask.setText(task.descripcion)
            fechaTask.setText(task.fecha)
            prioridadTask.setText(task.prioridad.toString())
            categoriaTask.setText(task.categoria)

            terminarTurno.setOnClickListener {

                val intent = Intent(contexto, DescripcionTask::class.java)
                intent.putExtra("titulo", task.titulo)
                intent.putExtra("descripcion", task.descripcion)
                intent.putExtra("fecha", task.fecha)
                intent.putExtra("categoria", task.categoria)
                intent.putExtra("prioridad", task.prioridad)
                intent.putExtra("terminado", task.terminado)
                contexto?.startActivity(intent)
            }



            return vista

        }

    }


}