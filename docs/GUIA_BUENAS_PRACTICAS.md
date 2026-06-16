# Guía de Buenas Prácticas — Ice and Fire Redux

## Objetivo
Este documento establece reglas claras para mejorar la calidad del código, eliminar deuda técnica, y permitir que el mod sea mantenible, portable a nuevas versiones de Minecraft y extensible sin romper funcionalidad existente.

---

## 1. Estructura y Organización de Clases

### 1.1 Una clase = una responsabilidad
- **Mal:** Una clase de 2600+ líneas que maneja rendering, IA, interacciones, animaciones, persistencia, etc.
- **Bien:** Separar responsabilidades en clases enfocadas. No necesitas archivos separados para todo, pero tampoco meter 20 responsabilidades en una clase.
- **Límite práctico:** Si una clase supera las ~600-800 líneas, es señal de que necesita dividirse.

### 1.2 Inner classes estáticas para lógica estrechamente acoplada
- Si una clase SOLO es usada por otra clase y está fuertemente acoplada a ella, puede ser una `public static class` interna.
- Si la lógica es independiente o usada desde múltiples lugares, debe ser una clase separada.

### 1.3 Evitar Dioses (God Classes)
- Identificar métodos que pertenecen a dominios diferentes dentro de una clase gigante.
- Extraer grupos de métodos relacionados a clases helper enfocadas.
- **No crear jerarquías de herencia profundas** — prefiere composición sobre herencia.

---

## 2. Programación Orientada a Objetos

### 2.1 Encapsulamiento
- **Campos:** `private` siempre. Exponer mediante getters/setters solo si es necesario.
- **Métodos:** `private` por defecto. Hacer `public` solo cuando otros paquetes lo necesiten.
- **No exponer implementación interna:** Nunca devolver referencias directas a colecciones internas sin encapsular.

### 2.2 Composición sobre herencia
- En lugar de crear `AbstractDragonBase` -> `FireDragon` -> `IceDragon`, prefiere componentes reutilizables.
- Ejemplo: un `DragonBreathComponent`, `DragonFlightComponent`, `DragonInventoryComponent` que se asignan como campos.

### 2.3 Métodos enfocados
- Un método debe hacer UNA cosa y hacerla bien.
- Si un método tiene más de ~30-40 líneas, extrae sub-métodos con nombre descriptivo.
- **Mal:**
  ```java
  public void onLivingUpdate() {
      // 300 líneas de lógica mezclada: vuelo, combate, animación, hambre, etc.
  }
  ```
- **Bien:**
  ```java
  public void onLivingUpdate() {
      updateBaseMovement();
      updateServerState();
      updateAnimations();
      updateCombat();
      updateFlight();
      updateAgingAndHunger();
  }
  ```

---

## 3. Manejo de Código Repetido

### 3.1 Principio DRY (Don't Repeat Yourself)
- Si ves el mismo bloque de código 3+ veces con solo cambios menores, extráelo a un método.
- Si la variación es pequeña (una constante, un tipo), usa parámetros.
- Si la variación es grande (comportamiento diferente), usa Strategy pattern o Template Method.

### 3.2 Constantes con nombre
- **Mal:** `0.35F`, `0.03125F`, `4`, `20.0F` — números mágicos sin significado.
- **Bien:**
  ```java
  private static final float FIRE_FRAME_HEIGHT = 0.35F;
  private static final int TICKS_PER_DAY = 24000;
  private static final float MAX_TWEEN_PROGRESS = 20.0F;
  ```

---

## 4. Simplificación de Código

### 4.1 Early Return (Guard Clauses)
- **Mal:**
  ```java
  if (!world.isRemote) {
      if (isServerState()) {
          if (canDoThing()) {
              // 50 líneas
          }
      }
  }
  ```
- **Bien:**
  ```java
  if (world.isRemote) return;
  if (!isServerState()) return;
  if (!canDoThing()) return;
  // 50 líneas sin anidamiento
  ```

