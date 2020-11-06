package netkt.dsl

import netkt.pipeline.InboundPipelineHandler
import netkt.pipeline.OutboundPipelineHandler
import netkt.pipeline.Pipeline
import netkt.pipeline.PipelineContext
import netkt.session.SessionContext
import kotlin.reflect.KClass

@NetKtDsl
class PipelineBuilder<T : Any>(sessionContext: SessionContext, tKClass: KClass<T>) {

    private val pipeline: Pipeline<T> = Pipeline(sessionContext, tKClass)

    inline fun <reified IN : Any, reified OUT : Any> inboundHandler(name: String, crossinline handler: PipelineContext.(IN) -> OUT) =
            inboundHandler(name, object : InboundPipelineHandler<IN, OUT> {
                override fun PipelineContext.handleIn(value: IN): OUT = handler(value)
            })

    inline fun <reified IN : Any, reified OUT : Any> inboundHandler(name: String, handler: InboundPipelineHandler<IN, OUT>) =
            inboundHandler(name, IN::class, OUT::class, handler)

    fun <IN : Any, OUT : Any> inboundHandler(name: String, `in`: KClass<IN>, out: KClass<OUT>, handler: InboundPipelineHandler<IN, OUT>) =
            pipeline.insertLast(`in`, out, name, handler)

    inline fun <reified IN : Any, reified OUT : Any> outboundHandler(name: String, crossinline handler: PipelineContext.(IN) -> OUT) =
            outboundHandler(name, object : OutboundPipelineHandler<IN, OUT> {
                override fun PipelineContext.handleOut(value: IN): OUT = handler(value)
            })

    inline fun <reified IN : Any, reified OUT : Any> outboundHandler(name: String, handler: OutboundPipelineHandler<IN, OUT>) =
            outboundHandler(name, IN::class, OUT::class, handler)

    fun <IN : Any, OUT : Any> outboundHandler(name: String, `in`: KClass<IN>, out: KClass<OUT>, handler: OutboundPipelineHandler<IN, OUT>) =
            pipeline.insertLast(`in`, out, name, handler)


}