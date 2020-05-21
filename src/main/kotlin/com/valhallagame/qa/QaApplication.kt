package com.valhallagame.qa

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@SpringBootApplication
class QaApplication {
    @Bean
    fun corsConfigurer(): WebMvcConfigurer? {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/api/*").allowedOrigins("http://localhost:8080")
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<QaApplication>(*args) {
        setBannerMode(Banner.Mode.LOG)
    }
}