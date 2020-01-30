package io.codelabs.churchinc.view.recyclerview.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.codelabs.churchinc.R
import io.codelabs.churchinc.model.Sermon
import io.codelabs.churchinc.view.recyclerview.ContentViewHolder
import io.codelabs.churchinc.view.recyclerview.EmptyViewHolder
import kotlinx.android.synthetic.main.item_bible_study.view.*

class BibleStudiesAdapter constructor(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val dataset: MutableList<Sermon> = mutableListOf()

    companion object {
        private const val TYPE_EMPTY = -1
        private const val TYPE_CONTENT = 0
    }

    override fun getItemViewType(position: Int): Int = if (dataset.isEmpty()) TYPE_EMPTY else TYPE_CONTENT

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CONTENT -> ContentViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_bible_study,
                    parent,
                    false
                )
            )

            else /*TYPE_EMPTY*/ -> EmptyViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_empty,
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount(): Int = if (dataset.isEmpty()) 1 else dataset.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        /*when (getItemViewType(position)) {
            TYPE_CONTENT -> {
                if(holder is ContentViewHolder){

                }
            }
        }*/
        if (getItemViewType(position) == TYPE_CONTENT && holder is ContentViewHolder) {
            val sermon = dataset[position]
            holder.v.sermon_name.text = sermon.name
            holder.v.sermon_keytext.text = sermon.keyText.trim()

            holder.v.setOnClickListener {

            }
        }
    }

    fun addData(listOfNewSermons: MutableList<Sermon>) {
        dataset.clear()
        dataset.addAll(listOfNewSermons)
        notifyDataSetChanged()
    }
}