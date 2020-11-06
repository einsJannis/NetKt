package netkt.pipeline

interface OutboundPipelineHandler<in IN, out OUT> : PipelineHandler {
    fun PipelineContext.handleOut(value: IN): OUT
}