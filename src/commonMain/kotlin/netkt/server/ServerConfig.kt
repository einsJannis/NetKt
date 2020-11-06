package netkt.server

import kotlinx.coroutines.CoroutineScope

interface ServerConfig {

    val scope: CoroutineScope

}