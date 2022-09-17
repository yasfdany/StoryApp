package dev.studiocloud.storyapp.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dev.studiocloud.storyapp.R

class ManagePermissions(private val activity: Activity, private val list: List<String>, private val code:Int) {
    fun checkPermissions(onGranted: () -> Unit) {
        if (isPermissionsGranted() != PackageManager.PERMISSION_GRANTED) {
            showAlert()
        } else {
            onGranted()
        }
    }

    private fun isPermissionsGranted(): Int {
        var counter = 0
        for (permission in list) {
            counter += ContextCompat.checkSelfPermission(activity, permission)
        }
        return counter
    }

    private fun deniedPermission(): String {
        for (permission in list) {
            if (ContextCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_DENIED) return permission
        }
        return ""
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.need_permission)
        builder.setMessage(R.string.need_permission_description)
        builder.setPositiveButton(R.string.ok) { _, _ -> requestPermissions() }
        builder.setNegativeButton(R.string.cancel, null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun requestPermissions() {
        val permission = deniedPermission()
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            Toast.makeText(activity, R.string.need_permission_description, Toast.LENGTH_SHORT).show()
        } else {
            ActivityCompat.requestPermissions(activity, list.toTypedArray(), code)
        }
    }

}