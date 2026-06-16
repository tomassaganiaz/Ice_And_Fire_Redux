# Ice and Fire Redux

**Dragons, Hippogryphs, Pixies, Oh my!**

Fork del Ice and Fire original por alexthe666, enfocado en RLCraft 1.12.2. Este fork consolida código duplicado, agrega compatibilidad con mods (Tinker's Construct, Construct's Armory, Spartan Weaponry), e implementa mecánicas nuevas como el baño de sangre de dragón y chain lightning.

---

## Tabla de Contenido

- [Diferencias con el original](#diferencias-con-el-original)
- [Nuevas mecánicas](#nuevas-mecánicas)
- [Compatibilidad con mods](#compatibilidad-con-mods)
- [Mejoras de código](#mejoras-de-código)
- [Problemas conocidos](#problemas-conocidos)
- [Compilación](#compilación)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Créditos](#créditos)

---

## Diferencias con el original

| Aspecto | Original (alexthe666) | Redux |
|---------|----------------------|-------|
| Namespace | `com.github.alexthe666.iceandfire` | `com.github.Redux.iceandfire` |
| Explosiones de dragón | 3 clases separadas (~950 líneas) | `DragonExplosion.java` unificada (~490 líneas) |
| Proyectiles de aliento | 6 clases separadas | `EntityDragonBreathProjectile` + `EntityDragonChargeProjectile` (~400 líneas base) |
| Subtipos de dragón | Lógica duplicada en Fire/Ice/LightningDragon | Consolidada en `EntityDragonBase` (~245 líneas eliminadas) |
| `instanceof EntityFireDragon` | 13 archivos | 0 — todo usa `EnumDragonType` |
| Layers de render | Inner classes en 9 renderers | 21 archivos independientes en `layer/` |
| Javadoc | ~0.6% de clases | 100% de clases |
| Tinker's Construct | Solo materiales de herramienta | Materiales + modifiers (flame, frost, lightning) + libro in-game |
| Construct's Armory | Inexistente | Materiales de armadura + traits de sangre + libro in-game |
| Spartan Weaponry | Compatibilidad básica vía reflection | Mismos materiales + modelos JSON + recetas completas |
| Chain Lightning | Solo `EntityLightningBolt` vanilla | Sistema `ChainLightningAttack` con rebote entre mobs |
| `sun.audio.AudioPlayer` | Warning de compilación | Eliminado |

---

## Nuevas mecánicas

### Dragon Blood Bathing (Baño de sangre de dragón)

Sistema que permite imbuir armas, herramientas y **armaduras** con sangre de dragón para obtener efectos elementales.

**Mecánica:**
1. Llena un caldero vacío con sangre de dragón (click derecho con frasco de sangre)
2. Sumerge un arma/herramienta/armadura en el caldero (click derecho con el ítem)
3. El ítem obtiene el efecto `DragonBloodBathe` con 100 usos

**Efectos en armas/herramientas (al golpear):**

| Sangre | Efecto | Bonus vs dragones |
|--------|--------|-------------------|
| Fuego | Prende fuego 5s | Hielo (+50% daño) |
| Hielo | Slow III por 5s | Fuego (+50% daño) |
| Rayo | Chain lightning (rebota entre mobs) | Fuego y Hielo (+50% daño) |

**Efectos en armadura (al llevarla puesta):**

| Sangre | Efecto pasivo |
|--------|--------------|
| Fuego | Fire Resistance I permanente |
| Hielo | Limpia Slowness automáticamente |
| Rayo | 75% reducción de daño de rayo |

> El chain lightning usa el sistema `ChainLightningAttack` de la API: el rayo golpea al objetivo y rebota a mobs cercanos con daño escalonado y parálisis configurable.

---

## Compatibilidad con mods

### Tinker's Construct

**Materiales de herramienta:**
- `dragonbone` — 500 dur, 7.0 eff, 4.0 dmg, harvest 3
- `fire_dragonsteel` — 2000 dur, 12.0 eff, 7.0 dmg, harvest 5
- `ice_dragonsteel` — 2000 dur, 12.0 eff, 7.0 dmg, harvest 5
- `lightning_dragonsteel` — 2000 dur, 12.0 eff, 7.0 dmg, harvest 5

**Modifiers (sangre de dragón en herramientas Tinker):**
- `flame` — fuego al golpear (sangre de dragón de fuego)
- `frost` — congelación al golpear (sangre de dragón de hielo)
- `lightning` — chain lightning al golpear (sangre de dragón de rayo)
- Incluye documentación en el libro in-game (EN + ZH)

### Construct's Armory

**Materiales de armadura:**
- `dragonbone` — CoreStats(20F dur, 16F def), PlatesStats(1.5F mod, 0.5F tough)
- `dragonsteel` (fuego/hielo/rayo) — CoreStats(35F dur, 22F def), PlatesStats(3.0F mod, 2.5F tough)

**Armor traits (sangre de dragón en armadura ConArm):**
- `dragonblood_fire_resist` — Fire Resistance al llevarla puesta
- `dragonblood_ice_resist` — Limpia Slowness al llevarla puesta
- `dragonblood_lightning_resist` — 75% reducción de daño de rayo

> Usa reflection multi-firma para compatibilidad con múltiples versiones de ConArm.

### Spartan Weaponry

**4 materiales × 23 tipos de arma:**
- `dragonbone`, `fire_dragonbone`, `ice_dragonbone`, `lightning_dragonbone`
- `desert` / `jungle` (myrmex chitin) y variantes `_venom`
- 184+ recetas de crafting JSON completas
- Modelos de arma y texturas con animaciones `.mcmeta`

### Otros mods compatibles

| Mod | Tipo de integración |
|-----|-------------------|
| JEI | Recetas de Dragonforge |
| HWYLA / The One Probe | Tooltips de entidades |
| Baubles | Blindfold, Earplugs como accesorios |
| Thaumcraft | Aspectos para items/bloques |
| CraftTweaker | Scripts de recetas de dragonforge |
| First Aid | Efectos en partes del cuerpo |
| SpartanWeaponry | Materiales de arma |

---

## Mejoras de código

### Consolidaciones (código duplicado eliminado)

| Componente | Antes | Después |
|-----------|-------|---------|
| Explosiones (Fire/Ice/Lightning) | 3 clases, ~950 líneas | 1 clase, ~490 líneas |
| Proyectiles breath (Fire/Ice/Lightning) | 3 clases, ~450 líneas | 1 base + 3 wrappers, ~195 líneas |
| Proyectiles charge (Fire/Ice/Lightning) | 3 clases, ~600 líneas | 1 base + 3 wrappers, ~215 líneas |
| Dragon subtypes (Fire/Ice/Lightning) | Código duplicado | Consolidado en EntityDragonBase, ~245 líneas menos |
| `instanceof EntityXxxDragon` | 13 archivos | 0 — reemplazado por `EnumDragonType` switch |
| Event handlers | EventLiving 892 líneas monolítico | 4 handlers especializados |
| Client proxy | 594 líneas | 479 líneas, delega a EntityRenderRegistry |
| Arrow renderers | 5 renders con 90% duplicado | RenderBaseArrow + wrappers de 15 líneas |
| Hippogryph/Hippocampus GUIs | Código duplicado | GuiHippogryphBase + ContainerHippogryphBase |

### Calidad

- **Javadoc:** 100% de clases documentadas (667/667)
- **0 errores de compilación, 0 warnings**
- **Build probado:** `compileJava` ✅, `runServer` ✅, `runClient` ✅
- **Estructura limpia:** `docs/` con plan de refactorización, reportes y faltantes; `LICENSE` LGPL v3

---

## Problemas conocidos

Ver [`docs/faltantes.md`](docs/faltantes.md) para la lista detallada. Resumen:

- **Dragonsteel assets:** ~31 modelos JSON creados, texturas PNG pendientes (items se ven rosa-negro)
- **Sin tests:** No hay tests unitarios ni de integración
- **Sin CI/CD:** Compilación manual
- **Refactorización pendiente:** ~65% del plan original (items, bloques, mundo, mensajes)
- **No testeado en multiplayer:** El servidor carga pero no se probó con clientes conectados

---

## Compilación

```bash
# Windows (PowerShell)
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-21.0.2+13'
.\gradlew.bat build

# Linux/macOS
./gradlew build
```

**Requisitos:**
- JDK 17+ (para el plugin Shadow de Gradle)
- El código fuente compila para Java 8 (Minecraft 1.12.2)
- Gradle auto-descarga JDK 8 toolchain para la compilación

**Ejecutar en desarrollo:**
```bash
.\gradlew.bat runClient    # Cliente con el mod cargado
.\gradlew.bat runServer    # Servidor dedicado
```

---

## Estructura del proyecto

```
Ice_and_fire_Redux/
├── build.gradle
├── gradle.properties
├── settings.gradle
├── LICENSE                 # LGPL v3
├── README.md
├── docs/                   # Documentación técnica
│   ├── faltantes.md
│   ├── PLAN_REFACTORIZACION.md
│   ├── RECOMENDACIONES.md
│   └── REPORTES.md
└── src/main/
    ├── java/com/github/Redux/iceandfire/
    │   ├── api/            # Chain Lightning API, efectos
    │   ├── block/          # Bloques personalizados
    │   ├── client/         # Renderers, modelos, GUIs, partículas
    │   ├── entity/         # Entidades, AI, proyectiles, explosiones
    │   ├── enums/          # EnumDragonType, EnumParticle, etc.
    │   ├── event/          # Handlers de eventos
    │   ├── integration/    # Compatibilidad con mods
    │   ├── item/           # Ítems, armaduras, herramientas
    │   ├── message/        # Paquetes de red
    │   ├── mixin/          # Mixins (9 clases)
    │   └── world/          # Generación de mundo, estructuras
    └── resources/assets/iceandfire/
        ├── lang/           # 9 idiomas
        ├── models/         # Modelos JSON de items y bloques
        ├── textures/       # Texturas
        ├── tinkers/book/   # Documentación Tinker's Construct
        └── sounds/         # Efectos de sonido
```

---

## Créditos

- **Ice and Fire original:** alexthe666, Raptorfarian, Kotlin-Programmer
- **Fork Redux RLCraft:** equipo RLCraft
- **Código base de Chain Lightning:** adaptado de Botania por ChickenBones
- **LegSolver:** adaptado de JurassiCraft por paul101

---

*Basado en Ice and Fire original por alexthe666 y Affehund.*
