package dev.csshortcourse.assignmenttwo.view.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import dev.csshortcourse.assignmenttwo.databinding.FragmentUserBinding
import dev.csshortcourse.assignmenttwo.model.User
import dev.csshortcourse.assignmenttwo.util.BaseActivity
import dev.csshortcourse.assignmenttwo.util.BaseFragment
import dev.csshortcourse.assignmenttwo.util.gone
import dev.csshortcourse.assignmenttwo.util.visible
import dev.csshortcourse.assignmenttwo.view.adapter.UserAdapter
import dev.csshortcourse.assignmenttwo.view.adapter.UserClickListener


class UserFragment : BaseFragment() {

    private lateinit var binding: FragmentUserBinding
    private val viewModel: UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Adapter
        val userAdapter =
            UserAdapter(requireActivity() as BaseActivity, object : UserClickListener {
                override fun onClick(user: User) {
                    // todo: click user
                }
            })

        binding.loading.visible()
        viewModel.users.observe(viewLifecycleOwner, Observer { users ->
            binding.usersList.apply {
                this.adapter = userAdapter
                this.layoutManager = LinearLayoutManager(requireContext())
                this.itemAnimator = DefaultItemAnimator()
                this.setHasFixedSize(false)
            }
            binding.loading.gone()
            userAdapter.submitList(users)
        })
    }

}
