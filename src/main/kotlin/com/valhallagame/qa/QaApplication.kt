package com.valhallagame.qa

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class QaApplication

fun main(args: Array<String>) {
    runApplication<QaApplication>(*args) {
        setBannerMode(Banner.Mode.LOG)
    }
}