package netkt.dsl

import netkt.server.ServerConfig

@NetKtDsl
interface ServerConfigBuilder<CONFIG : ServerConfig> {
    fun build(): CONFIG
}