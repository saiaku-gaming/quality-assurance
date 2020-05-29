package com.valhallagame.qa.controllers.public

import com.valhallagame.qa.client.JenkinsClient
import com.valhallagame.qa.service.CrashService
import org.apache.commons.fileupload.FileItemIterator
import org.apache.commons.fileupload.FileItemStream
import org.apache.commons.fileupload.servlet.ServletFileUpload
import org.apache.commons.fileupload.util.Streams
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.IOException
import java.io.InputStream
import java.nio.file.Path
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping("/public/crash")
class PublicCrashController(private val crashService: CrashService, private val jenkinsClient: JenkinsClient) {

    companion object {
        private val logger = LoggerFactory.getLogger(PublicCrashController::class.java)
    }

    @PostMapping(path = ["/add"], consumes = ["multipart/form-data"])
    fun addCrash(request: HttpServletRequest): Map<String, String> {
        logger.info("add crash called")
        val upload = ServletFileUpload()
        val iterStream: FileItemIterator = upload.getItemIterator(request)
        var pathOnDisk: Path? = null
        var userDescription = ""
        var version = ""
        while (iterStream.hasNext()) {
            val item: FileItemStream = iterStream.next()
            val name: String = item.fieldName
            val stream: InputStream = item.openStream()
            if (!item.isFormField) {
                pathOnDisk = crashService.storeFileOnDisk(stream, item.name)
            } else {
                val formFieldValue: String = Streams.asString(stream)
                if (name == "description") {
                    userDescription = formFieldValue
                }
                if (name == "version") {
                    version = formFieldValue
                }
            }
        }
        if (pathOnDisk == null) {
            throw IOException("Missing file!")
        }
        logger.info("adding crash report $pathOnDisk, $userDescription, $version")
        val id = crashService.addCrashReport(pathOnDisk, userDescription, version)
        jenkinsClient.triggerStacktraceParse(id)
        return mapOf("status" to "ok")
    }
}