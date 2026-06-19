# 🐉 Plan de Nuevas Implementaciones — Ice and Fire Redux

> *"Dejaye volar" edition — sin límites, después priorizamos.*

---

## ⏳ Implementaciones pendientes (próximas)

| # | Tarea | Estado |
|---|-------|--------|
| 1 | **Wither Dragon** — texturas, IA, armas, dragonforge | Base creada, falta contenido |
| 2 | **Dragón de Veneno** — nuevo tipo completo | No iniciado |
| 3 | **Dragonforges Wither** — recetas de forja para wither | Bloquea hasta que #1 avance |
| 4 | **Bestiario** — entradas Wither Dragon + Dread mobs | No iniciado |
| 5 | **Tinker's dragonsteel lightning** — `textures/tinkers/dragonsteel_lightning.png` | Huérfano, sin referenciar |
| 6 | **Balance configs** — ~150 valores hardcodeados | Ver sección abajo |

> ✅ Completado: loot tiers, modelos forja, cuevas cíclope, nidos con muros, rebranding

---

## ⚖️ Valores hardcodeados pendientes de config

Valores más impactantes que deberían ser configurables:

### Dragones
| Valor | Archivo | Default actual |
|---|---|---|
| Health min multiplier | `Entity*Dragon.java` | `dragonHealth * 0.046` |
| Health max multiplier | `Entity*Dragon.java` | `dragonHealth * 1.15` |
| Min/max speed | `Entity*Dragon.java` | `0.15F / 0.4F` |
| Min/max armor | `EntityDragonBase.java` | `1D / 22D` |
| Age scaling divisor | `EntityDragonBase.java` | `125` |
| Explosion size multiplier | `EntityDragonBase.java` | `stage * 2.5F` |
| Breath timing (ticks) | `EntityDragonBase.java` | `40` |
| Dragon stage age thresholds | `EntityDragonBase.java` | `25, 50, 75, 100` |
| Flight chance per tick | `EntityDragonBase.java` | `1500` |
| Fly speed base | `EntityDragonBase.java` | `2 + age/125 * 2` |

### Entidades
| Valor | Archivo | Default actual |
|---|---|---|
| Cyclops armor | `EntityCyclops.java` | `20D` |
| Cyclops walk speed | `EntityCyclops.java` | `0.35D` |
| Hydra head regrow ticks | `EntityHydra.java` | `100` |
| Hydra breath duration | `EntityHydra.java` | `60` |
| Hydra strike range | `EntityHydra.java` | `6` |
| Siren charm radius | `EntitySiren.java` | `50x12` |
| Siren melee damage | `EntitySiren.java` | `6D` |
| Troll armor | `EntityTroll.java` | `9D` |
| Myrmex stats (Queen+Worker) | `EntityMyrmex*.java` | todos hardcodeados |
| Sea Serpent ancient modifier | `EntitySeaSerpent.java` | `1.5x` |

### WorldGen
| Valor | Archivo | Default actual |
|---|---|---|
| Roost dragon age range | `WorldGenDragonRoost.java` | `25-99` |
| Roost radius formula | `WorldGenDragonRoost.java` | `age / 4` |
| Cyclops cave radius | `WorldGenCyclopsCave.java` | `16` |
| Dragon cave underground Y | `StructureGenerator.java` | `20-40` |
| Cyclops cave feature chances | `WorldGenCyclopsCave.java` | varios |
| Dragon cave ore distribution | `WorldGenDragonCave.java` | varios |

### Items
| Valor | Archivo | Default actual |
|---|---|---|
| Bow durability (base/elemental) | `ItemDragonBow.java` | `584 / 700` |
| Tide Trident melee base | `ItemTideTrident.java` | `7D` |
| Cyclops eye range + durability | `ItemCyclopsEye.java` | `15 / 500` |

### Dragon Forge
| Valor | Archivo | Default actual |
|---|---|---|
| Fallback cook time | `TileEntityDragonforge.java` | `1000` |
| Flame timer | `TileEntityDragonforge.java` | `40` |

---

## 🆕 Dragones Nuevos

