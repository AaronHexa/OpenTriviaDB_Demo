package com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.InnerObject

import com.fasterxml.jackson.annotation.JsonProperty


class Result {

    @JsonProperty("category")
    var category: String = ""
    @JsonProperty("type")
    var type: String = ""
    @JsonProperty("difficulty")
    var difficulty: String = ""
    @JsonProperty("question")
    var question: String = ""
    @JsonProperty("correct_answer")
    var correctAnswer: String = ""
    @JsonProperty("incorrect_answers")
    lateinit var incorrectAnswers: List<String>

}