import kotlinx.coroutines.GlobalScope
import netkt.dsl.server
import netkt.engine.jvm.JVMSocketEngine

@OptIn(ExperimentalUnsignedTypes::class)
fun main() {
    server(JVMSocketEngine, modelKClass = Unit::class) {
        port = 25565u
        engine {
            coroutineScope = GlobalScope
        }
        pipeline {
            outboundHandler<Unit, Unit>("test") {
                return@outboundHandler it
            }
        }
    }
}
