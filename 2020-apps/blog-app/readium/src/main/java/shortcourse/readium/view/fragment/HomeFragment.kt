package shortcourse.readium.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.dropbox.android.external.store4.MemoryPolicy
import com.dropbox.android.external.store4.StoreBuilder
import shortcourse.readium.R
import shortcourse.readium.core.database.ReadiumDatabase
import shortcourse.readium.core.model.account.Account
import shortcourse.readium.core.util.debugger

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val db = ReadiumDatabase.getInstance(requireContext())
        db.postDao()
            .getAllPosts().observe(viewLifecycleOwner, Observer {
                debugger("Showing posts -> $it")
            })

        StoreBuilder.from<String,Account> { db.accountDao().getAccount(it) }
            .cachePolicy(memoryPolicy = MemoryPolicy.builder().build())
            .build()
    }

}