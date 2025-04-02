package FirstScreen

import kotlinx.serialization.Serializable

//Permite serializaçåo e desserialização automática
@Serializable
data class ProductModel (
    val id: Int, //id unico
    val title: String, //Nome do produto
    val description: String, //Descrição do produto
    val category: String, // Categoria do produto
    val price: Double, //preço do produto
    val discountPercentage: Double, //Deconto em %
    val rating: Double, // Avaliação do produto
    val stock: Int, //estoque do produto
    val brand: String? = "", // Marca
    val thumbnail: String, // URL da imagem
    val images: List<String>? = arrayListOf<String>()//Lista de URL
)

@Serializable
//Resposta
data class ProductsResponse(
    val products: List<ProductModel>,//lista de produto
    val total: Int, //total de produtos
    val skip: Int, //pulados
    val limit: Int //limite de produtos
)