package shortcourse.homepa

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.android.ext.android.inject
import shortcourse.homepa.util.debugger

class MainActivity : AppCompatActivity() {

    // With DI
    private val firestore by inject<FirebaseFirestore>()

    // Without DI
    // private val firestore1 by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        debugger(firestore)

        firestore.collection("demo")
            .get()
            .addOnCompleteListener(this) { task ->
                debugger(task.isSuccessful)
            }
    }
}