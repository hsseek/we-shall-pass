package org.asuscomm.hsseek.weshallpass.starter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.asuscomm.hsseek.weshallpass.R

private const val ARG_EXAM_TITLE = "examTitle"
private const val ARG_SUBJECT_TITLE = "subjectTitle"

class ExamTitleFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_EXAM_TITLE)
            param2 = it.getString(ARG_SUBJECT_TITLE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exam_title, container, false)
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */

    companion object {
        @JvmStatic
        fun newInstance(examTitle: String, subjectTitle: String) =
            ExamTitleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_EXAM_TITLE, examTitle)
                    putString(ARG_SUBJECT_TITLE, subjectTitle)
                }
            }
    }
}
