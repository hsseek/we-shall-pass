package org.asuscomm.hsseek.weshallpass.starter

import org.asuscomm.hsseek.weshallpass.models.Exam
import org.asuscomm.hsseek.weshallpass.models.Subject

class StarterPresenter(private val view: View) {
    var exam: Exam? = null

    fun registerExam(exam: Exam) {
        this.exam = exam
        var totalDuration = 0

        for (subject in exam.subjects) {
            totalDuration += subject.duration
        }

        view.apply {
            refreshSubjects(exam.subjects)
            refreshTotalDuration(totalDuration)
        }

    }

    interface View {
        fun refreshSubjects(subjects: List<Subject>)
        fun refreshTotalDuration(totalDuration: Int)
    }
}