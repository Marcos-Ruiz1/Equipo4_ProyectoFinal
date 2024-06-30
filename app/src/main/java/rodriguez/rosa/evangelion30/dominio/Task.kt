package rodriguez.rosa.evangelion30.dominio

data class Task(
    var titulo: String,
    var descripcion: String,
    var fecha: String,
    var categoria: String,
    var prioridad: Int,
    var terminado: Boolean)