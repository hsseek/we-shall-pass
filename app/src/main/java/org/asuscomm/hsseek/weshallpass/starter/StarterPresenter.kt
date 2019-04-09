package org.asuscomm.hsseek.weshallpass.starter

import android.app.Activity
import android.content.Intent
import org.asuscomm.hsseek.weshallpass.models.Exam
import org.asuscomm.hsseek.weshallpass.models.Subject
import org.asuscomm.hsseek.weshallpass.timer.EXTRA_EXAM_TITLE_STRING
import org.asuscomm.hsseek.weshallpass.timer.EXTRA_SUBJECTS_ARRAY_LIST
import org.asuscomm.hsseek.weshallpass.timer.TimerActivity

class StarterPresenter(private val view: View, exam: Exam) {
    var exam = exam
        set (value) {
            field = value.also {
                view.refreshSubjects(it.subjects)
                view.refreshExamDuration(it.duration)
            }
        }


    fun launchTimer() {
        val activity = view as? Activity
        val intent = Intent(activity, TimerActivity::class.java).apply {
            putExtra(EXTRA_EXAM_TITLE_STRING, exam.title)
        }

        exam.subjects.let {
            val validSubject: ArrayList<Subject> = arrayListOf()

            for (subject in it) {
                if (subject.isIncluded) validSubject.add(subject)
            }

            intent.putParcelableArrayListExtra(EXTRA_SUBJECTS_ARRAY_LIST, validSubject)
        }

        activity?.startActivity(intent)
    }

    fun includeSubject(position: Int, isChecked: Boolean) {
        exam.subjects[position].isIncluded = isChecked

        calculateDuration()
    }

    private fun calculateDuration() {
        var examDuration = 0

        exam.subjects.let {
            for (subject in it) {
                if (subject.isIncluded) examDuration += subject.duration
            }
        }

        view.refreshExamDuration(examDuration)
    }

    interface View {
        fun refreshSubjects(subjects: ArrayList<Subject>)
        fun refreshExamDuration(examDuration: Int)
    }
}