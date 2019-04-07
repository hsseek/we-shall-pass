package org.asuscomm.hsseek.weshallpass.timer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import org.asuscomm.hsseek.weshallpass.R
import org.asuscomm.hsseek.weshallpass.models.Subject
import org.asuscomm.hsseek.weshallpass.starter.EXTRA_SUBJECT_LIST

const val TAG = "TimerActivity"

class TimerActivity : AppCompatActivity() {
    // The Fragments
    private var alarmFragment: TimerAlarmFragment? = null
    private var countFragment: TimerCountFragment? = null
    private var controlFragment: TimerControlFragment? = null

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
        var firstDuration: Int = 0
        for (subject in subjects) {
            if (subject.isIncluded) {
                firstDuration = subject.duration
                break
            }
        }

        val newCountFragment = TimerCountFragment.newInstance(firstDuration).also {
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
    }
}
