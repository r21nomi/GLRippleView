package r21nomi.com.glrippleview

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import r21nomi.com.glrippleview.AnimationUtil
import r21nomi.com.glrippleview.WindowUtil

/**
 * Created by Ryota Niinomi on 2017/05/24.
 */
class GLRippleView(context: Context, attrs: AttributeSet? = null) : GLSurfaceView(context, attrs) {

    companion object {
        val OPENGL_ES_VERSION = 2
    }

    val renderer: RippleRenderer = RippleRenderer(context)

    init {
        setEGLContextClientVersion(OPENGL_ES_VERSION)
        setRenderer(renderer)
        renderMode = RENDERMODE_CONTINUOUSLY
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return super.onTouchEvent(event)

        if (event.action == MotionEvent.ACTION_MOVE) {
            // offset (x)
            (AnimationUtil.map(event.x / WindowUtil.getWidth(context), 0f, 1f, 0f, 0.02f)).let { value ->
                Log.d(this.javaClass.name, "rippleOffset : " + value)
                renderer.rippleOffset = value
            }

            // frequency (y)
            (AnimationUtil.map(event.y / WindowUtil.getHeight(context), 0f, 1f, 0f, 0.3f)).let { value ->
                Log.d(this.javaClass.name, "rippleFrequency : " + value)
                renderer.rippleFrequency = value
            }
        }

        return true
    }
}