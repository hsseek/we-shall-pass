package org.asuscomm.hsseek.weshallpass.timer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.WindowManager
import org.asuscomm.hsseek.weshallpass.CountDownService
import org.asuscomm.hsseek.weshallpass.EXTRA_CONTINUE_COUNTDOWN
import org.asuscomm.hsseek.weshallpass.R
import org.asuscomm.hsseek.weshallpass.models.Subject
import org.asuscomm.hsseek.weshallpass.starter.StarterActivity

private const val TAG_LOG = "TimerActivity"
private const val TAG_FRAGMENT_ALARM = "org.asuscomm.hsseek.weshallpass.starter.TAG_FRAGMENT_ALARM"
private const val TAG_FRAGMENT_COUNT = "org.asuscomm.hsseek.weshallpass.starter.TAG_FRAGMENT_COUNT"
private const val TAG_FRAGMENT_CONTROL = "org.asuscomm.hsseek.weshallpass.starter.TAG_FRAGMENT_CONTROL"

const val EXTRA_TIMEUP_BOOLEAN = "org.asuscomm.hsseek.weshallpass.starter.EXTRA_TIMEUP"
const val EXTRA_SUBJECTS_ARRAY_LIST = "org.asuscomm.hsseek.weshallpass.starter.PASS_SUBJECT_LIST"
const val EXTRA_EXAM_TITLE_STRING = "org.asuscomm.hsseek.weshallpass.starter.EXAM_TITLE"
const val EXTRA_TIMER_SECONDS_LEFT = "org.asuscomm.hsseek.weshallpass.starter.TIMER_SECONDS_LEFT"

