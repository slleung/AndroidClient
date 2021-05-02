package com.vmiforall.client.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.vmiforall.client.domain.model.RemoteEndpoint
import com.vmiforall.client.ui.remotecontrol.RemoteControlScreen
import com.vmiforall.client.ui.remotecontrol.RemoteControlViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.slf4j.LoggerFactory

// The main activity. This is really just the navigation controller.
private val logger = LoggerFactory.getLogger(MainActivity::class.java)

// TODO this will be supplied in another way eventually
private val remoteEndpoint = RemoteEndpoint("10.0.2.2", 8888)
//private val remoteEndpoint = RemoteEndpoint("127.0.0.1", 8080)

// navigation scopes (for nested navigation)
sealed class NavGraph(val name: String) {
    object RemoteControlNavGraph : NavGraph("RemoteControlNavGraph")
}

// navigation destinations
sealed class Destination(val destinationRoute: String) {
    object RemoteControlScreen : Destination("RemoteControlScreen/{remoteEndpoint}")
}

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = NavGraph.RemoteControlNavGraph.name
            ) {
                navigation(
                    route = NavGraph.RemoteControlNavGraph.name,
                    startDestination = Destination.RemoteControlScreen.destinationRoute
                ) {
                    composable(Destination.RemoteControlScreen.destinationRoute,
                        arguments = listOf(navArgument("remoteEndpoint") {
                            type = NavType.ParcelableType(RemoteEndpoint::class.java)
                        })
                    ) { backStackEntry ->
                        // this view model is scoped to the navigation graph
                        backStackEntry.arguments?.putParcelable(RemoteControlViewModel.NAV_ARG_KEY_REMOTE_ENDPOINT, remoteEndpoint)
                        val remoteControlViewModel = hiltNavGraphViewModel<RemoteControlViewModel>(backStackEntry = backStackEntry)
                        RemoteControlScreen(navController, remoteControlViewModel)
                    }
                }
            }
        }
    }
}
