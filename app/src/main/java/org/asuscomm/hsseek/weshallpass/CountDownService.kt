package org.asuscomm.hsseek.weshallpass

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import org.asuscomm.hsseek.weshallpass.models.Subject
import org.asuscomm.hsseek.weshallpass.timer.*

private const val TAG_LOG = "CountDownService"
const val CHANNEL_ID = "org.asuscomm.hsseek.weshallpass.notification"
const val NOTIFICATION_ID = 728
const val EXTRA_CONTINUE_COUNTDOWN = "org.asuscomm.hsseek.weshallpass.EXTRA_CONTINUE_COUNTDOWN"

class CountDownService : Service() {
    var mExamTitle: String? = null
    var mSubjects: ArrayList<Subject> = arrayListOf()

    private val binder = CountDownBinder()
    private var mCountDownTimer: SecondCountDownTimer? = null

    private var listener: OnCountDownListener? = null

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.notif_channel_description)
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Reset the timer
        mCountDownTimer?.cancel()

        intent?.let {startingIntent ->
            startingIntent.getStringExtra(EXTRA_EXAM_TITLE_STRING)?.let { mExamTitle = it }
            startingIntent.getParcelableArrayListExtra<Subject>(EXTRA_SUBJECTS_ARRAY_LIST)?.let { mSubjects = it }

            // Start the countdown
            startCountDown(startingIntent.getIntExtra(EXTRA_TIMER_SECONDS_LEFT, 0))
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG_LOG, "Service destroyed")
        mCountDownTimer?.cancel()
        mCountDownTimer = null
    }

    fun registerListener(listener: OnCountDownListener?) {
        this.listener = listener
    }

    private fun startCountDown(durationSeconds: Int) {
        val pendingIntent: PendingIntent =
            Intent(this, TimerActivity::class.java).putExtras(/* TODO: Change isTimeUp to false after test */true).let { notificationIntent ->
                // Along with the extras, put a marker that the Activity was launched from the PendingIntent
                // (so that it continues the countdown on launching)
                notificationIntent.putExtra(EXTRA_CONTINUE_COUNTDOWN, true)

                PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            }

        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, CHANNEL_ID)
        } else {
            Notification.Builder(this).setPriority(Notification.PRIORITY_LOW)
        }.apply {
            setContentTitle(getString(R.string.notif_title))
            setSmallIcon(R.drawable.ic_timer)
            setContentIntent(pendingIntent)
        }

        startForeground(NOTIFICATION_ID, notification.build())

        mCountDownTimer = object : SecondCountDownTimer(durationSeconds) {
            override fun onFinish() {
                // Launch a TimerActivity if not bound
                val intent = Intent(this@CountDownService, TimerActivity::class.java)

                startActivity(intent.putExtras(true))

                // Update the notification
                with(NotificationManagerCompat.from(this@CountDownService)) {
                    // notificationId is a unique int for each notification that you must define
                    notification.setContentText(getString(R.string.notif_timeup))
                    notify(NOTIFICATION_ID, notification.build())
                }
            }

            override fun onTick(secondsLeft: Int) {
                listener?.refreshCount(secondsLeft)

                // Update the notification
                with(NotificationManagerCompat.from(this@CountDownService)) {
                    // notificationId is a unique int for each notification that you must define
                    notification.setContentText(secondsLeft.toString())
                    notify(NOTIFICATION_ID, notification.build())
                }
            }
        }

        mCountDownTimer?.start()
    }

    fun stopCountDown() {
        mCountDownTimer?.cancel()
        listener?.resetCount()

        // Update the notification
        with(NotificationManagerCompat.from(this@CountDownService)) {
            cancel(NOTIFICATION_ID)
        }
    }

    private fun Intent.putExtras(isTimeUp: Boolean): Intent {
        return this.apply {
            putExtra(EXTRA_TIME_UP_BOOLEAN, isTimeUp)
            putExtra(EXTRA_EXAM_TITLE_STRING, mExamTitle)
            putParcelableArrayListExtra(EXTRA_SUBJECTS_ARRAY_LIST, mSubjects)
        }
    }

    inner class CountDownBinder : Binder() {
        fun getService(): CountDownService = this@CountDownService
    }

    interface OnCountDownListener {
        fun refreshCount(secondsLeft: Int)
        fun resetCount()
    }
}
