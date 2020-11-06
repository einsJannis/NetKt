package netkt.pipeline

import netkt.io.ByteInBuffer
import netkt.io.ByteOutBuffer
import netkt.session.SessionContext
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class Pipeline<T : Any>(
        sessionContext: SessionContext,
        val tKClass: KClass<T>,
        private var inboundEntry: InboundPipelineSection<ByteInBuffer, out Any>? = null,
        private var outboundEntry: OutboundPipelineSection<T, out Any>? = null
) {

    private val context: PipelineContext = PipelineContext(this, sessionContext)

    fun submitInbound(byteInBuffer: ByteInBuffer) = inboundEntry?.submit(byteInBuffer)

    fun submitOutbound(value: T): ByteOutBuffer = TODO()

    inline fun <reified IN : Any, reified OUT : Any> insertLast(
            name: String,
            pipelineHandler: InboundPipelineHandler<IN, OUT>
    ) = insertLast(IN::class, OUT::class, name, pipelineHandler)

    fun <IN : Any, OUT : Any> insertLast(
            `in`: KClass<IN>,
            out: KClass<OUT>,
            name: String,
            pipelineHandler: InboundPipelineHandler<IN, OUT>
    ) {
        val new = InboundPipelineSection(`in`, out, name, pipelineHandler, context)
        if (inboundEntry == null) {
            if (`in` != ByteInBuffer::class) throw TODO()
            inboundEntry = new as InboundPipelineSection<ByteInBuffer, *>
        } else {
            var current: InboundPipelineSection<*, *> = inboundEntry!!
            while (current.next != null) {
                current = current.next!!
            }
            if (current.out != `in`) throw TODO()
            (current as InboundPipelineSection<*, IN>).next = new
        }
    }

    inline fun <reified IN : Any, reified OUT : Any> insertAfter(
            after: String,
            name: String,
            pipelineHandler: InboundPipelineHandler<IN, OUT>
    ) = insertAfter(after, IN::class, OUT::class, name, pipelineHandler)

    fun <IN : Any, OUT : Any> insertAfter(
        before: String,
        `in`: KClass<IN>,
        out: KClass<OUT>,
        name: String,
        pipelineHandler: InboundPipelineHandler<IN, OUT>
    ) {
        if (inboundEntry == null) throw TODO()
        var current: InboundPipelineSection<*, *> = inboundEntry!!
        while (current.name != before) {
            if (current.next == null) throw TODO()
            current = current.next!!
        }
        if (current.out != `in`) throw TODO()
        if (current.next?.let { out != it.`in` } == true) throw TODO()
        val next = current.next as InboundPipelineSection<OUT, *>?
        (current as InboundPipelineSection<*, IN>).next =
                InboundPipelineSection(`in`, out, name, pipelineHandler, context, next)
    }

    inline  fun <reified IN : Any, reified OUT : Any> insertBefore(
            after: String,
            name: String,
            pipelineHandler: InboundPipelineHandler<IN, OUT>
    ) = insertBefore(after, IN::class, OUT::class, name, pipelineHandler)

    fun <IN : Any, OUT : Any> insertBefore(
            after: String,
            `in`: KClass<IN>,
            out: KClass<OUT>,
            name: String,
            pipelineHandler: InboundPipelineHandler<IN, OUT>
    ) {
        if (inboundEntry == null) throw TODO()
        var current: InboundPipelineSection<*, *> = inboundEntry!!
        if (current.next?.name == after) {
            if (`in` != ByteInBuffer::class) throw TODO()
            if (current.next?.let { out != it.`in` } == true) throw TODO()
            val next = current.next as InboundPipelineSection<OUT, *>?
            inboundEntry = InboundPipelineSection(`in`, out, name, pipelineHandler, context, next) as InboundPipelineSection<ByteInBuffer, *>
        } else {
            do {
                if (current.next == null) throw TODO()
                current = current.next!!
            } while (current.next?.name != after)
            if (current.out != `in`) throw TODO()
            if (current.next?.let { `in` != it.`in` } == true) throw TODO()
            val next = current.next as InboundPipelineSection<OUT, *>?
            (current as InboundPipelineSection<*, IN>).next =
                    InboundPipelineSection(`in`, out, name, pipelineHandler, context, next)
        }
    }

    inline fun <reified OUT : Any> insertFirst(
            name: String,
            pipelineHandler: InboundPipelineHandler<ByteInBuffer, OUT>
    ) = insertFirst(OUT::class, name, pipelineHandler)

    fun <OUT : Any> insertFirst(
            out: KClass<OUT>,
            name: String,
            pipelineHandler: InboundPipelineHandler<ByteInBuffer, OUT>
    ) {
        if (inboundEntry?.let { out != it.`in` } == true) throw TODO()
        val next = inboundEntry?.next as InboundPipelineSection<OUT, *>?
        inboundEntry = InboundPipelineSection(ByteInBuffer::class, out, name, pipelineHandler, context, next)
    }
    inline fun <reified IN : Any, reified OUT : Any> insertLast(
            name: String,
            pipelineHandler: OutboundPipelineHandler<IN, OUT>
    ) = insertLast(IN::class, OUT::class, name, pipelineHandler)

    fun <IN : Any, OUT : Any> insertLast(
            `in`: KClass<IN>,
            out: KClass<OUT>,
            name: String,
            pipelineHandler: OutboundPipelineHandler<IN, OUT>
    ) {
        val new = OutboundPipelineSection(`in`, out, name, pipelineHandler, context)
        if (outboundEntry == null) {
            if (`in` != ByteInBuffer::class) throw TODO()
            outboundEntry = new as OutboundPipelineSection<T, *>
        } else {
            var current: OutboundPipelineSection<*, *> = outboundEntry!!
            while (current.next != null) {
                current = current.next!!
            }
            if (current.out != `in`) throw TODO()
            (current as OutboundPipelineSection<*, IN>).next = new
        }
    }

    inline fun <reified IN : Any, reified OUT : Any> insertAfter(
            after: String,
            name: String,
            pipelineHandler: OutboundPipelineHandler<IN, OUT>
    ) = insertAfter(after, IN::class, OUT::class, name, pipelineHandler)

    fun <IN : Any, OUT : Any> insertAfter(
            before: String,
            `in`: KClass<IN>,
            out: KClass<OUT>,
            name: String,
            pipelineHandler: OutboundPipelineHandler<IN, OUT>
    ) {
        if (outboundEntry == null) throw TODO()
        var current: OutboundPipelineSection<*, *> = outboundEntry!!
        while (current.name != before) {
            if (current.next == null) throw TODO()
            current = current.next!!
        }
        if (current.out != `in`) throw TODO()
        if (current.next?.let { out != it.`in` } == true) throw TODO()
        val next = current.next as OutboundPipelineSection<OUT, *>?
        (current as OutboundPipelineSection<*, IN>).next =
                OutboundPipelineSection(`in`, out, name, pipelineHandler, context, next)
    }

    inline  fun <reified IN : Any, reified OUT : Any> insertBefore(
            after: String,
            name: String,
            pipelineHandler: OutboundPipelineHandler<IN, OUT>
    ) = insertBefore(after, IN::class, OUT::class, name, pipelineHandler)

    fun <IN : Any, OUT : Any> insertBefore(
            after: String,
            `in`: KClass<IN>,
            out: KClass<OUT>,
            name: String,
            pipelineHandler: OutboundPipelineHandler<IN, OUT>
    ) {
        if (outboundEntry == null) throw TODO()
        var current: OutboundPipelineSection<*, *> = outboundEntry!!
        if (current.next?.name == after) {
            if (`in` != ByteInBuffer::class) throw TODO()
            if (current.next?.let { out != it.`in` } == true) throw TODO()
            val next = current.next as OutboundPipelineSection<OUT, *>?
            outboundEntry = OutboundPipelineSection(`in`, out, name, pipelineHandler, context, next) as OutboundPipelineSection<T, *>
        } else {
            do {
                if (current.next == null) throw TODO()
                current = current.next!!
            } while (current.next?.name != after)
            if (current.out != `in`) throw TODO()
            if (current.next?.let { `in` != it.`in` } == true) throw TODO()
            val next = current.next as OutboundPipelineSection<OUT, *>?
            (current as OutboundPipelineSection<*, IN>).next =
                    OutboundPipelineSection(`in`, out, name, pipelineHandler, context, next)
        }
    }

    inline fun <reified OUT : Any> insertFirst(
            name: String,
            pipelineHandler: OutboundPipelineHandler<T, OUT>
    ) = insertFirst(tKClass, OUT::class, name, pipelineHandler)

    fun <OUT : Any> insertFirst(
            `in`: KClass<T>,
            out: KClass<OUT>,
            name: String,
            pipelineHandler: OutboundPipelineHandler<T, OUT>
    ) {
        if (outboundEntry?.let { out != it.`in` } == true) throw TODO()
        val next = outboundEntry?.next as OutboundPipelineSection<OUT, *>?
        outboundEntry = OutboundPipelineSection(`in`, out, name, pipelineHandler, context, next)
    }

}
