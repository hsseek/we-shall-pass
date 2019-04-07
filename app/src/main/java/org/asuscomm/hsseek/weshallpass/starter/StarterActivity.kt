package org.asuscomm.hsseek.weshallpass.starter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_starter.*
import org.asuscomm.hsseek.weshallpass.R
import org.asuscomm.hsseek.weshallpass.models.Exam
import org.asuscomm.hsseek.weshallpass.models.Subject

const val EXTRA_SUBJECT_LIST = "PASS_SUBJECT_LIST"
private const val TAG = "StarterActivity"

class StarterActivity : AppCompatActivity(), StarterPresenter.View,
    ExamSubjectsFragment.OnSubjectInteractionListener,
    ExamStarterFragment.OnClickStartListener {
    private val presenter = StarterPresenter(this)
    private var subjectsFragment: ExamSubjectsFragment? = null
    private var starterFragment: ExamStarterFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize the views
        setContentView(R.layout.activity_starter)
        setSupportActionBar(toolbar)

        // TODO: Convert the example Exam object to an element of the local DB.
        val lgSubject1 = Subject(getString(R.string.db_lg_1), 25)
        val lgSubject2 = Subject(getString(R.string.db_lg_2), 25)
        val lgSubject3 = Subject(getString(R.string.db_lg_3), 15)
        val lgSubject4 = Subject(getString(R.string.db_lg_4), 35)
        val lgSubject5 = Subject(getString(R.string.db_lg_5), 20)
        val lgSubject6 = Subject(getString(R.string.db_lg_6), 20)

        val lgExam = Exam(getString(R.string.db_lg_title),
            arrayListOf(lgSubject1, lgSubject2, lgSubject3, lgSubject4, lgSubject5, lgSubject6)
        )

        // Instantiate the Fragments and the Presenter
        val newStarterFragment = ExamStarterFragment.newInstance().also {
            starterFragment = it
        }
        val newSubjectsFragment = ExamSubjectsFragment.newInstance().also {
            subjectsFragment = it
        }
        presenter.exam = lgExam

        // Begin Fragment transaction
        supportFragmentManager.beginTransaction()
            .add(R.id.frame_starter_top, newSubjectsFragment)
            .add(R.id.frame_starter_bottom, newStarterFragment)
            .commit()
    }

    override fun refreshSubjects(subjects: MutableList<Subject>) {
        subjectsFragment?.subjects = subjects
    }

    override fun refreshExamDuration(examDuration: Int) {
        val durationString = examDuration.toString() + getString(R.string.all_timeunit)
        starterFragment?.replaceDuration(durationString)
    }

    // Interaction with ExamSubjectsFragment
    override fun onCheckedSubjectChange(position: Int, isChecked: Boolean) {
        presenter.includeSubject(position, isChecked)
    }

    override fun onChangeSubjectTitle(position: Int, changedTitle: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onChangeSubjectDuration(position: Int, changedDuration: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDeleteSubject(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAddSubject() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // Interaction with ExamStarterFragment
    override fun onClickStart() {
        presenter.launchTimer()
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
