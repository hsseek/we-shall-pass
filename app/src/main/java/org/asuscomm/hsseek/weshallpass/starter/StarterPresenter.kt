package org.asuscomm.hsseek.weshallpass.starter

import org.asuscomm.hsseek.weshallpass.models.Exam
import org.asuscomm.hsseek.weshallpass.models.Subject

class StarterPresenter(private val view: View) {
    var exam: Exam? = null
        set (value) {
            field = value?.also {
                view.refreshSubjects(subjects)
                view.refreshTotalDuration(it.duration)
            }

        }

    var subjects: MutableList<Subject> = mutableListOf()
        get() {
            return exam?.subjects ?: mutableListOf()
        }
        private set

    interface View {
        fun refreshSubjects(subjects: MutableList<Subject>)
        fun refreshTotalDuration(totalDuration: Int)
    }
}