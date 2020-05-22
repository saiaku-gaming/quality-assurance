package com.valhallagame.qa.service

import com.valhallagame.qa.dao.CrashesDao
import org.springframework.stereotype.Service
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path

@Service
class CrashService(private val crashesDao: CrashesDao) {

    val storage: Path = Files.createTempDirectory("test")

    fun storeFileOnDisk(stream: InputStream, name: String): Path {
        val path = storage.resolve(name)
        Files.copy(stream, path)
        return path
    }

    fun addCrashReport(filePath: Path, description: String, version: String) {
        crashesDao.insertCrash(filePath.toString(), description, version)
    }
}
