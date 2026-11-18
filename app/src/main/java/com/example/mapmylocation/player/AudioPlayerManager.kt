package com.example.mapmylocation.player

import android.content.Context
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.mapmylocation.data.models.MusicPlayerState
import com.example.mapmylocation.data.models.RecommendedTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AudioPlayerManager(private val context: Context) {

    private var exoPlayer: ExoPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var progressUpdateJob: Job? = null

    private val _playerState = MutableStateFlow(MusicPlayerState())
    val playerState: StateFlow<MusicPlayerState> = _playerState.asStateFlow()

    init {
        initializePlayer()
    }

    private fun initializePlayer() {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build().apply {
                repeatMode = Player.REPEAT_MODE_ONE
                addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        _playerState.update { it.copy(isPlaying = isPlaying) }
                        if (isPlaying) {
                            startProgressUpdates()
                        } else {
                            stopProgressUpdates()
                        }
                    }

                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == Player.STATE_READY) {
                            val duration = duration.coerceAtLeast(0L)
                            _playerState.update {
                                it.copy(
                                    durationMs = duration,
                                    isLoaded = true
                                )
                            }
                        }
                    }
                })
            }
        }
    }

    fun playTrack(track: RecommendedTrack, forcePlay: Boolean = true) {
        val player = exoPlayer ?: return
        val currentTrack = _playerState.value.currentTrack

        if (currentTrack?.id == track.id && player.playbackState == Player.STATE_READY) {
            if (forcePlay && !player.isPlaying) {
                player.play()
            }
            return
        }

        val rawUri = Uri.parse("android.resource://${context.packageName}/${track.rawResId}")
        val mediaItem = MediaItem.fromUri(rawUri)

        player.setMediaItem(mediaItem)
        player.prepare()

        _playerState.update {
            it.copy(
                currentTrack = track,
                currentPositionMs = 0L,
                durationMs = track.durationSeconds * 1000L,
                isLoaded = true
            )
        }

        if (forcePlay) {
            player.play()
        }
    }

    fun pause() {
        exoPlayer?.pause()
        _playerState.update { it.copy(isPlaying = false) }
    }

    fun resume() {
        exoPlayer?.play()
        _playerState.update { it.copy(isPlaying = true) }
    }

    fun stop() {
        exoPlayer?.stop()
        stopProgressUpdates()
        _playerState.update {
            it.copy(
                isPlaying = false,
                currentPositionMs = 0L
            )
        }
    }

    fun seekTo(positionMs: Long) {
        exoPlayer?.seekTo(positionMs)
        _playerState.update { it.copy(currentPositionMs = positionMs) }
    }

    fun skipNext() {
        val state = _playerState.value
        val playlist = state.playlist
        if (playlist.isEmpty()) return
        val currentIndex = playlist.indexOfFirst { it.id == state.currentTrack?.id }
        val nextIndex = if (currentIndex != -1 && currentIndex < playlist.size - 1) currentIndex + 1 else 0
        playTrack(playlist[nextIndex], forcePlay = true)
    }

    fun skipPrevious() {
        val state = _playerState.value
        val playlist = state.playlist
        if (playlist.isEmpty()) return
        val currentIndex = playlist.indexOfFirst { it.id == state.currentTrack?.id }
        val prevIndex = if (currentIndex > 0) currentIndex - 1 else playlist.size - 1
        playTrack(playlist[prevIndex], forcePlay = true)
    }

    fun setPlaylist(playlist: List<RecommendedTrack>) {
        _playerState.update { it.copy(playlist = playlist) }
    }

    fun setAutoMusic(enabled: Boolean) {
        _playerState.update { it.copy(autoMusicEnabled = enabled) }
    }

    private fun startProgressUpdates() {
        stopProgressUpdates()
        progressUpdateJob = scope.launch {
            while (true) {
                val player = exoPlayer ?: break
                if (player.isPlaying) {
                    val pos = player.currentPosition.coerceAtLeast(0L)
                    val dur = player.duration.coerceAtLeast(0L)
                    _playerState.update {
                        it.copy(
                            currentPositionMs = pos,
                            durationMs = if (dur > 0) dur else it.durationMs
                        )
                    }
                }
                delay(500)
            }
        }
    }

    private fun stopProgressUpdates() {
        progressUpdateJob?.cancel()
        progressUpdateJob = null
    }

    fun release() {
        stopProgressUpdates()
        exoPlayer?.release()
        exoPlayer = null
    }
}
