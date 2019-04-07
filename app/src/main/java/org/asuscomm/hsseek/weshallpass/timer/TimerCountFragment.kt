package org.asuscomm.hsseek.weshallpass.timer

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.asuscomm.hsseek.weshallpass.R

private const val ARG_SUBJECT_DURATION = "ARG_SUBJECT_DURATION"

class TimerCountFragment : Fragment() {
    private var subjectDuration: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            subjectDuration = it.getInt(ARG_SUBJECT_DURATION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timer_count, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(subjectDuration: Int) =
            TimerCountFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SUBJECT_DURATION, subjectDuration)
                }
            }
    }
}
