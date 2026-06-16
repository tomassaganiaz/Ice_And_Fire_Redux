# Plan de Refactorización – Ice and Fire RLCraft Redux

> **Objetivo:** Refactorizar entre el 95% y 100% del código Java, manteniendo los archivos `.json`, `.tabula` y assets sin cambios salvo que sea estrictamente necesario para la correcta integración.
>
> **Versión del plan:** Junio 2026
> **Estado general:** ~35% completado (Fases 0, 1, 4, 5, 6, y 7)

---

## 📑 Tabla de Contenido

- [🎯 Objetivos Principales](#-objetivos-principales)
- [🔴 Fase 0: Errores de Compilación](#-fase-0-errores-de-compilación)
- [🔄 Orden sugerido para estas carpetas](#-orden-sugerido-para-estas-carpetas)
- [📂 Fase 1: Entidades (entity/)](#-fase-1-entidades-entity)
- [📂 Fase 2: Items (item/)](#-fase-2-items-item)
- [📂 Fase 3: Bloques (block/)](#-fase-3-bloques-block)
- [📂 Fase 4: Eventos (event/)](#-fase-4-eventos-event)
- [📂 Fase 5: Proxy (proxy/)](#-fase-5-proxy-proxy)
- [📂 Fase 6: Clase Principal y Config](#-fase-6-clase-principal-y-config)
- [📂 Fase 7: Render / Cliente (client/)](#-fase-7-render--cliente-client)
- [📂 Fase 8: API / Capacidades (api/ y capability/)](#-fase-8-api--capacidades-api-y-capability)
- [📂 Fase 9: Mundo (world/ y structures/)](#-fase-9-mundo-world-y-structures)
- [📂 Fase 10: Integraciones (integration/)](#-fase-10-integraciones-integration)
- [📂 Fase 11: Red y Mensajes (message/)](#-fase-11-red-y-mensajes-message)
- [📂 Fase 12: Utilidades y Miscelánea (util/, misc/, loot/)](#-fase-12-utilidades-y-miscelánea-util-misc-loot)
- [📂 Fase 13: Mixins (mixin/)](#-fase-13-mixins-mixin)
- [📂 Fase 14: Enums, Recetas, Sonidos, Pociones](#-fase-14-enums-recetas-sonidos-pociones)
- [🛠️ Buenas Prácticas a Aplicar](#️-buenas-prácticas-a-aplicar)
- [📦 Documentación](#-documentación)
- [📊 Resumen de Esfuerzo](#-resumen-de-esfuerzo)
- [📈 Historial de Avance](#-historial-de-avance)

---

## 🎯 Objetivos Principales

1. **Eliminar clases dios** y dependencias circulares.
2. **Reducir código repetido** mediante abstracciones y utilidades.
3. **Implementar POO y principios SOLID** (responsabilidad única, interfaces claras, inyección de dependencias).
4. **Mejorar la programación orientada a eventos** (listeners, suscripción/desuscripción limpia).
5. **Añadir comentarios** solo en partes complejas (algoritmos, integración con Forge/LLibrary).
6. **Documentar** con un README técnico cada módulo refactorizado.

---

## 🔴 Fase 0: Errores de Compilación

> **Antes de cualquier refactor, el proyecto debe compilar.**
> *Estado: ✅ COMPLETADO*

### 0.1 — `IEntityOwnable` no existe
- **Archivos:** `entity/util/DragonUtils.java` y `api/LightningSource.java`
- **Problema:** Importan `com.github.alexthe666.iceandfire.entity.util.IEntityOwnable` pero el archivo no existe.
- **Solución:** Reemplazar con `import net.minecraft.entity.IEntityOwnable;` (interfaz vanilla).

### 0.2 — `EnumParticle` en paquete incorrecto
- **Archivos:** `proxy/CommonProxy.java`
- **Problema:** Importan `com.github.alexthe666.iceandfire.core.EnumParticle`, clase movida a `com.github.alexthe666.iceandfire.enums`.
- **Solución:** Cambiar import a `com.github.alexthe666.iceandfire.enums.EnumParticle`.

### 0.3 — `RenderShivaxiFire.java` no encontrado
- Si el archivo falta, verificar que esté en `client/render/entity/effect/`.

---

## 🔄 Orden sugerido para estas carpetas

Basado en dependencias y visibilidad del cambio:

| Orden | Carpeta | Motivo |
|-------|---------|--------|
| **1º** | `ai/` (entity/ai/) | Impacta directamente en el comportamiento de las entidades (entity/). Refactorizar aquí primero evita re-trabajo. |
| **2º** | `registry/` y `compat/` | Centralizar registros (`IafBlockRegistry`, `IafItemRegistry`, `IafEntityRegistry`) y compatibilidades limpias antes de tocar la lógica de negocio. |
| **3º** | `data/` (si existe como lógica Java) | Solo si hay generadores de datos que producen assets/JSON. En este mod los datos son principalmente JSON estáticos. |
| **4º** | `client/gui/` | Optimización visual y modularización de GUIs. Depende de que las entidades y containers estén estables. |

---

## 📂 Fase 1: Entidades (entity/)

> Contiene las entidades principales.
> **Problema:** Clases demasiado grandes, lógica mezclada (IA, render, datos).
> **Acción:** Separar en subclases de comportamiento, aplicar Strategy para IA.
> *Estado: ✅ COMPLETADO (grandes entidades refactorizadas)*

### Subcarpetas dentro de entity/

| Carpeta | Propósito |
|---------|-----------|
| `entity/` | Clases de entidad principales |
| `entity/ai/` | Tareas de IA (tareas individuales `EntityAI*`) |
| `entity/behavior/` | Comportamientos complejos (nuevo, para entidades grandes) |
| `entity/explosion/` | Lógica de explosiones personalizadas |
| `entity/projectile/` | Proyectiles (flechas, bolas de fuego) |
| `entity/tile/` | Tile entities asociadas a entidades |
| `entity/util/` | Utilidades de entidad |

### 1.1 — `EntityDragonBase.java` (~2600 líneas) 🥇

**Problema:** Una sola clase maneja movimiento, estado, animación, combate, vuelo, física, tweens, navegación aérea, control de jugador, aging, hambre, ataque, fire breathing, interacciones con items (~11 tipos), persistencia NBT.

**Paso a paso:**

1. **Agrupar dentro de la misma clase** — Extraer cada responsabilidad a un método privado con nombre:
   - `updateBaseMovement()` — step height, beyond-height
   - `updateServerState()` — sitting, stuck, tailwhack
   - `updateWalkCycle()` — walk cycle, wing blast
   - `updateServerCombat()` — tackling, flying, hovering, stoned
   - `updateMotionAndAnimation()` — motion clamp, animation handler, leg solver
   - `updateFlightCycle()` — flight cycle animation
   - `processTweens()` — 8 tweenProgress calls
   - `updateServerFlight()` — flight navigation
   - `updatePlayerControlledMotion()` — player-controlled Y motion
   - `updateAgingAndHunger()` — aging, hunger, caffeine
   - `updateServerAttackDecision()` — attack decision, break block
   - `updateFireBreathing()` — fire ticks
   - `processInteract()` → `handleDeathInteract()`, `handleOwnerItemInteract()`, `handleSummoningCrystal()`, `handleFeed()`, `handleDragonMeal()`, `handleCoffeePotion()`, `handleSicklyDragonMeal()`, `handleDragonStick()`, `handleDragonHorn()`, `handleOwnerEmptyHand()`, `handleNonOwnerInteract()`, `handleSpawnEgg()`

2. **Extraer a helpers** — Si un grupo supera ~100 líneas, mover a clase helper:
   - `DragonFlightHelper.java` — toda la lógica de vuelo y navegación aérea
   - `DragonCombatHelper.java` — lógica de ataque, tackling, fire breathing
   - `DragonInteractHelper.java` — proceso de interacción con items

3. **Resultado:** `EntityDragonBase` pasa de ~2600 a ~400 líneas (solo datos y delegación).

### 1.2 — `EntitySeaSerpent.java` (~1150 líneas) 🥈

**Problema:** Mezcla IA, animación, combate, interacción, movimiento acuático.

**Pasos:**
1. Extraer `updateJump()` — lógica de salto acuático
2. Extraer `updateCombat()` — ataque, targeting
3. Extraer `updateAnimation()` — animaciones de la serpiente
4. Extraer `updateMovement()` — natación, rotación

### 1.3 — `EntityHippogryph.java` (~1080 líneas) 🥈

**Problema:** Montura voladora con inventario, vuelo, combate, reproducción.

**Pasos:**
1. Extraer `updateFlight()` — lógica de vuelo
2. Extraer `updateInventory()` — equipamiento (saddle, bridle, chest, armor)
3. Extraer `updateCombat()` — ataque aéreo
4. Extraer `updateBreeding()` — reproducción

### 1.4 — `EntityMyrmexQueen.java` (~1250 líneas) 🥈

**Problema:** Reina con lógica de colonia, huevos, minería, combate, interacción.

**Pasos:**
1. Extraer `updateColony()` — lógica de colonia
2. Extraer `updateMining()` — minería/construcción
3. Extraer `updateEggLaying()` — huevos
4. Extraer `updateCombat()` — combate

### 1.5 — Entidades restantes (ya no requieren refactor mayor)

| Entidad | Líneas actuales | Estado |
|---------|----------------|--------|
| `EntityDeathWorm.java` | **850** (era 932) | ✅ Refactorizado (onLivingUpdate→3, onUpdate→4 helpers) |
| `EntityHippocampus.java` | **786** (era 854) | ✅ Refactorizado (onLivingUpdate→7, travel→5, processInteract→5, readNBT simplificado, +11 @Override) |
| `EntityMyrmexBase.java` | **766** (era 844) | ✅ Refactorizado (onUpdate→6, useRecipe→3, processInteract→3, onStaffInteract→1, +14 @Override) |
| `EntityCockatrice.java` | **672** (era 711) | ✅ Refactorizado (onLivingUpdate 156-línea→13 helpers, +4 @Override) |
| `EntityMyrmexSoldier.java` | ~500 | Baja prioridad |
| `EntityMyrmexWorker.java` | ~500 | Baja prioridad |
| `EntityMyrmexSentinel.java` | ~500 | Baja prioridad |
| `EntitySiren.java` | ~570 | Baja prioridad |
| `EntityPixie.java` | ~480 | Baja prioridad |
| `EntityHydra.java` | ~450 | Baja prioridad |
| `EntityCyclops.java` | ~450 | Baja prioridad |
| `EntityGorgon.java` | ~350 | Muy baja |

---

## 📂 Fase 2: Items (item/)

> Ítems con lógica repetida.
> *Estado: ⏳ PENDIENTE*

| Subcarpeta | Propósito |
|------------|-----------|
| `item/` | Clases de items principales |
| `item/block/` | ItemBlocks (wrapper de bloques como items) |

### 2.1 — Items de sangre de dragón
- `ItemDragonBlood` — sangre de fuego, hielo, rayo comparten misma estructura
- **Acción:** Crear `ItemDragonBloodBase` con tipo enum `DragonBloodType { FIRE, ICE, LIGHTNING }`

### 2.2 — Armaduras de dragón
- `ItemDragonArmor` — múltiples constructores para fuego/hielo/rayo
- **Acción:** Unificar con enum `DragonArmorType`, eliminar constructores duplicados

### 2.3 — Armas
- `ItemDragonSword`, `ItemDragonAxe`, etc. — lógica de daño elemental repetida
- **Acción:** Extraer lógica de daño elemental a un utility o interfaz

### 2.4 — Registro centralizado
- `IafItemRegistry.java` (~1200 líneas) — registro de items con lógica mezclada
- **Acción:** Dividir por categoría: `ToolRegistry`, `ArmorRegistry`, `MiscItemRegistry`

---

## 📂 Fase 3: Bloques (block/)

> Bloques con lógica duplicada.
> *Estado: ⏳ PENDIENTE*

### 3.1 — `BlockGeneric.java`
- Mezcla lógica de hielo de dragón con sistema de beacon dinámico
- **Acción:** Extraer lógica de hielo a subclase o componente separado

### 3.2 — `IafBlockRegistry.java`
- Registro gigante con tile entities y beacon properties mezclados
- **Acción:** Separar en `BlockRegistry`, `TileEntityRegistry`, `BeaconRegistry`

### 3.3 — Bloques Myrmex
- `BlockMyrmexResin`, `BlockMyrmexCocoon`, `BlockMyrmexBiolight`, `BlockMyrmexConnectedResin`
- Usan `boolean isJungle` para variantes
- **Acción:** Reemplazar con enum `MyrmexHiveType { JUNGLE, DESERT }`

### 3.4 — Bloques con slipperiness
- `BlockDragonIce` tiene lógica de slipperiness dinámico
- **Acción:** Extraer a `SlipperinessType` enum con valores NORMAL(0.6F) y SLIPPERY(0.98F)

### 3.5 — Bloques revertibles
- `BlockReturningState`, `BlockFallingReturningState` comparten lógica de revert
- **Acción:** Crear interfaz `RevertibleBlock` con método `revertToOriginal()`

### 3.6 — Bloques decorativos
- `BlockDreadStoneFace`, `BlockDreadSlab`, `BlockDreadStairs`, `BlockDreadWoodLock`
- Lógica repetida de facing/hinge
- **Acción:** Unificar bajo `BlockDreadBase` con enum `DreadVariant`

---

## 📂 Fase 4: Eventos (event/)

> Eventos del mod.
> *Estado: ✅ COMPLETADO (EventLiving dividido en 4 handlers)*

### 4.1 — `EventLiving.java` ✅ REFACTORIZADO
- **Antes:** ~892 líneas, 25 handlers mezclados (sangre, calderos, drops, efectos, monturas, alarmas, AI injection)
- **Después:** Dividido en 4 handler classes:
  - `DragonBloodHandler.java` — sistema de sangre/calderos/efectos
  - `DragonRidingHandler.java` — montura/desmontaje/uso de ítems montado/flechas
  - `DragonEntityHandler.java` — alarmas (chicken/villager/sheep), AI injection, drops, efectos de entidad
  - `DragonBlockHandler.java` — interacción con bloques, loot tables, cofres fantasma, spawners
  - `EventLiving.java` — mantenido como facade estática (~50 líneas) para compatibilidad (updateRotation, isAnimaniaSheep, isQuarkCrab, onSwingGhostSword)

### 4.2 — `EventWorld.java`
- **Problema:** No está registrado en el bus de eventos
- **Acción:** Verificar que tenga `@Mod.EventBusSubscriber(modid = IceAndFire.MODID)`

### 4.3 — `EventClient.java` y `EventNewMenu.java`
- Revisar que no mezclen lógica de server
- Separar handlers de render de handlers de gameplay

### 4.4 — Suscripción/desuscripción
- Revisar que todos los `@SubscribeEvent` tengan el `modid` correcto en `@Mod.EventBusSubscriber`
- No mezclar lógica de render con lógica de evento

---

## 📂 Fase 5: Proxy (raíz/)

> Proxies del mod para lógica cliente/servidor.
> *Estado: ✅ COMPLETADO*

### Archivos

| Archivo | Líneas | Estado |
|---------|--------|--------|
| `ClientProxy.java` (raíz) | **479** (era 594) | ✅ Refactorizado — 28 armor fields→array, getArmorModel()→array lookup, registerModels()→3 helpers, renderEntities()→EntityRenderRegistry, state→ClientStateManager |
| `CommonProxy.java` (raíz) | **287** (era 333) | ✅ Refactorizado — registerEntities()→7 grupos, registerItems()→registerEnumItems() |
| `ServerProxy.java` | Minimalista | ✅ Sin cambios |
| ~~proxy/ subpackage~~ | Eliminado | ✅ 4 archivos huérfanos removidos |

### 5.1 — `ClientProxy.java` ✅ REFACTORIZADO
- **Antes:** 594 líneas mezclando renders, state, modelos, coloración
- **Después:** 479 líneas, delega a `EntityRenderRegistry`, `ClientStateManager`
- **Cambios:** 28 armor model fields→array `ARMOR_MODELS[28]`; `getArmorModel()` switch 28→1 línea; `registerModels()`→3 helpers

### 5.2 — `CommonProxy.java` ✅ REFACTORIZADO
- **Antes:** 333 líneas con registros monolíticos
- **Después:** 287 líneas, `registerEntities()` en 7 grupos lógicos, `registerItems()` con `registerEnumItems()`

### 5.3 — `proxy/ subpackage`
- **Eliminado** porque @SidedProxy apunta directamente a raíz (no subpackage)
- Archivos eliminados: `ClientProxy.java`, `CommonProxy.java`, `ServerProxy.java`, `RegistryHandler.java`

---

## 📂 Fase 6: Clase Principal y Config (raíz/)

> Archivos en la raíz del paquete `com.github.Redux.iceandfire`.
> *Estado: ✅ PARCIAL (IceAndFire.java + ConfigCacheHelper refactorizados)*

### 6.1 — `IceAndFire.java` ✅ REFACTORIZADO (parcial)

| Cambio | Antes | Después |
|--------|-------|---------|
| DamageSources | 7 clases anónimas (~50 líneas) | Factory method `createDamageSource()` (11 líneas) |
| Líneas totales | **188** | **157** |

**Pendiente:** Extraer constantes a `IceAndFireConstants.java` y config a `ConfigManager.java`.

### 6.2 — `IceAndFireConfig.java` ✅ REFACTORIZADO (parcial)

| Cambio | Antes | Después |
|--------|-------|---------|
| Lazy fields | 22 campos null-check manual | `LazyCache<T>` inner class |
| Líneas totales | ~1257 | **~1086** |

**Nota:** No se divide en clases separadas porque Forge 1.12.2 `@Config` requiere inner classes estáticas para mantener rutas de archivo .cfg existentes. `ConfigCacheHelper` extraído con `LazyCache<T>`.

### 6.3 — `IceAndFirePlugin.java` (~45 líneas)
- ✅ Sin cambios (solo Mixin init, minimalista)

---

## 📂 Fase 7: Render / Cliente (client/)

> Renderizadores, modelos y lógica visual.
> *Estado: ✅ COMPLETADO (layers, base classes, particles)*

### Subcarpetas

| Carpeta | Propósito |
|---------|-----------|
| `client/gui/` | Interfaces de usuario (bestiario, containers) |
| `client/model/` | Modelos 3D de entidades |
| `client/particle/` | Partículas personalizadas |
| `client/render/` | Renderizadores de entidades, tile entities, capas |
| `client/texture/` | Texturas dinámicas (layered textures) |

### 7.1 — Layers (capas de render) ✅ COMPLETADO

**21 layers extraídos — 0 inner classes restantes** verificados en 44 archivos Render*.java.

| Layer | Render Padre | Archivo Extraído | Estado |
|-------|-------------|------------------|--------|
| LayerDragonEyes | RenderDragonBase | `layer/LayerDragonEyes.java` | ✅ |
| LayerDragonRider | RenderDragonBase | `layer/LayerDragonRider.java` | ✅ |
| LayerDragonArmor | RenderDragonBase | `layer/LayerDragonArmor.java` | ✅ |
| LayerTrollWeapon | RenderTroll | `layer/LayerTrollWeapon.java` | ✅ |
| LayerTrollEyes | RenderTroll | `layer/LayerTrollEyes.java` | ✅ |
| LayerTrollStone | RenderTroll | `layer/LayerTrollStone.java` | ✅ |
| LayerHippocampusSaddle | RenderHippocampus | `layer/LayerHippocampusSaddle.java` | ✅ |
| LayerHippocampusRainbow | RenderHippocampus | `layer/LayerHippocampusRainbow.java` | ✅ |
| LayerHippocampusBridle | RenderHippocampus | `layer/LayerHippocampusBridle.java` | ✅ |
| LayerHippocampusChest | RenderHippocampus | `layer/LayerHippocampusChest.java` | ✅ |
| LayerHippocampusArmor | RenderHippocampus | `layer/LayerHippocampusArmor.java` | ✅ |
| LayerPixieItem | RenderPixie | `layer/LayerPixieItem.java` | ✅ |
| LayerPixieGlow | RenderPixie | `layer/LayerPixieGlow.java` | ✅ |
| LayerHippogriffSaddle | RenderHippogryph | `layer/LayerHippogriffSaddle.java` | ✅ |
| LayerHippogriffBridle | RenderHippogryph | `layer/LayerHippogriffBridle.java` | ✅ |
| LayerHippogriffChest | RenderHippogryph | `layer/LayerHippogriffChest.java` | ✅ |
| LayerHippogriffArmor | RenderHippogryph | `layer/LayerHippogriffArmor.java` | ✅ |
| LayerGorgonEyes | RenderGorgon | `layer/LayerGorgonEyes.java` | ✅ |
| LayerMyrmexItem | RenderMyrmexBase | `layer/LayerMyrmexItem.java` | ✅ |
| LayerSeaSerpentAncient | RenderSeaSerpent | `layer/LayerSeaSerpentAncient.java` | ✅ |
| LayerStonePlayerCrack | RenderStoneStatue | `layer/LayerStonePlayerEntityCrack.java` | ✅ |

### 7.2 — Arrow/Feather renderers ✅ COMPLETADO
- **Antes:** 5 renders con ~90% de código idéntico (~68 líneas c/u, ~340 total)
- **Después:** `RenderBaseArrow<T>` creado, 5 renders reducidos de ~90 a ~15 líneas c/u (~75 total)
- Renderers: `RenderAmphithereArrow`, `RenderSeaSerpentArrow`, `RenderStymphalianFeather`, `RenderStymphalianArrow`, `RenderHydraArrow`

### 7.3 — Partículas ✅ COMPLETADO
- **ParticleTargetedDragonBreath** creado como base compartida por `ParticleTargetedDragonFlame` y `ParticleTargetedDragonFrost` (~80 líneas duplicadas eliminadas)

**Pendiente:** `ParticleSnowflake`, `ParticleSpark`, `ParticleDragonFrost` — comparten `move()` de ~40 líneas idéntico (baja prioridad)

### 7.4 — GUIs ✅ COMPLETADO
| Clase | Antes | Después |
|-------|-------|---------|
| `GuiHippocampus` | ~80 líneas | **~20** (extends GuiHippogryphBase) |
| `GuiHippogryph` | ~80 líneas | **~20** (extends GuiHippogryphBase) |
| `GuiHippogryphBase` | — | Creada con hooks `isEntityChested()` |
| `ContainerHippocampus` | ~139 líneas | **~17** (extends ContainerHippogryphBase) |
| `ContainerHippogryph` | ~139 líneas | **~17** (extends ContainerHippogryphBase) |
| `ContainerHippogryphBase` | — | Creada con 3 hooks abstractos |

**Pendiente:** `GuiBestiary.java` (~1230 líneas) sigue siendo grande; `GuiMyrmexBase` para GuiMyrmexAddRoom/Staff (baja prioridad)

### 7.5 — Magic numbers en renders
- `RenderShivaxiFire.java` — revisar números mágicos
- **Acción:** Nombrar como constantes descriptivas

---

## 📂 Fase 8: API / Capacidades (api/ y capability/)

> Interfaces y capacidades del mod para interoperabilidad.
> *Estado: ⏳ PENDIENTE*

| Subcarpeta | Propósito |
|------------|-----------|
| `api/` | Interfaces públicas (IEntityEffectCapability, InFCapabilities, ChainLightningAPI) |
| `capability/entityeffect/` | Implementación de capacidades de efectos |

### 8.1 — API existente
- `IEntityEffectCapability.java` — interfaz para efectos de entidad (stoned, frozen, etc.)
- `InFCapabilities.java` — punto de acceso a capacidades
- `ChainLightningAttack.java`, `ChainLightningConfig.java`, `ChainLightningUtils.java` — sistema de rayos
- `FoodProperties.java`, `FoodUtils.java` — propiedades de comida

### 8.2 — Acciones
1. **Documentar** cada interfaz con JavaDoc (propósito, contrato)
2. **Revisar** que las capacidades usen `@CapabilityInject` correctamente
3. **Centralizar** acceso a capacidades en `CapabilityHandler.java`

---

## 📂 Fase 9: Mundo (world/ y structures/)

> Generación de biomas, estructuras y datos de mundo.
> *Estado: ⏳ PENDIENTE*

### Subcarpetas

| Carpeta | Propósito |
|---------|-----------|
| `world/` | Biomas, datos de mundo, generación de granjas |
| `world/village/` | Generación de aldeas |
| `structures/` | Generadores de estructuras (cuevas, mausoleos, colmenas) |

### 9.1 — Generación de estructuras
- `WorldGenDragonRoost`, `WorldGenMyrmexHive`, `WorldGenGorgonTemple`, etc.
- **Problema:** Lógica de ubicación, validación y construcción repetida
- **Acción:** Crear `AbstractWorldGenStructure` con métodos base:
  - `canGenerate(World, BlockPos)`
  - `getGenerationHeight(World, BlockPos)`
  - `buildStructure(World, BlockPos, Random)`

### 9.2 — Spawn de entidades
- Lógica de spawn mezclada en eventos y clases de mundo
- **Acción:** Centralizar en `EntitySpawnManager` con reglas por bioma/dimensión

### 9.3 — Datos de mundo
- `DragonPosWorldData.java` — posiciones de dragones guardadas
- `MyrmexWorldData.java` — datos de colonias
- **Acción:** Unificar bajo `WorldDataManager` con interfaz `IWorldData`

---

## 📂 Fase 10: Integraciones (integration/)

> Compatibilidad con otros mods.
> *Estado: ⏳ PENDIENTE*

### Subcarpetas

| Carpeta | Mod |
|---------|-----|
| `integration/baubles/` | Baubles (accesorios) |
| `integration/claimit/` | ClaimIt (protección de terrenos) |
| `integration/crafttweaker/` | CraftTweaker (scripts personalizados) |
| `integration/firstaid/` | First Aid (sistema de salud por partes) |
| `integration/jei/` | JEI (recetas) |
| `integration/tconstruct/` | Tinkers' Construct |
| `integration/thaumcraft/` | Thaumcraft |
| `integration/theoneprobe/` | The One Probe (Waila) |
| `integration/waila/` | Waila (HUD de bloques) |

### 10.1 — `CompatLoadUtil.java`
- Verifica qué mods están cargados y activa compatibilidades
- **Acción:** Mantener limpio, usar `@Optional` de Forge donde sea posible

### 10.2 — Mods principales
- **JEI:** `IceAndFireJEIRecipeCategories.java` — mostrar recetas de dragonforge
- **Tinkers' Construct:** Materiales y tool parts
- **Spartan Weaponry:** Compatibilidad de armas
- **First Aid:** Efectos en partes del cuerpo

### 10.3 — Acción general
- Usar `@Optional.Method(modid = "...")` para integraciones opcionales
- No cargar clases de mods inexistentes (ClassNotFound → crash)

---

## 📂 Fase 11: Red y Mensajes (message/)

> Paquetes de red.
> *Estado: ⏳ PENDIENTE*

### Archivos
| Archivo | Propósito |
|---------|-----------|
| `MessageChainLightningFX.java` | Efectos de rayo |
| `MessageDragonControl.java` | Control de dragón montado |
| `MessageDragonSyncFire.java` | Sincronización de fuego |
| `MessageEntityEffect.java` | Efectos de entidad |
| `MessageParticleFX.java` | Partículas |
| `MessageSirenSong.java` | Sonido de sirena |
| `MessageSwingGhostSword.java` | Animación de espada fantasma |
| ... (total ~15 mensajes) | |

### 11.1 — Problemas
- **Código repetido:** Cada mensaje implementa `IMessage` con `fromBytes()`, `toBytes()`, handler mezclado
- **Problema:** La mayoría del boilerplate es idéntico (leer/escribir entero, float, UUID)

### 11.2 — Acción
1. **Crear `AbstractMessage<T>`** con handlers genéricos
2. **Crear `MessageHandler.java`** separado para cada mensaje
3. **Unificar lectura/escritura** con utility methods

---

## 📂 Fase 12: Utilidades y Miscelánea (util/, misc/, loot/)

> Utilidades auxiliares.
> *Estado: ⏳ PENDIENTE*

### 12.1 — Reorganización de util/
- `util/math/` — `DragonMathUtils`, `RandomUtils`
- `util/loot/` — `LootTableUtils`, `DropGenerator`
- `util/world/` — `WorldUtils`, `BlockUtils`

### 12.2 — `DragonUtils.java`
- Archivo grande con utilidades mezcladas
- **Acción:** Separar por dominio: `DragonMathUtils`, `DragonFlightUtils`, `DragonBreedUtils`

### 12.3 — Miscelánea
- `misc/CreativeTab.java` — pestaña creativa ✅ (ya está bien)
- `misc/IafSoundRegistry.java` — registro de sonidos

### 12.4 — Loot
- `loot/CustomizeToDragon.java`, `loot/CustomizeToSeaSerpent.java` — modificadores de loot table
- **Acción:** Unificar bajo `AbstractLootModifier` si usan el sistema de Forge

---

## 📂 Fase 13: Mixins (mixin/)

> Mixins para modificar código de Minecraft/base.
> *Estado: ⏳ PENDIENTE*

### Estructura actual
```
mixin/
└── vanilla/
```

### Acciones
1. **Documentar** cada mixin con su propósito y qué parchea
2. **Revisar** que los mixins tengan prioridad correcta (no conflicto con otros mods)
3. **Asegurar** que el `refmap` se genere correctamente

---

## 📂 Fase 14: Enums, Recetas, Sonidos, Pociones

> Archivos de soporte.
> *Estado: ⏳ PENDIENTE*

| Carpeta | Archivos | Acción |
|---------|----------|--------|
| `enums/` | EnumDragonEgg, EnumDragonType, EnumHippogryphTypes, EnumTroll, etc. | Revisar duplicación |
| `recipe/` | DragonForgeRecipe, IafRecipeRegistry | Separar registros |
| `potion/` | BasePotion | ✅ Bien (base limpia) |
| `inventory/` | Containers (Dragon, Hippocampus, Hippogryph, etc.) | Revisar boilerplate |

### 14.1 — Enums
- **Problema:** Algunos enums podrían usar campos adicionales en lugar de switch statements
- **Ejemplo:** `EnumDragonType` tiene `FIRE, ICE, LIGHTNING` — cada uno con propiedades específicas
- **Acción:** Asegurar que cada enum tenga sus propiedades autosuficientes

### 14.2 — Containers
- `ContainerDragon`, `ContainerHippocampus`, `ContainerHippogryph`
- **Problema:** Lógica de slots repetida
- **Acción:** Crear `ContainerBase` con métodos genéricos

### 14.3 — Recetas
- `IafRecipeRegistry.java` (~1000 líneas)
- **Problema:** Registro de recetas mezclado con lógica de crafting
- **Acción:** Separar en `SmeltingRecipes`, `CraftingRecipes`, `DragonForgeRecipes`

---

## 🛠️ Buenas Prácticas a Aplicar

### SOLID

| Principio | Aplicación |
|-----------|-----------|
| **S** — Responsabilidad Única | Cada clase hace una sola cosa. Si tiene más de 400 líneas, dividir. |
| **O** — Abierto/Cerrado | Clases abiertas a extensión, cerradas a modificación. Usar interfaces. |
| **L** — Sustitución de Liskov | Subtipos deben ser reemplazables sin romper lógica. |
| **I** — Segregación de Interfaces | Interfaces pequeñas y específicas. No interfaces gigantes con 20 métodos. |
| **D** — Inversión de Dependencias | Depender de interfaces/abstractos, no de implementaciones concretas. |

### Composición sobre Herencia
- Preferir componentes reutilizables a jerarquías de herencia profundas.
- Ej: `DragonBreathComponent`, `DragonFlightComponent` como campos en lugar de `AbstractDragon -> FireDragon -> IceDragon`.

### Eventos
- No mezclar lógica de render con lógica de evento.
- Desuscribir listeners cuando no sean necesarios.
- Todas las suscripciones deben tener `modid` correcto.

### Código Limpio
- **DRY:** Código repetido 3+ veces → extraer a método.
- **Early Return:** Reducir anidamiento. `if (world.isRemote) return;` al inicio.
- **Constantes con nombre:** No magic numbers. `MAX_TWEEN_PROGRESS = 20.0F` en vez de `20.0F`.
- **Condicionales con nombre:** `boolean canDragonBreathe = fireTicks > 20 && isBreathingFire()`.
- **Switch sobre if-else:** 3+ comparaciones del mismo valor → switch o mapa.

### Patrones Recomendados
| Patrón | Uso |
|--------|-----|
| **Strategy** | IA de dragones, efectos de sangre, comportamiento de ataque |
| **Factory** | Creación de items/bloques por tipo, generación de estructuras |
| **Observer** | Eventos de Forge correctamente implementados |
| **Command** | Paquetes de red |
| **Template Method** | Algoritmos de generación de estructuras |
| **Component** | Comportamientos de entidades (vuelo, combate, respiración) |

---

## 📦 Documentación

### Documentar solo donde importa
- **Sí:** Algoritmos complejos (pathfinding, IA de dragones, generación de estructuras)
- **Sí:** Workarounds de Forge/LLibrary/Geckolib
- **Sí:** Reglas de negocio no obvias ("se resta 1 porque índice 0 = sin efecto")
- **Sí:** JavaDoc en métodos públicos (qué hace, parámetros, retorno)
- **No:** Getters/setters obvios
- **No:** Código auto-explicativo

### README por módulo
Cada módulo refactorizado debe tener un pequeño README explicando:
- Responsabilidad del módulo
- Estructura de archivos
- Dependencias con otros módulos

### Comentarios de code review
```java
// BUG: Este método no maneja el caso edge donde el dragón está muerto
// TODO: Refactorizar cuando se implemente el sistema de resurrect
```

---

## 📊 Resumen de Esfuerzo

| Fase | Módulo | Archivos aprox. | Esfuerzo estimado | Estado |
|------|--------|-----------------|-------------------|--------|
| **0** | Errores compilación | 3 | 10 min | ✅ Completo |
| **1** | Entidades (entity/) | ~20 | 4-5 días | ✅ Completo |
| **2** | Items (item/) | ~15 | 1 día | ⏳ Pendiente |
| **3** | Bloques (block/) | ~20 | 1-2 días | ⏳ Pendiente |
| **4** | Eventos (event/) | ~8 | 1 día | ✅ Completo |
| **5** | Proxy (proxy/) | ~4 | 1 día | ✅ Completo |
| **6** | Clase principal + Config | ~3 | 0.5 días | ✅ Parcial |
| **7** | Render/Cliente (client/) | ~40 | 3-4 días | ✅ Completo |
| **8** | API / Capacidades | ~10 | 0.5 días | ⏳ Pendiente |
| **9** | Mundo (world/ + structures/) | ~20 | 1-2 días | ⏳ Pendiente |
| **10** | Integraciones | ~15 | 1-2 días | ⏳ Pendiente |
| **11** | Red/Mensajes | ~15 | 0.5 días | ⏳ Pendiente |
| **12** | Utilidades/Misc | ~12 | 1 día | ⏳ Pendiente |
| **13** | Mixins | ~3 | 0.5 días | ⏳ Pendiente |
| **14** | Enums/Recetas/Containers | ~15 | 1 día | ⏳ Pendiente |
| | **Total** | **~203 archivos** | **~17-22 días** | **~35% completo** |

---

## 📈 Historial de Avance

| Fecha | Cambio |
|-------|--------|
| Jun 2026 | Plan inicial creado |
| Jun 2026 | **Fase 0:** 3 errores de compilación corregidos |
| Jun 2026 | **Fase 1 (parcial):** EntityDragonBase refactorizado (métodos extraídos, IDragonBehavior), EntitySeaSerpent, EntityHippogryph, EntityMyrmexQueen, EntityAmphithere parcialmente refactorizados |
| Jun 2026 | **Fase 4:** EventLiving.java dividido en 4 handler classes |
| Jun 2026 | **Fase 7 (parcial):** 13 de 19 layer inner classes extraídos a archivos standalone |
| Jun 2026 | **Fase 7 (completado):** 21 layers extraídos (0 inner classes), RenderBaseArrow, ParticleTargetedDragonBreath, GuiHippogryphBase, ContainerHippogryphBase |
| Jun 2026 | **Fase 5+6:** EntityRenderRegistry, ClientStateManager, ConfigCacheHelper + LazyCache, DamageSources factory method |
| Jun 2026 | **Fase 1 (completado):** EntityDeathWorm (932→850), EntityHippocampus (854→786, +11 @Override), EntityMyrmexBase (844→766, +14 @Override), EntityCockatrice (711→672, 156-line→13 helpers, +4 @Override) |
| Jun 2026 | **Fase 5 (completado):** proxy/ subpackage eliminado, ClientProxy (594→479), CommonProxy (333→287) |

---

> *Basado en Ice and Fire original por alexthe666, Affehund, y kotlin-programmer.*
> *Fork Redux por el equipo RLCraft.*
> *Plan de refactorización — Junio 2026.*
