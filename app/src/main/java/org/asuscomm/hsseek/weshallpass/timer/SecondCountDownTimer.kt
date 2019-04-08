package org.asuscomm.hsseek.weshallpass.timer

import java.util.*

abstract class SecondCountDownTimer(seconds: Int) : Timer() {
    private val milliSecDuration = (seconds * 1000).toLong()

    private var wasCancelled = false
    private var wasStarted = false
    private var restart = false

    private var task: TimerTask = getTask(milliSecDuration)
    private var startTime: Long = -1

    fun start() {
        wasStarted = true
        this.scheduleAtFixedRate(task, 0, 1000)
    }

    fun restart() {
        if (!wasStarted) {
            start()
        } else if (wasCancelled) {
            wasCancelled = false
            task = getTask(milliSecDuration)
            start()
        } else {
            restart = true
        }
    }


    override fun cancel() {
        wasCancelled = true
        task.cancel()
        purge()
    }

    private fun getTask(milliSecDuration: Long): TimerTask = object : TimerTask() {
        override fun run() {
            val milliSecLeft: Long
            if (startTime < 0 || restart) {
                startTime = scheduledExecutionTime()
                milliSecLeft = milliSecDuration
                restart = false
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