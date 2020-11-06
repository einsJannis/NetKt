package netkt.io

fun ByteArray.toByteInBuffer(): ByteInBuffer = object : ByteInBuffer {
    var index = 0
    val byteArray = this@toByteInBuffer
    override fun read(): Byte = byteArray[index].also { index++ }
    override fun read(amount: Int): ByteArray =
            byteArray.copyOfRange(index, index + amount).also { index += amount }
    override fun hasNext(): Boolean =
            byteArray.size < index
}
