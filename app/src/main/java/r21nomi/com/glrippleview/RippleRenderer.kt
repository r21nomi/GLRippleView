package r21nomi.com.glrippleview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.nio.FloatBuffer
import java.util.concurrent.TimeUnit
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Created by Ryota Niinomi on 2017/05/24.
 */
class RippleRenderer(val context: Context) : GLSurfaceView.Renderer {

    companion object {
        private val NS_PER_SECOND = TimeUnit.SECONDS.toNanos(1).toFloat()

        private val VERTICES: FloatArray = floatArrayOf(
                -1.0f, 1.0f, 0.0f, // ↖ left top︎
                -1.0f, -1.0f, 0.0f, // ↙︎ left bottom
                1.0f, 1.0f, 0.0f, // ↗︎ right top
                1.0f, -1.0f, 0.0f   // ↘︎ right bottom
        )

        private val TEX_COORDS = floatArrayOf(
                0.0f, 0.0f, // ↖ left top︎
                0.0f, 1.0f, // ↙︎ left bottom
                1.0f, 0.0f, // ↗︎ right top
                1.0f, 1.0f  // ↘︎ right bottom
        )
    }

    private var programId: Int = 0
    private var textureId: Int = 0

    private val vertexBuffer: FloatBuffer = BufferUtil.convert(VERTICES)
    private val texcoordBuffer: FloatBuffer = BufferUtil.convert(TEX_COORDS)

    private val windowWidth: Float = WindowUtil.getWidth(context).toFloat()
    private val windowHeight: Float = WindowUtil.getHeight(context).toFloat()

    var rippleOffset: Float = 0f
    var rippleFrequency: Float = 0f
    var point: Pair<Float, Float> = Pair(0.5f, 0.5f)

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0f, 0f, 0f, 1f)

        try {
            programId = GLES20.glCreateProgram()

            GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER).let { vertexShader ->
                GLES20.glShaderSource(vertexShader, loadRawResource(context, R.raw.ripple_vertex))
                GLES20.glCompileShader(vertexShader)
                GLES20.glAttachShader(programId, vertexShader)
            }

            GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER).let { fragmentShader ->
                GLES20.glShaderSource(fragmentShader, loadRawResource(context, R.raw.ripple_fragment))
                GLES20.glCompileShader(fragmentShader)
                GLES20.glAttachShader(programId, fragmentShader)
            }

            GLES20.glLinkProgram(programId)
            GLES20.glUseProgram(programId)
        } catch (e: IOException) {
            Log.e(this.javaClass.name, e.message)
        }

        BitmapFactory.decodeResource(context.resources, R.drawable.bg).let { bitmap ->
            textureId = loadTexture(bitmap)
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        // position
        val position: Int = GLES20.glGetAttribLocation(programId, "position")
        GLES20.glEnableVertexAttribArray(position)
        GLES20.glVertexAttribPointer(position, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer)

        // texCoord
        val texCoord: Int = GLES20.glGetAttribLocation(programId, "texcoord")
        GLES20.glEnableVertexAttribArray(texCoord)
        GLES20.glVertexAttribPointer(texCoord, 2, GLES20.GL_FLOAT, false, 0, texcoordBuffer)

        // texture
        GLES20.glGetUniformLocation(programId, "texture").run {
            GLES20.glUniform1i(this, 0)
        }

        // resolution
        GLES20.glGetUniformLocation(programId, "resolution").run {
            GLES20.glUniform2f(this, windowWidth, windowHeight)
        }

        // time
        GLES20.glGetUniformLocation(programId, "time").run {
            val now = System.nanoTime()
            val delta = now / NS_PER_SECOND
            GLES20.glUniform1f(this, delta)
        }

        GLES20.glGetUniformLocation(programId, "rippleStrength").run {
            GLES20.glUniform1f(this, 10f)
        }

        GLES20.glGetUniformLocation(programId, "rippleOffset").run {
            GLES20.glUniform1f(this, rippleOffset)
        }

        GLES20.glGetUniformLocation(programId, "rippleFrequency").run {
            GLES20.glUniform1f(this, rippleFrequency)
        }

        GLES20.glGetUniformLocation(programId, "rippleCenterUvX").run {
            GLES20.glUniform1f(this, point.first)
        }

        GLES20.glGetUniformLocation(programId, "rippleCenterUvY").run {
            GLES20.glUniform1f(this, point.second)
        }

        GLES20.glGetUniformLocation(programId, "rippleSineDisappearDistance").run {
            GLES20.glUniform1f(this, 100f)
        }

        GLES20.glEnable(GLES20.GL_TEXTURE_2D)
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

        // disable
        GLES20.glDisableVertexAttribArray(position)
        GLES20.glDisableVertexAttribArray(texCoord)
        GLES20.glDisable(GLES20.GL_BLEND)
        GLES20.glDisable(GLES20.GL_TEXTURE_2D)
    }

    private fun loadTexture(bitmap: Bitmap): Int {
        return loadTexture(bitmap, GLES20.GL_NEAREST, GLES20.GL_LINEAR)
    }

    private fun loadTexture(bitmap: Bitmap, min: Int, mag: Int): Int {
        val textures = IntArray(1)
        GLES20.glGenTextures(1, textures, 0)

        val texture = textures[0]
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture)
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)

        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, min.toFloat())
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, mag.toFloat())

        return texture
    }

    @Throws(IOException::class)
    private fun loadRawResource(context: Context, id: Int): String {
        val inputStream: InputStream = context.resources.openRawResource(id)
        val l = inputStream.available()
        val b = ByteArray(l)
        return if (inputStream.read(b) == l) String(b) else ""
    }
}