package netkt.pipeline

interface PipelineHandler<INBOUND, OUTBOUND> :
        InboundPipelineHandler<INBOUND, OUTBOUND>, OutboundPipelineHandler<OUTBOUND, INBOUND>