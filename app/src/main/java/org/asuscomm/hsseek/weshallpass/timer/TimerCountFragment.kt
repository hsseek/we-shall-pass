package org.asuscomm.hsseek.weshallpass.timer

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_timer_count.view.*

import org.asuscomm.hsseek.weshallpass.R

private const val ARG_SUBJECT_DURATION = "ARG_SUBJECT_DURATION"
private const val ARG_SUBJECT_TITLE = "ARG_SUBJECT_TITLE"
private const val TAG = "TimerCountFragment"

class TimerCountFragment : Fragment() {
    private var subjectDurationSeconds = 0
    private var subjectTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            subjectDurationSeconds = it.getInt(ARG_SUBJECT_DURATION)
            subjectTitle = it.getString(ARG_SUBJECT_TITLE) ?: getString(R.string.subject_na)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_timer_count, container, false)

        with(view) {
            text_count_title.text = subjectTitle
            text_count_time.text = formatCountdown(subjectDurationSeconds)
        }

        return view
    }

    fun setCount(countLeftSeconds: Int) {
        val countString = formatCountdown(countLeftSeconds)

        if (countLeftSeconds <= 3) Log.i(TAG, countString)
        view?.text_count_time?.text = countString
    }

    private fun formatCountdown(countSeconds: Int): String {
        val countInMinutes = countSeconds / 60
        val countInSeconds = countSeconds % 60

        return "${addZeroPadding(countInMinutes)}:${addZeroPadding(countInSeconds)}"
    }

    private fun addZeroPadding(int: Int): String {
        return if (int < 10) "0$int" else int.toString()
    }

    fun updateSubject(title: String, durationSeconds: Int) {
        view?.let {
            it.text_count_title?.text = title
            it.text_count_time?.text = formatCountdown(durationSeconds)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(subjectTitle: String, subjectDuration: Int) =
            TimerCountFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SUBJECT_DURATION, subjectDuration)
                    putString(ARG_SUBJECT_TITLE, subjectTitle)
                }
            }
    }
}
