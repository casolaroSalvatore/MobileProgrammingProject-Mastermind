package com.example.mastermind.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.mastermind.R
import com.example.mastermind.ui.component.BoardRow
import com.example.mastermind.viewmodel.HistoryDetailViewModel
import java.time.format.DateTimeFormatter

@Composable
fun HistoryDetailScreen(gameId: Long, nav: NavHostController) {
    val ctx = LocalContext.current.applicationContext as android.app.Application
    val vm: HistoryDetailViewModel = viewModel(
        factory = HistoryDetailViewModel.Factory(ctx, gameId)
    )

    val date     by vm.date.collectAsState()
    val rows     by vm.rows.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        /* ----- intestazione ----- */
        Text(
            text = date?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                ?: stringResource(R.string.loading),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(12.dp))

        /* ----- board completa ----- */
        LazyColumn(
            Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(rows) { row ->
                BoardRow(row.guess, row.blacks, row.whites)
            }
        }

        /* ----- back ----- */
        Button(
            onClick = { nav.popBackStack() },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 12.dp)
        ) { Text(stringResource(R.string.back)) }
    }
}
