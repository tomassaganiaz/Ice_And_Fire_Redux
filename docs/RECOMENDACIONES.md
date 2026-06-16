# Recomendaciones: Paso a Paso para Mejorar el Código

> **Nota:** Archivos `.json`, `.tabula` y otros assets no se modifican a menos que sea 100% necesario para el funcionamiento.

---

## Fase 0: Corrección de Errores de Compilación

Antes de cualquier refactor, el proyecto debe compilar.

### 0.1 — `IEntityOwnable` no existe
- **Archivo:** `entity/util/DragonUtils.java` y `api/LightningSource.java`
- **Problema:** Importan `com.github.alexthe666.iceandfire.entity.util.IEntityOwnable` pero el archivo no existe.
- **Solución:** Reemplazar con `import net.minecraft.entity.IEntityOwnable;` (interfaz vanilla de Minecraft). La interfaz vanilla tiene el método `getOwner()` que es lo único que se usa.

### 0.2 — `EnumParticle` en paquete incorrecto
- **Archivos:** `proxy/CommonProxy.java` y `CommonProxy.java` (raíz)
- **Problema:** Importan `com.github.alexthe666.iceandfire.core.EnumParticle` pero la clase fue movida a `com.github.alexthe666.iceandfire.enums`.
- **Solución:** Cambiar el import a `com.github.alexthe666.iceandfire.enums.EnumParticle`.

---

## Fase 1: God Classes — Prioridad Máxima

Estas clases son las que más dificultan el mantenimiento. Refactorizar en orden:

### 1.1 — `EntityDragonBase.java` (~2600 líneas)

**Problema:** Una sola clase maneja:
- Movimiento base (step height, límites de altura)
- Estado del servidor (sitting, stuck, love, tailwhack)
- Walk cycle y wing blast
- Combate y vuelo (tackling, hovering, flying)
- Física y animación
- Flight cycle
- Progress tweens (8 animaciones)
- Navegación aérea
- Movimiento controlado por jugador
- Aging y hambre
- Decisiones de ataque
- Fire breathing
- Interacciones con items (~11 tipos diferentes)
- Persistencia (NBT read/write)

**Paso a paso:**

1. **Extraer grupos de métodos dentro de la misma clase**
   - No crear archivos nuevos todavía — solo agrupar lógica relacionada en métodos privados con nombre.
   - Ej: `updateServerState()`, `updateCombat()`, `updateFlightNavigation()`, `handleOwnerItemInteract()`

2. **Luego, evaluar extracción a clases helper**
   - Si un grupo de métodos es grande y autocontenido (ej: toda la lógica de vuelo), mover a una clase helper como `DragonFlightHelper`.
   - La helper recibe la entidad como parámetro: `DragonFlightHelper.update(entity)`.

3. **Romper `processInteract()` (~220 líneas)**
   - Cada tipo de interacción (comida, poción, cuerno, bastón, collar, huevo, etc.) en su propio método.
   - Si hay más de 3 métodos relacionados, agrupar en un helper.

4. **Romper `onLivingUpdate()` (~300 líneas)**
   - Extraer cada fase del update a su propio método.
   - Usar early return para los bloques `if (!world.isRemote)`.

### 1.2 — `EntitySeaSerpent.java` (~1150 líneas)

**Problema:** Similar a EntityDragonBase — mezcla IA, animación, combate, interacción.

**Pasos:**
1. Identificar responsabilidades (jump, animation, AI tasks, combat, interact)
2. Extraer cada una a método privado con nombre
3. Evaluar helpers si algún grupo supera ~100 líneas

### 1.3 — `EntityHippogryph.java` (~1080 líneas)

**Problema:** Montura voladora con lógica de inventario, vuelo, combate, reproducción.

**Pasos:**
1. Separar lógica de vuelo (`updateFlight()`)
2. Separar lógica de inventario/equipamiento
3. Separar lógica de combate/IA

### 1.4 — `EntityMyrmexQueen.java` (~1250 líneas)

**Problema:** Reina de hormigas con lógica de colonia, huevos, minería, combate, interacción.

**Pasos:**
1. Extraer lógica de colonia a métodos separados
2. Extraer lógica de minería/construcción
3. Extraer lógica de reproducción/huevos

---

## Fase 2: Renderers — Separar Capas (Layers)

### 2.1 — `RenderDragonBase.java` (~290 líneas original)

Tiene 3 inner classes: `LayerDragonEyes`, `LayerDragonRider`, `LayerDragonArmor`.

**Opción A (conservadora):** Mantener como inner classes pero extraer cada una a su propio método de factory.
**Opción B (recomendada):** Mover cada Layer a su propio archivo en `client/render/entity/layer/`.

Misma lógica aplica para:
- `RenderTroll.java` — LayerTrollWeapon, LayerTrollEyes, LayerTrollStone
- `RenderHippocampus.java` — LayerHippocampusSaddle, LayerHippocampusRainbow, etc.
- `RenderPixie.java` — LayerPixieItem, LayerPixieGlow
- `RenderHippogryph.java` — LayerHippogryphSaddle, LayerHippogryphBridle, etc.
- `RenderGorgon.java` — LayerGorgonEyes
- `RenderMyrmexBase.java` — LayerMyrmexItem
- `RenderSeaSerpent.java` — LayerSeaSerpentAncient
- `RenderStoneStatue.java` — LayerStonePlayerCrack

### 2.2 — Arrow/Feather Renderers

