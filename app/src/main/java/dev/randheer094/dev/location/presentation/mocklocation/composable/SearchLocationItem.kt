package dev.randheer094.dev.location.presentation.mocklocation.composable

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.randheer094.dev.location.domain.SearchResult
import dev.randheer094.dev.location.presentation.mocklocation.state.SearchLocation

@Composable
fun SearchLocationItem(
    state: SearchLocation,
    modifier: Modifier = Modifier,
    onClick: (SearchResult) -> Unit,
) {
    ListItem(
        modifier = modifier.clickable { onClick(state.searchResult) },
        headlineContent = { Text(state.searchResult.name) },
        supportingContent = { Text(state.searchResult.address) },
    )
}