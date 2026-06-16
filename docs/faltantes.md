# Faltantes — Ice and Fire Redux

> **Opinión sincera sobre el estado real del proyecto y lo que falta para ser un mod funcional en Minecraft 1.12.2.**
>
> Última actualización: 15 Jun 2026

---

## Tabla de Contenido

- [Resumen Ejecutivo](#resumen-ejecutivo)
- [Lo que SÍ funciona](#lo-que-sí-funciona)
- [🔴 Bloqueantes (impiden jugar)](#-bloqueantes-impiden-jugar)
- [🟠 Críticos (bajo rendimiento o bugs graves)](#-críticos-bajo-rendimiento-o-bugs-graves)
- [🟡 Importantes (afectan la experiencia)](#-importantes-afectan-la-experiencia)
- [🟢 Mejoras (calidad de vida y mantenibilidad)](#-mejoras-calidad-de-vida-y-mantenibilidad)
- [Infraestructura del Proyecto](#infraestructura-del-proyecto)
- [Plan de Refactorización Pendiente](#plan-de-refactorización-pendiente)
- [Deuda Técnica a Largo Plazo](#deuda-técnica-a-largo-plazo)
- [Opinión Sincera](#opinión-sincera)

---

## Resumen Ejecutivo

**El proyecto compila y arranca sin errores.** Se hizo una migración completa de namespace (alexthe666 → Redux), consolidación de clases duplicadas (dragones, explosiones, proyectiles), extracción de layers, y se agregó Javadoc al 100% de las clases. Sin embargo, **nunca se ha probado en un juego real** (ni singleplayer ni multiplayer). Hay ~60% del plan de refactorización pendiente. El código base tiene ~3082 líneas en una sola clase (EntityDragonBase) que sigue siendo un problema de mantenibilidad. No hay tests, no hay git, no hay CI/CD. No se ha verificado la generación de mundo, los drops, el balance, ni la compatibilidad con otros mods en runtime.

---

## Lo que SÍ funciona

| Componente | Estado |
|------------|--------|
| Compilación (`compileJava`) | ✅ 0 errores, 0 warnings |
| Build (`reobfJar`) | ✅ BUILD SUCCESSFUL |
| Mixins (9 mixins aplicados) | ✅ Carga correcta en PREINIT/INIT/DEFAULT |
| Coremod (`IceAndFirePlugin`) | ✅ Inyectado por FML sin errores |
| Registro de entidades | ✅ Todas las entidades registradas |
| Network (21 mensajes) | ✅ @NetworkWrapper configurado |
| Capacidades (InFCapabilities) | ✅ Registradas |
| Servidor dedicado (`runServer`) | ✅ Arranca hasta pantalla de EULA |
| Javadoc de clase | ✅ 667/667 archivos (100%) |
| Namespace migration | ✅ 0 referencias a `alexthe666` en .java |
| Eliminación de `instanceof EntityFireDragon/IceDragon/LightningDragon` | ✅ 13 archivos |
| Consolidadción de explosiones (DragonExplosion) | ✅ ~950 líneas eliminadas |
| Consolidadción de proyectiles (BreathProjectile + ChargeProjectile) | ✅ ~550 líneas eliminadas |
| Consolidadción de dragones (EntityDragonBase) | ✅ ~245 líneas eliminadas |
| Extracción de layers (21 layers a archivos propios) | ✅ 0 inner classes en renders |
| Eliminación de `proxy/` subpackage | ✅ 4 archivos huérfanos removidos |
| Construct's Armory (`conarm`) — materiales de armadura | ✅ dragonbone + fire/ice/lightning dragonsteel (ver [C5](#c5-constructs-armory---verificación-runtime)) |

---

## 🔴 Bloqueantes (impiden jugar)

Estos impiden que el mod funcione en un entorno real de juego:

### R1. Cliente nunca probado
- No se ha ejecutado `runClient` ni se ha abierto el juego con el mod cargado.
- No se sabe si las texturas cargan, si los modelos renderizan, si las animaciones funcionan.
- Los renderers y modelos son los componentes con más probabilidad de fallo silencioso.
- **Acción:** Ejecutar `gradlew runClient`, aceptar EULA, crear mundo, spawnear un dragón y verificar visualmente.

### R2. EULA no aceptada
- `run/eula.txt` tiene `eula=false`. El servidor se detiene inmediatamente después de cargar.
- **Acción:** Cambiar a `eula=true`.

### R3. Generación de mundo no verificada
- No se ha probado si las estructuras (cuevas de dragón, mausoleos, colmenas myrmex, islas de sirenas) se generan correctamente con el nuevo namespace.
- Los `WorldGen*` y `MapGen*` referencian clases del mod para spawn de entidades — cualquier error de ClassNotFound rompe la generación del chunk.
- **Acción:** Generar un mundo nuevo y verificar que no crashea; usar `/locate` o volar en creativo para confirmar estructuras.

### R4. Drops y loot tables sin validar
- Las loot tables referencian items por ID (`iceandfire:fire_dragon_blood`). Si algún item no se registró bien, el drop da error silencioso.
- **Acción:** Matar entidades en juego y verificar drops; revisar consola por errores de loot table.

### R5. Red/network sin prueba multiplayer
- 21 mensajes de red — no se ha probado ninguno en un entorno cliente-servidor real.
- `MessageDragonControl`, `MessageDragonSyncFire`, `MessageSirenSong` etc. pueden tener bugs de sincronización.
- **Acción:** Abrir un servidor dedicado, conectar un cliente, montar un dragón y verificar que los mensajes de control funcionan.

---

## 🟠 Críticos (bajo rendimiento o bugs graves)

Estos no impiden arrancar pero degradan severamente la experiencia o causan crashes en condiciones específicas:

### C1. EntityDragonBase — 3082 líneas
- La clase más grande del mod. Mezcla movimiento, IA, animación, combate, vuelo, interacciones (11 tipos), NBT, aging, hambre, breeding.
- Cualquier cambio en esta clase tiene alto riesgo de efectos colaterales.
- No se aplicó la refactorización planeada (extraer a helpers `DragonFlightHelper`, `DragonCombatHelper`, `DragonInteractHelper`).
- **Acción:** Completar Fase 1 del plan — extraer responsabilidades a métodos con nombre, luego a clases helper.

### C2. Sin pruebas de regresión
- No hay tests unitarios, de integración, ni de ningún tipo.
- Cada cambio se verifica solo con `compileJava`. Si algo rompe en runtime, no se detecta.
- 667 archivos sin cobertura de pruebas.
- **Acción:** Mínimo: tests de humo para registro de entidades, items, bloques y capacidades.

### C3. `GameRegistry` deprecated
- `IafEntityRegistry.java` y `IafBlockRegistry.java` usan `GameRegistry.register()` que está deprecated en Forge 1.12.2.
- Funciona, pero puede romperse con futuras actualizaciones de Forge o conflictos con otros mods.
- **Acción:** Migrar a `RegistryEvent.Register<T>` en handlers dedicados.

### C4. Client-only code en paquetes comunes
- Algunas clases en paquetes no-client referencian `org.lwjgl`, `net.minecraft.client.*`, etc. sin `@SideOnly(Side.CLIENT)`.
- Esto puede causar `NoClassDefFoundError` en servidor dedicado si alguna clase se carga por reflexión.
- **Acción:** Auditar imports en entity/, api/, event/ y agregar @SideOnly donde corresponda.

### C5. Construct's Armory — verificación runtime

- **Estado:** ✅ CÓDIGO ROBUSTO, pendiente verificación en juego.
- Se agregó compatibilidad con Construct's Armory (`conarm`) en `TConstructCompat.java`.
- **Materiales de armadura registrados:** dragonbone, fire_dragonsteel, ice_dragonsteel, lightning_dragonsteel.
- Se usa `Class.forName` (reflection). Si `conarm` no está instalado, no se ejecuta el código.
- **Stats finales ajustados:**
  - Dragonbone: `CoreStats(dur=20F, def=16F)`, `PlatesStats(mod=1.5F, tough=0.5F)`, `TrimStats(extra=5F)`
  - Dragonsteel (fuego/hielo/rayo): `CoreStats(35F, 22F)`, `PlatesStats(3F, 2.5F)`, `TrimStats(8F)`
  - Dragonsteel ≈ Tinker Manyullyn tier, dragonbone ≈ entre Iron y Manyullyn
- **Reflection robusto (hecho 15 Jun 2026):**
  1. `newCoreStats()` — prueba `(float,float)` luego `(float)`
  2. `newPlatesStats()` — prueba `(float,float)` luego `(float)`
  3. `newTrimStats()` — prueba `(float)` luego `()`
  4. `findAddArmorStats()` — prueba 4 params luego 2 params
  5. Logging detallado: cada fallo muestra `NombreExcepcion: mensaje`
  6. Null-check en `getMaterial()`: si el material no existe aún, saltea con warning
- **Qué falta verificar manualmente:**
  1. ~~Firmas de Core/Plates/Trim stats~~ → El código prueba múltiples firmas automáticamente.
  2. ~~TinkerRegistry.getMaterial~~ → Confirmado que existe en Tinker 1.12.2, con null-check.
  3. ~~ArmoryRegistry.addArmorMaterialStats~~ → El código prueba 2 variantes del método.
  4. Ajustar stats si el balance en juego no convence.
  5. Si se quiere compilar con el jar, verificar el file ID exacto en CurseForge (proyecto 287683). `conarm_version=287683:3174536` en gradle.properties es un placeholder aproximado. No se pudo verificar vía web (CurseForge requiere API key). Confirmar manualmente entrando a https://www.curseforge.com/minecraft/mc-mods/constructs-armory/files y buscando la versión 1.12.2 más reciente.
- **Archivos modificados:** `TConstructCompat.java`, `CompatLoadUtil.java`, `gradle.properties`.

### C6. Compatibilidad con otros mods no verificada
- Si un mod externo referencia clases por nombre (reflection), el cambio de namespace rompe la integración.
- **Acción:** Probar al menos JEI, HWYLA, y Baubles (los más comunes en RLCraft).

### C7. Assets faltantes — texturas y modelos JSON (dragonsteel)

- **Encontrado:** 15 Jun 2026 durante `runClient`.
- **12 texturas PNG faltantes:**
  - `fire_dragonsteel_{helmet,chestplate,leggings,boots}.png`
  - `ice_dragonsteel_{helmet,chestplate,leggings,boots}.png`
  - `lightning_dragonsteel_{helmet,chestplate,leggings,boots}.png`
- **~31 modelos JSON faltantes:**
  - `models/item/{fire,ice,lightning}_dragonsteel_{ingot,block,hoe,pickaxe,axe,shovel,sword}.json`
  - `models/item/dragonarmor_{fire,ice,lightning}_dragonsteel_{body,head,legs,boots}.json`
  - `blockstates/{fire,ice,lightning}_dragonsteel_block.json`
- **Efecto:** Items se ven rosa-negro (missing texture). No crashea.
- **Causa:** Items registrados en `IafItemRegistry` sin sus assets correspondientes en `src/main/resources/`.
- **Acción:** Generar JSONs de modelo genéricos (`item/generated`) para cada item faltante.

---

## 🟡 Importantes (afectan la experiencia)

### I1. `EntityDragonBase.onLivingUpdate()` — ~300 líneas monolíticas
- No se aplicó la división en métodos con nombre. Sigue siendo un bloque gigante difícil de mantener.
- **Acción:** Extraer a `updateServerState()`, `updateWalkCycle()`, `updateFlightCycle()`, `updatePlayerControlledMotion()`, `updateAgingAndHunger()`, etc.

### I2. `EntityDragonBase.processInteract()` — ~220 líneas, 11 branches
- If-else anidado para cada tipo de interacción (comida, poción, cuerno, bastón, collar, huevo...).
- **Acción:** Extraer cada branch a su propio método (`handleFeed()`, `handleDragonHorn()`, etc.).

### I3. `EntityEffectHandler` — if-else gigante
- Mismo patrón if-else duplicado en `EntityEffectHandler` y `EntityEffectClientHandler` para cada efecto (Charmed, Frozen, Blazed...).
- Agregar un nuevo efecto requiere tocar ambos handlers en ~5 lugares.
- **Acción:** Strategy pattern con `IEffectBehavior` — ya está en el plan (Fase 4).

### I4. `IafItemRegistry` — ~1200 líneas
- Mezcla registro de items, crafting recipes, smelting recipes, y dragonforge recipes.
- **Acción:** Separar en `ToolRegistry`, `ArmorRegistry`, `MiscItemRegistry`.

### I5. `ParticleLightning.java` — 8 constructores
- Usa `boolean isProjectile` como flag. Debería ser un enum `LightningType { PROJECTILE, BOLT }`.
- **Acción:** Refactorizar constructores (mantener compatibilidad con @Deprecated).

### I6. Items de sangre de dragón duplicados
- `ItemDragonBlood` para fuego, hielo, y rayo comparten misma estructura pero son clases separadas.
- **Acción:** Unificar con `DragonBloodType` enum.

### I7. Strings hardcodeados en lang
- El archivo `pt_BR.lang` línea 463 tiene `entity.hippogryph.alex=Hipogrifo do AlexThe666` — referencia残余 al autor original.
- Varios mensajes de debug/log tienen texto hardcodeado en inglés en el código Java en vez de usar `I18n`.
- **Acción:** Mover todo texto visible al usuario a archivos .lang; limpiar referencia a "Alex" en pt_BR.

---

## 🟢 Mejoras (calidad de vida y mantenibilidad)

### M1. Magic numbers sin nombre
- `RenderShivaxiFire.java` — floats sin nombre (`0.35F`, `0.03125F`, `7`).
- `EntityDragonBase.java` — `0.5F`, `20.0F`, `5.0F`, `1.5F`, `40`, `58` dispersos sin constante.
- **Acción:** Crear constantes `MAX_TWEEN_PROGRESS`, `DRAGON_STEP_HEIGHT`, etc.

### M2. Condicionales booleanos complejos sin nombre
```java
// Actual:
if (!isTackling() && this.isFlying() || !this.onGround && !this.isHovering() && this.airTarget != null)

// Debería ser:
boolean canNavigateAir = !isTackling() && this.isFlying() || !this.onGround && !this.isHovering() && this.airTarget != null;
if (canNavigateAir)
```

### M3. Bloques con booleanos en vez de enums
- `BlockMyrmexResin`, `BlockMyrmexCocoon`, `BlockMyrmexBiolight` usan `boolean isJungle`.
- Debería ser `enum MyrmexHiveType { JUNGLE, DESERT }`.
- **Acción:** Refactorizar — rompe compatibilidad con mundos existentes (requiere migración de NBT).

### M4. `IafBlockRegistry` — mezcla bloques, tile entities, y beacon properties
- **Acción:** Separar en 3 archivos: `BlockRegistry`, `TileEntityRegistry`, `BeaconRegistry`.

### M5. Containers con lógica de slots repetida
- `ContainerDragon`, `ContainerHippocampus`, `ContainerHippogryph` comparten addSlotToContainer.
- **Acción:** Crear `ContainerBase` con métodos helper.

### M6. GUIs con código repetido
- `GuiMyrmexAddRoom` y `GuiMyrmexStaff` comparten texturas y lógica de fondo.
- **Acción:** Crear `GuiMyrmexBase`.
- `GuiBestiary.java` (~1230 líneas) con switch de ~880 líneas en `drawPerPage()`.
- **Acción:** Extraer a `BestiaryPageRenderer`.

### M7. Mensajes de red con boilerplate repetido
- Cada mensaje implementa `IMessage` con `fromBytes()`, `toBytes()`, y handler interno.
- **Acción:** Crear `AbstractMessage<T>` con handlers genéricos.

### M8. Código comentado y dead code
- Varios archivos tienen código comentado con `//` que debería eliminarse o convertirse en issues de GitHub.
- `build_output.txt` está stale (errores de una versión anterior del código).
- **Acción:** Eliminar dead code y archivos de build viejos.

---

## Infraestructura del Proyecto

| Aspecto | Estado | Acción |
|---------|--------|--------|
| **Git** | ❌ No es repo git | `git init` + `.gitignore` (ya existe) + commit inicial |
| **Licencia** | ✅ LGPL v3 (creada 15 Jun 2026) | — |
| **CI/CD** | ❌ No existe | GitHub Actions o similar para compilar en cada push |
| **Issues/Bugs** | ❌ Sin tracker | Usar GitHub Issues vinculado al repo |
| **Changelog** | ❌ No existe | Crear CHANGELOG.md |
| **Créditos** | ⚠️ `gradle.properties` lista a Alexthe666 como autor | Agregar a los nuevos contribuidores, mantener crédito original |
| **README** | ✅ Existe pero desactualizado | Actualizar con estado real (build passing, lista de features refactorizados) |
| **Docs técnicas** | ⚠️ Parcial (PLAN, RECOMENDACIONES, REPORTES) | Consolidar en un solo documento de arquitectura |

---

## Plan de Refactorización Pendiente

Del plan original (~17-22 días, 203 archivos), queda **~65% pendiente**:

| Fase | Módulo | Estado | Prioridad |
|------|--------|--------|-----------|
| 0 | Errores compilación | ✅ | — |
| 1 | Entidades — God classes (helpers) | ⏳ Parcial | 🟠 Alta |
| 2 | Items — consolidación | ⏳ Pendiente | 🟡 Media |
| 3 | Bloques — consolidación | ⏳ Pendiente | 🟡 Media |
| 4 | Eventos | ✅ Completo | — |
| 5 | Proxy | ✅ Completo | — |
| 6 | Clase principal + Config | ✅ Parcial | 🟢 Baja |
| 7 | Render/Cliente | ✅ Completo | — |
| 8 | API / Capacidades — Strategy | ⏳ Pendiente | 🟡 Media |
| 9 | Mundo / Estructuras | ⏳ Pendiente | 🟠 Alta |
| 10 | Integraciones — verificación | ⏳ Pendiente — conarm agregado, falta testear | 🟠 Alta |
| 11 | Red/Mensajes — unificar | ⏳ Pendiente | 🟢 Baja |
| 12 | Utilidades — DragonUtils split | ⏳ Pendiente | 🟢 Baja |
| 13 | Mixins — documentar | ⏳ Pendiente | 🟢 Baja |
| 14 | Enums/Recetas/Containers | ⏳ Pendiente | 🟡 Media |

---

## Deuda Técnica a Largo Plazo

1. **Minecraft 1.12.2 es EOL.** Forge no recibe updates para esta versión. Migrar a 1.16+ o 1.20+ requeriría reescribir casi todo (sistema de registros cambió completamente en 1.13+).
2. **Mixin 0.8.7** — versión vieja. La actual es 0.8.5+. Compatibilidad con Java 21 es frágil.
3. **Shadow plugin 9.3.2** requiere JDK 17+ mientras el código fuente compila para Java 8 — esto es una bomba de tiempo en términos de build tooling.
4. **LLibrary** — dependencia externa que podría dejar de mantenerse. Ya no es común en mods modernos de 1.12.2.
5. **Sin sistema de configuración moderno** — IceAndFireConfig usa el sistema viejo de Forge con `@Config`. Forge 1.13+ usa TOML.

---

## 🆕 Dragones Planeados (futuro)

### Dragón de Veneno
- **Spawn:** Biomas de jungla y pantanos
- **Ataque:** Aliento tóxico — Veneno II
- **Comportamiento:** Territorial. Enemigo de dragones eléctricos
- **Drop:** Escamas verdes → pociones y armas envenenadas
- **Implementación:** Nuevo `EnumDragonType.POISON`, `EntityPoisonDragon`, el tipo se agrega al switch de `DragonExplosion`

### Dragón de Wither
- **Spawn:** Nether, cerca de fortalezas
- **Ataque:** Aliento oscuro — Wither II
- **Comportamiento:** El más fuerte. Hostil a todo excepto dragones esqueleto/wither
- **Drop:** Escamas negras → armas con efecto Wither
- **Implementación:** `EnumDragonType.WITHER`, `EntityWitherDragon`, switch en `DragonExplosion`

### Dragones Esqueleto (4 variantes)
- **Mecánica:** Fingen estar muertos hasta que el jugador se acerca
- **Desierto/Badlands:** Mordida física. Climas cálidos
- **Wither:** Golpes con Wither I. Cerca de fortalezas del Nether
- **Pantanoso:** Golpes con Veneno I. Semienterrado en barro
- **Ártico:** Golpes con Congelamiento + Lentitud II. Cubierto de escarcha
- **Implementación:** `EntitySkeletonDragon` base con `EnumSkeletonType`

### Dragón de Agua
- **Spawn:** Océanos y ríos grandes
- **Ataque:** Chorro de agua + mordida. Emboscada desde abajo
- **Drop:** Escamas azules → armaduras con respiración acuática
- **Implementación:** `EnumDragonType.WATER`, `EntityWaterDragon`

### Dragón de Tierra
- **Spawn:** Montañas y cuevas profundas
- **Ataque:** Aliento de polvo — Ceguera I. Excava túneles
- **Drop:** Escamas marrones → armaduras con resistencia extra
- **Implementación:** `EnumDragonType.EARTH`, `EntityEarthDragon`

### Dragón de Viento
- **Spawn:** Altas montañas y biomas elevados
- **Ataque:** Ráfagas de viento — empuje + daño por caída
- **Habilidad:** Vuelo x1.5 más rápido que otros dragones
- **Drop:** Escamas plateadas → armas con knockback mejorado
- **Implementación:** `EnumDragonType.WIND`, `EntityWindDragon`

> **Ventaja de la consolidación:** `DragonExplosion`, `EntityDragonBreathProjectile` y `EntityDragonChargeProjectile` ya usan `EnumDragonType` con switch. Agregar un tipo nuevo solo requiere agregar el caso al switch — no crear explosiones ni proyectiles nuevos.

---

## Opinión Sincera

**El proyecto está en un estado "compila pero no sé si funciona".**

Lo bueno:
- La migración de namespace se hizo bien. Cero referencias residuales.
- Las consolidaciones (DragonExplosion, BreathProjectile, ChargeProjectile) están bien diseñadas — usan `EnumDragonType` correctamente como switch en vez de herencia.
- La extracción de layers (21 archivos) eliminó todas las inner classes de los renderers. Código más limpio y fácil de mantener.
- Los comentarios Javadoc agregados cubren el 100% de las clases. No es documentación profunda, pero da contexto rápido.

Lo malo:
- El proyecto se refactorizó "a ciegas" — compila, pero nadie ha jugado con él. Esto es peligroso porque bugs de runtime (modelos que no renderizan, entidades que no spawnean, drops rotos) solo se detectan jugando.
- `EntityDragonBase` sigue siendo una god class de 3082 líneas. Es el corazón del mod y la parte más frágil.
- Falta infraestructura básica: git, tests, CI/CD.
- El plan de refactorización original era ambicioso (17-22 días) pero se completó solo ~35%. El 65% restante incluye las partes más riesgosas (generación de mundo, integraciones).

**Recomendación prioritaria:**

1. **Antes de seguir refactorizando: PROBAR EL MOD.** Ejecutar `runClient`, spawnear entidades, verificar que todo funciona visual y mecánicamente.
2. **Si hay bugs de runtime, arreglarlos primero.** La refactorización sin testing es peligrosa.
3. **Inicializar git.** Sin control de versiones, cualquier error es irreversible.
4. **Completar Fase 1** (EntityDragonBase helpers) — es la clase que más se toca y la más propensa a bugs.
5. **Luego seguir con Fases 9 (mundo) y 10 (integraciones)** — son las más riesgosas para el jugador final.

---

*Documento generado como parte de la auditoría del código base.*
*Próxima revisión recomendada: después de la primera prueba de juego completa.*
