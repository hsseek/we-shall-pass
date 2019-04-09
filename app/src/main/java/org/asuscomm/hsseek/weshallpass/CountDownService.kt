package org.asuscomm.hsseek.weshallpass

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import org.asuscomm.hsseek.weshallpass.models.Subject
import org.asuscomm.hsseek.weshallpass.timer.EXTRA_EXAM_TITLE_STRING
import org.asuscomm.hsseek.weshallpass.timer.EXTRA_SUBJECTS_ARRAY_LIST
import org.asuscomm.hsseek.weshallpass.timer.EXTRA_TIMEUP_BOOLEAN
import org.asuscomm.hsseek.weshallpass.timer.TimerActivity

private const val TAG_LOG = "CountDownService"

class CountDownService : Service() {
    var mExamTitle: String? = null
    var mSubjects: ArrayList<Subject> = arrayListOf()

    private val binder = CountDownBinder()
    private var mCountDownTimer: SecondCountDownTimer? = null

    private var listener: OnCountDownListener? = null

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    fun registerListener(listener: OnCountDownListener?) {
        this.listener = listener
    }

    fun startCountDown(durationSeconds: Int) {
        mCountDownTimer = object : SecondCountDownTimer(durationSeconds) {
            override fun onFinish() {
                // Launch a TimerActivity if not bound
                val intent = Intent(this@CountDownService, TimerActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    putExtra(EXTRA_TIMEUP_BOOLEAN, true)
                    putExtra(EXTRA_EXAM_TITLE_STRING, mExamTitle)
                    putParcelableArrayListExtra(EXTRA_SUBJECTS_ARRAY_LIST, mSubjects)
                }

                startActivity(intent)

                /* Vibrate*/
            }

            override fun onTick(secondsLeft: Int) {
                listener?.refreshCount(secondsLeft)
                Log.d(TAG_LOG, "$secondsLeft seconds left.")
            }
        }

        mCountDownTimer?.start()
    }

    fun stopCountDown() {
        // TODO: mCountDownTimer has became null after resuming the Activity. Why?
        mCountDownTimer?.cancel()

        listener?.resetCount()
    }

    inner class CountDownBinder : Binder() {
        fun getService(): CountDownService = this@CountDownService
    }

    interface OnCountDownListener {
        fun refreshCount(secondsLeft: Int)
        fun resetCount()
    }
}
