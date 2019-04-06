package org.asuscomm.hsseek.weshallpass.starter

import org.asuscomm.hsseek.weshallpass.models.Exam
import org.asuscomm.hsseek.weshallpass.models.Subject

class StarterPresenter(private val view: View) {
    var exam: Exam? = null
        private set

    var subjects: MutableList<Subject> = mutableListOf()
        get() {
            return exam?.subjects ?: mutableListOf()
        }
        private set

    fun registerExam(exam: Exam) {
        this.exam = exam

        with(view) {
            refreshSubjects(subjects)
            refreshTotalDuration(exam.duration)
        }
    }

    interface View {
        fun refreshSubjects(subjects: MutableList<Subject>)
        fun refreshTotalDuration(totalDuration: Int)
    }
}