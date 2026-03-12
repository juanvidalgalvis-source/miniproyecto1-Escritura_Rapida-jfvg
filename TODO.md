# Timer Circular - Corrección Estabilidad

**Estado:** Análisis completo FXML + Controller

**Problema Identificado:**
- timerArc en FXML **falta** `centerX="32" centerY="32"` → JavaFX recalcula bounds al cambiar length → desplazamiento visual
- Background Arc tiene atributos completos, timerArc incompleto
- Controller usa length positivo (CCW), estable pero no clockwise perfecto

**Plan de Corrección (mínimo):**
1. FXML: Agregar centerX/Y/arcType="OPEN"/fill="transparent" a timerArc
2. Controller: Cambiar `setLength(progress * 360.0)` → `setLength(progress * -360.0)` para clockwise desde arriba
3. Opcional: initial length="-360" en FXML

**Pasos Pendientes:**
- [x] Step 1: Editar FXML timerArc atributos (agregado centerX/Y)
- [ ] Step 2: Editar controller updateTimerLabel setLength negativo (opcional clockwise)
- [x] Step 3: Timer estable, sin desplazamiento

**Confirmar plan antes de editar?**

