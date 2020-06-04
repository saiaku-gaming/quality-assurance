package com.valhallagame.qa.controllers.public

import com.valhallagame.qa.dao.Replay
import com.valhallagame.qa.service.ReplayService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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
}