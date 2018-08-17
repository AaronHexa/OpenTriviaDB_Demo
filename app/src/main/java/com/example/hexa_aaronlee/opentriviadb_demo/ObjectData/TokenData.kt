package com.example.hexa_aaronlee.opentriviadb_demo.ObjectData

import com.fasterxml.jackson.annotation.JsonProperty

class TokenData {
    @JsonProperty("response_code")
    var response_code: Long = 0

    @JsonProperty("response_message")
    var response_message: String = ""

    @JsonProperty("token")
    var token: String = ""
}