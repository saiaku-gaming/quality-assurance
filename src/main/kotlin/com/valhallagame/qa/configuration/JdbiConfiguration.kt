package com.valhallagame.qa.configuration

import com.valhallagame.qa.dao.CrashesDao
import com.valhallagame.qa.dao.ReplayDao
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.postgres.PostgresPlugin
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin
import org.jdbi.v3.sqlobject.kotlin.onDemand
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.sql.DataSource


@Configuration
class JdbiConfiguration {
    @Bean
    fun jdbi(datasource: DataSource): Jdbi {
        return Jdbi.create(datasource)
                .installPlugin(KotlinPlugin())
                .installPlugin(KotlinSqlObjectPlugin())
                .installPlugin(PostgresPlugin())
    }

    @Bean
    fun crashesDao(jdbi: Jdbi): CrashesDao? {
        return jdbi.onDemand<CrashesDao>()
    }

    @Bean
    fun replayDao(jdbi: Jdbi): ReplayDao? {
        return jdbi.onDemand<ReplayDao>()
    }
}