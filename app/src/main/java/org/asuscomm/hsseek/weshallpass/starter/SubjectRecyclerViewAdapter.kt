package org.asuscomm.hsseek.weshallpass.starter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.asuscomm.hsseek.weshallpass.R


import org.asuscomm.hsseek.weshallpass.starter.ExamSubjectsFragment.OnSubjectInteractionListener

import kotlinx.android.synthetic.main.fragment_subject_item.view.*
import org.asuscomm.hsseek.weshallpass.models.Subject

class SubjectRecyclerViewAdapter(
    private val subjects: MutableList<Subject>,
    private val mListener: OnSubjectInteractionListener?
) : RecyclerView.Adapter<SubjectRecyclerViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Subject
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_subject_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val subject = subjects[position]
        holder.subjectTitle.text = subject.title
        holder.subjectDuration.text = subject.duration.toString()

        with(holder.mView) {
            tag = subject
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = subjects.size

    fun replaceSubject(subjects: List<Subject>) {
        this.subjects.clear()
        for (subject in subjects) {
            this.subjects.add(subject)
        }
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val subjectTitle: TextView = mView.text_subject_title
        val subjectDuration: TextView = mView.edit_subject_duration

        override fun toString(): String {
            return super.toString() + " '" + subjectDuration.text + "'"
        }
    }
}
