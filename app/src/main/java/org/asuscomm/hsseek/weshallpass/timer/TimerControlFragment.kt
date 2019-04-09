package org.asuscomm.hsseek.weshallpass.timer

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import kotlinx.android.synthetic.main.fragment_subjects.*
import kotlinx.android.synthetic.main.fragment_timer_control.view.*

import org.asuscomm.hsseek.weshallpass.R

private const val TAG_LOG = "TimerControlFragment"

class TimerControlFragment : Fragment() {
    private var listener: OnControlInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_timer_control, container, false)

        view.button_control_start.setOnClickListener {
            listener?.onClickStart()
            enableStartButton(false)
        }

        view.button_control_stop.setOnClickListener {
            listener?.onClickStop()
            enableStartButton(true)
        }

        view.button_control_forwards.setOnClickListener {
            listener?.onClickForwards()
            enableStartButton(true)
        }

        with(view.button_control_backwards) {
            setOnClickListener {
                listener?.onClickBackwards()
                enableStartButton(true)
            }
            isClickable = false
        }

        return view
    }

    private fun enableStartButton(enable: Boolean) {
        val button = view?.button_control_start

        button?.isClickable = enable
        if (enable) button?.setImageResource(R.drawable.ic_play_circle) else {
            button?.setImageResource(R.drawable.ic_pause_circle)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnControlInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnStartClickListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun setForwardButtonEnabled(enable: Boolean) {
        view?.button_control_forwards?.let {
            setButtonEnabled(it, enable)
        }
    }

    fun setBackwardButtonEnabled(enable: Boolean) {
        view?.button_control_backwards?.let {
            setButtonEnabled(it, enable)
        }
    }

    private fun setButtonEnabled(button: ImageButton, enable: Boolean) {
        button.isClickable = enable

        if (enable) {
            button.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        } else {
            button.setColorFilter(ContextCompat.getColor(requireContext(), R.color.grey))
        }
    }

    interface OnControlInteractionListener {
        fun onClickStart()
        fun onClickStop()
        fun onClickForwards()
        fun onClickBackwards()
    }

    companion object {
        @JvmStatic
        fun newInstance() = TimerControlFragment()
    }
}
