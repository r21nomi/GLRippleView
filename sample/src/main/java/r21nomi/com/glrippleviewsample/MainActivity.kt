package r21nomi.com.glrippleviewsample

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import r21nomi.com.glrippleview.GLRippleView
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    val glRippleView: GLRippleView by lazy {
        findViewById(R.id.glRippleView) as GLRippleView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        glRippleView.run {
            addBackgroundImages(listOf(
                    BitmapFactory.decodeResource(resources, R.drawable.bg2),
                    BitmapFactory.decodeResource(resources, R.drawable.bg3)
            ))
            setFadeInterval(TimeUnit.SECONDS.toMillis(10))
            startCrossFadeAnimation()
        }
    }
}
