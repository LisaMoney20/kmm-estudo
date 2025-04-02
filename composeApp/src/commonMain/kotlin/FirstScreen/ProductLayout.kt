@file:OptIn(ExperimentalMaterialApi::class)

package FirstScreen


import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


//Tela principal
@Composable
fun TesteApp(){
    var selectedCategory by remember { mutableStateOf("") }// Estado local para a categoria selecionada
    var products by remember { mutableStateOf(emptyList<ProductModel>()) }
    var categories by remember { mutableStateOf(emptyList<String>()) }
    val productController = remember { ProductController() }
    val coroutineScope = rememberCoroutineScope()


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

            }
                GreetingSection(name = "Money")

                SearchBar() //barra de pesquisa
                if (categories.isNotEmpty()){
                CategorySection( //Categoria atualmente selecionada
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it } //Atualiza o estado da categoria ao clicar em uma opção
                )}
               //SEÇÃO DE RECOMENDADOS
            val filteredProducts = if (selectedCategory.isEmpty()){
            products
            } else {
                products.filter { it.category == selectedCategory }
            }
            RecommendedSection(products = filteredProducts)
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
fun SearchBar() {
    var searchText by remember { mutableStateOf("") } //Armazena o texto digitado pelo usuário
    //Campo de entrada com borda
    OutlinedTextField(
        value = searchText,
        onValueChange = {searchText = it},
        placeholder =  {Text("Pesquisar", color = CORES.COLOR.GREENLIGHT )}, // Cor do placeholder  //textColor = Color.White
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
)
{
    Column(modifier = Modifier.padding(20.dp)) {
        Text(
            "Categoria",
            style = MaterialTheme.typography.h6,
            color = CORES.COLOR.GREENLIGHT
        )

        // Layout em grade 2x2
        Column(modifier = Modifier.padding(10.dp)) {
            // Primeira linha (2 chips)
            Row {
                FilterChip(
                    selected = (categories[0] == selectedCategory),
                    onClick = { onCategorySelected(categories[0]) },
                    colors = filterChipColors(
                        backgroundColor = CORES.COLOR.YELLOWLI, // Cor quando NÃO selecionado
                        selectedBackgroundColor = CORES.COLOR.YELLOW, // Cor quando SELECIONADO
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp, bottom = 8.dp)
                        .width(120.dp) // Defina uma largura adequada
                ) {
                    Text(
                        text = categories[0],
                        textAlign = TextAlign.Center,//centraliza o texto
                        modifier = Modifier.fillMaxWidth() // Ocupa toda a largura disponível
                    )
                }

                FilterChip(
                    selected = (categories[1] == selectedCategory),
                    onClick = { onCategorySelected(categories[1]) },
                    colors = filterChipColors(
                        backgroundColor = CORES.COLOR.YELLOWLI, // Cor quando NÃO selecionado
                        selectedBackgroundColor = CORES.COLOR.YELLOW, // Cor quando SELECIONADO
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp, bottom = 8.dp)
                ) {
                    Text(
                        text = categories[1],
                        textAlign = TextAlign.Center,//centraliza o texto
                        modifier = Modifier.fillMaxWidth() // Ocupa toda a largura disponível
                    )
                }
            }

            // Segunda linha (2 chips)
            Row {
                FilterChip(
                    selected = (categories[2] == selectedCategory),
                    onClick = { onCategorySelected(categories[2]) },
                    colors = filterChipColors(
                        backgroundColor = CORES.COLOR.YELLOWLI, // Cor quando NÃO selecionado
                        selectedBackgroundColor = CORES.COLOR.YELLOW, // Cor quando SELECIONADO
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                ) {
                    Text(
                        text = categories[2],
                        textAlign = TextAlign.Center,//centraliza o texto
                        modifier = Modifier.fillMaxWidth() // Ocupa toda a largura disponível
                    )
                }

                FilterChip(
                    selected = (categories[3] == selectedCategory),
                    onClick = { onCategorySelected(categories[3]) },
                    colors = filterChipColors(
                        backgroundColor = CORES.COLOR.YELLOWLI, // Cor quando NÃO selecionado
                        selectedBackgroundColor = CORES.COLOR.YELLOW, // Cor quando SELECIONADO
                    ),

                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 4.dp)
                ) {
                    Text(
                        text = categories[3],
                        textAlign = TextAlign.Center,//centraliza o texto
                        modifier = Modifier.fillMaxWidth() // Ocupa toda a largura disponível
                    )
                }
            }








            // Seção de Frutas Recomendadas
@Composable
fun RecommendedSection(fruits: List<Fruit>) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Recomendados para você",
                style = MaterialTheme.typography.h6,
                color = CORES.COLOR.GREEN)
            Spacer(Modifier.weight(1f))
            TextButton(onClick = {}) {
                Text("Ver mais", color = Color.Green)
            }
        }

        // Substituição do LazyRow
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
        ) {
            fruits.forEach { fruit ->
                FruitCard(fruit = fruit)
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

//card de frutas
@Composable
fun FruitCard(fruit: Fruit ) {
    Card(

        modifier = Modifier
            .width(150.dp)
            .padding(end = 8.dp),
        elevation = 4.dp,
        backgroundColor = CORES.COLOR.YELLOW
    ) //Cada fruta é exibida dentro de um Cartão
    {
        Column(modifier = Modifier.padding(8.dp)){
//            AsyncImage(
//                model = "https://ibb.co/Kx9Z22nj",
//                contentDescription = "Fruit.name"
 //           )
//            Image(
//                painter = painterResource(id = R.drawable.kiwi),
//                contentDescription = fruit.name,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(100.dp)
//            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.LightGray)
            )
            Spacer(Modifier.height(8.dp))
            // Text(fruit.name, FontWeight = FontWeight.Bold) //Negrito
            Text("Em estoque",
                color = CORES.COLOR.DARKGREEN)
            Text(fruit.price,
                style = MaterialTheme.typography.body2,
                fontWeight =  FontWeight.Bold)
            Text(fruit.name,
                style = MaterialTheme.typography.subtitle2,
                fontWeight = FontWeight.Bold,
                color = CORES.COLOR.GREEN )

        }

    }
}




//Define o modelo de dados para representar uma fruta
data class Fruit(
    val name: String,
    val price: String,
    //val imageRes: String
)
//Lista fixa de frutas
fun getFruits(): List<Fruit> =listOf(
    Fruit("Kiwi", "R$7.50/1kg"),
//    Fruit("Laranja", "R$9.99/1kg","laranja"),
//    Fruit("Manga", "R$5.20/1kg","manga"),
//    Fruit("Morango", "R$12.00/1kg","morango"),
//    Fruit("Graviola", "R$10.00/1kg","graviola"),
//    Fruit("Cupuaçu", "R$5.00/1kg","cupuacu"),
//    Fruit("Açai", "R$50.00/1kg","acai")
)




