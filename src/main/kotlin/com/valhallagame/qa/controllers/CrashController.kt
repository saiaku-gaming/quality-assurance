package com.valhallagame.qa.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/crash")
class CrashController {
    @GetMapping("/list")
    fun listCrashes(): Map<String, Any> {
        return mapOf("hello" to "WORLD!")
    }
}