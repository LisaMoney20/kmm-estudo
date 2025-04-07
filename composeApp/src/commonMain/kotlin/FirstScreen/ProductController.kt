package FirstScreen

import http.AppHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
//ProductController: responsável por buscar os dados da API e processar os dados
//Classe ProductController ela recebe como parametro o HttpClient e herda (:) de AppHttpClient todas as funcionabilidades dessa classe
class ProductController(httpClient1: HttpClient) : AppHttpClient() {
    private val httpClient = HttpClient {//uma nova variavel instaciada no HttpClient privada, ou seja, faz com que a variavel só seja acessada dentro da própria classe ProductController
        install(ContentNegotiation) {//plugin de negociação de contaudo, informando que ser trabalhado comJ SON
            json(Json {
                ignoreUnknownKeys = true //faz com que o campo seja ignorado caso não esteja presente no data class
            })
        }
    }
//O suspend fun faz com que a função seja pausada e retorna o ProductModel
    suspend fun fetchProducts(): List<ProductModel> {
        val response: ProductsResponse = httpClient //o HTTP do tipo GET vai da URL e o servidor responde com dados
            .get("https://dummyjson.com/products")//Requisição usada para buscar informaçōes
            .body()
        println("carrro${response.products}")
        return response.products //retorna a lista de produtos dentro do response
    }
//categoria getCategories onde se recebe a lista de produtos e retorna uma lista de categorias unicas
    fun getCategories(products: List<ProductModel>): List<String> {
        return products.map { it.category }.distinct() //.map:cada item da lista de produtos extrai só um campo e category e o resultado vira uma lista de categorias

}
}

