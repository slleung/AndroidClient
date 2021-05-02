package com.vmiforall.client.data

import android.view.MotionEvent
import com.vmiforall.client.domain.model.RemoteEndpoint
import kotlinx.coroutines.flow.Flow

interface RemoteControlRepository {

    suspend fun initClient(remoteEndpoint: RemoteEndpoint)

    fun shutDownClient()

    fun isConnected() : Boolean

    fun sendMotionEvent(motionEvent: MotionEvent)

}
