package dev.maxsiomin.libpass.fragments.passes

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.libpass.R
import dev.maxsiomin.libpass.databinding.FragmentPassesBinding
import dev.maxsiomin.libpass.fragments.base.BaseFragment
import dev.maxsiomin.libpass.fragments.passes.PassesFragment.DeleteLibpassDialog.Companion.DIALOG_RESULT
import dev.maxsiomin.libpass.fragments.passes.PassesFragment.DeleteLibpassDialog.Companion.ID_TO_DELETE
import dev.maxsiomin.libpass.util.runOnIoCoroutine

/**
 * Fragment with recycler view. All libpasses are shown here
 */
@AndroidEntryPoint
class PassesFragment : BaseFragment(R.layout.fragment_passes) {

    private lateinit var binding: FragmentPassesBinding

    override val mRoot get() = binding.root

    private val viewModel by viewModels<PassesViewModel>()

    private lateinit var databaseLoader: DatabaseLoader

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPassesBinding.bind(view)

        databaseLoader = DatabaseLoader(viewModel)
        databaseLoader.itemsLiveData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                viewModel.toast(R.string.no_libpasses, Toast.LENGTH_SHORT)
                findNavController().popBackStack()
            } else
                onPassesListUpdated(it)
        }

        setFragmentResultListener(DIALOG_RESULT) { _, bundle ->
            val id = bundle.getInt(ID_TO_DELETE)
            onDeleteLibpass(id)
        }
    }

    private fun onPassesListUpdated(passesList: List<DatabaseLoader.PlaceholderItem>) {
        with (binding) {
            // Set the adapter
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter =
                PassesListAdapter(
                    viewModel,
                    passesList,
                    this@PassesFragment::onItemClicked,
                    this@PassesFragment::onItemLongClicked,
                )
        }
    }

    private fun onItemClicked(id: Int) {
        val direction = PassesFragmentDirections.actionPassesFragmentToEditFragment(id)
        findNavController().navigate(direction)
    }

    private fun onItemLongClicked(id: Int) {
        DeleteLibpassDialog.newInstance(id).show(parentFragmentManager)
    }

    private fun onDeleteLibpass(id: Int) {
        runOnIoCoroutine {
            viewModel.deleteLibpass(id)
            databaseLoader.getPasses()
        }
    }

    /**
     * Ask user "Do you want to delete this libpass?"
     */
    class DeleteLibpassDialog : DialogFragment() {

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

            val dialog = AlertDialog.Builder(requireContext())
                .setMessage(R.string.want_to_delete)
                .setNegativeButton(R.string.action_cancel) { _, _ ->
                    dismiss()
                }
                .setPositiveButton(R.string.delete) { _, _ ->
                    val id = requireArguments().getInt(ID_TO_DELETE)
                    setFragmentResult(DIALOG_RESULT, bundleOf(ID_TO_DELETE to id))
                    dismiss()
                }
                .create()

            dialog.setCanceledOnTouchOutside(false)

            return dialog
        }

        fun show(manager: FragmentManager) {
            show(manager, TAG)
        }

        companion object {

            const val TAG = "VerifyEmailDialog"
            const val DIALOG_RESULT = "dialogResult"
            const val ID_TO_DELETE = "idToDelete"

            @JvmStatic
            fun newInstance(id: Int) = DeleteLibpassDialog().apply {
                arguments = bundleOf(ID_TO_DELETE to id)
            }
        }
    }
}
