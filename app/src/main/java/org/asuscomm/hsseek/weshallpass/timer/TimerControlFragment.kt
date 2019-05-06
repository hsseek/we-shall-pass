package org.asuscomm.hsseek.weshallpass.timer

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import kotlinx.android.synthetic.main.fragment_timer_control.view.*

import org.asuscomm.hsseek.weshallpass.R

private const val TAG_LOG = "TimerControlFragment"
private const val ARG_BACKWARDS_ENABLED = "ARG_BACKWARDS_ENABLED"
private const val ARG_FORWARDS_ENABLED = "ARG_FORWARDS_ENABLED"
private const val ARG_IS_COUNTING = "ARG_IS_COUNTING"

class TimerControlFragment : Fragment() {
    private var mListener: OnControlInteractionListener? = null
    private var mBackwardsEnabled = true
    private var mForwardsEnabled = true
    private var mIsCounting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mBackwardsEnabled = it.getBoolean(ARG_BACKWARDS_ENABLED)
            mForwardsEnabled = it.getBoolean(ARG_FORWARDS_ENABLED)
            mIsCounting = it.getBoolean(ARG_IS_COUNTING)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_timer_control, container, false)

        with(view.button_control_start) {
            setOnClickListener {
                mListener?.onClickStart()
                enableStartButton(false)
            }

            if (mIsCounting) this.setImageResource(R.drawable.ic_pause_circle)
        }

        view.button_control_stop.setOnClickListener {
            mListener?.onClickStop()
            enableStartButton(true)
        }

        with(view.button_control_forwards) {
            setOnClickListener {
                mListener?.onClickForwards()
                enableStartButton(true)
            }

            this.setButtonEnabled(mForwardsEnabled)
        }

        with(view.button_control_backwards) {
            setOnClickListener {
                mListener?.onClickBackwards()
                enableStartButton(true)
            }

            this.setButtonEnabled(mBackwardsEnabled)
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
            mListener = context
        } else {
            throw RuntimeException("$context must implement OnStartClickListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    fun setForwardsButtonEnabled(enable: Boolean) {
        view?.button_control_forwards?.setButtonEnabled(enable)
    }

    fun setBackwardsButtonEnabled(enable: Boolean) {
        view?.button_control_backwards?.setButtonEnabled(enable)
    }

    private fun ImageButton.setButtonEnabled(enable: Boolean) {
        this.isClickable = enable

        if (enable) {
            this.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        } else {
            this.setColorFilter(ContextCompat.getColor(requireContext(), R.color.grey))
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
        fun newInstance(backwardsEnabled: Boolean, forwardsEnabled: Boolean, isCounting: Boolean) =
            TimerControlFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_BACKWARDS_ENABLED, backwardsEnabled)
                    putBoolean(ARG_FORWARDS_ENABLED, forwardsEnabled)
                    putBoolean(ARG_IS_COUNTING, isCounting)
                }
            }
    }
}
