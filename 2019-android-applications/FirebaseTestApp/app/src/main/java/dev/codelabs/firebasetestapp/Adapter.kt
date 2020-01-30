package dev.codelabs.firebasetestapp

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list.view.*

class DataViewHolder(val v: View) : RecyclerView.ViewHolder(v)

class DataAdapter() : ListAdapter<DummyData, DataViewHolder>(DATA_DIFF) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        return DataViewHolder(
            parent.context.layoutInflater.inflate(
                R.layout.item_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val data = getItem(position)
        with(holder.v) {
            data_id.text = data.id
            data_title.text = data.title
            data_desc.text = data.description
        }
    }


    companion object {
        private val DATA_DIFF: DiffUtil.ItemCallback<DummyData> =
            object : DiffUtil.ItemCallback<DummyData>() {
                override fun areItemsTheSame(oldItem: DummyData, newItem: DummyData): Boolean =
                    oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: DummyData, newItem: DummyData): Boolean =
                    oldItem == newItem
            }
    }
}