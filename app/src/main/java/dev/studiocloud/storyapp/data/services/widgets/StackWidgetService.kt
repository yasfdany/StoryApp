package dev.studiocloud.storyapp.data.services.widgets

import android.content.Intent
import android.widget.RemoteViewsService
import dev.studiocloud.storyapp.data.receivers.widgets.StackRemoteViewsFactory

class StackWidgetService: RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory =
        StackRemoteViewsFactory(this.applicationContext)
}