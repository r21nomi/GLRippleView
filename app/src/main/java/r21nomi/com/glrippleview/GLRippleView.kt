package r21nomi.com.glrippleview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent


/**
 * Created by Ryota Niinomi on 2017/05/24.
 */
class GLRippleView(context: Context, attrs: AttributeSet? = null) : GLSurfaceView(context, attrs) {

    companion object {
        val OPENGL_ES_VERSION = 2
    }

    private var bgImage: Bitmap? = null
    private var renderer: RippleRenderer? = null
    private val windowWidth: Float = WindowUtil.getWidth(context).toFloat()
    private val windowHeight: Float = WindowUtil.getHeight(context).toFloat()

    init {
        setBackgroundImage(attrs)

        val image = bgImage?.run { mutableListOf(this) } ?: mutableListOf()

        renderer = RippleRenderer(context, image)

        setEGLContextClientVersion(OPENGL_ES_VERSION)
        setRenderer(renderer)
        renderMode = RENDERMODE_CONTINUOUSLY
    }

    fun addBackgroundImages(images: List<Bitmap>) {
        renderer?.addBackgroundImages(images)
    }

    fun setFadeDuration(duration: Long) {
        renderer?.fadeDuration = duration
    }

    fun setFadeInterval(interval: Long) {
        renderer?.fadeInterval = interval
    }

    fun startCrossFadeAnimation() {
        renderer?.startCrossFadeAnimation()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return super.onTouchEvent(event)

        if (event.action == MotionEvent.ACTION_MOVE) {
            // center position
            renderer?.point = Pair(
                    AnimationUtil.map(event.x, 0f, windowWidth, -1f, 1f),
                    AnimationUtil.map(event.y, 0f, windowHeight, -1f, 1f)
            )

            // offset (x)
            (AnimationUtil.map(event.x / windowWidth, 0f, 1f, 0f, 0.02f)).let { value ->
                Log.d(this.javaClass.name, "rippleOffset : " + value)
                renderer?.rippleOffset = value
            }

            // frequency (y)
            (AnimationUtil.map(event.y / height, 0f, 1f, 0f, 0.3f)).let { value ->
                Log.d(this.javaClass.name, "rippleFrequency : " + value)
                renderer?.rippleFrequency = value
            }
        }

        return true
    }

    private fun setBackgroundImage(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.GLRippleView)

        typedArray.getDrawable(R.styleable.GLRippleView_backgroundImage)?.let { drawable ->
            bgImage = (drawable as BitmapDrawable).bitmap
            typedArray.recycle()
        }
    }
}