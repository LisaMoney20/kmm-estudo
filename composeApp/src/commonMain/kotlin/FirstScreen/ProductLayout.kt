@file:OptIn(ExperimentalMaterialApi::class)

package FirstScreen


import FirstScreen.ProductModel
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ChipDefaults.filterChipColors
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FilterChip
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import http.AppHttpClient
import kotlinx.coroutines.launch
import kotlinx.serialization.descriptors.PrimitiveKind


//Tela principal -> interface com Jetpack Compose
@Composable
fun TesteApp(){
    val httpClient = remember { AppHttpClient.httpClient }//variavel que obtem o httClient de AppHttpClient e guarda ele para ser usado
    var selectedCategory by remember { mutableStateOf("") }//Variavel onde o estado da categoria selecionada pelo usuario começa vazio.
    var produtoPesquisado = remember { mutableStateOf("") }//
    var products by remember { mutableStateOf<List<ProductModel>>(arrayListOf()) }// variável onde estado da lista de produtos que será mostrada na tela
    var categories by remember { mutableStateOf(emptyList<String>()) } // lista de produtos que será mostrada na tela
    val productController = remember { ProductController(httpClient) } ////variável onde cria o controlador responsável por buscar os produtos.
    val coroutineScope = rememberCoroutineScope() // variável onde cria um escopo de corrotina, ou seja, onde você pode rodar código assíncrono


    //carrega os dados ao iniciar
    LaunchedEffect (Unit){//  Executa um bloco de código assíncrono quando o composable é iniciado
        coroutineScope.launch { //Executa código assíncrono sem travar a interface
            products = productController.fetchProducts() //A UI será automaticamente atualizada, pois products é um mutableStateOf
            categories = productController.getCategories(products)
            selectedCategory = categories.firstOrNull()?:"" // Retorna o primeiro item da lista, ou null se a lista estiver vazia.
        }
    }

//Define o tema do Material Design
    MaterialTheme {
        //Estrutura principal que inclui a barra superior e o conteúdo da tela
        Scaffold(
            backgroundColor = CORES.COLOR.DARKGREEN,
            topBar = {
                TopAppBar(
                    title = { Text("Produtos APP")},
                    backgroundColor = CORES.COLOR.YELLOW,
                )
            }

        ) { paddingValues ->
            //Permite rolar a tela verticalmente.
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                   .verticalScroll(rememberScrollState())

            ) {
                GreetingSection(name = "Money")
                SearchBar(products, produtoPesquisado) //barra de pesquisa
                if (categories.isNotEmpty()){
                    CategorySection( //Categoria atualmente selecionada
                        categories = categories,
                        selectedCategory = selectedCategory,
                        onCategorySelected = { selectedCategory = it } //Atualiza o estado da categoria ao clicar em uma opção
                    )
                    RecommendedSection(products, selectedCategory, produtoPesquisado)
                }
//                    SEÇÃO DE RECOMENDADOS

                if (selectedCategory.isEmpty()){

//                    products
//                                    println("oi${products.first()}")
                } else {
//                    products.filter { it.category == selectedCategory }
                }
            }
        }
    }
}



@Composable
fun GreetingSection(name: String) {
    var expanded by remember { mutableStateOf(false) } // Controla se o menu está aberto
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(12.dp)
    ) {
        // Ícone do menu
        IconButton(
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription  = "Menu suspenso",
                tint = CORES.COLOR.GREENLIGHT
            )
        }
        // Menu suspenso
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {

            DropdownMenuItem(onClick = {
                expanded = false
                // Ação para item 1
            }) {
                Text("Configurações")
            }
            DropdownMenuItem(onClick = {
                expanded = false
                // Ação para item
            }) {
                Text("Sair")
            }
        }
        //Texto alinhado ao centro
        Text(
            text = "Olá, $name",
            color = CORES.COLOR.GREENLIGHT,
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(Modifier.weight(1f)) // Empurra o ícone para a direita
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Perfil do usuário",
                tint = CORES.COLOR.GREENLIGHT
            )
        }
    }
}

