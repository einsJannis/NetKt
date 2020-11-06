package netkt.engine

import netkt.client.ClientConfig
import netkt.client.ClientContext
import netkt.io.ByteInBuffer
import netkt.io.ByteOutBuffer

interface ClientEngine<CONFIG: ClientConfig, CONTEXT: ClientContext> : Engine {

    fun newClientContext(config: CONFIG): CONTEXT

    @OptIn(ExperimentalUnsignedTypes::class)
    suspend fun CONTEXT.connect(address: String, port: UShort)

    suspend fun CONTEXT.write(bytes: ByteOutBuffer): ByteInBuffer

    suspend fun CONTEXT.close()

}