### Dragón de Veneno (Jungla/Pantano)
- **Aliento**: Veneno II en área, deja nubes tóxicas
- **Pasiva**: Inmune a veneno, regenera en agua estancada
- **Drop**: Escamas verdes → herramientas que envenenan; colmillos → pociones
- **Huevo**: Eclosiona en biomas húmedos, necesita Mycelium abajo

### Dragón de Wither (Nether)
- **Aliento**: Wither II + absorción de vida (life steal 10%)
- **Pasiva**: Inmune al fuego, vuela más lento pero pega más fuerte
- **Drop**: Escamas negras → armas con Wither; esencia → bloques decorativos
- **Huevo**: Solo eclosiona en Nether, rodeado de almas de fuego

### Dragón de Agua (Océanos)
- **Aliento**: Chorro de agua a presión (empuje + drowning damage)
- **Pasiva**: Emboscada desde el agua, visibilidad reducida para enemigos
- **Drop**: Escamas azules → armadura con Respiración Acuática; aletas → Depth Strider
- **Huevo**: Eclosiona sumergido en agua, rodeado de coral

### Dragón de Tierra (Montañas/Cuevas)
- **Aliento**: Polvo de roca → Ceguera I + daño por asfixia
- **Pasiva**: Excava túneles, puede emerger del suelo (animación de burst)
- **Drop**: Escamas marrones → armadura con Resistance extra; garras → Excavación
- **Huevo**: Eclosiona bajo tierra (Y<30), rodeado de piedra

### Dragón de Viento (Altas montañas)
- **Aliento**: Ráfaga → empuje extremo + daño por caída
- **Pasiva**: Vuelo ×1.5 más rápido, doble salto
- **Drop**: Escamas plateadas → herramientas con knockback mejorado; plumas → elytra boost
- **Huevo**: Eclosiona arriba de Y=120, necesita estar expuesto al cielo

### Dragón Espectral (End)
- **Aliento**: Teletransporta al objetivo a corta distancia + daño de vacío
- **Pasiva**: Fasea a través de bloques, inmune a proyectiles
- **Drop**: Escamas púrpuras → herramientas que teletransportan drops; polvo → Ender Pearl mejorada
- **Huevo**: Eclosiona en el End, rodeado de obsidiana llorona

---

## 🏰 Estructuras

### Nidos de Dragón Mejorados
- **Tier I-III**: Pepitas de oro/plata en el suelo, 1-4 cofres
- **Tier IV-V**: Bloques de metal, cofres OP, armadura de diamante encantada
- Cada tipo de dragón tiene su propio estilo de nido (fuego=lava, hielo=hielo, rayo=piedras flotantes, etc.)

### Santuario del Dragón Anciano
- Estructura rara end-game
- Guardada por 3 dragones de distintos tipos
- Cofre central con item legendario (Escama Primordial)

### Forja de Dragón Mejorada
- Nuevos tiers de forja: Básica → Mejorada → Ancestral
- La forja ancestral puede combinar 2 tipos de sangre para crear aleaciones
- Recetas exclusivas para cada tier

---

## ⚔️ Equipamiento Nuevo

### Armadura de Sangre de Dragón
- Hecha con escamas + sangre → activa habilidades pasivas
- Set completo de fuego → Fire Aura (daño a mobs cercanos)
- Set completo de hielo → Frost Aura (ralentiza mobs cercanos)
- Set completo de rayo → Lightning Aura (parálisis)

### Joyería de Dragón (Baubles)
- **Anillo de Fuego**: Inmunidad al fuego + cocina automáticamente carne en inventario
- **Anillo de Hielo**: Water Breathing + camina sobre agua (Frost Walker mejorado)
- **Anillo de Rayo**: Atrae rayos durante tormentas, carga herramientas
- **Collar de Dragón**: El dragón domesticado recibe 50% menos daño
- **Corona de Dragón**: Los dragones salvajes no te atacan (hasta que los golpees)

### Armas Legendarias
- **Espada Primordial**: Hecha con 1 escama de cada tipo → aplica TODOS los efectos elementales
- **Arco de Cazador de Dragones**: +200% daño a dragones, flechas teletransportan drops a tu inventario
- **Báculo de Control de Dragones**: Domina dragones salvajes temporalmente (30s cooldown)
- **Lanza de Jinete**: Daño ×3 cuando estás montado en un dragón

