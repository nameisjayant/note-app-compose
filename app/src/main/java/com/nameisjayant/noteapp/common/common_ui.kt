package com.nameisjayant.noteapp.common


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog


@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.noRippleEffect(onClick: () -> Unit) = composed {
    composed {
        clickable(
            interactionSource = MutableInteractionSource(),
            indication = null
        ) {
            onClick()
        }
    }
}

@Composable
fun CommonTextField(
    text: String,
    label: String,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Next,
    color: Color = Color.Transparent,
    keyboardType: KeyboardType = KeyboardType.Text,
    enable: Boolean = true,
    onNext: () -> Unit = {},
    onTextChange: (String) -> Unit
) {

    TextField(
        value = text, onValueChange = { onTextChange(it) },
        placeholder = {
            Text(
                text = label,
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            )
        },
        modifier = modifier,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = color,
            backgroundColor = Color.Transparent,
            unfocusedIndicatorColor = color,
        ),
        keyboardOptions = KeyboardOptions(imeAction = imeAction, keyboardType = keyboardType),
        keyboardActions = KeyboardActions {
            onNext()
        },
        enabled = enable
    )
}

@Composable
fun CommonRadioButton(
    selected: Boolean,
    title: String,
    onTitleUpdate: (String) -> Unit
) {

    Row(
        modifier = Modifier.padding(start = 10.dp)
    ) {
        RadioButton(selected = selected, onClick = {
            onTitleUpdate(title)
        })
        Text(
            title, style = TextStyle(
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
            ),
            modifier = Modifier.align(CenterVertically)
        )
    }

}

@Composable
fun CommonButton(
    title: String,
    modifier: Modifier = Modifier,
    enable: Boolean = true,
    background: Color = Color.Blue,
    onClick: () -> Unit = {}
) {

    Button(
        onClick = {
            onClick()
        },
        modifier = modifier,
        enabled = enable,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = background
        )
    ) {
        Text(text = title, color = Color.White)
    }

}

fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}


@Composable
fun LoadingDialog() {

    Dialog(onDismissRequest = {}) {
        CircularProgressIndicator()
    }

}

fun Context.showToast(
    msg: String,
    duration: Int = Toast.LENGTH_SHORT
) = Toast.makeText(this, msg, duration).show()