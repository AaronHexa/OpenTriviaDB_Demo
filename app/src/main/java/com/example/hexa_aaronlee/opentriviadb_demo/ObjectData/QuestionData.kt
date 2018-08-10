package com.example.hexa_aaronlee.opentriviadb_demo.ObjectData

import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.InnerObject.Result
import com.fasterxml.jackson.annotation.JsonProperty

class QuestionData {

    @JsonProperty("response_code")
    var responseCode: Int = 0

    @JsonProperty("results")
    lateinit var results: List<Result>
}