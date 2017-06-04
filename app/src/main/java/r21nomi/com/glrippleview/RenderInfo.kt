package r21nomi.com.glrippleview

import android.graphics.Bitmap
import java.nio.FloatBuffer

/**
 * Created by r21nomi on 2017/06/04.
 */
data class RenderInfo(
        val vertexBuffer: FloatBuffer,
        val texcoordBuffer: FloatBuffer,
        var programId: Int,
        var textureId: Int,
        val bgImage: Bitmap,
        var alpha: Float
)