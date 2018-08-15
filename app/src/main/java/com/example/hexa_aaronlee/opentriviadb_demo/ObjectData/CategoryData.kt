package com.example.hexa_aaronlee.opentriviadb_demo.ObjectData

import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.InnerObject.TriviaCategory
import com.fasterxml.jackson.annotation.JsonProperty

class CategoryData {

    @JsonProperty("trivia_categories")
    lateinit var triviaCategories : ArrayList<TriviaCategory>
}