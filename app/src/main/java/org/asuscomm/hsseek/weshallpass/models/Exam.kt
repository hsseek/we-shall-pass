package org.asuscomm.hsseek.weshallpass.models

class Exam (val name: String, val subjects: MutableList<Subject>) {
    var duration: Int = 0
        get() {
            var sum = 0
            for (subject in subjects) {
                 sum += subject.duration
            }
            return sum
        }
        private set
}