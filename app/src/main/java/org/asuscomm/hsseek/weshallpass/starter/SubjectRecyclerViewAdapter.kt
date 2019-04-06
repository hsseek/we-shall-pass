package org.asuscomm.hsseek.weshallpass.starter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import org.asuscomm.hsseek.weshallpass.R


import org.asuscomm.hsseek.weshallpass.starter.ExamSubjectsFragment.OnSubjectInteractionListener

import kotlinx.android.synthetic.main.fragment_subject_item.view.*
import org.asuscomm.hsseek.weshallpass.models.Subject

const val VIEW_TYPE_SUBJECT = 1
const val VIEW_TYPE_FOOTER = 2

class SubjectRecyclerViewAdapter(
    var subjects: MutableList<Subject>,
    private val mListener: OnSubjectInteractionListener?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View

        return if (viewType == VIEW_TYPE_FOOTER) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_subject_add, parent, false)
            FooterViewHolder(view)
        } else {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_subject_item, parent, false)
            SubjectViewHolder(view)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == subjects.size) VIEW_TYPE_FOOTER else VIEW_TYPE_SUBJECT
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FooterViewHolder) { /* The add button row */
            holder.itemView.setOnClickListener {
                // TODO: Add a subject
            }
        } else if (holder is SubjectViewHolder) { /* Normal subject item */
            val subject = subjects[position]
            holder.subjectInclude.setOnCheckedChangeListener { buttonView, isChecked ->

            }
            holder.subjectTitle.text = subject.title
            holder.subjectDuration.text = subject.duration.toString()

//            with(holder.mView) {
//                tag = subject
//                setOnClickListener(mOnClickListener)
//            }
        }
    }

    override fun getItemCount(): Int = subjects.size + 1

    inner class SubjectViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val subjectInclude: CheckBox = mView.checkbox_subject_include
        val subjectTitle: TextView = mView.text_subject_title
        val subjectDuration: TextView = mView.edit_subject_duration
        val subjectDelete: ImageView = mView.button_subject_remove
    }

    inner class FooterViewHolder(mView: View): RecyclerView.ViewHolder(mView)
}