`RenderAmphithereArrow`, `RenderSeaSerpentArrow`, `RenderStymphalianFeather`, `RenderStymphalianArrow`, `RenderHydraArrow` comparten ~90% del código (`doRender` idéntico).

**Opción recomendada:** Crear un `RenderBaseArrow` genérico y que cada uno herede de él. Alternativa: extraer el `doRender` a un método static utilitario.

---

## Fase 3: Partículas — Reducir Duplicación

### 3.1 — `ParticleTargetedDragonFlame` y `ParticleTargetedDragonFrost`

Comparten ~80% del `onUpdate()` y `move()`.

**Opción:** Crear una base abstracta o un utility method static que ambas llamen.

### 3.2 — `ParticleSnowflake`, `ParticleSpark`, `ParticleDragonFrost`

Comparten el mismo `move()` de ~40 líneas.

**Opción:** Extraer `move()` a un método static utility.

### 3.3 — `ParticleLightning.java` (8 constructores)

Demasiados constructores con `boolean isProjectile`.

**Opción:** Reemplazar `boolean` con un enum `LightningType { PROJECTILE, BOLT }`. Mantener constructores deprecados con boolean para backward compat.

---

## Fase 4: Capability / Efectos — Strategy Pattern

### `EntityEffectHandler` y `EntityEffectClientHandler`

Ambos tienen un if-else gigante idéntico para cada efecto (Charmed, Frozen, Blazed, etc.).

**Solución recomendada:** Strategy pattern.
- Crear interfaz `IEffectBehavior` con método `tick(Entity entity)`.
- Cada efecto implementa la interfaz.
- El handler delega: `behaviors[effectId].tick(this)`.

Esto elimina ~300 líneas de if-else duplicado y hace trivial agregar nuevos efectos.

---

## Fase 5: Blocks — Separar Lógica de Registro

### 5.1 — `BlockGeneric.java`

Mezcla lógica de hielo de dragón con el sistema de beacon dinámico.

**Opción:** Extraer la lógica de hielo a una subclase o componente separado.

### 5.2 — `IafBlockRegistry.java`

Registro gigante con tile entities y beacon properties mezclados.

**Opción:** Separar en `BlockRegistry` (solo blocks), `TileEntityRegistry` (solo tile entities), `BeaconRegistry` (solo beacons).

### 5.3 — Bloques Myrmex

`BlockMyrmexResin`, `BlockMyrmexCocoon`, `BlockMyrmexBiolight`, `BlockMyrmexConnectedResin` usan booleanos para variantes (jungle/desert).

**Opción:** Reemplazar `boolean isJungle` con un enum `MyrmexHiveType { JUNGLE, DESERT }`.

---

## Fase 6: GUIs — Reducir Duplicación

### 6.1 — `GuiHippocampus` y `GuiHippogryph`

~90% del código es idéntico (solo cambia la entidad y el container).

**Opción:** Crear una base `GuiHippogryphBase` con un hook abstracto `isEntityChested()`.

### 6.2 — `GuiMyrmexAddRoom` y `GuiMyrmexStaff`

Comparten texturas, lógica de fondo, y renderizado de nombre de colonia.

**Opción:** Crear `GuiMyrmexBase` con la lógica común.

### 6.3 — `GuiBestiary.java` (~1230 líneas)

El switch `drawPerPage()` ocupa ~880 líneas con coordenadas hardcodeadas.

**Opción:** Extraer `drawPerPage()` a una clase `BestiaryPageRenderer`. El switch es datos (coordenadas), no lógica, y contamina la navegación del bestiario.

---

## Fase 7: Constantes y Magic Numbers

Buscar en todo el proyecto números literales que deberían ser constantes con nombre:

```java
// Mal:
if (flightCycle == 2) playSound(...);  // ¿qué significa 2?

// Bien:
private static final int FLIGHT_CYCLE_SOUND_TICK = 2;
if (flightCycle == FLIGHT_CYCLE_SOUND_TICK) playSound(...);
```

**Priorizar:**
- `RenderShivaxiFire.java` — varios floats sin nombre
- `EntityDragonBase.java` — 0.5F, 20.0F, 5.0F, 1.5F, 40, 58, etc.
- Todos los renderers con escalas hardcodeadas

---

## Fase 8: Documentación

Agregar JavaDoc mínimo en:
- Toda clase pública (qué hace, cómo se usa)
- Todo método público (qué hace, parámetros, retorno)
- Campos públicos (para qué sirven)
- Workarounds conocidos (ej: "Forge 1.12.2 bug: ...")

No documentar lo obvio:
```java
// Mal:
int getHealth() { return health; }  // getter de health

// Bien:
// Nada — el nombre del método ya explica qué hace
```

---

## Resumen del Orden Recomendado

| Fase | Descripción | Archivos afectados | Esfuerzo |
|------|-------------|-------------------|----------|
| 0 | Corregir errores compilación | 3 | 10 min |
| 1 | God classes (entity/) | ~6 | 3-4 días |
| 2 | Layers / Renderers | ~35 | 1-2 días |
| 3 | Partículas duplicadas | ~6 | 1 día |
| 4 | Capability Strategy | ~10 | 1 día |
| 5 | Blocks | ~9 | 1 día |
| 6 | GUIs | ~6 | 1 día |
| 7 | Constantes | ~30+ | 2-3 días |
| 8 | Documentación | ~50+ | 2 días |

> **Regla de oro:** No tocar archivos `.json`, `.tabula` ni assets a menos que sea estrictamente necesario para el funcionamiento del juego.
