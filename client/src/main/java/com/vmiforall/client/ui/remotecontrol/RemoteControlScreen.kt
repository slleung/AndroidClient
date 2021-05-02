package com.vmiforall.client.ui.remotecontrol

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import org.webrtc.PeerConnectionFactory
import org.webrtc.SurfaceViewRenderer

@Composable
fun RemoteControlScreen(
    navController: NavController,
    remoteControlViewModel: RemoteControlViewModel
) {
    InputCapturerOverlay(remoteControlViewModel)
}

@Composable
fun WebRTCVideoView() {
    AndroidView(modifier = Modifier.fillMaxSize(),
    factory = { context ->
        SurfaceViewRenderer(context).apply {
            setMirror(true)
            setEnableHardwareScaler(true)
//            setScalingType()
        }
    })
}

/**
 * A transparent surface for capturing touch inputs.
 */
@Composable
fun InputCapturerOverlay(remoteControlViewModel: RemoteControlViewModel) {
    Surface(color = Color.Black, modifier = Modifier
        .fillMaxSize()
        .pointerInteropFilter {
            remoteControlViewModel.onMotionEventCaptured(it)
            true
        }) {
    }
}
