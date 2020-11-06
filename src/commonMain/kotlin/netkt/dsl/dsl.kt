package netkt.dsl

import netkt.engine.ServerEngine
import netkt.server.Server
import netkt.server.ServerConfig
import netkt.server.ServerContext
import netkt.session.SessionContext
import kotlin.reflect.KClass

@DslMarker
annotation class NetKtDsl

fun client(block: ClientBuilder.() -> Unit) = ClientBuilder().apply(block).build()

inline fun <CONFIG : ServerConfig, CONFIG_BUILDER : ServerConfigBuilder<CONFIG>, CONTEXT : ServerContext, SESSION_CONTEXT : SessionContext, reified MODEL : Any>
        server(
        serverEngine: ServerEngine<CONFIG, CONFIG_BUILDER, CONTEXT, SESSION_CONTEXT>,
        port: UShort = 80u,
        noinline block: ServerBuilder<CONFIG, CONFIG_BUILDER, CONTEXT, SESSION_CONTEXT, MODEL>.() -> Unit,
        ): Server<CONFIG, CONTEXT, MODEL> = server(serverEngine, port, MODEL::class, block)

fun <CONFIG : ServerConfig, CONFIG_BUILDER : ServerConfigBuilder<CONFIG>, CONTEXT : ServerContext, SESSION_CONTEXT : SessionContext, MODEL : Any>
        server(
            serverEngine: ServerEngine<CONFIG, CONFIG_BUILDER, CONTEXT, SESSION_CONTEXT>,
            port: UShort = 80u,
            modelKClass: KClass<MODEL>,
            block: ServerBuilder<CONFIG, CONFIG_BUILDER, CONTEXT, SESSION_CONTEXT, MODEL>.() -> Unit,
        ): Server<CONFIG, CONTEXT, MODEL> =
        ServerBuilder(serverEngine, port, modelKClass).apply(block).build()
