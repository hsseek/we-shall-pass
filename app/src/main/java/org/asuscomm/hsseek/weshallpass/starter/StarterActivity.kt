package org.asuscomm.hsseek.weshallpass.starter

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.activity_starter.*
import org.asuscomm.hsseek.weshallpass.R
import org.asuscomm.hsseek.weshallpass.models.Exam
import org.asuscomm.hsseek.weshallpass.models.Subject

const val EXTRA_SUBJECT_LIST = "org.asuscomm.hsseek.weshallpass.starter.PASS_SUBJECT_LIST"
const val EXTRA_EXAM_TITLE = "org.asuscomm.hsseek.weshallpass.starter.EXAM_TITLE"

private const val TAG_FRAGMENT_STARTER = "org.asuscomm.hsseek.weshallpass.starter.TAG_FRAGMENT_STARTER"
private const val TAG_FRAGMENT_SUBJECT = "org.asuscomm.hsseek.weshallpass.starter.TAG_FRAGMENT_SUBJECT"
private const val TAG_LOG = "StarterActivity"

// TODO: Convert the example Exam object to an element of the local DB.
private val lgExam = Exam("LG 적성 검사",
    arrayListOf(Subject("언어 이해", 25),
        Subject("언어 추리", 25),
        Subject("인문 역량", 15),
        Subject("수리력", 35),
        Subject("도형 추리", 20),
        Subject("도식적 추리", 20))
)

class StarterActivity : AppCompatActivity(), StarterPresenter.View,
    ExamSubjectsFragment.OnSubjectInteractionListener,
    ExamStarterFragment.OnClickStartListener {
    private val presenter = StarterPresenter(this, lgExam)
    private var subjectsFragment: ExamSubjectsFragment? = null
    private var starterFragment: ExamStarterFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO: Restore Subjects' configuration on rotation
        super.onCreate(savedInstanceState)

        // Initialize the views
        setContentView(R.layout.activity_starter)
        setSupportActionBar(toolbar)

        presenter.exam = lgExam

        // Instantiate the Fragments and the Presenter
        if (savedInstanceState == null) {
            starterFragment = ExamStarterFragment.newInstance(presenter.exam.duration)
            subjectsFragment = ExamSubjectsFragment.newInstance(presenter.exam.subjects)
        } else {
            starterFragment = supportFragmentManager.findFragmentByTag(TAG_FRAGMENT_STARTER) as? ExamStarterFragment
            subjectsFragment = supportFragmentManager.findFragmentByTag(TAG_FRAGMENT_SUBJECT) as? ExamSubjectsFragment
        }

        val transaction = supportFragmentManager.beginTransaction()

        // Begin Fragment transaction
        subjectsFragment?.let {
            transaction.replace(R.id.frame_starter_subjects, it, TAG_FRAGMENT_SUBJECT)
        }
        starterFragment?.let {
            transaction.replace(R.id.frame_starter_starter, it, TAG_FRAGMENT_STARTER)
        }

        transaction.commit()
    }

    override fun refreshSubjects(subjects: ArrayList<Subject>) {
        subjectsFragment?.subjects = subjects
    }

    override fun refreshExamDuration(examDuration: Int) {
        starterFragment?.replaceDuration(examDuration)
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
