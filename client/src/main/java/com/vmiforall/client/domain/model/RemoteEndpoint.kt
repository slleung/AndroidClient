package com.vmiforall.client.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RemoteEndpoint(val ip : String, val port : Int) : Parcelable
