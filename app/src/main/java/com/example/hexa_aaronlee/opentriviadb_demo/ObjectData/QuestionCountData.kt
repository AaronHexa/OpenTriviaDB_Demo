package com.example.hexa_aaronlee.opentriviadb_demo.ObjectData

import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.InnerObject.CategoryQuestionCount
import com.fasterxml.jackson.annotation.JsonProperty


class QuestionCountData {

    @JsonProperty("category_id")
    var categoryId: Int = 0

    @JsonProperty("category_question_count")
    lateinit var categoryQuestionCount: CategoryQuestionCount
}