package com.nameisjayant.noteapp.features.notes.ui.screens

import android.annotation.SuppressLint
import android.text.TextUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.nameisjayant.noteapp.R
import com.nameisjayant.noteapp.common.CommonTextField
import com.nameisjayant.noteapp.common.LoadingDialog
import com.nameisjayant.noteapp.common.base.NoteState
import com.nameisjayant.noteapp.common.getActivity
import com.nameisjayant.noteapp.common.showToast
import com.nameisjayant.noteapp.data.local.models.Notes
import com.nameisjayant.noteapp.features.notes.ui.viewmodel.NoteEvents
import com.nameisjayant.noteapp.features.notes.ui.viewmodel.NoteViewModel
import kotlinx.coroutines.flow.collectLatest


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddNoteScreen(
    navHostController: NavHostController,
    viewModel: NoteViewModel = hiltViewModel()
) {

    var title by remember { mutableStateOf("") }
    var task by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current.getActivity()!!

    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        viewModel.noteAddedEventFlow.collectLatest {
            isLoading = when (it) {
                is NoteState.Success -> {
                    context.showToast(it.data)
                    navHostController.navigateUp()
                    false
                }
                is NoteState.Failure -> {
                    context.showToast(
                        it.msg
                    )
                    false
                }
                NoteState.Loading -> {
                    true
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar {
                IconButton(onClick = {
                    context.finish()
                }) {
                    Icon(Icons.Rounded.ArrowBack, contentDescription = "")
                }
                Text(
                    text = stringResource(id = R.string.add_task),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(CenterVertically)
                )
            }
        },
        floatingActionButton = {
            if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(task))
                FloatingActionButton(
                    onClick = {
                        viewModel.onEvent(
                            NoteEvents.NoteAddEvent(
                                Notes(title, task)
                            )
                        )
                    },
                    backgroundColor = Color.Blue,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)

        ) {
            Column {
                CommonTextField(
                    text = title,
                    label = stringResource(id = R.string.enter_title),
                    modifier = Modifier.fillMaxWidth(),
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                ) {
                    title = it

                }

                CommonTextField(
                    text = task,
                    label = stringResource(id = R.string.write_task),
                    modifier = Modifier.fillMaxSize(),
                    imeAction = ImeAction.Default,
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Right)
                    }
                ) {
                    task = it

                }
            }
        }
    }

    if (isLoading)
        LoadingDialog()

}