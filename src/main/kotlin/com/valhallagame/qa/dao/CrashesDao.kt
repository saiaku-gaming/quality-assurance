package com.valhallagame.qa.dao

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.jdbi.v3.sqlobject.statement.UseRowMapper
import java.sql.ResultSet
import java.time.LocalDateTime

interface CrashesDao {

    @SqlUpdate("""
        INSERT INTO crashes (path_on_disc, user_description, crash_in_version)
        VALUES (:filePath, :description, :version) ON CONFLICT (path_on_disc) 
        DO UPDATE SET user_description = :description, crash_in_version = :version WHERE crashes.path_on_disc = :filePath 
        """)
    fun insertCrash(@Bind("filePath") filePath: String,
                    @Bind("description") description: String,
                    @Bind("version") version: String)

    @SqlQuery("""
       SELECT * FROM crashes 
    """)
    @UseRowMapper(CrashMetadataRowMapper::class)
    fun listsCrashes(): List<CrashMetadata>
}

class CrashMetadataRowMapper : RowMapper<CrashMetadata> {
    override fun map(rs: ResultSet, ctx: StatementContext?): CrashMetadata {
        return CrashMetadata(
                rs.getInt("id"),
                rs.getString("path_on_disc"),
                rs.getTimestamp("ts").toLocalDateTime(),
                rs.getString("crash_in_version"),
                rs.getInt("fixed_in_version"),
                rs.getString("user_description"),
                rs.getString("fixer_description")
        )
    }
}

data class CrashMetadata(
        val id: Int,
        val pathOnDisc: String,
        val timestamp: LocalDateTime,
        val crashInVersion: String,
        val fixedInVersion: Int,
        val userDescription: String,
        val fixerDescription: String?
)