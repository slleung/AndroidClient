package com.vmiforall.client.data

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import com.vmiforall.client.IoDispatcher
import com.vmiforall.client.domain.model.RemoteEndpoint
import com.vmiforall.client.domain.model.toMotionEventProto
import com.vmiforall.remotecontrol.RemoteControlGrpcKt
import com.vmiforall.remotecontrol.RemoteControlProto
import dagger.hilt.android.qualifiers.ApplicationContext
import io.grpc.ManagedChannel
import io.grpc.android.AndroidChannelBuilder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

private val logger = LoggerFactory.getLogger(RemoteControlRepositoryImpl::class.java)

@Singleton
class RemoteControlRepositoryImpl @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : RemoteControlRepository {

    private lateinit var managedChannel: ManagedChannel
    private lateinit var remoteControlStub: RemoteControlGrpcKt.RemoteControlCoroutineStub

    private lateinit var motionEventRequestFlow: MutableSharedFlow<RemoteControlProto.SendMotionEventRequest>

    @SuppressLint("CheckResult")
    override suspend fun initClient(remoteEndpoint: RemoteEndpoint) {
//        CronetProviderInstaller.installProvider(applicationContext)
//            .addOnSuccessListener {
//                managedChannel = CronetChannelBuilder.forAddress(remoteEndpoint.ip, remoteEndpoint.port).build()
//                remoteControlStub = RemoteControlGrpcKt.RemoteControlCoroutineStub(managedChannel)
//            }
//            .addOnFailureListener { exception ->
//                when (exception) {
//                    is GooglePlayServicesRepairableException,
//                    is GooglePlayServicesNotAvailableException -> {
//                        logger.error("GooglePlayServicesNotAvailableException")
//                        managedChannel = AndroidChannelBuilder.forAddress(remoteEndpoint.ip, remoteEndpoint.port).usePlaintext().build()
//                        remoteControlStub = RemoteControlGrpcKt.RemoteControlCoroutineStub(managedChannel)
//                        remoteControlStub.addInputMotionEventStream()
//                    }
//                }
//            }
        logger.debug("initClient: $remoteEndpoint")
        managedChannel =
            AndroidChannelBuilder.forAddress(remoteEndpoint.ip, remoteEndpoint.port)
                .usePlaintext()
                .idleTimeout(5, TimeUnit.MINUTES)
                .build()
        remoteControlStub = RemoteControlGrpcKt.RemoteControlCoroutineStub(managedChannel)

        withContext(ioDispatcher) {
            motionEventRequestFlow = MutableSharedFlow(
                extraBufferCapacity = 64,
                onBufferOverflow = BufferOverflow.DROP_OLDEST
            )
            remoteControlStub.sendMotionEventStream(motionEventRequestFlow)
        }
    }

    override fun shutDownClient() {
        managedChannel.shutdown()
    }

    override fun isConnected(): Boolean {
        return managedChannel.isShutdown || managedChannel.isTerminated
    }

    override fun sendMotionEvent(motionEvent: MotionEvent) {
        val sendMotionEventRequest = RemoteControlProto.SendMotionEventRequest.newBuilder()
            .setMotionEvent(motionEvent.toMotionEventProto())
            .build()

        motionEventRequestFlow.tryEmit(sendMotionEventRequest)
    }

}
