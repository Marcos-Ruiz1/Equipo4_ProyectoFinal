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
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jakewharton.threetenabp.AndroidThreeTen
import com.loper7.date_time_picker.number_picker.NumberPicker
import rodriguez.rosa.evangelion30.Controladores.ControladorAddTask
import rodriguez.rosa.evangelion30.Controladores.ControladorLogIn
import rodriguez.rosa.evangelion30.DAO.UserDAO
import rodriguez.rosa.evangelion30.Modelo.ModeloAddTask
import rodriguez.rosa.evangelion30.Modelo.ModeloLogIn
import rodriguez.rosa.evangelion30.dominio.Task
import rodriguez.rosa.evangelion30.util.NotificacionesUsuario
import rodriguez.rosa.evangelion30.util.Subscriptor

class Add_Task_Activity : AppCompatActivity(), Subscriptor{

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
    private lateinit var checkBoxSinFecha: CheckBox
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
        checkBoxSinFecha      = findViewById<CheckBox>(R.id.checkboxDeadline)
        tituloActividad       = findViewById<TextView>(R.id.tituloActividad)

        //Modificación del texto del boton y el titulo de la actividad
        botonAgregar.text = "AGREGAR TAREA"
        tituloActividad.text = "Agregar tarea"

        setearDatos()
        setearBotones()


        botonAgregar.setOnClickListener {


            Log.e(null,"WORKING?")

            //subscribir la vista al modelo
            ModeloAddTask.getInstance().addSubscriber(this)

            val title = textoTitulo.text.toString()
            val description = textoDescripcion.text.toString()
            val category = textoCategoria.text.toString()
            val priority = (spinner.selectedItem as String).toInt()

            val day = seleccionadorDia.value
            val month = seleccionadorMes.value
            val year = seleccionadorAnio.value

            val date = "%02d/%02d/%04d".format(day, month, year)

            val hasLimitDate = !checkBoxSinFecha.isChecked





            if (isAnyFieldBlank(title, description, category, priority)) {
                Toast.makeText(
                    this, "Favor de llenar los campos de manera correcta",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }


            ControladorAddTask.getInstance().addTask(
                title, description, category, priority
            )
        }

    }

    private fun isAnyFieldBlank(title: String, description: String, category: String, priority: Int ): Boolean {
        return title.isBlank() || description.isBlank()|| category.isBlank()|| priority.toString().isBlank()

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
        seleccionadorDia.maxValue = timestamp.month.maxLength()

        seleccionadorMes.minValue = 1
        seleccionadorMes.maxValue = 12

        seleccionadorMes.setOnValueChangedListener { picker, oldVal, newVal ->
            updateDateFromMonth(picker, oldVal, newVal)
        }

        seleccionadorAnio.setOnValueChangedListener { picker, oldVal, newVal ->
            updateDateFromYear(picker, oldVal, newVal)
        }

    }

    private fun updateDateFromMonth(picker: NumberPicker, oldVal: Int, newVal: Int) {
        var month: String = ""
        if (newVal < 10) {
            month = month.plus("0")
        }
        month = month.plus(newVal)
        val fechaReferencia = org.threeten.bp.LocalDate.parse("${seleccionadorAnio.value}-${month}-01")
        seleccionadorDia.maxValue = fechaReferencia.month.maxLength()
        seleccionadorDia.minValue = 1
    }

    private fun updateDateFromYear(picker: NumberPicker, oldVal: Int, newVal: Int) {
        var month: String = ""
        if (seleccionadorMes.value < 10) {
            month = month.plus("0")
        }
        month = month.plus(seleccionadorMes.value)
        val fechaReferencia = org.threeten.bp.LocalDate.parse("${newVal}-${month}-01")

//        por alguna razon toma todos los anios como leapYear asi q le tenemos que restar uno
//        al mes de febrero
        var monthValue = fechaReferencia.month.maxLength()
        if (!fechaReferencia.isLeapYear && fechaReferencia.month.value == 2) {
            monthValue -= 1
        }

        seleccionadorDia.maxValue = monthValue
        seleccionadorDia.minValue = 1
    }

    private fun setearBotones(): Unit {

        botonAgregar.setOnClickListener {
//            toda la logica para guardar la tarea o verificar que la tarea tiene un formato adecuado
            val intent: Intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }

    }

    override fun notificar(data: NotificacionesUsuario) {
        TODO("Not yet implemented")
    }


}