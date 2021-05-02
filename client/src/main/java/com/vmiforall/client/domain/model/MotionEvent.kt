package com.vmiforall.client.domain.model

import android.view.MotionEvent
import com.vmiforall.remotecontrol.RemoteControlProto

fun MotionEvent.toMotionEventProto(): RemoteControlProto.MotionEvent {

    val motionEventProtoBuilder = RemoteControlProto.MotionEvent.newBuilder()
        .setAction(action)
        .setPointerCount(pointerCount)
        .setMetaState(metaState)
        .setButtonState(buttonState)
        .setXPrecision(xPrecision)
        .setYPrecision(yPrecision)
        .setDeviceId(deviceId)
        .setEdgeFlags(edgeFlags)
        .setSource(source)
        .setFlags(flags)

    for (i in 0 until pointerCount) {
        val pointerProperties = MotionEvent.PointerProperties()
        val pointerCoords = MotionEvent.PointerCoords()

        getPointerProperties(i, pointerProperties)
        getPointerCoords(i, pointerCoords)

        motionEventProtoBuilder.addPointerProperties(
            RemoteControlProto.PointerProperties.newBuilder().apply {
                id = pointerProperties.id
                toolType = pointerProperties.toolType
            })

        motionEventProtoBuilder.addPointerCoords(
            RemoteControlProto.PointerCoords.newBuilder().apply {
                x = pointerCoords.x
                y = pointerCoords.y
                pressure = pointerCoords.pressure
                size = pointerCoords.size
                touchMajor = pointerCoords.touchMajor
                touchMinor = pointerCoords.touchMinor
                toolMajor = pointerCoords.toolMajor
                toolMinor = pointerCoords.toolMinor
                orientation = pointerCoords.orientation
            })
    }

    return motionEventProtoBuilder.build()
}
