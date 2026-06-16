# Ice and Fire Redux

Dragons, Hippogryphs, Pixies, Oh my!

Fork/re-imagining of the original Ice and Fire mod for Minecraft 1.12.2, adding mythical creatures, items, and mechanics.

---

## Tabla de Contenido

- [Modificaciones Implementadas](#modificaciones-implementadas)
- [Dragon Blood Bathing](#dragon-blood-bathing)
- [Problemas Conocidos](#problemas-conocidos)
- [Compilación](#compilación)

---

## Modificaciones Implementadas

### Dragon Blood Bathing

Sistema que permite imbuir armas y herramientas con sangre de dragón para obtener efectos elementales temporales.

**Mecánica:**
1. Llena un caldero vacío con sangre de dragón (click derecho con frasco de sangre)
2. Sumerge un arma o herramienta en el caldero con sangre (click derecho con el arma)
3. El arma obtiene el efecto `DragonBloodBathe` con 100 usos

**Efectos por tipo de sangre:**

| Sangre | Efecto al golpear | Bonificación contra |
|--------|-------------------|---------------------|
| 🔥 Fuego | Prende fuego 5s | Dragones de hielo (+50% daño) |
| ❄️ Hielo | Slow III por 5s | Dragones de fuego (+50% daño) |
| ⚡ Rayo | 33% de rayo | Dragones de fuego y hielo (+50% daño) |

Cuando los usos se agotan, el efecto desaparece del arma automáticamente.

**Archivos modificados:**
- `EventLiving.java` — lógica principal (interacción con caldero, efectos al golpear, tooltip)
- `en_US.lang` — traducciones para tooltips

---

## Problemas Conocidos

Ver [`Faltantes.md`](Faltantes.md) para la lista completa de errores y omisiones identificados en el código, incluyendo:

- **Críticos:** falta `pack.mcmeta`, error de compilación en `getHorn()`, typos en `@ObjectHolder`
- **Serios:** `@Mod.EventBusSubscriber` sin `modid`, `EventWorld` sin registrar
- **Moderados:** typos en nombres, aspecto Thaumcraft incorrecto
- **Leves:** código muerto, ortografía inconsistente

---

## Compilación

```bash
# Windows
gradlew.bat build

# Linux/macOS
./gradlew build
```

**Nota:** Se requiere JDK 17+ para compilar (el plugin shadow lo exige). El código fuente compila para Java 8 (Minecraft 1.12.2).

---

*Basado en Ice and Fire original por alexthe666 y Affehund.*
