package dev.maxsiomin.libpass.fragments.enteralias

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.libpass.R
import dev.maxsiomin.libpass.database.LibraryPass
import dev.maxsiomin.libpass.databinding.FragmentEnterAliasBinding
import dev.maxsiomin.libpass.fragments.base.BaseFragment
import dev.maxsiomin.libpass.fragments.base.BaseViewModel
import dev.maxsiomin.libpass.util.runOnIoCoroutine

/**
 * User goes to this fragment from ConfirmFragment after confirming their id
 */
@AndroidEntryPoint
class EnterAliasFragment : BaseFragment(R.layout.fragment_enter_alias) {

    private lateinit var binding: FragmentEnterAliasBinding

    override val mRoot get() = binding.root

    private val viewModel by viewModels<BaseViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentEnterAliasBinding.bind(view)

        binding.apply {
            buttonAdd.setOnClickListener {
                val alias = aliasEditText.text.toString()
                if (alias.isBlank() || "|" in alias) {
                    aliasEditTextLayout.error = viewModel.getString(R.string.wrong_alias)
                } else {
                    val libpassId = requireArguments().getString(ARG_LIBPASS_ID)!!

                    val libpass = LibraryPass(
                        libpassId,
                        alias,
                    )

                    processNewLibpass(libpass)
                }
            }
        }
    }

    /**
     * Adds libpass to database, returns to LibpassFragment
     */
    private fun processNewLibpass(libpass: LibraryPass) {
        // Create instance in main thread
        viewModel

        runOnIoCoroutine {
            pushLibpassToDatabase(libpass)
        }

        // Return to root screen
        findNavController().popBackStack(R.id.libpassFragment, false)
    }

    private suspend fun pushLibpassToDatabase(libpass: LibraryPass) {
        viewModel.dao.insertPass(libpass)
    }

    companion object {
        private const val ARG_LIBPASS_ID = "libpassId"
    }
}
