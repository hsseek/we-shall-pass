package org.asuscomm.hsseek.weshallpass.timer

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_timer_alarm.view.*

import org.asuscomm.hsseek.weshallpass.R

private const val ARG_VIBRATION_ENABLED = "VIBRATION_ENABLED"

class TimerAlarmFragment : Fragment() {
    private var vibEnabled: Boolean = false
    private var listener: OnChangeAlarmConfigListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            vibEnabled = it.getBoolean(ARG_VIBRATION_ENABLED)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_timer_alarm, container, false)

        view.checkbox_alarm_vibration.isChecked = vibEnabled

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnChangeAlarmConfigListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnChangeAlarmConfigListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnChangeAlarmConfigListener {
        fun onChangeVibEnabled(isEnabled: Boolean)
    }

    companion object {
        @JvmStatic
        fun newInstance(vibEnabled: Boolean) =
            TimerAlarmFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_VIBRATION_ENABLED, vibEnabled)
                }
            }
    }
}
