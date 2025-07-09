package com.example.authapp.ui.screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    val state by viewModel.authState.collectAsState()
    val focusManager = LocalFocusManager.current
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Criar Conta") })
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        ) { Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 24.dp)
                .wrapContentHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("E-mail") },
                leadingIcon = { Icon(Icons.Default.Email,
                    contentDescription = null) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                leadingIcon = { Icon(Icons.Default.Lock,
                    contentDescription = null) },
                trailingIcon = {
                    val icon = if (showPassword)
                        Icons.Default.Visibility else Icons.Default.VisibilityOff
                    IconButton(onClick = { showPassword =
                        !showPassword }) {
                        Icon(icon, contentDescription =
                            "Mostrar/ocultar senha")
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = if (showPassword)
                    VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    focusManager.clearFocus()

                    viewModel.register(email.trim(), password)

                },
                enabled = email.isNotBlank() &&
                        password.isNotBlank() && state != AuthState.Loading,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (state == AuthState.Loading)
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier
                            .size(18.dp)
                            .padding(end = 8.dp),
                        strokeWidth = 2.dp
                    )
                Text("Cadastrar")
            }
            Spacer(Modifier.height(16.dp))
            TextButton(onClick = onNavigateToLogin) {
                Text("Já tenho conta")
            }
            if (state is AuthState.Error) {
                Text(
                    (state as AuthState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
            if (state == AuthState.Authenticated) onRegisterSuccess()
        }
        }
    }
}