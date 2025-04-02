package FirstScreen

import http.AppHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class ProductController : AppHttpClient() {

    // função assíncrona, porque a requisição HTTP pode demorar
    suspend fun fetchProducts(): List<ProductModel>{
        try {
            val response = httpClient
                .get("https://dummyjson.com/products") // requisição GET para a API de produtososta para um objeto da classe
            var corpo : ProductsResponse = response.body<ProductsResponse>()
            println("CARRO AUTOMATICO: $corpo")
            return arrayListOf()
        }catch (e: Exception){
            println("Exception: $e")
            return arrayListOf()
        }

//        return response.products.map{ // Percorre todos os produtos retornados
//            ProductModel( //apenas com os dados necessários
//                id = it.id,
//                title = it.title,
//                price = it.price,
//                stock = it.stock,
//                category = it.category,
//                thumbnail = it.thumbnail,
//                images = it.images
//            )
//        }
        return arrayListOf()
    }

    fun getCategories(products: List<ProductModel>): // A função recebe uma lista de produtos (products).
            List<String>{ //Retorna uma lista de strings, representando as categorias únicas dos produtos
        return products.map{ it.category}.distinct() //Extrai a categoria de cada produto sem as duplicatas
    }
}

