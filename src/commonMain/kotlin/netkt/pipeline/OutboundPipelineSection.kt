package netkt.pipeline

import kotlin.reflect.KClass

class OutboundPipelineSection<IN : Any, OUT : Any> (
        val `in`: KClass<IN>,
        val out: KClass<OUT>,
        val name: String,
        val handler: OutboundPipelineHandler<IN, OUT>,
        val context: PipelineContext,
        var next: OutboundPipelineSection<OUT, *>? = null
) {
    fun submit(value: IN): Unit = handler.run {
        context.handleOut(value)
    }.let { next?.submit(it) }
}