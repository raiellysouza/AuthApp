package com.example.authapp.ui.screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: AuthViewModel, onLogout: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("AuthApp") })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Bem-vindo!",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                Button(
                    onClick = { viewModel.logout(); onLogout() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sair")
                }
            }
        }
    }
}