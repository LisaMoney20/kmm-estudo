package http

import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

abstract class AppHttpClient {
    companion object{
        val httpClient = HttpClient {//gerencia requisições HTTP e processar produtos/ usado para se comunicar com APIs
            install(ContentNegotiation) {// converte automaticamente respostas da API em objetos Kotlin
                json(Json{
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true //Ignora campos desconhecidos na resposta da API.
                })
            }
        }
    }
}