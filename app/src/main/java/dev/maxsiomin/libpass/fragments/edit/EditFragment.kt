package dev.maxsiomin.libpass.fragments.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.libpass.R
import dev.maxsiomin.libpass.databinding.FragmentEditBinding
import dev.maxsiomin.libpass.extensions.runOnUiThread
import dev.maxsiomin.libpass.extensions.toEditable
import dev.maxsiomin.libpass.fragments.base.BaseFragment
import dev.maxsiomin.libpass.util.runOnIoCoroutine

@AndroidEntryPoint
class EditFragment : BaseFragment(R.layout.fragment_edit) {

    private lateinit var binding: FragmentEditBinding

    override val mRoot get() = binding.root

    private val viewModel by viewModels<EditViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentEditBinding.bind(view)

        val id = requireArguments().getInt(ARG_LIBPASS_ID)
        viewModel.loadLibpass(id)

        binding.apply {
            run {
                val default = viewModel.isDefaultLibpass(id)
                if (default) checkBox.isEnabled = false
                checkBox.isChecked = viewModel.isDefaultLibpass(id)
            }

            if (checkBox.isEnabled) {
                viewModel.getLibpassesCount {
                    runOnUiThread {
                        checkBox.isEnabled = it > 1
                    }
                }
            }

            viewModel.libpassLiveData.observe(viewLifecycleOwner) { libpass ->
                aliasEditText.text = libpass.alias.toEditable()
                idEditText.text = libpass.value.toEditable()
            }

            buttonCancel.setOnClickListener {
                findNavController().popBackStack()
            }

            buttonSave.setOnClickListener {
                if (viewModel.isDefaultLibpass(id) != checkBox.isChecked) {
                    runOnIoCoroutine {
                        viewModel.resaveDefaultLibpass(id)
                    }
                }

                if (viewModel.libpassLiveData.value!!.value != idEditText.text.toString() || viewModel.libpassLiveData.value!!.alias != aliasEditText.text.toString()) {
                    viewModel.resaveLibpass(id, idEditText.text.toString(), aliasEditText.text.toString())
                }

                findNavController().popBackStack()
            }
        }
    }

    companion object {
        const val ARG_LIBPASS_ID = "id"
    }
}
