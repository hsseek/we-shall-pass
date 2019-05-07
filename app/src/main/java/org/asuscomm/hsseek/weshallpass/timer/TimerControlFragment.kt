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
private const val ARG_IS_TIME_UP = "ARG_IS_TIME_UP"

class TimerControlFragment : Fragment() {
    private var mListener: OnControlInteractionListener? = null
    private var mBackwardsEnabled = true
    private var mForwardsEnabled = true
    private var mIsCounting = false
    private var mRefreshable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mBackwardsEnabled = it.getBoolean(ARG_BACKWARDS_ENABLED)
            mForwardsEnabled = it.getBoolean(ARG_FORWARDS_ENABLED)
            mIsCounting = it.getBoolean(ARG_IS_COUNTING)
            mRefreshable = it.getBoolean(ARG_IS_TIME_UP)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment.
        val view = inflater.inflate(R.layout.fragment_timer_control, container, false)

        with(view.button_control_start) {
            // If the countdown has been running, replace the icon.
            if (mIsCounting) this.setImageResource(R.drawable.ic_pause_circle)

            // Set the listener to assign actions on clicking the start button.
            setOnClickListener {
                // Let the Context know the start button has been clicked.
                mListener?.onClickStart()

                // Replace the icon to pause icon.
                enableStartButton(false)
            }
        }

        with(view.button_control_stop) {
            // If the time is up, show the refresh icon.
            if (mRefreshable) this.setImageResource(R.drawable.ic_refresh)

            setOnClickListener {
                // Let the Context know the stop button has been clicked.
                mListener?.onClickStop()

                // Enable the start button.
                enableStartButton(true)

                // Consume the refreshable-ness on clicking if it was refreshable
                if (mRefreshable) {
                    setImageResource(R.drawable.ic_stop)
                    mRefreshable = !mRefreshable
                }
            }
        }

        with(view.button_control_forwards) {
            // Enable or disable the button (on the last subject or not)
            this.setButtonEnabled(mForwardsEnabled)

            setOnClickListener {
                mListener?.onClickForwards()
                enableStartButton(true)
            }
        }

        with(view.button_control_backwards) {
            // Enable or disable the button (on the first subject or not)
            this.setButtonEnabled(mBackwardsEnabled)

            setOnClickListener {
                mListener?.onClickBackwards()
                enableStartButton(true)
            }
        }

        return view
    }

    private fun enableStartButton(enable: Boolean) {
        view?.button_control_start?.let {
            it.isClickable = enable
            if (enable) it.setImageResource(R.drawable.ic_play_circle) else {
                it.setImageResource(R.drawable.ic_pause_circle)
            }
        }
    }

    fun enableRefreshButton(enable: Boolean) {
        mRefreshable = enable

        view?.button_control_stop?.let {
            if (enable) it.setImageResource(R.drawable.ic_refresh) else {
                it.setImageResource(R.drawable.ic_stop)
            }
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
        fun newInstance(backwardsEnabled: Boolean, forwardsEnabled: Boolean, isCounting: Boolean, isTimeUp: Boolean) =
            TimerControlFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_BACKWARDS_ENABLED, backwardsEnabled)
                    putBoolean(ARG_FORWARDS_ENABLED, forwardsEnabled)
                    putBoolean(ARG_IS_COUNTING, isCounting)
                    putBoolean(ARG_IS_TIME_UP, isTimeUp)
                }
            }
    }
}
