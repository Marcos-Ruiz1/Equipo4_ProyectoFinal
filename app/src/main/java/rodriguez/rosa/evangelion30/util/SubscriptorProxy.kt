package rodriguez.rosa.evangelion30.util

interface SubscriptorProxy {
    fun notificar(data: NotificacionesUsuario, topic: Topics)
}