package org.asuscomm.hsseek.weshallpass.starter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_exam_title.view.*

import org.asuscomm.hsseek.weshallpass.R

private const val ARG_EXAM_TITLE = "ARG_EXAM_TITLE"
private const val ARG_SUBJECT_TITLE = "ARG_SUBJECT_TITLE"

class ExamTitleFragment : Fragment() {
    private var examTitle: String? = null
    private var subjectTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            examTitle = it.getString(ARG_EXAM_TITLE)
            subjectTitle = it.getString(ARG_SUBJECT_TITLE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exam_title, container, false)

        // Set the titles
        view.apply {
            text_title_exam.text = examTitle ?: getString(R.string.starter_examtitle_default)
            text_title_subject.text = subjectTitle ?: getString(R.string.starter_subjecttitle_default)
        }

        return view
    }

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
