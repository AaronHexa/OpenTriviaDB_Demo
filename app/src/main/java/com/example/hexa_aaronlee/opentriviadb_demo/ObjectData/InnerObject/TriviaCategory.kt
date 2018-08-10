package com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.InnerObject

import com.fasterxml.jackson.annotation.JsonProperty

class TriviaCategory {
    @JsonProperty("id")
    var id : Long = 0

    @JsonProperty("name")
    var name : String = ""
}