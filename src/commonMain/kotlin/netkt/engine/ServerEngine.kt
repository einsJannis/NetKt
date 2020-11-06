package netkt.engine

import netkt.dsl.PipelineBuilder
import netkt.io.ByteInBuffer
import netkt.io.ByteOutBuffer
import netkt.server.ServerConfig
import netkt.server.ServerContext
import netkt.session.SessionContext

interface ServerEngine<CONFIG: ServerConfig, CONFIG_BUILDER, CONTEXT : ServerContext, SESSION_CONTEXT : SessionContext> : Engine {

    fun newConfigBuilder(): CONFIG_BUILDER

    fun newServerContext(config: CONFIG, pipelineBuilder: PipelineBuilder): CONTEXT

    @OptIn(ExperimentalUnsignedTypes::class)
    suspend fun CONTEXT.bind(port: UShort)

    suspend fun CONTEXT.listen(sessionHandler: suspend SessionContext.(ByteInBuffer) -> Unit)

    suspend fun SESSION_CONTEXT.write(bytes: ByteOutBuffer)

}