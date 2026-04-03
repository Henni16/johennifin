package org.jellyfin.androidtv.data.compat

import org.jellyfin.sdk.model.api.PlaybackErrorCode

class PlaybackException(val errorCode: PlaybackErrorCode = PlaybackErrorCode.NOT_ALLOWED) : RuntimeException("Playback failed with error code: $errorCode")
