package com.valhallagame.qa.util

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

object JS {
    private val mapper = ObjectMapper()
            .registerModule(Jdk8Module())
            .registerModule(JavaTimeModule())

    fun message(status: HttpStatus, o: Any): ResponseEntity<JsonNode> {
        return ResponseEntity.status(status).body(parse(if(o is String) message(o) else o))
    }

    fun message(message: String) = parse(ObjectWrapperForString(message))

    private fun parse(o: Any): JsonNode = mapper.valueToTree(o)

    data class ObjectWrapperForString(
            private val message: String
    )
}
