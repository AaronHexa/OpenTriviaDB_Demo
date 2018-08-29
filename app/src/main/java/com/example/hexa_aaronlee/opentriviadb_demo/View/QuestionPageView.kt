package com.example.hexa_aaronlee.opentriviadb_demo.View

import android.support.constraint.ConstraintLayout
import android.support.design.widget.TabLayout
import android.view.View
import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.GetDetailQuestionData
import io.realm.Realm

interface QuestionPageView {
    interface View {

        fun getRequirementQuestion()

        fun showRetrieveDataError(error: String,
                                  functionName: String)

        fun showResetDone()

        fun saveNewToken(token: String)

        fun updateQuestionToUI(dataQuestion : GetDetailQuestionData)

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

        fun onDestroy()
    }
}