---

## 🧪 Sistema de Alquimia de Dragón

### Mesa de Alquimia
- Nuevo bloque que combina sangre, escamas y otros ingredientes
- Recetas para pociones mejoradas (Fire Resistance IV, Speed III, etc.)
- Posibilidad de crear "Elixir de Dragón" — transformación temporal en dragón (5 min)

### Cristal de Dragon
- Cristales que almacenan la esencia de un dragón
- Se cargan cerca de dragones o en la forja de dragón
- Al romperse, liberan el efecto del dragón en área

---

## 🤖 Mecánicas

### Sistema de Reputación de Dragones
- Matar dragones baja reputación con ese tipo
- Alimentar/tamear dragones sube reputación
- Reputación alta → dragones no te atacan, precios de aldeanos bajan
- Reputación baja → dragones te cazan activamente

### Híbridos de Dragón
- Combinar 2 huevos en la forja ancestral → huevo híbrido
- Dragón Fuego+Hielo → Dragón de Vapor (aliento de vapor que quema y ralentiza)
- Dragón Fuego+Rayo → Dragón de Plasma (aliento de plasma que derrite armadura)
- Dragón Hielo+Rayo → Dragón de Tormenta (aliento de granizo eléctrico)

### Evolución de Dragones
- Los dragones tameados pueden "evolucionar" después de cierta cantidad de kills
- Etapa 1 → 2 → 3 con cambios visuales y nuevas habilidades
- Cada evolución desbloquea un ataque especial (cooldown largo)

### Eventos de Dragón
- **Migración**: Cada X días, un tipo de dragón migra — spawnean más de ese tipo
- **Eclipse de Dragón**: Evento raro, dragón jefe spawnea en el overworld, drops legendarios
- **Guerra de Dragones**: Dos dragones de tipos opuestos pelean en el cielo — lootea al ganador

---

## 🎮 Calidad de Vida

### Bestiario Mejorado
- Pestañas por tipo de dragón con estadísticas detalladas
- Seguimiento de kills por tipo
- Recompensas por completar entradas del bestiario

### Silbato de Dragón
- Item que llama a tu dragón desde cualquier distancia (misma dimensión)
- Diferentes tonos para diferentes dragones

### Montura Mejorada
- GUI de dragón muestra armadura equipada, salud, hambre, y experiencia
- Botón de "modo agresivo/pasivo" para dragones tameados
- Órdenes de dragón: seguir, esperar, patrullar, cazar

---

## 🌍 Dimensiones / Biomas

### El Nido Primordial (Dimensión)
- Portal construido con bloques de hueso de dragón + ojo de dragón
- Dimensión flotante con islas donde spawnean dragones ancianos
- Jefe final: El Primordial (dragón que cambia de tipo durante la pelea)

### Biomas Temáticos
- **Montañas de Cristal**: Dragones de rayo spawnean más, bloques de cristal
- **Pantano Tóxico**: Dragones de veneno, nueva vegetación
- **Valle de Huesos**: Dragones esqueleto, bloques de hueso, loot arqueológico

---

## 📊 Priorización Sugerida

| Prioridad | Feature | Esfuerzo | Impacto |
|-----------|---------|----------|---------|
| 🔴 1 | Dragon Wither (completar) | Alto | Alto — nuevo dragón jugable |
| 🔴 2 | Dragon Roost tiers | Alto | Alto — mejor exploración |
| 🔴 3 | Armas legendarias | Medio | Alto — end-game content |
| 🟠 4 | Joyería de dragón | Medio | Medio — utilidad diaria |
| 🟠 5 | Sistema de reputación | Alto | Medio — profundidad |
| 🟡 6 | Bestiario mejorado | Medio | Bajo — QoL |
| 🟡 7 | Híbridos | Muy alto | Alto — pero requiere mucho |
| 🟢 8 | Dimensión nueva | Extremo | Muy alto — proyecto grande |

---
*Plan generado el 16 Jun 2026 — siguiente paso: elegir qué empezar.*
