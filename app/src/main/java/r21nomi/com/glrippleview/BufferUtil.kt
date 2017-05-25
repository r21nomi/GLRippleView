package r21nomi.com.glrippleview

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer


/**
 * Created by Ryota Niinomi on 2017/05/24.
 */
object BufferUtil {

    fun convert(data: FloatArray): FloatBuffer {
        val bb: ByteBuffer = ByteBuffer.allocateDirect(data.size * 4).apply {
            order(ByteOrder.nativeOrder())
        }
        return bb.asFloatBuffer().apply {
            put(data)
            position(0)
        }
    }

    fun convert(data: ShortArray): ShortBuffer {
        val bb: ByteBuffer = ByteBuffer.allocateDirect(data.size * 2).apply {
            order(ByteOrder.nativeOrder())
        }
        return bb.asShortBuffer().apply {
            put(data)
            position(0)
        }
    }
}