import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.github.kota.apps.gemini.components.AppHeader
import com.github.kota.apps.gemini.components.ChatNameInputDialog
import com.github.kota.apps.gemini.components.SpeechToText
import com.github.kota.apps.gemini.components.parseMarkdown
import com.github.kota.apps.gemini.composition.LocalFontSize
import com.github.kota.apps.gemini.model.Message
import com.github.kota.apps.gemini.viewmodel.ChatViewModel
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    onSettingsClick: () -> Unit,
) {
    //Messageの自動スクロール
    val listState = rememberLazyListState()

    val viewModel: ChatViewModel = hiltViewModel()
    //callback系
    val messages by viewModel.messages.collectAsState()
    val chatName by viewModel.selectedChatName.collectAsState()
    val chatNames by viewModel.chatNames.collectAsState()

    var inputText by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    RequestPermissionIfNeeded()

    ChatNameInputDialog(
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        onConfirm = { newChatName ->
            viewModel.addChatName(newChatName)
            showDialog = false
        }
    )

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        chatNames.forEach { name ->
                            Row {

                                Text(
                                    text = name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = (LocalFontSize.current+4).sp,
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .clickable {
                                            viewModel.selectChat(name)
                                            coroutineScope.launch { drawerState.close() }
                                        }
                                )

                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Chat",
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .clickable {
                                            viewModel.deleteMessagesForChat(name)
                                        }
                                )
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(24.dp)
                            .clickable {
                                onSettingsClick()
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "設定",
                            modifier = Modifier.size(26.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "設定",
                            fontSize = (LocalFontSize.current).sp
                        )
                    }

                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                AppHeader(
                    title = chatName ?: "Chat",
                    onMenuClick = {
                        coroutineScope.launch{
                            drawerState.open()
                        }
                    },
                    onNewChatClick = {
                        showDialog = true
                    }
                )
            },
            bottomBar = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    DisplayImage(selectedImageUri)

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .imePadding()
                            .padding(16.dp)
                    ) {
                        TextField(
                            value = inputText,
                            onValueChange = { text -> inputText = text },
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceDim,
                                    shape = RoundedCornerShape(13.dp)
                                )
                                .clip(RoundedCornerShape(13.dp))
                                .weight(1f),
                            placeholder = {
                                Text(
                                    text = "メッセージ",
                                    color = MaterialTheme.colorScheme.surfaceBright
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Send,
                                autoCorrect = true
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // 🔹 音声入力ボタン（SpeechToText を統合）
                        SpeechToText { recognizedText ->
                            inputText += recognizedText
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        ImagePicker { uri ->
                            selectedImageUri = uri
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                if (inputText.isNotBlank() || selectedImageUri != null) {
                                    viewModel.sendMessage(inputText, selectedImageUri)
                                    inputText = ""
                                    selectedImageUri = null

                                    coroutineScope.launch {
                                        listState.animateScrollToItem(messages.size - 1)
                                    }
                                }
                            },
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier
                                .height(40.dp)
                                .width(40.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceDim
                            ),
                        ) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowUp,
                                contentDescription = "Send",
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->

            LaunchedEffect(messages.lastOrNull()) {
                messages.lastOrNull()?.let {
                    listState.animateScrollToItem(messages.size - 1)
                }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.padding(innerPadding),
                reverseLayout = false
            ) {
                items(messages) { message ->
                    ChatBubble(message)
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: Message) {
    val backgroundColor = if (message.isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    var showDialogMessage by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(backgroundColor,shape = RoundedCornerShape(12.dp))
                .padding(8.dp)
                .widthIn(
                    min = 40.dp,
                    max = screenWidth * 0.7f
                )
        ) {
            message.text?.let { text ->
                Text(
                    text = parseMarkdown(text),
                    color = MaterialTheme.colorScheme.surfaceBright,
                    fontSize = LocalFontSize.current.sp,
                    modifier = Modifier
                        .combinedClickable (
                            onClick = {},
                            onLongClick = {
                                showDialogMessage = true
                            }
                        )
                )

                if (showDialogMessage) {
                    AlertDialog(
                        onDismissRequest = { showDialogMessage = false },
                        title = { Text("メッセージを選択") },
                        text = { Text("コピーまたは共有を選択してください。") },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(text))
                                    Toast.makeText(context, "コピーしました", Toast.LENGTH_SHORT).show()
                                    showDialogMessage = false
                                }
                            ) {
                                Text("コピー")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    showShareDialog(context, text)
                                    showDialogMessage = false
                                }
                            ) {
                                Text("共有")
                            }
                        }
                    )
                }
            }
            message.imageUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "Sent Image",
                    modifier = Modifier
                        .size(150.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
}

@Composable
fun RequestPermissionIfNeeded(){
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Toast.makeText(context,"画像を選択するには許可が必要です",Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect (Unit){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
        }else{
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }
}

@Composable
fun ImagePicker(onImageSelected: (Uri) -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImageSelected(it) }
    }

    Button(
        onClick = { launcher.launch("image/*") },
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .height(40.dp)
            .width(40.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceDim
        ),
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Image",
        )
    }
}


@Composable
fun DisplayImage(imageUri: Uri?) {
    imageUri?.let {
        Image(
            painter = rememberAsyncImagePainter(it),
            contentDescription = "Selected Image",
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(8.dp))
        )
    }
}

fun showShareDialog(context: Context, message: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    context.startActivity(Intent.createChooser(intent, "共有するアプリを選択"))
}
