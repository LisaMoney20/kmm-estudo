package org.project.first.compose

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform