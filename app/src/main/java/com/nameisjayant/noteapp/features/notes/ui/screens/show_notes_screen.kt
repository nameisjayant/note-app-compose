package com.nameisjayant.noteapp.features.notes.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.nameisjayant.noteapp.features.notes.navigation.Add_Notes
import com.nameisjayant.noteapp.features.notes.ui.viewmodel.NoteViewModel
import com.nameisjayant.noteapp.ui.theme.Background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavHostController
import com.nameisjayant.noteapp.R
import com.nameisjayant.noteapp.common.LoadingDialog
import com.nameisjayant.noteapp.common.base.NoteState
import com.nameisjayant.noteapp.common.showToast
import com.nameisjayant.noteapp.data.local.models.Notes
import com.nameisjayant.noteapp.features.notes.navigation.Update_Notes
import com.nameisjayant.noteapp.features.notes.ui.viewmodel.NoteEvents
import com.nameisjayant.noteapp.ui.theme.Purple500
import com.nameisjayant.noteapp.ui.theme.Teal200
import kotlinx.coroutines.flow.collectLatest


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ShowNotesScreen(
    navHostController: NavHostController,
    viewModel: NoteViewModel = hiltViewModel()
) {

    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    if (isLoading) LoadingDialog()

    LaunchedEffect(key1 = true) {
        viewModel.deleteNoteEventFlow.collectLatest {
            isLoading = when (it) {
                is NoteState.Success -> {
                    context.showToast(it.data)
                    false
                }
                is NoteState.Failure -> {
                    context.showToast(it.msg)
                    false
                }
                NoteState.Loading -> true
            }
        }
    }

    val response = viewModel.notes.value
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navHostController.navigate(Add_Notes)
                },
                backgroundColor = Color.Blue,
            ) {
                Icon(Icons.Default.Add, contentDescription = "", tint = Color.White)
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Purple500)
            ) {
                Row(
                    modifier = Modifier.padding(start = 20.dp, top = 50.dp, bottom = 15.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.all_notes),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(CenterVertically)
                    )
                }
            }
            when (response) {
                is NoteState.Success -> {
                    LazyColumn(
                        modifier = Modifier.padding(top = 90.dp)
                    ) {
                        items(response.data, key = { it.noteId!! }) {
                            NoteEachRow(data = it, onEdit = {
                                navHostController.currentBackStackEntry?.savedStateHandle?.set(
                                    "data",
                                    it
                                )
                                navHostController.navigate(Update_Notes)
                            }) {
                                viewModel.onEvent(NoteEvents.DeleteNoteEvent(it))
                            }
                        }
                    }
                }
                is NoteState.Failure -> {}
                NoteState.Loading -> {}
            }


        }
    }

}


@Composable
fun NoteEachRow(
    data: Notes,
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {}
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                }
                .background(
                    if (data.color == 1) Color.Red.copy(alpha = 0.4f) else if (data.color == 2) Teal200.copy(
                        alpha = 0.3f
                    ) else Color.Green.copy(alpha = 0.3f)
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.weight(0.8f)
                ) {
                    Column {
                        CategorySection(notes = data)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = data.title ?: "-",
                            style = TextStyle(
                                color = Color.Black,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.W400
                            )
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = data.description ?: "-",
                            style = TextStyle(
                                color = Color.Gray,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal
                            )
                        )

                    }
                }
                PopUpDialog(
                    edit = { onEdit.invoke() },
                    modifier = Modifier
                        .weight(0.2f)
                        .align(CenterVertically)
                ) {
                    onDelete.invoke()
                }
            }
        }

    }

}

@Composable
fun PopUpDialog(
    modifier: Modifier = Modifier,
    edit: () -> Unit,
    delete: () -> Unit
) {
    var openPopUp by remember {
        mutableStateOf(false)
    }

    Column(modifier = modifier.background(color = Color.Transparent)) {
        IconButton(onClick = {
            openPopUp = !openPopUp
        }) {
            Icon(
                Icons.Default.MoreVert,
                contentDescription = null
            )
        }

        if (openPopUp) {
            Popup(
                alignment = Alignment.TopStart,
                properties = PopupProperties(
                    dismissOnClickOutside = true,
                    dismissOnBackPress = true
                ), onDismissRequest = {
                    openPopUp = !openPopUp
                }
            ) {

                Box(
                    modifier = Modifier
                        .wrapContentWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            border = BorderStroke(.1.dp, Color.Gray),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .background(color = Color.White),
                ) {

                    Column(modifier = Modifier.padding(8.dp)) {

                        Row(modifier = Modifier
                            .clickable {
                                edit.invoke()
                                openPopUp = !openPopUp
                            }
                            .padding(8.dp)) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                            Text(
                                text = stringResource(R.string.edit),
                                modifier = Modifier.padding(horizontal = 8.dp),
                                color = Color.Black
                            )
                        }
                        Row(modifier = Modifier
                            .clickable {
                                delete.invoke()
                                openPopUp = !openPopUp
                            }
                            .padding(8.dp)) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                modifier = Modifier.padding(horizontal = 4.dp)
                            )
                            Text(
                                text = stringResource(R.string.delete),
                                modifier = Modifier.padding(horizontal = 8.dp),
                                color = Color.Black
                            )
                        }
                    }
                }

            }
        }
    }


}

@Composable
fun CategorySection(
    notes: Notes
) {
    Row {
        Box(
            modifier = Modifier
                .size(15.dp)
                .clip(CircleShape)
                .background(
                    if (notes.color == 1) Color.Red.copy(alpha = 0.5f) else if (notes.color == 2) Teal200.copy(
                        alpha = 0.5f
                    ) else Color.Green.copy(alpha = 0.5f)
                )
                .align(CenterVertically)
        )
        Spacer(modifier = Modifier.padding(start = 5.dp))
        Text(
            text = notes.category, style = TextStyle(
                color = Color.Black,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

