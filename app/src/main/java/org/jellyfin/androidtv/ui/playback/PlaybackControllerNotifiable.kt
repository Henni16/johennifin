package org.jellyfin.androidtv.ui.playback

interface PlaybackControllerNotifiable {
	fun onCompletion()
	fun onError(error: Throwable? = null)
	fun onPrepared()
	fun onProgress()
	fun onPlaybackSpeedChange(newSpeed: Float)
}
