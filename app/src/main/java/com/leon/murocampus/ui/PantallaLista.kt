@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.leon.murocampus.ui

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.input.ImeAction
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
    busquedaAbierta: Boolean = false,
    onCerrarBusqueda: () -> Unit = {},
    personasAbiertas: Boolean = false,
    onCerrarPersonas: () -> Unit = {},
    onAbrirComentarios: (Int) -> Unit = {},
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
    val locales = remember(guardados, consulta) { guardados.filter { it.title.contains(consulta.trim(), ignoreCase = true) } }
    val actual: UiState<List<Post>> = if (guardadas) UiState.Exito(locales) else estado
    val numeroCargadas = (descargados as? UiState.Exito)?.datos?.size ?: 0
    val numeroVisible = (actual as? UiState.Exito)?.datos?.size ?: 0
    val scope = rememberCoroutineScope()
    val teclado = LocalSoftwareKeyboardController.current
    val foco = remember { FocusRequester() }
    val gestorFoco = LocalFocusManager.current
    val c = MaterialTheme.colorScheme
    val motion = LocalMotionEnabled.current
    fun seleccionarPersona(id: Int?) {
        vm.filtrar(id); onCerrarPersonas()
        gestorFoco.clearFocus(); teclado?.hide()
        scope.launch { scroll.scrollToItem(0) }
    }
    LaunchedEffect(busquedaAbierta) {
        if (busquedaAbierta) scroll.scrollToItem(0)
    }
    if (personasAbiertas && !guardadas) {
        ModalBottomSheet(onDismissRequest = onCerrarPersonas, containerColor = c.surface,
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
                    ListItem(headlineContent = { Text(autores[id]?.name ?: "Usuario $id") },
                        supportingContent = { autores[id]?.username?.let { Text("@$it") } },
                        leadingContent = { AvatarAutor(id) },
                        trailingContent = { if (filtro == id) Icon(painterResource(R.drawable.ic_check), null, tint = c.primary) },
                        colors = ListItemDefaults.colors(containerColor = if (filtro == id) c.primaryContainer.copy(alpha = .35f) else c.surface),
                        modifier = Modifier.fillMaxWidth().heightIn(min = 72.dp).clickable { seleccionarPersona(id) }
                            .semantics { selected = filtro == id })
                }
            }
        }
    }
    LazyColumn(state = scroll, modifier = Modifier.fillMaxSize().testTag("lista_publicaciones"),
        contentPadding = PaddingValues(bottom = 12.dp)) {
        item(key = "alcance") {
            Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 7.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(if (guardadas) "Guardadas" else filtro?.let { autores[it]?.name ?: "Usuario $it" } ?: "Todas las personas",
                    style = MaterialTheme.typography.labelLarge, color = c.onSurface,
                    maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))
                Text(when {
                    consulta.isNotBlank() -> "$numeroVisible resultados"
                    guardadas -> "${guardados.size} guardadas"
                    else -> "$numeroCargadas descargadas"
                }, style = MaterialTheme.typography.labelSmall, color = c.onSurfaceVariant)
                if (!guardadas && filtro != null) IconButton(onClick = { seleccionarPersona(null) }) {
                    Icon(painterResource(R.drawable.ic_cerrar), "Quitar filtro de persona", Modifier.size(18.dp))
                }
            }
            if (guardadas && consulta.isBlank()) Text("Disponibles en este dispositivo", style = MaterialTheme.typography.bodySmall,
                color = c.onSurfaceVariant, modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp))
            if (consulta.isNotBlank()) Text(if (guardadas) "En ${guardados.size} guardadas" else "En $numeroCargadas descargadas",
                style = MaterialTheme.typography.bodySmall, color = c.onSurfaceVariant,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp))
            HorizontalDivider(color = c.outlineVariant.copy(alpha = .6f))
        }
        item(key = "busqueda") {
            AnimatedVisibility(visible = busquedaAbierta,
                enter = if (motion) fadeIn() + expandVertically() else EnterTransition.None,
                exit = if (motion) fadeOut() + shrinkVertically() else ExitTransition.None) {
                Column(Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(consulta, vm::buscar, label = { Text("Buscar por título") }, singleLine = true,
                            leadingIcon = { Icon(painterResource(R.drawable.ic_buscar), null, Modifier.size(20.dp)) },
                            trailingIcon = { if (consulta.isNotEmpty()) IconButton(onClick = { vm.buscar("") }) {
                                Icon(painterResource(R.drawable.ic_cerrar), "Limpiar búsqueda")
                            } },
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = { gestorFoco.clearFocus(); teclado?.hide() }),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = c.surface, focusedContainerColor = c.surface),
                            modifier = Modifier.weight(1f).focusRequester(foco).testTag("busqueda"))
                        IconButton(onClick = { vm.buscar(""); gestorFoco.clearFocus(); teclado?.hide(); onCerrarBusqueda() }) {
                            Icon(painterResource(R.drawable.ic_cerrar), "Cerrar búsqueda", Modifier.size(22.dp))
                        }
                    }
                    LaunchedEffect(Unit) { foco.requestFocus(); teclado?.show() }
                }
            }
        }
        if (!guardadas && (offline || avisoCache != null || avisoAutores != null || estadoAutores is UiState.Error)) item(key = "avisos") {
            Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                if (estadoAutores is UiState.Error) TextButton(onClick = vm::recargarAutores) { Text("Reintentar nombres de autores") }
                if (avisoAutores != null) Aviso(avisoAutores!!)
                if (offline) Aviso("Sin conexión · copia descargada", R.drawable.ic_nube)
                if (avisoCache != null) Aviso(avisoCache!!)
            }
        }
        when (actual) {
            UiState.Cargando -> item(key = "cargando") { Box(Modifier.padding(horizontal = 16.dp)) { Cargando() } }
            is UiState.Error -> item(key = "error") { ErrorConReintento(actual.mensaje, vm::cargar) }
            is UiState.Exito -> {
                if (actual.datos.isEmpty()) item(key = "vacio") {
                    when {
                        guardadas && consulta.isBlank() -> Vacio("Tus lecturas, a mano", "Guarda una publicación con el marcador para encontrarla aquí.", "Explorar el muro", { onTab(false) })
                        guardadas -> Vacio("Sin coincidencias", "Prueba otro título entre tus lecturas guardadas.", "Limpiar búsqueda", { vm.buscar("") })
                        consulta.isNotBlank() -> Vacio("Sin coincidencias", if (hayMas) "Prueba otro título o carga más publicaciones." else "Prueba otro título o quita el filtro de persona.", "Limpiar búsqueda", { vm.buscar("") })
                        else -> Vacio("Aún no hay publicaciones", "Puedes explorar las de otra persona.", "Ver todas las personas", { seleccionarPersona(null) })
                    }
                }
                items(actual.datos, key = { "post_${it.id}" }) { post ->
                    FilaPublicacion(post, { onAbrir(post.id) }, Modifier.padding(horizontal = 16.dp)
                        .then(if (motion) Modifier.animateItem() else Modifier).testTag("post_${post.id}"),
                        onComentarios = { onAbrirComentarios(post.id) })
                }
                if (!guardadas) item(key = "mas") {
                    Column(Modifier.fillMaxWidth().padding(top = 18.dp, bottom = 12.dp), horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
