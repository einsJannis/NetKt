package netkt.pipeline

import netkt.session.SessionContext

class PipelineContext(
    internal val pipeline: Pipeline,
    val sessionContext: SessionContext
) {

}