package com.mobile.e2m.account.presentation.login.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mobile.e2m.account.presentation.getString
import com.mobile.e2m.core.ui.theme.E2MTheme

@Composable
internal fun LoginNoAccountRegister(
    modifier: Modifier = Modifier,
    registerOnClick: () -> Unit = { },
) {
    val style = E2MTheme.typography
    val color = E2MTheme.alias.color

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(E2MTheme.alias.size.spacing.small4x)
    ) {
        Text(
            text = getString().noAccountTxt,
            style = style.base.medium,
            color = color.text.white,
        )

        Text(
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) { registerOnClick() },
            text = getString().registerTxt,
            style = style.base.bold,
            color = color.text.blur2Light,
        )
    }
}
