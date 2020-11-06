package netkt.engine.jvm

import netkt.client.ClientContext
import netkt.exception.NoConnectionException
import java.net.Socket

class JVMClientContext() : ClientContext {
    var socket: Socket? = null
    val outputStream get() = socket?.getOutputStream() ?: throw NoConnectionException()
}