@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.leon.murocampus.ui

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leon.murocampus.R
import com.leon.murocampus.model.*
import com.leon.murocampus.viewmodel.PostsViewModel
import kotlinx.coroutines.launch

@Composable
fun PantallaLista(
    vm: PostsViewModel,
    scroll: LazyListState = rememberLazyListState(),
    guardadas: Boolean = false,
    onTab: (Boolean) -> Unit = {},
    onAbrir: (Int) -> Unit
) {
    val estado by vm.visibles.collectAsStateWithLifecycle()
    val descargados by vm.lista.collectAsStateWithLifecycle()
    val filtro by vm.usuarioFiltro.collectAsStateWithLifecycle()
    val consulta by vm.busqueda.collectAsStateWithLifecycle()
    val offline by vm.sinConexion.collectAsStateWithLifecycle()
    val hayMas by vm.hayMas.collectAsStateWithLifecycle()
    val cargandoMas by vm.cargandoMas.collectAsStateWithLifecycle()
    val errorMas by vm.errorMas.collectAsStateWithLifecycle()
    val avisoCache by vm.avisoCache.collectAsStateWithLifecycle()
    val estadoAutores by vm.estadoAutores.collectAsStateWithLifecycle()
    val avisoAutores by vm.avisoCacheAutores.collectAsStateWithLifecycle()
    val autores = LocalAutores.current
    val guardados = LocalGuardados.current
    val locales = remember(guardados, consulta) {
        guardados.filter { it.title.contains(consulta.trim(), ignoreCase = true) }
    }
    val actual: UiState<List<Post>> = if (guardadas) UiState.Exito(locales) else estado
    val numeroCargadas = (descargados as? UiState.Exito)?.datos?.size ?: 0
    val numeroVisible = (actual as? UiState.Exito)?.datos?.size ?: 0
    var busquedaAbierta by rememberSaveable { mutableStateOf(consulta.isNotBlank()) }
    var personasAbiertas by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val teclado = LocalSoftwareKeyboardController.current
    val foco = remember { FocusRequester() }
    val gestorFoco = LocalFocusManager.current
    val c = MaterialTheme.colorScheme
    val motion = LocalMotionEnabled.current
    val herramientas = if (guardadas) 2 else 3
    fun buscar(texto: String) {
        vm.buscar(texto)
        // Mantener el campo compuesto y el foco mientras se escribe.
    }
    fun seleccionarPersona(id: Int?) {
        vm.filtrar(id)
        personasAbiertas = false
        gestorFoco.clearFocus(); teclado?.hide()
        scope.launch { scroll.scrollToItem(0) }
    }
    if (personasAbiertas && !guardadas) {
        ModalBottomSheet(onDismissRequest = { personasAbiertas = false }, containerColor = c.surface,
            contentColor = c.onSurface, sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)) {
            Text("Personas", style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp))
            LazyColumn(Modifier.fillMaxWidth().heightIn(max = 560.dp), contentPadding = PaddingValues(bottom = 24.dp)) {
                item {
                    ListItem(headlineContent = { Text("Todas las personas") },
                        leadingContent = { Icon(painterResource(R.drawable.ic_usuario), null, Modifier.size(24.dp)) },
                        trailingContent = { if (filtro == null) Icon(painterResource(R.drawable.ic_check), null, tint = c.primary) },
                        colors = ListItemDefaults.colors(containerColor = c.surface),
                        modifier = Modifier.fillMaxWidth().heightIn(min = 64.dp).clickable { seleccionarPersona(null) }
                            .semantics { selected = filtro == null })
                }
                items((1..10).toList(), key = { it }) { id ->
                    ListItem(
                        headlineContent = { Text(autores[id]?.name ?: "Usuario $id") },
                        supportingContent = { autores[id]?.username?.let { Text("@$it") } },
                        leadingContent = { AvatarAutor(id) },
                        trailingContent = { if (filtro == id) Icon(painterResource(R.drawable.ic_check), null, tint = c.primary) },
                        colors = ListItemDefaults.colors(containerColor = if (filtro == id) c.primaryContainer.copy(alpha = .35f) else c.surface),
                        modifier = Modifier.fillMaxWidth().heightIn(min = 72.dp).clickable { seleccionarPersona(id) }
                            .semantics { selected = filtro == id }
                    )
                }
            }
        }
    }
    LazyColumn(
        state = scroll,
        modifier = Modifier.fillMaxSize().testTag("lista_publicaciones"),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 104.dp)
    ) {
        item(key = "contexto") { CabeceraComunidad() }
        item(key = "pestanas") {
            PrimaryTabRow(selectedTabIndex = if (guardadas) 1 else 0, containerColor = c.background,
                contentColor = c.primary, modifier = Modifier.padding(top = 10.dp)) {
                Tab(selected = !guardadas, onClick = { if (guardadas) onTab(false) }, text = { Text("Muro") })
                Tab(selected = guardadas, onClick = { if (!guardadas) onTab(true) }, text = { Text("Guardadas") })
            }
        }
        if (!guardadas) item(key = "personas") {
            Row(Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                (1..4).forEach { id ->
                    val seleccionado = filtro == id
                    val nombre = autores[id]?.name ?: "Usuario $id"
                    Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(5.dp)) {
                        FilledTonalIconToggleButton(checked = seleccionado,
                            onCheckedChange = { seleccionarPersona(if (seleccionado) null else id) },
                            colors = IconButtonDefaults.filledTonalIconToggleButtonColors(
                                containerColor = if (id % 3 == 0) c.secondaryContainer.copy(alpha = .65f) else c.primaryContainer.copy(alpha = .65f),
                                contentColor = if (id % 3 == 0) c.onSecondaryContainer else c.onPrimaryContainer,
                                checkedContainerColor = c.primary, checkedContentColor = c.onPrimary),
                            modifier = Modifier.size(48.dp).semantics {
                                contentDescription = "Filtrar por $nombre"
                                stateDescription = if (seleccionado) "Filtro activo" else "Sin seleccionar"
                            }) {
                            Text(inicialesAutor(autores[id]?.name, id), style = MaterialTheme.typography.labelMedium)
                        }
                        Text(if (autores[id] == null) "Usuario $id" else nombre.substringBefore(' '),
                            style = MaterialTheme.typography.labelSmall, color = c.onSurfaceVariant,
                            maxLines = 1, overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center)
                    }
                }
                TextButton(onClick = { personasAbiertas = true },
                    modifier = Modifier.widthIn(min = 60.dp).heightIn(min = 64.dp).semantics { contentDescription = "Filtrar por usuario" }, contentPadding = PaddingValues(horizontal = 4.dp)) {
                    Text("Ver\ntodos", style = MaterialTheme.typography.labelMedium, textAlign = TextAlign.Center)
                }
            }
        }
        item(key = "herramientas") {
            Column(Modifier.fillMaxWidth().padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(when {
                            actual !is UiState.Exito -> if (guardadas) "Tus lecturas" else "Publicaciones"
                            consulta.isNotBlank() -> "$numeroVisible resultados"
                            guardadas -> "${guardados.size} guardadas"
                            else -> "$numeroCargadas descargadas"
                        }, style = MaterialTheme.typography.titleSmall, color = c.onSurface)
                        Text(when {
                            guardadas && consulta.isNotBlank() -> "En ${guardados.size} guardadas"
                            guardadas -> "Disponibles en este dispositivo"
                            consulta.isNotBlank() -> "En $numeroCargadas descargadas" + if (filtro != null) " · ${autores[filtro]?.name ?: "Usuario $filtro"}" else ""
                            filtro != null -> autores[filtro]?.name ?: "Usuario $filtro"
                            else -> "Todas las personas"
                        }, style = MaterialTheme.typography.bodySmall, color = c.onSurfaceVariant)
                    }
                    GlassIconButton(if (busquedaAbierta) R.drawable.ic_cerrar else R.drawable.ic_buscar,
                        if (busquedaAbierta) "Cerrar búsqueda" else "Buscar publicaciones", {
                            if (busquedaAbierta) {
                                vm.buscar(""); gestorFoco.clearFocus(); teclado?.hide()
                                scope.launch { scroll.scrollToItem(0) }
                            }
                            busquedaAbierta = !busquedaAbierta
                        })
                }
                if (!guardadas && filtro != null) {
                    InputChip(selected = true, onClick = { seleccionarPersona(null) }, label = { Text("Quitar filtro de persona") },
                        trailingIcon = { Icon(painterResource(R.drawable.ic_cerrar), null, Modifier.size(16.dp)) },
                        modifier = Modifier.heightIn(min = 48.dp))
                }
                AnimatedVisibility(visible = busquedaAbierta,
                    enter = if (motion) fadeIn() + expandVertically() else EnterTransition.None,
                    exit = if (motion) fadeOut() + shrinkVertically() else ExitTransition.None) {
                    OutlinedTextField(consulta, ::buscar, label = { Text("Buscar por título") }, singleLine = true,
                        leadingIcon = { Icon(painterResource(R.drawable.ic_buscar), null, Modifier.size(20.dp)) },
                        trailingIcon = { if (consulta.isNotEmpty()) IconButton(onClick = { buscar("") }) {
                            Icon(painterResource(R.drawable.ic_cerrar), "Limpiar búsqueda")
                        } },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { gestorFoco.clearFocus(); teclado?.hide() }),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = c.surface, focusedContainerColor = c.surface),
                        modifier = Modifier.fillMaxWidth().focusRequester(foco).testTag("busqueda"))
                    LaunchedEffect(Unit) { foco.requestFocus(); teclado?.show() }
                }
                if(!guardadas && estadoAutores is UiState.Error)TextButton(onClick=vm::recargarAutores) { Text("Reintentar nombres de autores") }
                if(!guardadas && avisoAutores!=null)Aviso(avisoAutores!!)
                if (!guardadas && offline) Aviso("Sin conexión · copia descargada", R.drawable.ic_nube)
                if (!guardadas && avisoCache != null) Aviso(avisoCache!!)
            }
        }
        when (actual) {
            UiState.Cargando -> item(key = "cargando") { Cargando() }
            is UiState.Error -> item(key = "error") { ErrorConReintento(actual.mensaje, vm::cargar) }
            is UiState.Exito -> {
                if (actual.datos.isEmpty()) item(key = "vacio") {
                    when {
                        guardadas && consulta.isBlank() -> Vacio("Tus lecturas, a mano",
                            "Guarda una publicación con el marcador para encontrarla aquí.", "Explorar el muro", { onTab(false) })
                        guardadas -> Vacio("Sin coincidencias", "Prueba otro título entre tus lecturas guardadas.", "Limpiar búsqueda", { buscar("") })
                        consulta.isNotBlank() -> Vacio("Sin coincidencias",
                            if (hayMas) "Prueba otro título o carga más publicaciones." else "Prueba otro título o quita el filtro de persona.",
                            "Limpiar búsqueda", { buscar("") })
                        else -> Vacio("Aún no hay publicaciones", "Puedes explorar las de otra persona.", "Ver todas las personas", { seleccionarPersona(null) })
                    }
                }
                items(actual.datos, key = { "post_${it.id}" }) { post ->
                    FilaPublicacion(post, { onAbrir(post.id) }, Modifier
                        .then(if (motion) Modifier.animateItem() else Modifier).testTag("post_${post.id}"))
                }
                if (!guardadas) item(key = "mas") {
                    Column(Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 8.dp), horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        if (errorMas != null) Text(errorMas!!, style = MaterialTheme.typography.bodySmall, color = c.error)
                        if (hayMas) GlassButton(if (errorMas != null) "Reintentar carga" else "Cargar más", vm::cargarMas,
                            icon = R.drawable.ic_agregar, loading = cargandoMas)
                        else if (actual.datos.isNotEmpty()) Text(if (offline) "Fin de la copia disponible" else "Llegaste al final",
                            style = MaterialTheme.typography.bodySmall, color = c.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
private fun CabeceraComunidad() {
    val c = MaterialTheme.colorScheme
    Box(Modifier.fillMaxWidth().heightIn(min = 116.dp).clip(RoundedCornerShape(16.dp))) {
        Image(painterResource(R.drawable.campus_atrio), null, Modifier.matchParentSize(), contentScale = ContentScale.Crop)
        Box(Modifier.matchParentSize().background(Brush.horizontalGradient(listOf(
            c.surface.copy(alpha = .96f), c.surface.copy(alpha = .80f), c.surface.copy(alpha = .10f)))))
        Column(Modifier.padding(horizontal = 18.dp, vertical = 22.dp), verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Text("Comunidad", style = MaterialTheme.typography.headlineMedium, color = c.onSurface)
            Text("Explora, guarda y comparte", style = MaterialTheme.typography.bodySmall, color = c.onSurfaceVariant)
        }
    }
}
