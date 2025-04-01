package FirstScreen

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.http.ContentType.Application.Json

abstract class DadosJson {
    companion object{
        var Jsonurl = "https://dummyjson.com/products"
    }
        val local = HttpClient {
            install(ContentNegotion) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }

            install(HttpTimeout) {
                socketTimeoutMillis = 60_000
                requestTimeoutMillis = 60_000
            }
    }

}