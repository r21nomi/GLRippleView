package r21nomi.com.glrippleview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.MotionEvent
import kotlin.properties.Delegates


/**
 * Created by Ryota Niinomi on 2017/05/24.
 */
class GLRippleView(context: Context, attrs: AttributeSet? = null) : GLSurfaceView(context, attrs) {

    companion object {
        val OPENGL_ES_VERSION = 2
    }

    private var bgImage: Bitmap? = null
    private var renderer: RippleRenderer by Delegates.notNull()

    var listener: Listener? = null

    init {
        setBackgroundImage(attrs)

        val image = bgImage?.run { mutableListOf(this) } ?: mutableListOf()

        renderer = RippleRenderer(context.applicationContext, image)

        setEGLContextClientVersion(OPENGL_ES_VERSION)
        setRenderer(renderer)
        renderMode = RENDERMODE_CONTINUOUSLY
    }

    /**
     * Add images to cross-fade.
     */
    fun addBackgroundImages(images: List<Bitmap>) {
        renderer.addBackgroundImages(images)
    }

    /**
     * Set duration for cross-fade.
     */
    fun setFadeDuration(duration: Long) {
        renderer.fadeDuration = duration
    }

    /**
     * Set interval time until start cross-fade.
     */
    fun setFadeInterval(interval: Long) {
        renderer.fadeInterval = interval
    }

    /**
     * Set center point of ripple.
     * @FloatRange(from = -1.0, to = 1.0)
     */
    fun setRipplePoint(xAndY: Pair<Float, Float>) {
        renderer.point = xAndY
    }

    /**
     * Set offset for ripple.
     * This value affects to ripple strength.
     */
    fun setRippleOffset(offset: Float) {
        renderer.rippleOffset = offset
    }

    /**
     * Start cross-fade animation.
     */
    fun startCrossFadeAnimation() {
        renderer.startCrossFadeAnimation()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return super.onTouchEvent(event)

        listener?.onTouchEvent(event)

        return true
    }

    private fun setBackgroundImage(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.GLRippleView)

        typedArray.getDrawable(R.styleable.GLRippleView_backgroundImage)?.let { drawable ->
            bgImage = (drawable as BitmapDrawable).bitmap
            typedArray.recycle()
        }
    }

    interface Listener {
        fun onTouchEvent(event: MotionEvent)
    }
}