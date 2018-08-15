package com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.InnerObject

import com.fasterxml.jackson.annotation.JsonProperty

class CategoryQuestionCount {

    @JsonProperty("total_question_count")
    var totalCount: Int = 0

    @JsonProperty("total_easy_question_count")
    var easyCount: Int = 0

    @JsonProperty("total_medium_question_count")
    var mediumCount: Int = 0

    @JsonProperty("total_hard_question_count")
    var hardCount: Int = 0
}