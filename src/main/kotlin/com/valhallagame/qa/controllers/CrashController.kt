package com.valhallagame.qa.controllers

import com.valhallagame.qa.dao.CrashMetadata
import com.valhallagame.qa.dao.CrashesDao
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/crash")
class CrashController(private val crashesDao: CrashesDao) {
    @GetMapping("/list")
    fun listCrashes(): List<CrashMetadata> {
        return crashesDao.listsCrashes()
    }
}