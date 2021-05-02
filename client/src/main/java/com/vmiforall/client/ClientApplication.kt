package com.vmiforall.client

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

// need to annotate the base application for hilt
@HiltAndroidApp
class ClientApplication : Application() {
}
