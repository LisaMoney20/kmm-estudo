@file:OptIn(ExperimentalMaterialApi::class)

package FirstScreen


import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ChipDefaults.filterChipColors
import androidx.compose.material.DrawerDefaults.backgroundColor
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
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.selects.select



//Tela principal
@Composable
fun TesteApp(){
    var selectedCategory by remember { mutableStateOf("Cítricas") }// Estado local para a categoria selecionada *adicionei .toString()
    val fruits = remember {getFruits() } //Estado para armazenar a lista de frutas disponíveis


//Define o tema do Material Design
    MaterialTheme {
        //Estrutura principal que inclui a barra superior e o conteúdo da tela
        Scaffold(
            backgroundColor = CORES.COLOR.DARKGREEN,
            topBar = {
                TopAppBar(
                    title = { Text("APP Fruta")},
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

                SearchBar() //Renderiza um campo de busca onde o usuário pode digitar
                CategorySection( //Categoria atualmente selecionada
                    categories = listOf("Cítricas",
                        "Tropical",
                        "Frutas Vermelhas",
                        "Exóticas"),
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it } //Atualiza o estado da categoria ao clicar em uma opção
                )
                RecommendedSection(fruits = fruits) //Exibe frutas recomendadas em uma lista rolável horizontalmente (LazyRow).
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
                contentDescription = "Menu suspenso",
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

//        IconButton(
//            onClick = { expanded = true }
//        ) {
////            Icon(
////                imageVector = Icons.Default.Person,
////                contentDescription = "Perfil",
////                tint = CORES.COLOR.GREENLIGHT
////            )
////           DropdownMenuItem(onClick = {
////             expanded = false
////           // Ação para item 1
////         }) {
////               Text("Perfil")
////         }


////Seção de Boas-Vindas
//@Composable
//fun GreetingSection(name: String) {
//    Text(
//        text = "Olá, $name",
//        style = MaterialTheme.typography.h6,
//        color = Color.White,
//
//        modifier = Modifier
//            .padding(12.dp) //Usa o MaterialTheme.typography.h5 para estilo
//
//
//    )
//}
//icone de perfil
//@Composable
//fun Profile() {
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(12.dp)
//    ) {
//        // Ícone de perfil alinhado à direita
//        Icon(
//            imageVector = Icons.Default.Person,
//            contentDescription = "Perfil do usuário",
//            tint = CORES.COLOR.GREENLIGHT,
//            modifier = Modifier.align(Alignment.CenterEnd)
//        )
//    }
//}







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
        }
    }
}











    //LazyRow para rolagem horizontal de categorias
//        LazyRow {
//            items(categories) { category ->
//                FilterChip(
//                    selected = (category == selectedCategory),
//                    onClick = { onCategorySelected(category) },
//                    modifier = Modifier.padding(end = 8.dp) ) //FilterChip permite selecionar uma categoria (estilo botão) *em category adicionei .toString()
//                {
//                    Text(category)  // Atualiza a categoria selecionada
//                }
//            }
//        }








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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color.LightGray)
            )
            Spacer(Modifier.height(8.dp))
            // Text(fruit.name, FontWeight = FontWeight.Bold) //Negrito
            Text("Em estoque", color = CORES.COLOR.DARKGREEN)
            Text(fruit.name)
            Text(fruit.price)
        }
    }
}

//Define o modelo de dados para representar uma fruta
data class Fruit(
    val name: String,
    val price: String
)
//Lista fixa de frutas
fun getFruits(): List<Fruit> =listOf(
    Fruit("Kiwi", "R$7.50/1kg"),
    Fruit("Laranja", "R$9.99/1kg"),
    Fruit("Manga", "R$5.20/1kg"),
    Fruit("Morango", "R$12.00/1kg"),
    Fruit("Graviola", "R$10.00/1kg"),
    Fruit("Cupuaçu", "R$5.00/1kg"),
    Fruit("Açai", "R$50.00/1kg")
)




