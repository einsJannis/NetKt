package netkt.engine.jvm

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import netkt.dsl.ServerConfigBuilder
import netkt.server.ServerConfig

class JVMServerConfigBuilder : ServerConfigBuilder<ServerConfig> {
    var coroutineScope: CoroutineScope = GlobalScope
    override fun build() = object : ServerConfig {
        override val scope: CoroutineScope = coroutineScope
    }
}