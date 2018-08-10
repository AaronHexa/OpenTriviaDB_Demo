package com.example.hexa_aaronlee.opentriviadb_demo.ObjectData

import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.InnerObject.Categories
import com.example.hexa_aaronlee.opentriviadb_demo.ObjectData.InnerObject.Overall
import com.fasterxml.jackson.annotation.JsonProperty



class QuestionCountData {

    @JsonProperty("overall")
    lateinit var overall: Overall
    @JsonProperty("categories")
    lateinit var categories: Categories
}