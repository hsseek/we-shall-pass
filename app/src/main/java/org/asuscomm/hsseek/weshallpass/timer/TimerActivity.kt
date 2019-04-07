package org.asuscomm.hsseek.weshallpass.timer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import org.asuscomm.hsseek.weshallpass.R
import org.asuscomm.hsseek.weshallpass.models.Subject
import org.asuscomm.hsseek.weshallpass.starter.EXTRA_SUBJECT_LIST

const val TAG = "TimerActivity"

class TimerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        // Retrieve the extra for subject information
        val subjects = intent.getParcelableArrayListExtra<Subject>(EXTRA_SUBJECT_LIST)
        for (subject in subjects) {
            Log.i(TAG, "${subject.title}/${subject.duration}")
        }
    }
}
