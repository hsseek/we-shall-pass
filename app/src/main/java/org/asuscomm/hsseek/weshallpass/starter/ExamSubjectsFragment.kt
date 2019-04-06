package org.asuscomm.hsseek.weshallpass.starter

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.asuscomm.hsseek.weshallpass.R
import org.asuscomm.hsseek.weshallpass.models.Subject

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ExamSubjectsFragment.OnSubjectInteractionListener] interface.
 */
class ExamSubjectsFragment : Fragment() {

    private var listener: OnSubjectInteractionListener? = null
    private var recyclerViewAdapter: SubjectRecyclerViewAdapter? = null
    var subjects: MutableList<Subject> = mutableListOf()
        set(value) {
            field = value
            recyclerViewAdapter?.run {
                replaceSubject(field)
                notifyDataSetChanged()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            throw RuntimeException(context.toString() + " must implement OnSubjectInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnSubjectInteractionListener {
        // TODO: Update argument type and title
        fun onListFragmentInteraction(item: Subject?)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ExamSubjectsFragment()
    }
}
