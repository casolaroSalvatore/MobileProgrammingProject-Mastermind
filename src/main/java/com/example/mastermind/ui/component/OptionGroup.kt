package com.example.mastermind.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/*
 * Gruppo di pulsanti “chip” per la selezione di un’opzione discreta.
 *
 * @param label      Etichetta del gruppo (es. "Colori")
 * @param options    Lista di possibili valori
 * @param selected   Valore correntemente selezionato
 * @param toString   Convertitore -> testo da mostrare sul chip
 * @param onSelect   Callback quando l’utente sceglie un’opzione
 */

@Composable
fun <T> OptionGroup(
    label: String,
    options: List<T>,
    selected: T,
    toString: (T) -> String = { it.toString() },
    onSelect: (T) -> Unit,
    modifier: Modifier = Modifier
) = Column(modifier) {
    Text(label, style = MaterialTheme.typography.labelLarge)
    Spacer(Modifier.height(4.dp))

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        options.forEach { option ->
            val isSel = option == selected
            OutlinedButton(
                onClick = { onSelect(option) },
                border = BorderStroke(
                    width = if (isSel) 2.dp else 1.dp,
                    color  = if (isSel) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.outline
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isSel)
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    else Color.Transparent
                ),
                shape = RoundedCornerShape(50)
            ) { Text(toString(option)) }
        }
    }
}