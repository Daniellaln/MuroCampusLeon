# MuroCampus

<!-- impeccable:product-schema 1 -->

## Platform

android

## Users

Estudiantes que consultan un muro de práctica y Daniella Leon al sustentar el laboratorio 09.

## Product Purpose

Demostrar consultas REST, creación y mejoras de una publicación con MVVM y Repository, con una app fácil de recorrer.

## Operating Context

Kotlin, Jetpack Compose y Material 3. Paquete com.leon.murocampus. Guía GLAB-S09-JLEONS-2026.docx. main conserva fase 1; mejora-ia reúne fase 2. Cada commit tiene una copia completa acumulativa.

## Capabilities and Constraints

Lista, detalle, comentarios, filtro por usuario, crear, errores y reintentar. Función propia desde fase 1: compartir título, texto y enlace con Android. Fase 2 implementa las seis opciones de la guía: buscar, editar, eliminar, Accept JSON, caché Room y paginación de veinte. JSONPlaceholder es de práctica: sus escrituras se simulan. No inventar persistencia remota ni cuentas. No llamar a Retrofit desde ViewModel o Compose. Conservar la cancelación.

## Brand Commitments

Nombre MuroCampus. Daniella confirmó fondo blanco fresco, verde suave, rosa ligero, controles de vidrio e imagen universitaria. Pide cuidado femenino sin saturación ni fondo beige. Contenido original de la API, con latín de relleno, sin sustituirlo por noticias inventadas.

## Evidence on Hand

La fase 1 compiló y aprobó 26 pruebas. Capturas reales en docs/capturas; no son mockups. No hay logo institucional autorizado para este muro ni perfil autenticado.

## Product Principles

Ver primero, leer lo necesario. Una acción principal por pantalla. El diseño mejora todas las pantallas y estados. Las imágenes son contexto visual, nunca evidencia del servicio. Publicación y caché se identifican con honestidad.

## Accessibility & Inclusion

Controles mínimos de 48 dp; contraste AA, texto ampliado, teclado y Atrás del sistema. Movimiento desactivable desde Android. Soporte de tema claro y oscuro.

## Ajuste confirmado durante la fase 2

Daniella rechazó una primera composición que seguía el patrón de fase 1: banner, buscador, chips y tarjetas de texto. La versión final usa un feed de lectura con autores reales e iniciales, títulos legibles sin portadas repetidas, búsqueda y filtro por persona, Muro y Guardadas. El detalle muestra título y cuerpo completos, con comentarios a demanda. Guardar para leer conserva snapshots locales completos sin red; los borradores se protegen al salir. Reducir texto no permite inventar contenido de la API.
