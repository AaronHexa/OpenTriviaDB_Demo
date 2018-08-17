package com.example.hexa_aaronlee.opentriviadb_demo.View

import android.support.constraint.ConstraintLayout
import android.support.design.widget.TabLayout
import io.realm.Realm

interface QuestionPageView {
    interface View {

        fun getRequirementQuestion()

        fun showRetrieveDataError(error: String,
                                  functionName: String)

        fun showResetDone()

        fun saveNewToken(token: String)

        fun updateQuestionToUI(questionTxt: String,
                               difficultyQuestion: String,
                               correctAnswer: String,
                               answerArray: ArrayList<String>,
                               type: String)

        fun answerIsCorrect(answerLayout: ConstraintLayout)

        fun answerIsWrong(answerLayout: ConstraintLayout)

    }

    interface Presenter {
        fun checkTokenAvailable(token: String)

        fun setRetrofitAndMapper()

        fun requestQuestionData(token: String,
                                category: String,
                                difficulty: String,
                                typeQuestion: String)

        fun checkCorrectAnswer(correctAnswer: String,
                               selectedAnswer: String,
                               answerLayout: ConstraintLayout)

        fun saveInRealmDB(myRealm: Realm, token: String)
    }
}