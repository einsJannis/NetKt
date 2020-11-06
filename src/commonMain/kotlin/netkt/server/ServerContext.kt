package netkt.server

import netkt.dsl.PipelineBuilder

interface ServerContext {
    var running: Boolean
    val pipelineBuilder: PipelineBuilder
}