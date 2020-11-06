package netkt.pipeline

interface InboundPipelineHandler<in IN, out OUT> : PipelineHandler {
    fun PipelineContext.handleIn(value: IN): OUT
}
