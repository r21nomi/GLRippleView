package r21nomi.com.glrippleview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GLRippleView(this).run {
            setContentView(this)
        }
    }
}
