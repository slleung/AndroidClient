package com.vmiforall.client.ui.remotecontrol

import android.app.Application
import android.content.Context
import android.view.MotionEvent
import androidx.lifecycle.*
import com.vmiforall.client.DefaultDispatcher
import com.vmiforall.client.data.RemoteControlRepository
import com.vmiforall.client.domain.model.RemoteEndpoint
import com.vmiforall.client.domain.model.toMotionEventProto
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.webrtc.DefaultVideoDecoderFactory
import org.webrtc.DefaultVideoEncoderFactory
import org.webrtc.EglBase
import org.webrtc.PeerConnectionFactory
import javax.inject.Inject

private val logger = LoggerFactory.getLogger(RemoteControlViewModel::class.java)


@HiltViewModel
class RemoteControlViewModel @Inject constructor(
    private val application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val remoteControlRepository: RemoteControlRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    companion object {
        const val NAV_ARG_KEY_REMOTE_ENDPOINT = "NAV_ARG_REMOTE_ENDPOINT"
    }

//    private val _peerConnectionFactory = MutableLiveData<PeerConnectionFactory>()
//    val peerConnectionFactory: PeerConnectionFactory
//        get() = _peerConnectionFactory.value

    init {
        val remoteEndpoint = savedStateHandle.get<RemoteEndpoint>(NAV_ARG_KEY_REMOTE_ENDPOINT)
        if (remoteEndpoint != null) {
            viewModelScope.launch(defaultDispatcher) {
                remoteControlRepository.initClient(remoteEndpoint)
            }

//            val options = PeerConnectionFactory.InitializationOptions.builder(application.applicationContext)
//                .setFieldTrials("WebRTC-H264HighProfile/Enabled/")
//                .createInitializationOptions()
//            PeerConnectionFactory.initialize(options)
//
//            val rootEglBase = EglBase.create()
//            PeerConnectionFactory
//                .builder()
//                .setVideoDecoderFactory(DefaultVideoDecoderFactory(rootEglBase.eglBaseContext))
//                .setVideoEncoderFactory(DefaultVideoEncoderFactory(rootEglBase.eglBaseContext, true, true))
//                .createPeerConnectionFactory()
        }
    }

    override fun onCleared() {
        super.onCleared()
        remoteControlRepository.shutDownClient()
    }

    fun onMotionEventCaptured(motionEvent: MotionEvent) {
        remoteControlRepository.sendMotionEvent(motionEvent)
    }

}
