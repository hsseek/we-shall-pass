package org.asuscomm.hsseek.weshallpass

import java.util.*

abstract class SecondCountDownTimer(seconds: Int) : Timer() {
    private val milliSecDuration = (seconds * 1000).toLong()

    private var task: TimerTask = createTimerTask(milliSecDuration)
    private var startTime: Long = -1

    fun start() {
        this.scheduleAtFixedRate(task, 0, 1000)
    }

    override fun cancel() {
        task.cancel()
        purge()
    }

    private fun createTimerTask(milliSecDuration: Long): TimerTask = object : TimerTask() {
        override fun run() {
            val milliSecLeft: Long
            if (startTime < 0) {
                startTime = scheduledExecutionTime()
                milliSecLeft = milliSecDuration
            } else {
                milliSecLeft = milliSecDuration - (scheduledExecutionTime() - startTime)

                if (milliSecLeft <= 0) {
                    this.cancel()

                    // Timer finished: Reset startTime and call the abstract method
                    startTime = -1
                    onFinish()
                    return
                }
            }

            onTick((milliSecLeft / 1000).toInt())
        }
    }

    abstract fun onTick(secondsLeft: Int)
    abstract fun onFinish()
}