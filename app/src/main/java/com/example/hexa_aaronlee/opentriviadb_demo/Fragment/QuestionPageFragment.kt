package com.example.hexa_aaronlee.opentriviadb_demo.Fragment

import android.content.Context
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
import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.GetDetailQuestionData
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

        (activity as MainActivity).supportActionBar?.title = "Question"
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        token = mySharePreference.getToken()

        Log.i("token", mySharePreference.getToken())

        mPresenter.setRetrofitAndMapper()

        mPresenter.checkTokenAvailable(token)

        loadQuestionBar.visibility = View.VISIBLE

        anotherBtn.isClickable = false

        setButtonNoClickable()

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

    override fun updateQuestionToUI(dataQuestion : GetDetailQuestionData) {
        loadQuestionBar.visibility = View.INVISIBLE

        answerLayout2.visibility = View.VISIBLE
        answerLayout3.visibility = View.VISIBLE

        questionTitle.text = Html.fromHtml(dataQuestion.questionTxt,Html.FROM_HTML_MODE_LEGACY).toString()
        questionDifficulty.text = dataQuestion.difficultyQuestion

        mCorrectAnswer = dataQuestion.correctAnswer

        when (dataQuestion.difficultyQuestion) {
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

        if (dataQuestion.type == "multiple") {
            answerText1.text = Html.fromHtml(dataQuestion.answerArray[0],Html.FROM_HTML_MODE_LEGACY).toString()
            answerText2.text = Html.fromHtml(dataQuestion.answerArray[1],Html.FROM_HTML_MODE_LEGACY).toString()
            answerText3.text = Html.fromHtml(dataQuestion.answerArray[2],Html.FROM_HTML_MODE_LEGACY).toString()
            answerText4.text = Html.fromHtml(dataQuestion.answerArray[3],Html.FROM_HTML_MODE_LEGACY).toString()
        } else if (dataQuestion.type == "boolean") {
            answerText1.text = dataQuestion.answerArray[0]
            answerText4.text = dataQuestion.answerArray[1]
            answerLayout2.visibility = View.INVISIBLE
            answerLayout3.visibility = View.INVISIBLE
        }

        setButtonOnClick()
    }

    fun setButtonOnClick() {

        answerLayout1.isClickable = true
        answerLayout2.isClickable = true
        answerLayout3.isClickable = true
        answerLayout4.isClickable = true
        anotherBtn.isClickable = true

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

            setButtonNoClickable()
            anotherBtn.isClickable = false
        }
    }

    fun setButtonNoClickable() {
        answerLayout1.isClickable = false

        answerLayout2.isClickable = false

        answerLayout3.isClickable = false

        answerLayout4.isClickable = false

    }

    override fun answerIsCorrect(answerLayout: ConstraintLayout) {
        answerLayout.setBackgroundResource(R.color.colorGreen)

        for (i in arrayLayout.indices){
            val layout = arrayLayout[i]

            layout.setBackgroundResource(R.color.colorWhite)
        }
    }

    override fun answerIsWrong(answerLayout: ConstraintLayout) {
        val context : Context = view?.context ?: throw IllegalAccessException("Context is Null")

        answerLayout.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake))
        answerLayout.setBackgroundResource(R.color.colorRed)

        for (i in arrayLayout.indices){
            val layout = arrayLayout[i]

            layout.setBackgroundResource(R.color.colorWhite)
        }
    }

    override fun onDestroy() {
        mPresenter.onDestroy()
        super.onDestroy()
    }
}
