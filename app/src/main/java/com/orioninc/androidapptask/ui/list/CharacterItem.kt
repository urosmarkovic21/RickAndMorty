package com.orioninc.androidapptask.ui.list

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.orioninc.androidapptask.R
import com.orioninc.androidapptask.data.model.Character

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun CharacterItem(
    character: Character,
    onClick: () -> Unit,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
) {
    with(sharedTransitionScope) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable { onClick() }
                    .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model =
                    ImageRequest
                        .Builder(LocalContext.current)
                        .data(character.image)
                        .placeholder(R.drawable.portal)
                        .memoryCacheKey(character.image)
                        .diskCacheKey(character.image)
                        .crossfade(true)
                        .size(300, 300)
                        .build(),
                contentDescription = character.name,
                modifier =
                    Modifier
                        .size(64.dp)
                        .sharedElement(
                            state = rememberSharedContentState(key = "image-${character.id}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                        ).clip(CircleShape),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = character.name, style = MaterialTheme.typography.titleMedium)
        }
    }
}
