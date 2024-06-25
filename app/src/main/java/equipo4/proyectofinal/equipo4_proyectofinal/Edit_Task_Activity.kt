package equipo4.proyectofinal.equipo4_proyectofinal

import Equipo4.proyectofinal.equipo4_proyectofinal.R
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.GridLayout
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jakewharton.threetenabp.AndroidThreeTen

class Edit_Task_Activity : AppCompatActivity() {

    data class fechas(val dia: Int, val mes: Int, val anio: Int)

    //    Atributos de la pantalla
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
        botonAgregar          = findViewById<Button>(R.id.botonAgregarTarea)

        setearDatos()

        if (this.intent.extras != null) {
            setExtras(this.intent.extras!!)
        }


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
            val intent: Intent = Intent(this, TasksHome::class.java)
            this.startActivity(intent)
        }

    }

    private fun setExtras(extras: Bundle): Unit {

        val titulo = extras.getString("titulo") ?: "TAREA SIN TITULO"
        val descripcion = extras.getString("descripcion") ?: "TAREA SIN DESCRIPCION"

        val fecha = getFecha(extras.getString("fecha") ?: "01/01/2000")

        textoTitulo.setText(titulo)
        textoDescripcion.setText(descripcion)

        seleccionadorDia.value = fecha.dia
        seleccionadorMes.value = fecha.mes
        seleccionadorAnio.value = fecha.anio

    }

    private fun getFecha(fecha: String): fechas {
        val values = fecha.split("/")
        return fechas(values[0].toInt(), values[1].toInt(), values[2].toInt())
    }

}