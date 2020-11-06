package netkt.session

import netkt.pipeline.Pipeline

interface SessionContext {

    val id: Int

    val pipeline: Pipeline<*>

}
