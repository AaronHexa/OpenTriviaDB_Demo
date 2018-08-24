package com.example.hexa_aaronlee.opentriviadb_demo.Fragment

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.example.hexa_aaronlee.opentriviadb_demo.MainActivity
import com.example.hexa_aaronlee.opentriviadb_demo.Presenter.QuestionPagePresenter
import com.example.hexa_aaronlee.opentriviadb_demo.R
import com.example.hexa_aaronlee.opentriviadb_demo.SharedPreference.MySharedPreference
import com.example.hexa_aaronlee.opentriviadb_demo.View.QuestionPageView
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.fragment_question_page.*


class QuestionPageFragment : Fragment(), QuestionPageView.View {

    lateinit var mPresenter: QuestionPagePresenter
    lateinit var mySharePreference: MySharedPreference
    private var token = ""
    private var mCorrectAnswer = ""
    private var selectedAnswer = ""
    lateinit var myRealm: Realm
    lateinit var arrayLayout : ArrayList<ConstraintLayout>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Realm.init(view.context)
        val config = RealmConfiguration.Builder().name("token.realm").build()
        myRealm = Realm.getInstance(config)

        mPresenter = QuestionPagePresenter(this)
        mySharePreference = MySharedPreference(view.context)

        (activity as MainActivity).supportActionBar!!.title = "Question"

        token = mySharePreference.getToken()

        Log.i("token", mySharePreference.getToken())

        mPresenter.setRetrofitAndMapper()

        mPresenter.checkTokenAvailable(token)

        loadQuestionBar.visibility = View.VISIBLE

        anotherBtn.setOnClickListener {
            loadQuestionBar.visibility = View.VISIBLE

            answerLayout1.setBackgroundResource(R.color.colorWhite)
            answerLayout2.setBackgroundResource(R.color.colorWhite)
            answerLayout3.setBackgroundResource(R.color.colorWhite)
            answerLayout4.setBackgroundResource(R.color.colorWhite)

            questionTitle.text = ""
            questionDifficulty.text = null
            answerText1.text = ""
            answerText2.text = ""
            answerText3.text = ""
            answerText4.text = ""

            token = mySharePreference.getToken()

            mPresenter.checkTokenAvailable(token)
        }

        setButtonOnClick(view)

    }

    override fun getRequirementQuestion() {
        val category = mySharePreference.getCategory()
        val difficulty = mySharePreference.getDifficulty()
        val typeQuestion = mySharePreference.getType()
        token = mySharePreference.getToken()



        mPresenter.requestQuestionData(token, category, difficulty, typeQuestion)
    }

    override fun showRetrieveDataError(error: String, functionName: String) {
        Log.e(functionName, error)
    }

    override fun showResetDone() {
        getRequirementQuestion()
    }

    override fun saveNewToken(token: String) {
        mySharePreference.setToken(token)

        mPresenter.saveInRealmDB(myRealm, token)

        getRequirementQuestion()
    }

    override fun updateQuestionToUI(questionTxt: String, difficultyQuestion: String, correctAnswer: String, answerArray: ArrayList<String>, type: String) {
        loadQuestionBar.visibility = View.INVISIBLE

        answerLayout2.visibility = View.VISIBLE
        answerLayout3.visibility = View.VISIBLE

        questionTitle.text = Html.fromHtml(questionTxt,Html.FROM_HTML_MODE_LEGACY).toString()
        questionDifficulty.text = difficultyQuestion

        mCorrectAnswer = correctAnswer

        when (difficultyQuestion) {
            "easy" -> {
                questionDifficulty.setBackgroundResource(R.drawable.easy_box)
            }
            "medium" -> {
                questionDifficulty.setBackgroundResource(R.drawable.medium_box)
            }
            "hard" -> {
                questionDifficulty.setBackgroundResource(R.drawable.hard_box)
            }
        }

        if (type == "multiple") {
            answerText1.text = Html.fromHtml(answerArray[0],Html.FROM_HTML_MODE_LEGACY).toString()
            answerText2.text = Html.fromHtml(answerArray[1],Html.FROM_HTML_MODE_LEGACY).toString()
            answerText3.text = Html.fromHtml(answerArray[2],Html.FROM_HTML_MODE_LEGACY).toString()
            answerText4.text = Html.fromHtml(answerArray[3],Html.FROM_HTML_MODE_LEGACY).toString()
        } else if (type == "boolean") {
            answerText1.text = answerArray[0]
            answerText4.text = answerArray[1]
            answerLayout2.visibility = View.INVISIBLE
            answerLayout3.visibility = View.INVISIBLE
        }


    }

    fun setButtonOnClick(view: View) {
        answerLayout1.setOnClickListener {

            selectedAnswer = answerText1.text.toString()

            arrayLayout = ArrayList()
            arrayLayout.add(answerLayout2)
            arrayLayout.add(answerLayout3)
            arrayLayout.add(answerLayout4)

            mPresenter.checkCorrectAnswer(mCorrectAnswer, selectedAnswer, answerLayout1)
        }

        answerLayout2.setOnClickListener {

            selectedAnswer = answerText2.text.toString()

            arrayLayout = ArrayList()
            arrayLayout.add(answerLayout1)
            arrayLayout.add(answerLayout3)
            arrayLayout.add(answerLayout4)

            mPresenter.checkCorrectAnswer(mCorrectAnswer, selectedAnswer, answerLayout2)
        }

        answerLayout3.setOnClickListener {

            selectedAnswer = answerText3.text.toString()

            arrayLayout = ArrayList()
            arrayLayout.add(answerLayout1)
            arrayLayout.add(answerLayout2)
            arrayLayout.add(answerLayout4)

            mPresenter.checkCorrectAnswer(mCorrectAnswer, selectedAnswer, answerLayout3)
        }

        answerLayout4.setOnClickListener {

            selectedAnswer = answerText4.text.toString()

            arrayLayout = ArrayList()
            arrayLayout.add(answerLayout1)
            arrayLayout.add(answerLayout2)
            arrayLayout.add(answerLayout3)

            mPresenter.checkCorrectAnswer(mCorrectAnswer, selectedAnswer, answerLayout4)
        }
    }

    override fun answerIsCorrect(answerLayout: ConstraintLayout) {
        answerLayout.setBackgroundResource(R.color.colorGreen)

        for (i in arrayLayout.indices){
            val layout = arrayLayout[i]

            layout.setBackgroundResource(R.color.colorWhite)
        }
    }

    override fun answerIsWrong(answerLayout: ConstraintLayout) {
        answerLayout.startAnimation(AnimationUtils.loadAnimation(view!!.context, R.anim.shake))
        answerLayout.setBackgroundResource(R.color.colorRed)

        for (i in arrayLayout.indices){
            val layout = arrayLayout[i]

            layout.setBackgroundResource(R.color.colorWhite)
        }
    }
}
