package rodriguez.rosa.evangelion30

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.GridLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jakewharton.threetenabp.AndroidThreeTen
import rodriguez.rosa.evangelion30.dominio.Task
import rodriguez.rosa.evangelion30.util.DataBaseManager

class Edit_Task_Activity : AppCompatActivity() {

    data class fechas(val dia: Int, val mes: Int, val anio: Int)

    //    Atributos de la pantalla
    private lateinit var tituloActividad: TextView
    private lateinit var textoTitulo: EditText
    private lateinit var textoDescripcion: EditText
    private lateinit var spinner: Spinner
    private lateinit var textoCategoria: EditText
    private lateinit var botonAgregarCategoria: Button
    private lateinit var layoutCategorias: GridLayout
    private lateinit var seleccionadorAnio: com.loper7.date_time_picker.number_picker.NumberPicker
    private lateinit var seleccionadorMes: com.loper7.date_time_picker.number_picker.NumberPicker
    private lateinit var seleccionadorDia: com.loper7.date_time_picker.number_picker.NumberPicker
    private lateinit var checkBoxSinFehca: CheckBox
    private lateinit var botonAgregar: Button
    private lateinit var id: String
//    fin de los atributos de la pantalla

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidThreeTen.init(this)

        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_task)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        botonAgregar          = findViewById(R.id.botonAgregarTarea)
        textoTitulo           = findViewById<EditText>(R.id.tituloTarea)
        textoDescripcion      = findViewById<EditText>(R.id.descripcionTarea)
        spinner               = findViewById<Spinner>(R.id.prioritySpinner)
        textoCategoria        = findViewById<EditText>(R.id.textoCategoria)
        botonAgregarCategoria = findViewById<Button>(R.id.botonAgregarCategoria)
        layoutCategorias      = findViewById(R.id.layoutCategorias)
        seleccionadorAnio     = findViewById(R.id.np_datetime_year)
        seleccionadorMes      = findViewById(R.id.np_datetime_month)
        seleccionadorDia      = findViewById(R.id.np_datetime_day)
        checkBoxSinFehca      = findViewById<CheckBox>(R.id.checkboxDeadline)
        tituloActividad       = findViewById<TextView>(R.id.tituloActividad)
        id = intent.getStringExtra("id").toString()

        if (this.intent.extras != null) {
            setExtras(this.intent.extras!!)
        }

        setearDatos()
        setearBotones()


    }

    private fun setearDatos(): Unit {

        ArrayAdapter.createFromResource(
            this, R.array.taskPriorities,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        val timestamp = org.threeten.bp.LocalDate.now()

        seleccionadorAnio.maxValue = 2050
        seleccionadorAnio.minValue = timestamp.year

        seleccionadorDia.minValue = 1
        seleccionadorDia.maxValue = 31

        seleccionadorMes.minValue = 1
        seleccionadorMes.maxValue = 12

    }

    private fun setearBotones(): Unit {

        botonAgregar.setOnClickListener {
//            toda la logica para guardar la tarea o verificar que la tarea tiene un formato adecuado
            val intent: Intent = Intent(this, MainActivity::class.java)
            val day = seleccionadorDia.value
            val month = seleccionadorMes.value
            val year = seleccionadorAnio.value
            val date = "%02d/%02d/%04d".format(day, month, year)

            val task = Task(
                id = id,
                titulo = textoTitulo.text.toString(),
                descripcion = textoDescripcion.text.toString(),
                fecha = date,
                categoria = textoCategoria.text.toString(),
                prioridad =  (spinner.selectedItem as String).toInt(),
                terminado = false
            )

            editTask(task)
            this.startActivity(intent)
        }
    }

    private fun setExtras(extras: Bundle): Unit {

        val titulo = extras.getString("titulo") ?: "TAREA SIN TITULO"
        val descripcion = extras.getString("descripcion") ?: "TAREA SIN DESCRIPCION"
        val categoria = extras.getString("categoria") ?: "TAREA SIN CATEGORIA"
        val fecha = getFecha(extras.getString("fecha") ?: "01/01/2000")

        textoTitulo.setText(titulo)
        textoDescripcion.setText(descripcion)
        textoCategoria.setText(categoria)
        seleccionadorDia.value = fecha.dia
        seleccionadorMes.value = fecha.mes
        seleccionadorAnio.value = fecha.anio

        botonAgregar.text = resources.getText(R.string.tituloEditarTarea)
        tituloActividad.text = resources.getText(R.string.tituloEditarTarea)

    }

    private fun getFecha(fecha: String): fechas {
        val values = fecha.split("/")
        return fechas(values[0].toInt(), values[1].toInt(), values[2].toInt())
    }

    private fun editTask(task: Task){
        AuthManager.currentUserId?.let { it1 ->
            DataBaseManager.databaseReference.child("User").child(
                it1
            ).child("Tasks").child(task.id).setValue(task)
        }
        Log.e("null","AAAAAAAAAAAAAAAAA"+task.toString())
        startActivity(Intent(this, MainActivity::class.java))
    }


}