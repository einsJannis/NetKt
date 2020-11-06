package netkt.engine.jvm

import netkt.io.ByteOutBuffer
import netkt.session.SessionContext
import java.net.Socket

class JVMSessionContext(val socket: Socket) : SessionContext {
    val outData = mutableListOf<ByteOutBuffer>()
}
