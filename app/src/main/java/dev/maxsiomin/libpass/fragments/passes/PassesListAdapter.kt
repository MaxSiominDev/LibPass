package dev.maxsiomin.libpass.fragments.passes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.maxsiomin.libpass.databinding.RecyclerViewItemBinding
import dev.maxsiomin.libpass.util.UiActions

/**
 * Recycler view adapter for [PassesFragment]
 */
class PassesListAdapter(
    private val uiActions: UiActions,
    private val values: List<DatabaseLoader.PlaceholderItem>,
    private val onItemClicked: (Int) -> Unit,
    private val onItemLongClicked: (Int) -> Unit
) : RecyclerView.Adapter<PassesListAdapter.ViewHolder>(), UiActions by uiActions {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            RecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.id = values[position].id
        holder.alias.text = values[position].alias
        holder.value.text = values[position].value
    }

    override fun getItemCount() = values.size

    inner class ViewHolder(binding: RecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root) {

        var id: Int? = null
        val alias = binding.itemAlias
        val value = binding.itemValue

        init {
            // If item clicked, go to edit fragment
            itemView.setOnClickListener {
                onItemClicked(id!!)
            }

            // If item long clicked, show dialog (delete or not)
            itemView.setOnLongClickListener {
                onItemLongClicked(id!!)
                true
            }
        }
    }
}