### 4.2 Simplificar condicionales complejos
- Extraer sub-expresiones booleanas a variables con nombre.
- **Mal:** `if (a && !b && (c || d) && !e && f > 3 && g == null)`
- **Bien:**
  ```java
  boolean canDragonBreathe = fireTicks > 20 && isBreathingFire();
  boolean shouldLand = flyTicks > 6000 || down();
  ```

### 4.3 Switch sobre if-else largos
- Cuando hay 3+ comparaciones del mismo valor, usa `switch` o un mapa.

---

## 5. Eventos (Programación Orientada a Eventos)

### 5.1 Usar el sistema de eventos de Forge correctamente
- No meter lógica de eventos dentro de `onLivingUpdate()` o `updateEntity()`.
- Para eventos específicos (muerte, daño, interacción), suscribirse al bus de eventos de Forge.

### 5.2 Separar lógica de eventos en handlers dedicados
- **Mal:** Un método `onEntityInteract()` de 200 líneas con 15 if-else anidados.
- **Bien:** Cada tipo de interacción (comida, curación, montura, comando) en su propio método handler.

---

## 6. Documentación y Comentarios

### 6.1 Cuándo comentar
- **Comportamiento no obvio:** `// Se resta 1 porque el índice 0 representa "sin efecto"`
- **Reglas de negocio:** `// Según el diseño original, el dragón solo dropea corazón en etapa avanzada`
- **Workarounds:** `// Workaround: Forge 1.12.2 no llama a onChunkUnload para entidades`
- **Campos/métodos públicos:** Documentar qué hace, parámetros y retorno.

### 6.2 Cuándo NO comentar
- Código auto-explicativo: `setHealth(getHealth() + 1);` no necesita comentario.
- `// getter for health` es ruido.

### 6.3 JavaDoc para API pública
Toda clase, método público o campo público debe tener JavaDoc mínimo:
```java
/**
 * Intenta alimentar al dragón con el item en la mano del jugador.
 * @return true si el item fue consumido, false si no aplica.
 */
```

---

## 7. Forge y Minecraft: Prácticas Específicas

### 7.1 Inicialización
- Usar `@ObjectHolder` para items/bloques cuando sea posible.
- Separar registro de lógica de inicialización.

### 7.2 Network
- No enviar paquetes innecesarios en cada tick.
- Agrupar datos de estado en paquetes sincronizados periódicamente, no por separado.

### 7.3 TileEntities
- No mezclar lógica de rendering con lógica de tile entity.
- Usar TESR solo para rendering; la lógica de ticks va en `update()`.

### 7.4 Entidades
- `onLivingUpdate()` es para lógica de actualización, no para manejo de eventos.
- Separar: IA en `applyEntityAI()`, actualización en `onLivingUpdate()`, interacción en `processInteract()`.

---

## 8. Refactorización: Reglas Prácticas

### 8.1 Proceso seguro
1. Identificar el problema (god class, código repetido, etc.)
2. Planificar la extracción sin cambiar comportamiento
3. Implementar
4. Compilar y probar
5. Limpiar imports/archivos huérfanos

### 8.2 Prioridades
1. **Errores de compilación** siempre primero
2. Código quebradizo (el que se rompe fácil al cambiar algo)
3. God classes grandes (>1000 líneas)
4. Código repetido
5. Magic numbers
6. Documentación

### 8.3 Lo que NO debe hacerse
- No refactorizar y cambiar comportamiento en el mismo commit
- No mezclar refactor con nuevas features
- No crear abstracciones innecesarias "por si acaso"
- No renombrar sin actualizar TODAS las referencias

---

## Checklist para Revisión de Código

- [ ] ¿La clase tiene una sola responsabilidad clara?
- [ ] ¿Los métodos privados son menores a 40 líneas?
- [ ] ¿No hay números mágicos sin constante con nombre?
- [ ] ¿No hay código comentado?
- [ ] ¿Los imports no usados fueron eliminados?
- [ ] ¿El encapsulamiento es correcto (campos private)?
- [ ] ¿Las condiciones complejas tienen variables con nombre?
- [ ] ¿No hay duplicación de código obvia?
- [ ] ¿Los métodos públicos tienen JavaDoc?
- [ ] ¿Compila sin errores nuevos?
