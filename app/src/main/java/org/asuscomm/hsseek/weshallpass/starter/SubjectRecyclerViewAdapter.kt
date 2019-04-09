package org.asuscomm.hsseek.weshallpass.starter

import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
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
    private val listener: OnSubjectInteractionListener?
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
                listener?.onAddSubject()
            }
        } else if (holder is SubjectViewHolder) { /* Normal subject item */
            val subject = subjects[position]

            // The CheckBox
            holder.subjectInclude.setOnCheckedChangeListener { _, isChecked ->
                listener?.onCheckedSubjectChange(position, isChecked)
            }

            // The Subject title (EditText)
            with(holder.subjectTitle) {
                this.setText(subject.title, TextView.BufferType.EDITABLE)
                addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        listener?.onChangeSubjectTitle(position, s.toString())
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    }
                })
            }

            // The duration (EditText)
            with(holder.subjectDuration) {
                this.setText(subject.duration.toString(), TextView.BufferType.EDITABLE)
                addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        listener?.onChangeSubjectDuration(position, s.toString().toInt())
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    }
                })
            }

            // The delete button
            holder.subjectDelete.setOnClickListener {
                listener?.onDeleteSubject(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return subjects.size + 1
    }

    inner class SubjectViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val subjectInclude: CheckBox = mView.checkbox_subject_include
        val subjectTitle: EditText = mView.text_subject_title
        val subjectDuration: EditText = mView.edit_subject_duration
        val subjectDelete: ImageView = mView.button_subject_remove
    }

    inner class FooterViewHolder(mView: View): RecyclerView.ViewHolder(mView)
}
