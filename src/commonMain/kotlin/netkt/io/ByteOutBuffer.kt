package netkt.io

interface ByteOutBuffer {

    fun write(byte: Byte)

    fun write(bytes: ByteArray)

    fun flush(): ByteArray

}