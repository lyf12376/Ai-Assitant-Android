package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapplication.page.chatPage.ChatPage
import com.example.myapplication.page.modelPage.ModelPage
import com.example.myapplication.page.recordPage.RecordPage

@Composable
fun NavigationGraph(
    navHostController: NavHostController,
    startDestination: String = Screen.RecordPage.route,
){

    NavHost(navController = navHostController, startDestination = startDestination){
        composable(Screen.RecordPage.route){
            RecordPage(navHostController)
        }
        composable(Screen.ModelPage.route){
            ModelPage(navHostController)
        }
        composable(
            route = Screen.ChatPage.route + "/{model}" + "/{id}",
            arguments = listOf(navArgument("model") { type = NavType.StringType }, navArgument("id"){type = NavType.IntType})
        ) {
            val model = it.arguments?.getString("model") ?: "GPT-4o"
            val chatId = it.arguments?.getInt("id")?:0
            ChatPage(navHostController, model,chatId)
        }
    }
}