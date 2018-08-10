package com.example.hexa_aaronlee.opentriviadb_demo.ObjectData

import com.fasterxml.jackson.annotation.JsonProperty

class ResetData {

    @JsonProperty("response_code")
    var response_code : Long = 0

    @JsonProperty("token")
    var token : String = ""
}