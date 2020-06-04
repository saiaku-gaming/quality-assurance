package com.valhallagame.qa.controllers

import com.valhallagame.qa.dao.CrashMetadata
import com.valhallagame.qa.dao.CrashesDao
import com.valhallagame.qa.service.CrashService
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.FileInputStream

@RestController
@RequestMapping("/api/crash")
class CrashController(private val crashesDao: CrashesDao, private val crashService: CrashService) {
    @GetMapping("/list")
    fun listCrashes(): List<CrashMetadata> {
        return crashesDao.listsCrashes()
    }

    @GetMapping("/{id}")
    fun crash(@PathVariable("id") id: Long): CrashMetadata {
        return crashesDao.getCrash(id)
    }

    @GetMapping("/{id}/download")
    fun getCrash(@PathVariable("id") id: Long): ResponseEntity<InputStreamResource> {
        val file = crashService.getFile(id)
        val resource = InputStreamResource(FileInputStream(file))
        val headers = HttpHeaders()
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.name)
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource)
    }
}