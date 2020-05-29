package com.valhallagame.qa.controllers.public

import com.fasterxml.jackson.databind.JsonNode
import com.valhallagame.qa.dao.Replay
import com.valhallagame.qa.service.ReplayService
import com.valhallagame.qa.util.JS
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/public/replay")
class PublicReplayController(private val replayService: ReplayService) {
    companion object {
        private val logger = LoggerFactory.getLogger(PublicReplayController::class.java)
    }

    @PostMapping(consumes = ["application/json"])
    fun addReplay(@RequestBody replay: Replay): Map<String, String> {
        replayService.addReplay(replay)
        return mapOf("status" to "ok")
    }

    @GetMapping(path = ["/{replayId}"])
    fun getReplay(@PathVariable("replayId") replayId: Long): ResponseEntity<JsonNode> {
        return JS.message(HttpStatus.OK, replayService.getReplay(replayId))
    }
}