//Barra de pesquisa
@Composable
fun SearchBar(products: List<ProductModel>, produtoPesquisado : MutableState<String>) {
    //Campo de entrada com borda
    OutlinedTextField(
        value = produtoPesquisado.value,
        onValueChange = {
            produtoPesquisado.value = it
        },
        placeholder =  {Text("Pesquisar", color = CORES.COLOR.GREENLIGHT )}, // Cor do placeholder  //textColor = Color.White
        singleLine = true, //uma unica linha
        modifier = Modifier
            .fillMaxWidth() //faz a função que pode ser composta preencher a largura máxima atribuída a ela pelo elemento pai
            .padding(horizontal = 10.dp),
        shape = RoundedCornerShape(50.dp), //define bordas arredondadas
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.Gray,
            textColor = Color.White, // Cor do texto digitado
            cursorColor = Color.Black, // Cor do cursor
            focusedLabelColor = Color.White, // Cor do label quando focado
            unfocusedLabelColor = Color.Gray // Cor do label quando não focado
        ),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "Pesquisar",
                tint = Color.Green// Cor do ícone
            )} //icone de lupa ao lado direito
    )

}

////Seção de categorias
@Composable
//exibe titulo "categoria"
fun CategorySection(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)) {
        Text(
            "Categorias",
            style = MaterialTheme.typography.h6,
            color = CORES.COLOR.GREENLIGHT,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        // Layout horizontal com scroll para muitas categorias
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            categories.forEach { category ->
                FilterChip(
                    selected = (category == selectedCategory),
                    onClick = { onCategorySelected(category) },
                    colors = filterChipColors(
                        backgroundColor = CORES.COLOR.YELLOWLI,
                        selectedBackgroundColor = CORES.COLOR.YELLOW,
                        selectedContentColor = Color.Black
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(
                        text = category.replaceFirstChar { it.titlecase() },
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.caption
                    )
                }
            }
        }
    }
}

// Seção de Frutas Recomendadas
@Composable
fun RecommendedSection(product: List<ProductModel>, selectedCategory : String, produtoPesquisado : MutableState<String>) {
    var produtosFiltrados : List<ProductModel>
    if(produtoPesquisado.value.isNotEmpty()){
        produtosFiltrados = product.filter {
            it.title.uppercase().contains(produtoPesquisado.value.uppercase())
        }
    }else{
        produtosFiltrados = product.filter {
            it.category == selectedCategory
        }
    }

    Column(modifier = Modifier
        .padding(horizontal = 16.dp, vertical = 20.dp)
        .fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Recomendados para você",
                style = MaterialTheme.typography.h6,
                color = CORES.COLOR.GREEN
            )

            Spacer(Modifier.weight(1f))
            TextButton(onClick = {}) {
                Text("Ver mais", color = Color.Green)
            }
        }

        // grid de produtos
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
           //verticalItemSpacing = 4.dp,
            verticalArrangement = Arrangement.spacedBy(15.dp),
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            modifier = Modifier.height(550.dp)
        ) {
            items(produtosFiltrados.size) { index -> // Usando 'products' diretamente
                    ProductCard(product = produtosFiltrados[index])
                println("Sssss: $index")
            }

        }
    }





    //Define o modelo de dados para representar uma fruta
    data class products(
        val title: String,
        val price: String,
        //val imageRes: String
    )

}



//card de frutas
@Composable
fun ProductCard(product: ProductModel) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .padding(end = 8.dp),
        elevation = 4.dp,
        backgroundColor = CORES.COLOR.YELLOW
    )
    {
        Column(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = product.thumbnail ?: "https://dummyjson.com/products",
                contentDescription = product.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )

            Spacer(Modifier.height(8.dp))
            Text(
                "Em estoque: ${product.stock}",
                color = CORES.COLOR.DARKGREEN
            ) // Cor original

            Text(
                "R$${(product.price)}",
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.Bold
            )
            Text(
                product.title,
                style = MaterialTheme.typography.subtitle2,
                fontWeight = FontWeight.Bold,
                color = CORES.COLOR.GREEN
            )
        }
    }
}







