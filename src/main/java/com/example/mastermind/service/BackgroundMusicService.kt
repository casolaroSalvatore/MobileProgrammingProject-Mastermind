package com.example.mastermind.service

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.IBinder
import com.example.mastermind.R
import com.example.mastermind.data.preferences.PreferencesManager
import kotlinx.coroutines.*

class BackgroundMusicService : Service() {

    private lateinit var player: MediaPlayer
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate() {
        super.onCreate()

        // AudioAttributes: usa STREAM_MUSIC per non essere zittito
        player = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build()
            )
            // Carica dal row
            setDataSource(
                resources.openRawResourceFd(R.raw.background).fileDescriptor,
                resources.openRawResourceFd(R.raw.background).startOffset,
                resources.openRawResourceFd(R.raw.background).length
            )
            isLooping = true
            prepare()
            start()
        }

        // Sincronizza con il volume preferenze
        val pref = PreferencesManager(this)
        scope.launch {
            pref.floatFlow("music_vol", 0.8f).collect { v ->
                player.setVolume(v, v)
            }
        }
    }

    // START_STICKY indica che il volume resta invariato
    override fun onStartCommand(i: Intent?, f: Int, id: Int): Int = START_STICKY

    override fun onDestroy() {
        player.release()
        scope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}

