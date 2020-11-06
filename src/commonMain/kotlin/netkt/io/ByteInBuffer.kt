package netkt.io

interface ByteInBuffer : Iterable<Byte> {

    fun read(): Byte

    fun read(amount: Int): ByteArray

    fun hasNext(): Boolean

    override fun iterator(): Iterator<Byte> = object : Iterator<Byte> {

        override fun hasNext(): Boolean = this@ByteInBuffer.hasNext()

        override fun next(): Byte = this@ByteInBuffer.read()

    }

}