package com.shri.nycschools.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.shri.nycschools.R
import com.shri.nycschools.model.HighSchoolDTO

class SchoolListAdapter(private val dataSet: List<HighSchoolDTO>) : RecyclerView.Adapter<SchoolListAdapter.SchoolListViewHolder>(){
    class SchoolListViewHolder(itemView: View) : ViewHolder(itemView) {
        val schoolTitleTextView: TextView
        val schoolContentTextView: TextView
        init {
            schoolTitleTextView = itemView.findViewById(R.id.item_content1)
            schoolContentTextView = itemView.findViewById(R.id.item_content2)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.school_card_list_item, parent, false)
        return SchoolListViewHolder(view)
    }

    override fun onBindViewHolder(holder: SchoolListViewHolder, position: Int) {
        holder.schoolTitleTextView.text = dataSet[position].school_name
        holder.schoolContentTextView.text = dataSet[position].city
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}