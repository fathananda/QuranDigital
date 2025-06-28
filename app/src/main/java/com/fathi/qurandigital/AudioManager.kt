package com.fathi.qurandigital

import android.content.Context
import android.media.MediaPlayer
import android.os.Environment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioManager @Inject constructor(
    private val context: Context
) {
    private var mediaPlayer: MediaPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Main)
    private var progressJob: Job? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPosition = MutableStateFlow(0L)
    val currentPosition: StateFlow<Long> = _currentPosition.asStateFlow()

    private val _duration = MutableStateFlow(0L)
    val duration: StateFlow<Long> = _duration.asStateFlow()

    fun playAudio(url: String) {
        try {
            release()
            mediaPlayer = MediaPlayer().apply {
                setDataSource(url)
                setOnPreparedListener { mp ->
                    mp.start()
                    _isPlaying.value = true
                    _duration.value = mp.duration.toLong()
                    startProgressUpdater()
                }
                setOnCompletionListener {
                    _isPlaying.value = false
                    _currentPosition.value = 0L
                    stopProgressUpdater()
                }
                setOnErrorListener { _, what, extra ->
                    _isPlaying.value = false
                    true
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            _isPlaying.value = false
        }
    }

    fun pause() {
        mediaPlayer?.let { mp ->
            if (mp.isPlaying) {
                mp.pause()
                _isPlaying.value = false
                stopProgressUpdater()
            }
        }
    }

    fun resume() {
        mediaPlayer?.let { mp ->
            if (!mp.isPlaying) {
                mp.start()
                _isPlaying.value = true
                startProgressUpdater()
            }
        }
    }

    fun stop() {
        mediaPlayer?.let { mp ->
            if (mp.isPlaying) {
                mp.stop()
            }
            _isPlaying.value = false
            _currentPosition.value = 0L
            stopProgressUpdater()
        }
    }

    fun seekTo(position: Long) {
        mediaPlayer?.seekTo(position.toInt())
        _currentPosition.value = position
    }

    suspend fun downloadAudio(url: String, fileName: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val downloadDir = File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "Murottal")
                if (!downloadDir.exists()) {
                    downloadDir.mkdirs()
                }

                val file = File(downloadDir, "$fileName.mp3")
                if (file.exists()) return@withContext true

                val connection = URL(url).openConnection()
                connection.connect()

                val input = connection.getInputStream()
                val output = FileOutputStream(file)

                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (input.read(buffer).also { bytesRead = it } != -1) {
                    output.write(buffer, 0, bytesRead)
                }

                output.close()
                input.close()
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    private fun startProgressUpdater() {
        progressJob = scope.launch {
            while (_isPlaying.value) {
                mediaPlayer?.let { mp ->
                    if (mp.isPlaying) {
                        _currentPosition.value = mp.currentPosition.toLong()
                    }
                }
                delay(1000)
            }
        }
    }

    private fun stopProgressUpdater() {
        progressJob?.cancel()
        progressJob = null
    }

    fun release() {
        stopProgressUpdater()
        mediaPlayer?.release()
        mediaPlayer = null
        _isPlaying.value = false
        _currentPosition.value = 0L
        _duration.value = 0L
    }
}