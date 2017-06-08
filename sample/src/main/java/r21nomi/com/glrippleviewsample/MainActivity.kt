package r21nomi.com.glrippleviewsample

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Point
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Display
import android.view.MotionEvent
import android.view.WindowManager
import r21nomi.com.glrippleview.AnimationUtil
import r21nomi.com.glrippleview.GLRippleView
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val glRippleView: GLRippleView by lazy {
        findViewById(R.id.glRippleView) as GLRippleView
    }

    private val windowWidth: Float by lazy {
        getWidth(this)
    }

    private val windowHeight: Float by lazy {
        getHeight(this)
    }

    private val listener: GLRippleView.Listener = object : GLRippleView.Listener {
        override fun onTouchEvent(event: MotionEvent) {
            if (event.action == MotionEvent.ACTION_MOVE) {
                // center position
                glRippleView.setRipplePoint(Pair(
                        AnimationUtil.map(event.x, 0f, windowWidth, -1f, 1f),
                        AnimationUtil.map(event.y, 0f, windowHeight, -1f, 1f)
                ))

                // offset (x)
                (AnimationUtil.map(event.x / windowWidth, 0f, 1f, 0f, 0.02f)).let { value ->
                    Log.d(this.javaClass.name, "rippleOffset : " + value)
                    glRippleView.setRippleOffset(value)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        glRippleView.run {
            listener = this@MainActivity.listener
            addBackgroundImages(listOf(
                    BitmapFactory.decodeResource(resources, R.drawable.bg2),
                    BitmapFactory.decodeResource(resources, R.drawable.bg3)
            ))
            setFadeInterval(TimeUnit.SECONDS.toMillis(5))
            startCrossFadeAnimation()
        }
    }

    override fun onStart() {
        super.onStart()
        glRippleView.onResume()
    }

    override fun onStop() {
        glRippleView.onPause()
        super.onStop()
    }

    fun getWidth(context: Context): Float {
        val display = getDisplay(context)
        val size = Point()
        display.getSize(size)
        return size.x.toFloat()
    }

    fun getHeight(context: Context): Float {
        val display = getDisplay(context)
        val size = Point()
        display.getSize(size)
        return size.y.toFloat()
    }

    private fun getDisplay(context: Context): Display {
        return (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
    }
}
