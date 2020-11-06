package netkt.pipeline

import kotlin.reflect.KClass

class InboundPipelineSection<IN : Any, OUT : Any> (
        val `in`: KClass<IN>,
        val out: KClass<OUT>,
        val name: String,
        val handler: InboundPipelineHandler<IN, OUT>,
        val context: PipelineContext,
        var next: InboundPipelineSection<OUT, *>? = null
) {
    fun submit(value: IN): Unit = handler.run {
        context.handleIn(value)
    }.let { next?.submit(it) }
}