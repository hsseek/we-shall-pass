package org.asuscomm.hsseek.weshallpass.starter

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_exam_starter.view.*

import org.asuscomm.hsseek.weshallpass.R

private const val ARG_EXAM_DURATION = "EXAM_DURATION"

class ExamStarterFragment : Fragment() {
    private var examDuration: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            examDuration = it.getString(ARG_EXAM_DURATION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_exam_starter, container, false)
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(examDuration: String) =
            ExamStarterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_EXAM_DURATION, examDuration)
                }
            }
    }
}
