package org.asuscomm.hsseek.weshallpass.starter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_exam_starter.view.*
import org.asuscomm.hsseek.weshallpass.R
import org.asuscomm.hsseek.weshallpass.timer.TimerActivity

class ExamStarterFragment : Fragment() {
    private var examDuration: String? = null
    private var listener: OnClickStartListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnClickStartListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnClickStartListener")
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

        view.text_starter_examduration.text = examDuration

        return view
    }

    fun replaceDuration(durationString: String) {
        examDuration = durationString
        view?.text_starter_examduration?.text = durationString
    }

    interface OnClickStartListener {
        fun onClickStart()
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = ExamStarterFragment()
    }
}
