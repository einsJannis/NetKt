package netkt.server

import netkt.dsl.PipelineBuilder
import netkt.engine.ServerEngine
import netkt.pipeline.Pipeline
import netkt.pipeline.PipelineContext
import netkt.pipeline.PipelineDirection
import netkt.session.SessionContext

class Server<CONFIG : ServerConfig, SERVER_CONTEXT : ServerContext, MODEL : Any>
    @OptIn(ExperimentalUnsignedTypes::class)
    constructor(
        val engine: ServerEngine<CONFIG, *, SERVER_CONTEXT, *>,
        engineConfig: CONFIG,
        val port: UShort,
        pipelineBuilder: PipelineBuilder<MODEL>
    ) {

    private val serverContext: SERVER_CONTEXT = engine.newServerContext(engineConfig, pipelineBuilder)

    suspend fun start() = engine.run {
        serverContext.bind(port)
        serverContext.listen { message ->
            pipeline.submitInbound(message)
        }
    }

}