class TimerActivity : AppCompatActivity(),
    TimerAlarmFragment.OnChangeAlarmConfigListener,
    TimerControlFragment.OnControlInteractionListener,
    CountDownService.OnCountDownListener {
    // The Vibrator for alarm
    private lateinit var mVibrator: Vibrator

    // The Fragments
    private var mAlarmFragment: TimerAlarmFragment? = null
    private var mCountFragment: TimerCountFragment? = null
    private var mControlFragment: TimerControlFragment? = null

    // The valid Subjects
    private val mSubjects: ArrayList<Subject> = arrayListOf()
    // The exam title
    private var mExamTitle: String? = null

    // The countdown amount
    private var mCountDurationSeconds = 0
    // The current subject index for countdown
    private var mCurrentSubjectIndex = 0

    // Variables regarding Service
    private var mService: CountDownService? = null
    private var mBound = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as CountDownService.CountDownBinder
            mService = binder.getService().apply {
                registerListener(this@TimerActivity)
            }
            mBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mBound = false
            mService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO: Restore timer status(alarms, counts and the control buttons) on rotation
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        // Instantiate the mVibrator
        mVibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // Keep screen on
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // Retrieve the extra for exam title
        mExamTitle = intent.getStringExtra(EXTRA_EXAM_TITLE_STRING)

        // Retrieve the extra for subject information
        val validSubjects = intent.getParcelableArrayListExtra<Subject>(EXTRA_SUBJECTS_ARRAY_LIST)
        // Populate the mSubjects list for countdown
        for (subject in validSubjects) mSubjects.add(subject)

        if (intent.getBooleanExtra(EXTRA_CONTINUE_COUNTDOWN, false)) {
            bindService(Intent(this, CountDownService::class.java), connection, Context.BIND_AUTO_CREATE)
            Log.d(TAG_LOG, "Bound to the service")
        }

        // Should the alarm go off?
        val isTimeUp = intent.getBooleanExtra(EXTRA_TIMEUP_BOOLEAN, false)

        // Instantiate the Fragments
        if (savedInstanceState == null) {
            // TODO: Retrieve whether the option has been enabled from SharedPreferences
            mAlarmFragment = TimerAlarmFragment.newInstance(true)

            val firstSubject = mSubjects[0]
            mCountDurationSeconds = firstSubject.duration * 60

            val initialDuration = if (isTimeUp) 0 else mCountDurationSeconds
            mCountFragment = TimerCountFragment.newInstance(mExamTitle?:"", firstSubject.title, initialDuration)

            mControlFragment = TimerControlFragment.newInstance()
        } else {
            mAlarmFragment = supportFragmentManager.findFragmentByTag(TAG_FRAGMENT_ALARM) as? TimerAlarmFragment
            mCountFragment = supportFragmentManager.findFragmentByTag(TAG_FRAGMENT_COUNT) as? TimerCountFragment
            mControlFragment = supportFragmentManager.findFragmentByTag(TAG_FRAGMENT_CONTROL) as? TimerControlFragment
        }

        // Begin Fragment transaction
        val transaction = supportFragmentManager.beginTransaction()

        mAlarmFragment?.let {
            transaction.replace(R.id.frame_timer_alarms, it, TAG_FRAGMENT_ALARM)
        }
        mCountFragment?.let {
            transaction.replace(R.id.frame_timer_counter, it, TAG_FRAGMENT_COUNT)
        }
        mControlFragment?.let {
            transaction.replace(R.id.frame_timer_controls, it, TAG_FRAGMENT_CONTROL)
        }

        transaction.commit()

        if (isTimeUp) goOffAlarm()
    }

    override fun onDestroy() {
        super.onDestroy()

        mService?.registerListener(null)

        // Unbind the Service
        if (mBound) {
            unbindService(connection)
            mBound = false
        }

        // If it was still vibrating, stop it.
        mVibrator.cancel()
    }

    override fun onBackPressed() {
        val intent = Intent(this, StarterActivity::class.java)

        startActivity(intent.apply {
            addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        })
        finish()
    }

    // Interface for CountDownService
    override fun refreshCount(secondsLeft: Int) {
        runOnUiThread {
            mCountFragment?.setCount(secondsLeft)
        }
    }

    override fun resetCount() {
        mCountFragment?.setCount(mCountDurationSeconds)
    }

    override fun onNewIntent(intent: Intent?) {
        Log.d(TAG_LOG, "onNewIntent called: mBound = $mBound")
        if (intent?.extras?.getBoolean(EXTRA_TIMEUP_BOOLEAN) == true) {
            goOffAlarm()
        }
    }

    private fun goOffAlarm() {
        val vibPattern = longArrayOf(0, 1250, 1000)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mVibrator.vibrate(VibrationEffect.createWaveform(vibPattern, 1))
        } else {
            //deprecated in API 26
            mVibrator.vibrate(vibPattern, 1)
        }

        mCountFragment?.setCount(0)
    }

    // Interface for TimerAlarmFragment
    override fun onChangeVibEnabled(isEnabled: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // Interface for TimerControlFragment
    override fun onClickStart() {
        val intent = Intent(this, CountDownService::class.java).apply {
            putExtra(EXTRA_TIMER_SECONDS_LEFT, mCountDurationSeconds)
            putExtra(EXTRA_EXAM_TITLE_STRING, mExamTitle)
            putExtra(EXTRA_SUBJECTS_ARRAY_LIST, mSubjects)
        }

        startService(intent)

        if (!mBound) bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    override fun onClickStop() {
        mVibrator.cancel()

        if (mBound) {
            unbindService(connection)
            mBound = false
        }

        // Stop the countdown and the Service
        stopService()

        val subject = mSubjects[mCurrentSubjectIndex]
        // Reset the timer
        mCountFragment?.updateSubject(subject.title, subject.duration * 60)
    }

    override fun onClickForwards() {
        // TODO: The notification won't dismiss.
        stopService()

        mCurrentSubjectIndex += 1
        val newSubjectDurationSeconds = mSubjects[mCurrentSubjectIndex].duration * 60
        mCountFragment?.updateSubject(mSubjects[mCurrentSubjectIndex].title, newSubjectDurationSeconds)

        // Update the CDT duration as well
        mCountDurationSeconds = newSubjectDurationSeconds

        if (mCurrentSubjectIndex == mSubjects.size - 1) mControlFragment?.setForwardButtonEnabled(false)
        else if (mCurrentSubjectIndex > 0) mControlFragment?.setBackwardButtonEnabled(true)
    }

    override fun onClickBackwards() {
        stopService()

        mCurrentSubjectIndex -= 1
        val newSubjectDurationSeconds = mSubjects[mCurrentSubjectIndex].duration * 60
        mCountFragment?.updateSubject(mSubjects[mCurrentSubjectIndex].title, newSubjectDurationSeconds)

        // Update the CDT duration as well
        mCountDurationSeconds = newSubjectDurationSeconds

        if (mCurrentSubjectIndex == 0) mControlFragment?.setBackwardButtonEnabled(false)
        else if (mCurrentSubjectIndex < mSubjects.size - 1) mControlFragment?.setForwardButtonEnabled(true)
    }

    private fun stopService() {
        // Stop the countdown and the Service
        mService?.let {
            it.stopCountDown()
            it.stopSelf()
        }

        mService = null
    }
}
