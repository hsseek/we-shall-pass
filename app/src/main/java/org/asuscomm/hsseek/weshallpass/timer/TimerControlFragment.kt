package org.asuscomm.hsseek.weshallpass.timer

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_timer_control.view.*

import org.asuscomm.hsseek.weshallpass.R

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
        }

        view.button_control_stop.setOnClickListener {
            listener?.onClickStop()
        }

        // TODO: Implement the other buttons

        return view
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
