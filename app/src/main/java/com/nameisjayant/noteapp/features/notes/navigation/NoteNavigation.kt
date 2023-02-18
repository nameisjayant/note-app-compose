package com.nameisjayant.noteapp.features.notes.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nameisjayant.noteapp.features.notes.ui.screens.AddNoteScreen
import com.nameisjayant.noteapp.features.notes.ui.screens.ShowNotesScreen
import com.nameisjayant.noteapp.features.notes.ui.screens.UpdateNoteScreen


@Composable
fun NoteNavigation() {

    val navHostController = rememberNavController()

    NavHost(navController = navHostController, startDestination = Show_Notes) {
        composable(Show_Notes) {
            ShowNotesScreen(navHostController)
        }
        composable(Add_Notes) {
            AddNoteScreen(navHostController)
        }
        composable(Update_Notes){
            UpdateNoteScreen(navHostController = navHostController)
        }
    }
    

}

const val Show_Notes = "show_notes"
const val Add_Notes = "add_notes"
const val Update_Notes = "update_notes"