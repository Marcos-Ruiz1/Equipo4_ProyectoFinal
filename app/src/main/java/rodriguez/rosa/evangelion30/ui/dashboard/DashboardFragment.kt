package rodriguez.rosa.evangelion30.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import rodriguez.rosa.evangelion30.R
import rodriguez.rosa.evangelion30.databinding.FragmentDashboardBinding
import rodriguez.rosa.evangelion30.dominio.Task
import java.text.SimpleDateFormat
import java.util.Locale

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

//    del equipo
    private lateinit var yearChart: BarChart
    private lateinit var monthChart: BarChart
    private lateinit var weekChart: BarChart
    private var tasks = ArrayList<Task>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        configuracion

        yearChart =  root.findViewById(R.id.YearChart)
        monthChart = root.findViewById(R.id.MonthChart)
        weekChart = root.findViewById(R.id.WeekChart)

        fillTasks()
        setUpCharts()

//        fin de la configuracion

        return root
    }

    fun fillTasks() {
        val userID: String? = AuthManager.currentUserId

        if (userID != null) {
            val taskReference = FirebaseDatabase.getInstance().reference
                .child("User")
                .child(userID)
                .child("Tasks")

            taskReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val tasks = mutableListOf<Task>()

                    for (taskSnapshot in snapshot.children) {
                        try {
                            // Extract data from the snapshot as a HashMap
                            val taskMap = taskSnapshot.value as HashMap<*, *>

                            // Create a Task object manually from the HashMap
                            val titulo = taskMap["titulo"] as String
                            val descripcion = taskMap["descripcion"] as String
                            val fecha = taskMap["fecha"] as String
                            val categoria = taskMap["categoria"] as String
                            val prioridad = (taskMap["prioridad"] as Long).toInt() // Firebase returns Long for Integers
                            val terminado = taskMap["terminado"] as Boolean

                            val task = Task(titulo, descripcion, fecha, categoria, prioridad, terminado)
                            tasks.add(task)
                        } catch (e: Exception) {
                            Log.e("Task Conversion", "Error converting Task", e)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle cancelled event
                    Log.e("Firebase Database", "Error fetching Tasks", error.toException())
                }
            })
        }
    }

    private fun setUpCharts() {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}