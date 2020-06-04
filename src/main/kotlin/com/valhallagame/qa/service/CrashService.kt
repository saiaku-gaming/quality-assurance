package com.valhallagame.qa.service

import com.valhallagame.qa.dao.CrashesDao
import org.springframework.core.env.Environment
import org.springframework.stereotype.Service
import java.io.File
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

@Service
class CrashService(private val crashesDao: CrashesDao, environment: Environment) {

    val storage: Path = if(environment.activeProfiles.contains("prod")) {
        Path.of("/app/data")
    } else {
        Files.createTempDirectory("test")
    }

    fun storeFileOnDisk(stream: InputStream, name: String): Path {
        val path = storage.resolve(name)
        Files.copy(stream, path, StandardCopyOption.REPLACE_EXISTING)
        return path
    }

    fun getFile(id: Long): File {
        val crashMetadata = crashesDao.getCrash(id)
        return File(crashMetadata.pathOnDisc)
    }

    fun addCrashReport(filePath: Path, description: String, version: String): Long {
        return crashesDao.insertCrash(filePath.toString(), description, version)
    }

    fun addDiagnostics(id: Long, diagnostics: String) {
        return crashesDao.addDiagnostics(id, diagnostics)
    }
}

