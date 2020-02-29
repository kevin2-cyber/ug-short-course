package shortcourse.homepa

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    // With DI
    private val firestore by inject<FirebaseFirestore>()

    // Without DI
    // private val firestore by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }
}