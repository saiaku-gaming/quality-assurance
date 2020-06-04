package com.valhallagame.qa.controllers

import com.fasterxml.jackson.databind.JsonNode
import com.valhallagame.qa.service.ReplayService
import com.valhallagame.qa.util.JS
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/replay")
class ReplayController(private val replayService: ReplayService) {
    @GetMapping("/{mapName}")
    fun getReplays(@PathVariable mapName: String): ResponseEntity<JsonNode> {
        return JS.message(HttpStatus.OK, replayService.getReplaysFromMapName(mapName))
    }
}