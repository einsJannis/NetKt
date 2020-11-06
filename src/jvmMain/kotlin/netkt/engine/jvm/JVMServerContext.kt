package netkt.engine.jvm

import netkt.dsl.PipelineBuilder
import netkt.server.ServerConfig
import netkt.server.ServerContext
import java.net.ServerSocket

class JVMServerContext(
        val config: ServerConfig,
        override val pipelineBuilder: PipelineBuilder
) : ServerContext {
    var socket: ServerSocket? = null
    override var running: Boolean = false
}
