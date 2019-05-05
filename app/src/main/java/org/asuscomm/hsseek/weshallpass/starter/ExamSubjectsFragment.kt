package org.asuscomm.hsseek.weshallpass.starter

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.asuscomm.hsseek.weshallpass.R
import org.asuscomm.hsseek.weshallpass.models.Subject

private const val TAG = "ExamSubjectsFragment"
const val ARG_SUBJECT_CONFIG = "ARG_SUBJECT_CONFIG"

class ExamSubjectsFragment : Fragment() {

    private var listener: OnSubjectInteractionListener? = null
    private var recyclerViewAdapter: SubjectRecyclerViewAdapter? = null
    var subjects: ArrayList<Subject> = arrayListOf()
        set(value) {
            field = value
            recyclerViewAdapter?.run {
                this.subjects = field
                notifyDataSetChanged()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            subjects = it.getParcelableArrayList(ARG_SUBJECT_CONFIG) ?: arrayListOf()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_subjects, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = SubjectRecyclerViewAdapter(subjects, listener).also {
                    recyclerViewAdapter = it
                }
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSubjectInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnSubjectInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnSubjectInteractionListener {
        fun onCheckedSubjectChange(position: Int, isChecked: Boolean)
        fun onChangeSubjectTitle(position: Int, changedTitle: String)
        fun onChangeSubjectDuration(position: Int, changedDuration: Int)
        fun onDeleteSubject(position: Int)
        fun onAddSubject()
    }

    companion object {
        @JvmStatic
        fun newInstance(subjects: ArrayList<Subject>) = ExamSubjectsFragment().apply {
            arguments = Bundle().apply {
                putParcelableArrayList(ARG_SUBJECT_CONFIG, subjects)
            }
        }
    }
}
