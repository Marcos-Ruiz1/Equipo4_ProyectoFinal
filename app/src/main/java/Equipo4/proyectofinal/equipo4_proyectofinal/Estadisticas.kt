package Equipo4.proyectofinal.equipo4_proyectofinal

import Dominio.Task
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Estadisticas : AppCompatActivity() {

    private lateinit var yearChart: BarChart
    private lateinit var monthChart: BarChart
    private lateinit var weekChart: BarChart
    private var tasks = ArrayList<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_estadisticas)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupTasks()

        setUpCharts()
    }

    private fun setupTasks() {
        tasks.add(Task("realizar informes", "tengo que hacer los informes que me pide el jefe...", "10/06/2024", "Educación", 10, true))
        tasks.add(Task("Presentación del proyecto", "Preparar y presentar el proyecto final...", "15/07/2024", "Escuela", 9, false))
        tasks.add(Task("Reunión con el cliente", "Reunirse con el cliente para discutir...", "20/07/2024", "Trabajo", 8, true))
        tasks.add(Task("Investigación de mercado", "Realizar una investigación de mercado...", "25/07/2024", "Trabajo", 7, false))
        tasks.add(Task("Desarrollo de la app", "Implementar las funcionalidades principales...", "30/07/2024", "Tecnología", 9, false))
        tasks.add(Task("Documentación del código", "Escribir la documentación detallada...", "05/08/2024", "Tecnología", 8, true))
        tasks.add(Task("Presentación del proyecto", "Preparar y presentar el proyecto final...", "15/07/2024", "Escuela", 9, false))
        tasks.add(Task("Reunión con el cliente", "Reunirse con el cliente para discutir...", "20/07/2024", "Trabajo", 8, true))
        tasks.add(Task("Investigación de mercado", "Realizar una investigación de mercado...", "25/07/2024", "Trabajo", 7, false))
        tasks.add(Task("Desarrollo de la app", "Implementar las funcionalidades principales...", "30/07/2024", "Tecnología", 9, false))
        tasks.add(Task("Documentación del código", "Escribir la documentación detallada...", "05/08/2024", "Tecnología", 8, true))
        tasks.add(Task("Presentación del proyecto", "Preparar y presentar el proyecto final...", "15/07/2024", "Escuela", 9, false))
        tasks.add(Task("Investigación de mercado", "Realizar una investigación de mercado...", "25/07/2024", "Trabajo", 7, false))
        tasks.add(Task("Desarrollo de la app", "Implementar las funcionalidades principales...", "30/07/2024", "Tecnología", 9, false))
        tasks.add(Task("Documentación del código", "Escribir la documentación detallada...", "05/08/2024", "Tecnología", 8, true))
        tasks.add(Task("Lanzamiento del producto", "Preparar y lanzar el producto al mercado...", "10/08/2024", "Marketing", 10, false))
    }

    private fun setUpCharts() {
        yearChart = findViewById(R.id.YearChart)
        monthChart = findViewById(R.id.MonthChart)
        weekChart = findViewById(R.id.WeekChart)

        setupChart(yearChart, getTasksPerCategory(tasks, "yyyy"))
        setupChart(monthChart, getTasksPerCategory(tasks, "yyyy-MM"))
        setupChart(weekChart, getTasksPerCategory(tasks, "yyyy-'W'ww"))
    }



    private fun setupChart(chart: BarChart, data: Map<String, Int>) {
        val entries = data.entries.mapIndexed { index, entry -> BarEntry(index.toFloat(), entry.value.toFloat()) }
        val dataSet = BarDataSet(entries, "Tareas finalizadas")
        dataSet.color = Color.BLUE

        val barData = BarData(dataSet)
        barData.barWidth = 0.9f

        chart.data = barData
        chart.setFitBars(true)
        chart.invalidate()

        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase): String {
                return data.keys.elementAtOrNull(value.toInt()) ?: value.toString()
            }
        }

        val yAxis = chart.axisLeft
        yAxis.axisMinimum = 0f
        yAxis.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase): String {
                return value.toInt().toString()
            }
        }
        chart.axisRight.isEnabled = false
    }

    private fun getTasksPerCategory(tasks: List<Task>, dateFormat: String): Map<String, Int> {
        val format = SimpleDateFormat(dateFormat, Locale.getDefault())
        val taskMap = tasks.filter { it.terminado }
            .groupBy { format.format(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(it.fecha)) }
            .mapValues { entry -> entry.value.groupBy { it.categoria }.mapValues { it.value.size } }
        return taskMap.flatMap { it.value.entries }
            .groupBy { it.key }
            .mapValues { it.value.sumBy { it.value } }
    }
}
