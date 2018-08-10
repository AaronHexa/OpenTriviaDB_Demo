package com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.InnerObject.SubjectCategory

import com.fasterxml.jackson.annotation.JsonProperty

class CategoryItem28 {
    @JsonProperty("total_num_of_questions")
    var totalNumOfQuestions: Int = 0

    @JsonProperty("total_num_of_pending_questions")
    var totalNumOfPendingQuestions: Int = 0

    @JsonProperty("total_num_of_verified_questions")
    var totalNumOfVerifiedQuestions: Int = 0

    @JsonProperty("total_num_of_rejected_questions")
    var totalNumOfRejectedQuestions: Int = 0
}