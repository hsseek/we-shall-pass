package org.asuscomm.hsseek.weshallpass.starter

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_exam_starter, container, false)

        view.button_starter_start.setOnClickListener {
            val intent = Intent(activity, TimerActivity::class.java)
            startActivity(intent)
        }

        view.text_starter_examduration.text = examDuration

        return view
    }

    fun replaceDuration(durationString: String) {
        examDuration = durationString
        view?.text_starter_examduration?.text = durationString
    }

    companion object {
        @JvmStatic
        fun newInstance() = ExamStarterFragment()
    }
}
