package netkt.engine.jvm

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import netkt.client.ClientConfig
import netkt.dsl.PipelineBuilder
import netkt.dsl.ServerConfigBuilder
import netkt.engine.ClientEngine
import netkt.engine.ServerEngine
import netkt.exception.AlreadyConnectedException
import netkt.exception.ConnectionClosedException
import netkt.io.ByteInBuffer
import netkt.io.ByteOutBuffer
import netkt.io.toByteInBuffer
import netkt.server.ServerConfig
import netkt.session.SessionContext
import java.net.ServerSocket
import java.net.Socket

object JVMSocketEngine : ClientEngine<ClientConfig, JVMClientContext>, ServerEngine<ServerConfig, JVMServerConfigBuilder, JVMServerContext, JVMSessionContext> {

    override fun newClientContext(config: ClientConfig): JVMClientContext = JVMClientContext()

    @OptIn(ExperimentalUnsignedTypes::class)
    override suspend fun JVMClientContext.connect(address: String, port: UShort, sessionHandler: (ByteInBuffer) -> Unit) {
        if(socket == null)
            socket = Socket(address, port.toInt())
        else throw AlreadyConnectedException()
    }

    override suspend fun JVMClientContext.write(bytes: ByteOutBuffer) {
        if (socket?.isConnected == true)
            outputStream.write(bytes.flush())
        else throw ConnectionClosedException()
    }

    override suspend fun JVMClientContext.close() {
        socket?.close() ?: throw ConnectionClosedException()
    }

    override fun newConfigBuilder(): JVMServerConfigBuilder = JVMServerConfigBuilder()

    override fun newServerContext(config: ServerConfig, pipelineBuilder: PipelineBuilder): JVMServerContext = JVMServerContext(config, pipelineBuilder)

    @OptIn(ExperimentalUnsignedTypes::class)
    override suspend fun JVMServerContext.bind(port: UShort) {
        socket = ServerSocket(port.toInt())
    }

    override suspend fun JVMServerContext.listen(sessionHandler: suspend SessionContext.(ByteInBuffer) -> Unit) {
        if (socket == null) throw ConnectionClosedException()
        running = true
        while (running) {
            val sessionContext = JVMSessionContext(socket!!.accept())
            config.scope.launch {
                while (sessionContext.socket.isConnected) {
                    sessionContext.sessionHandler(sessionContext.socket.getInputStream().readAllBytes().toByteInBuffer())
                    sessionContext.outData.forEach {
                        sessionContext.socket.getOutputStream().write(it.flush())
                    }
                }
            }
        }
    }

    override suspend fun JVMSessionContext.write(bytes: ByteOutBuffer) {
        outData += bytes
    }

}