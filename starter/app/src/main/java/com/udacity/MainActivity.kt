package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var URL = ""

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var loadingButton: LoadingButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        loadingButton= findViewById(R.id.custom_button)

        loadingButton.setOnClickListener {
            if (radio_group.checkedRadioButtonId != -1) {
                download()
                loadingButton.setButtonState(ButtonState.Clicked)
            } else {
                Toast.makeText(
                    this,
                    resources.getString(R.string.select_project),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }



    private fun download() {
        getUrl()
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            val action = intent?.action

            if (id == downloadID) {
                if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                    /**
                     * get the download status & pass it to the notification
                     * (so that it could then send it to the detail activity)
                     */
                    val query = DownloadManager.Query()
                    query.setFilterById(id)
                    val manager = context!!.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    val cursor = manager.query(query)

                    if (cursor.moveToFirst()) {
                        if (cursor.count > 0) {
                            val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                                Log.d("ggg", "success")
                                /* notificationManager.sendNotification(repoSelected,context,"Download successful")*/
                            } else {
                                Log.d("ggg", "fail")
                                /* notificationManager.sendNotification(repoSelected,context,"Download failed")*/
                            }
                            loadingButton.setButtonState(ButtonState.Completed)
                        }
                    }
                }
            }
        }

    }

    private fun getUrl() {
        when(radio_group.checkedRadioButtonId){
            R.id.radio_glide -> URL = GLIDE_URL
            R.id.radio_load_app -> URL = LOAD_UP_URL
            R.id.radio_retrofit -> URL = RETROFIT_URL
        }
    }

    companion object {
        private const val LOAD_UP_URL = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val GLIDE_URL = "https://github.com/bumptech/glide/archive/master.zip"
            private const val RETROFIT_URL = "https://github.com/square/retrofit/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

}
