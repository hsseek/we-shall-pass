package org.asuscomm.hsseek.weshallpass.timer

import android.content.Context
import android.os.*
import android.support.v7.app.AppCompatActivity
import org.asuscomm.hsseek.weshallpass.R
import org.asuscomm.hsseek.weshallpass.models.Subject
import org.asuscomm.hsseek.weshallpass.starter.EXTRA_SUBJECT_LIST

private const val TAG = "TimerActivity"

class TimerActivity : AppCompatActivity(),
    TimerAlarmFragment.OnChangeAlarmConfigListener,
    TimerControlFragment.OnControlInteractionListener {
    // The Fragments
    private var alarmFragment: TimerAlarmFragment? = null
    private var countFragment: TimerCountFragment? = null
    private var controlFragment: TimerControlFragment? = null

    // The countdown amount
    private var countDuration: Int = 0

    // The Vibrator for alarm
    private var vibrator: Vibrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        // Retrieve the extra for subject information
        val subjects = intent.getParcelableArrayListExtra<Subject>(EXTRA_SUBJECT_LIST)

        // Instantiate the Fragments and the Presenter
        // TODO: Retrieve whether the option has been enabled from SharedPreferences
        val newAlarmFragment = TimerAlarmFragment.newInstance(true).also {
            alarmFragment = it
        }

        // The first duration that will be displayed on the TimerCountFragment
        for (subject in subjects) {
            if (subject.isIncluded) {
                countDuration = subject.duration * 60 /* In seconds */
                break
            }
        }

        val newCountFragment = TimerCountFragment.newInstance(countDuration).also {
            countFragment = it
        }

        val newControlFragment = TimerControlFragment.newInstance().also {
            controlFragment = it
        }

        // Begin Fragment transaction
        supportFragmentManager.beginTransaction()
            .add(R.id.frame_timer_top, newAlarmFragment)
            .add(R.id.frame_timer_middle, newCountFragment)
            .add(R.id.frame_timer_bottom, newControlFragment)
            .commit()

        // Instantiate the vibrator
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    // Interface for TimerAlarmFragment
    override fun onChangeVibEnabled(isEnabled: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // Interface for TimerControlFragment
    override fun onClickStart() {
        object : CountDownTimer(countDuration.toLong() * 1000, 1000) {
            override fun onFinish() {
                // Vibrate
                val vibPattern = longArrayOf(0, 1250, 1000)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator?.vibrate(VibrationEffect.createWaveform(vibPattern, 1))
                } else {
                    //deprecated in API 26
                    vibrator?.vibrate(vibPattern, 1)
                }
            }

            override fun onTick(millisUntilFinished: Long) {
                val countLeftSeconds = (millisUntilFinished/1000).toInt()
                countFragment?.setCount(countLeftSeconds)
//                countDuration = (millisUntilFinished/1000).toInt()
            }
        }.start()
    }

    override fun onClickStop() {
        vibrator?.cancel()
        // TODO: Reset the timer
    }

    override fun onClickForwards() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onClickBackwards() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        super.onDestroy()
        vibrator = null
    }
}
