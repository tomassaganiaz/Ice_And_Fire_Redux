# Reportes de Errores y Deuda Técnica

## 🔴 ROJO: URGENTE — Bloquea compilación o funcionamiento

| # | Archivo | Problema |
|---|---------|----------|
| 1 | `entity/util/DragonUtils.java` | Importa `IEntityOwnable` del paquete `com.github.alexthe666.iceandfire.entity.util` — el archivo no existe |
| 2 | `proxy/CommonProxy.java` | Importa `EnumParticle` de `com.github.alexthe666.iceandfire.core` — la clase fue movida a `com.github.alexthe666.iceandfire.enums` |
| 3 | `CommonProxy.java` (raíz) | Mismo error: importa `EnumParticle` del paquete `core` incorrecto |

## 🟠 NARANJA: IMPORTANTE — Deuda técnica que dificulta mantenimiento

| # | Archivo | Problema |
|---|---------|----------|
| 4 | `entity/EntityDragonBase.java` (~2600 líneas) | God class: mezcla IA, animación, interacciones, persistencia, combate, vuelo, hambre, curación, eventos |
| 5 | `entity/EntitySeaSerpent.java` (~1150 líneas) | God class, múltiples responsabilidades mezcladas |
| 6 | `entity/EntityHippogryph.java` (~1080 líneas) | God class |
| 7 | `entity/EntityMyrmexQueen.java` (~1250 líneas) | God class |
| 8 | Varios renderers | Inner classes gigantes (LayerTrollWeapon, LayerDragonArmor, etc.) mezcladas con el renderer principal |
| 9 | `client/render/entity/RenderDragonBase.java` | Inner class `LayerDragonArmor` con lógica compleja (>100 líneas) dentro del renderer |
| 10 | `entity/EntityDragonBase.java` | `processInteract()` ~220 líneas con 11+ branches de if-else anidados |
| 11 | `entity/EntityDragonBase.java` | `onLivingUpdate()` ~300 líneas con lógica de servidor/cliente entremezclada |
| 12 | `capability/entityeffect/EntityEffectHandler.java` | If-else gigante para cada efecto |
| 13 | `capability/entityeffect/EntityEffectClientHandler.java` | If-else gigante duplicado para cliente |

## 🟢 VERDE: PASABLE PERO CORREGIBLE

| # | Archivo | Problema |
|---|---------|----------|
| 14 | `client/render/entity/effect/RenderShivaxiFire.java` | Magic numbers sin nombre (`0.35F`, `0.03125F`, `7`) |
| 15 | Varios | Números mágicos dispersos sin constantes con nombre |
| 16 | Todo el proyecto | Ausencia casi total de JavaDoc en métodos/classes públicas |
| 17 | `client/particle/ParticleLightning.java` | Constructor deprecado con `boolean` que debería ser un enum (hay 8 constructores) |
| 18 | `entity/EntityDragonBase.java` | Condicionales booleanos complejos sin nombre descriptivo ej: `if (!isTackling() && this.isFlying() || !this.onGround && !this.isHovering() && this.airTarget != null)` |
| 19 | `entity/EntityDragonBase.java` | Métodos con `return;` silencioso en medio de `onLivingUpdate()` que pueden ocultar bugs |
| 20 | Varios | Código comentado (ruido) que debería eliminarse |
| 21 | Varios | Imports no usados |
| 22 | `entity/util/DragonUtils.java` | Import estático de API interna de Java (`sun.audio.AudioPlayer`) — warning de compilación |
| 23 | `client/render/entity/RenderTroll.java` | Layer classes públicas dentro del renderer — acoplamiento innecesario |
| 24 | Varios | Ausencia de `@Override` en métodos que sobreescriben (dificulta detección de errores) |
