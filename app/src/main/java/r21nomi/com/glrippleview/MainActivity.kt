package r21nomi.com.glrippleview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    val glRippleView1: GLRippleView by lazy {
        findViewById(R.id.glRippleView1) as GLRippleView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }
}
