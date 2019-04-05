package org.asuscomm.hsseek.weshallpass.starter

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_starter.*
import kotlinx.android.synthetic.main.fragment_exam_starter.*
import kotlinx.android.synthetic.main.fragment_exam_title.*
import org.asuscomm.hsseek.weshallpass.R
import org.asuscomm.hsseek.weshallpass.models.Exam
import org.asuscomm.hsseek.weshallpass.models.Subject
import org.asuscomm.hsseek.weshallpass.timer.TimerActivity

class StarterActivity : AppCompatActivity(), StarterPresenter.View {
    private val presenter = StarterPresenter(this)
    private var examTitleFragment: ExamTitleFragment? = null
    private var subjectFragment: ExamSubjectFragment? = null
    private var startFragment: ExamStarterFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the views
        setContentView(R.layout.activity_starter)
        setSupportActionBar(toolbar)

        // and the FAB
        fab.setOnClickListener {
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
        }

        // TODO: Convert the example Exam object to an element of the local DB.
        val lgSubject1 = Subject(getString(R.string.db_lg_1), 25)
        val lgSubject2 = Subject(getString(R.string.db_lg_2), 25)
        val lgSubject3 = Subject(getString(R.string.db_lg_3), 15)
        val lgSubject4 = Subject(getString(R.string.db_lg_4), 35)
        val lgSubject5 = Subject(getString(R.string.db_lg_5), 20)
        val lgSubject6 = Subject(getString(R.string.db_lg_6), 20)

        val lgExam = Exam(getString(R.string.db_lg_title),
            listOf(lgSubject1, lgSubject2, lgSubject3, lgSubject4, lgSubject5, lgSubject6))

        presenter.registerExam(lgExam)
    }

    override fun refreshExamTitle(name: String) {
        text_title_exam.text = name
    }

    override fun refreshSubjects(subjects: List<Subject>) {
        // TODO: Populate the RecyclerView with the List
    }

    override fun refreshTotalDuration(totalDuration: Int) {
        val durationString = totalDuration.toString() + getString(R.string.all_timeunit)
        text_starter_totalduration.text = durationString
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
