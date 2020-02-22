package shortcourse.readium.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import shortcourse.readium.core.viewmodel.AccountViewModel
import shortcourse.readium.databinding.FragmentAccountBinding

/**
 * A simple [Fragment] subclass.
 */
class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private val accountViewModel by viewModel<AccountViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.run {

        }
    }

}