package shortcourse.readium.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import org.jetbrains.anko.support.v4.toast
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import shortcourse.readium.core.storage.AccountPrefs
import shortcourse.readium.core.util.debugger
import shortcourse.readium.core.viewmodel.AccountViewModel
import shortcourse.readium.databinding.FragmentSettingsBinding


/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val accountViewModel by viewModel<AccountViewModel>()

    // Account Prefs instance
    private val prefs by inject<AccountPrefs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Check user login state
        if (!prefs.isLoggedIn) {
            toast("You are not logged in yet")
            findNavController().navigate(SettingsFragmentDirections.actionNavSettingsToNavAuth())
            return
        }

        lifecycleScope.launchWhenStarted {
            accountViewModel.currentUser.observe(viewLifecycleOwner, Observer {
                debugger("Logged in as -> $it")
            })
        }

    }

}