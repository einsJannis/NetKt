package netkt.dsl

import netkt.engine.ServerEngine
import netkt.server.Server
import netkt.server.ServerConfig
import netkt.server.ServerContext
import netkt.session.SessionContext
import kotlin.reflect.KClass

@NetKtDsl
class ServerBuilder<CONFIG : ServerConfig, CONFIG_BUILDER : ServerConfigBuilder<CONFIG>, CONTEXT : ServerContext, SESSION_CONTEXT : SessionContext, MODEL : Any>
    internal constructor(
        val serverEngine: ServerEngine<CONFIG, CONFIG_BUILDER, CONTEXT, SESSION_CONTEXT>,
        @OptIn(ExperimentalUnsignedTypes::class)
        var port: UShort = 80u,
        val modelKClass: KClass<MODEL>
    ) {

    private var pipelineBuilder: MutableList<PipelineBuilder<MODEL>.() -> Unit> = mutableListOf()

    fun pipeline(block: PipelineBuilder<MODEL>.() -> Unit) {
        pipelineBuilder.add(block)
    }

    private val configBuilder = serverEngine.newConfigBuilder()

    fun engine(block: CONFIG_BUILDER.() -> Unit) = configBuilder.block()

    internal fun build(): Server<CONFIG, CONTEXT> =
            Server(serverEngine, configBuilder.build(), port, pipelineBuilder)

}
