package org.asuscomm.hsseek.weshallpass.starter

import android.app.Activity
import android.content.Intent
import org.asuscomm.hsseek.weshallpass.models.Exam
import org.asuscomm.hsseek.weshallpass.models.Subject
import org.asuscomm.hsseek.weshallpass.timer.TimerActivity

class StarterPresenter(private val view: View) {
    var exam: Exam? = null
        set (value) {
            field = value?.also {
                view.refreshSubjects(it.subjects)
                view.refreshTotalDuration(it.duration)
            }

        }

    fun launchTimer() {
        val activity = view as? Activity
        val intent = Intent(activity, TimerActivity::class.java)

        exam?.subjects?.let {
            intent.putParcelableArrayListExtra(EXTRA_SUBJECT_LIST, it)
        }

        activity?.startActivity(intent)
    }

    interface View {
        fun refreshSubjects(subjects: MutableList<Subject>)
        fun refreshTotalDuration(totalDuration: Int)
    }
}