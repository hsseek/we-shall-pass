package org.asuscomm.hsseek.weshallpass.starter

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_exam_starter.view.*
import org.asuscomm.hsseek.weshallpass.R

const val ARG_EXAM_DURATION = "EXAM_DURATION"

class ExamStarterFragment : Fragment() {
    private var examDuration: Int = 0
    private var listener: OnClickStartListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnClickStartListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnClickStartListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            examDuration = it.getInt(ARG_EXAM_DURATION, 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_exam_starter, container, false)

        view.button_starter_start.setOnClickListener {
            listener?.onClickStart()
        }

        view.text_starter_examduration.text = formatDuration(examDuration)

        return view
    }

    fun replaceDuration(duration: Int) {
        examDuration = duration
        view?.text_starter_examduration?.text = formatDuration(duration)
    }

    private fun formatDuration(duration: Int) = duration.toString() + getString(R.string.all_timeunit)

    interface OnClickStartListener {
        fun onClickStart()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        @JvmStatic
        fun newInstance(examDuration: Int) = ExamStarterFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_EXAM_DURATION, examDuration)
            }
        }
    }
}
