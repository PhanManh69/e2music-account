package com.mobile.e2m.account.presentation.login.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mobile.e2m.account.presentation.getString
import com.mobile.e2m.core.ui.theme.E2MTheme

@Composable
internal fun LoginOtherMethodLayout(
    modifier: Modifier = Modifier,
) {
    val size = E2MTheme.alias.size
    val color = E2MTheme.alias.color

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(size.spacing.small2x),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = size.stroke.thick,
            color = color.border.white,
        )

        Text(
            text = getString().continueWithTxt,
            style = E2MTheme.typography.small.regular,
            color = color.text.white,
        )

        HorizontalDivider(
            modifier = Modifier.weight(1f),
            thickness = size.stroke.thick,
            color = color.border.white,
        )
    }
}
