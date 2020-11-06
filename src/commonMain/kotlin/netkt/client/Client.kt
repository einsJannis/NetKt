package netkt.client

import netkt.engine.ClientEngine
import netkt.pipeline.Pipeline

class Client<CONFIG : ClientConfig, CONTEXT : ClientContext, MODEL : Any>(
    val clientEngine: ClientEngine<CONFIG, CONTEXT>,
    val config: CONFIG,
    val address: String,
    val port: UShort,
    val pipeline: Pipeline<MODEL>
) {
    val connected = false
    val context: CONTEXT = clientEngine.newClientContext(config)
    suspend fun connect() = clientEngine.run {
        context.connect(address, port)
    }
    suspend fun request(value: MODEL): MODEL = clientEngine.run {
        pipeline.submitOutbound(value)
                .let { context.write(it) }
                .let { pipeline.submvitInbound(value) }
    }
}
