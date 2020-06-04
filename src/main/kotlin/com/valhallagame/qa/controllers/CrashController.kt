package com.valhallagame.qa.controllers

import com.valhallagame.qa.dao.CrashMetadata
import com.valhallagame.qa.dao.CrashesDao
import com.valhallagame.qa.service.CrashService
import org.apache.commons.fileupload.FileItemIterator
import org.apache.commons.fileupload.FileItemStream
import org.apache.commons.fileupload.servlet.ServletFileUpload
import org.apache.commons.fileupload.util.Streams
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.FileInputStream
import java.io.InputStream
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/crash")
class CrashController(private val crashesDao: CrashesDao, private val crashService: CrashService) {

    var logger: Logger = LoggerFactory.getLogger(CrashController::class.java)

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

    @PostMapping(path = ["/{id}/diagnostics"], consumes = ["multipart/form-data"])
    fun addDiagnostics(request: HttpServletRequest, @PathVariable("id") id: Long): Map<String, String> {
        logger.info("add diagnostics called")
        val upload = ServletFileUpload()
        val iterStream: FileItemIterator = upload.getItemIterator(request)
        var diagnostics = ""

        while (iterStream.hasNext()) {
            val item: FileItemStream = iterStream.next()
            val stream: InputStream = item.openStream()
            diagnostics = Streams.asString(stream)
        }
        if (diagnostics.isEmpty()) {
            return mapOf("status" to "could not parse diagnostics")
        }
        crashService.addDiagnostics(id, diagnostics)
        return mapOf("status" to "ok")
    }

}