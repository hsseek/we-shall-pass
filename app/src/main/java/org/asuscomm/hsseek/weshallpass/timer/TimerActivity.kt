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

    // The valid Subjects
    private val subjects: ArrayList<Subject> = arrayListOf()
    // The countdown amount
    private var countDurationSeconds = 0
    // The current subject index for countdown
    private var currentSubjectIndex = 0

    // The Vibrator for alarm
    private var vibrator: Vibrator? = null

    // The CountDownTimer
    private var countDownTimer: SecondCountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        // Retrieve the extra for subject information
        val validSubjects = intent.getParcelableArrayListExtra<Subject>(EXTRA_SUBJECT_LIST)
        // Populate the subjects list for countdown
        for (subject in validSubjects) subjects.add(subject)

        // Instantiate the Fragments and the Presenter
        // TODO: Retrieve whether the option has been enabled from SharedPreferences
        val newAlarmFragment = TimerAlarmFragment.newInstance(true).also {
            alarmFragment = it
        }

        val firstSubject = subjects[0]
        countDurationSeconds = firstSubject.duration * 60

        val newCountFragment
                = TimerCountFragment.newInstance(firstSubject.title, countDurationSeconds).also {
            countFragment = it
        }

        val newControlFragment = TimerControlFragment.newInstance().also {
            controlFragment = it
            // Disable the backwards button at first
            controlFragment?.setBackwardButtonEnabled(false)
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

    private fun createCountDownTimer(durationSeconds: Int): SecondCountDownTimer {
        return object : SecondCountDownTimer(durationSeconds) {
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

            override fun onTick(secondsLeft: Int) {
                runOnUiThread {
                    countFragment?.setCount(secondsLeft)
                }
//                countDurationSeconds = (millisUntilFinished/1000).toInt()
            }
        }
    }

    // Interface for TimerAlarmFragment
    override fun onChangeVibEnabled(isEnabled: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // Interface for TimerControlFragment
    override fun onClickStart() {
        countDownTimer = createCountDownTimer(countDurationSeconds)
        countDownTimer?.start()
    }

    override fun onClickStop() {
        vibrator?.cancel()
        countDownTimer?.cancel()

        val subject = subjects[currentSubjectIndex]
        countFragment?.updateSubject(subject.title, subject.duration * 60)
    }

    override fun onClickForwards() {
        countDownTimer?.cancel()

        currentSubjectIndex += 1
        val newSubjectDurationSeconds = subjects[currentSubjectIndex].duration * 60
        countFragment?.updateSubject(subjects[currentSubjectIndex].title, newSubjectDurationSeconds)

        // Update the CDT duration as well
        countDurationSeconds = newSubjectDurationSeconds

        if (currentSubjectIndex == subjects.size -1) controlFragment?.setForwardButtonEnabled(false)
        else if (currentSubjectIndex > 0) controlFragment?.setBackwardButtonEnabled(true)
    }

    override fun onClickBackwards() {
        countDownTimer?.cancel()

        currentSubjectIndex -= 1
        val newSubjectDurationSeconds = subjects[currentSubjectIndex].duration * 60
        countFragment?.updateSubject(subjects[currentSubjectIndex].title, newSubjectDurationSeconds)

        // Update the CDT duration as well
        countDurationSeconds = newSubjectDurationSeconds

        if (currentSubjectIndex == 0) controlFragment?.setBackwardButtonEnabled(false)
        else if (currentSubjectIndex < subjects.size -1) controlFragment?.setForwardButtonEnabled(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        vibrator = null
    }
}
