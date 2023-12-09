/*
 * Station.kt
 * Implements the Station class
 * A Station object holds the base data of a radio
 *
 * This file is part of
 * TRANSISTOR - Radio App for Android
 *
 * Copyright (c) 2015-23 - Y20K.org
 * Licensed under the MIT-License
 * http://opensource.org/licenses/MIT
 */


package org.y20k.transistor.core

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.media3.common.MimeTypes
import com.google.gson.annotations.Expose
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import org.y20k.transistor.Keys
import java.util.Date
import java.util.UUID


/*
 * Station class
 */
@Keep
@Parcelize
data class Station (@Expose val uuid: String = UUID.randomUUID().toString(),
                    @Expose var starred: Boolean = false,
                    @Expose var name: String = String(),
                    @Expose var nameManuallySet: Boolean = false,
                    @Expose var streamUris: MutableList<String> = mutableListOf<String>(),
                    @Expose var stream: Int = 0,
                    @Expose var streamContent: String = Keys.MIME_TYPE_UNSUPPORTED,
                    @Expose var homepage: String = String(),
                    @Expose var image: String = String(),
                    @Expose var smallImage: String = String(),
                    @Expose var imageColor: Int = -1,
                    @Expose var imageManuallySet: Boolean = false,
                    @Expose var remoteImageLocation: String = String(),
                    @Expose var remoteStationLocation: String = String(),
                    @Expose var modificationDate: Date = Keys.DEFAULT_DATE,
                    @Expose var isPlaying: Boolean = false,
                    @Expose var radioBrowserStationUuid: String = String(),
                    @Expose var codec: String = String(),
                    @Expose var bitrate: Int = 0,
                    @Expose var radioBrowserChangeUuid: String = String()): Parcelable {

    /* Define log tag */
    @IgnoredOnParcel
    private val TAG: String = Station::class.java.simpleName


    /* overrides toString method */
    override fun toString(): String {
        val stringBuilder: StringBuilder = StringBuilder()
        stringBuilder.append("Name: ${name}\n")
        if (streamUris.isNotEmpty()) stringBuilder.append("Stream: ${streamUris[stream]}\n")
        stringBuilder.append("Last Update: ${modificationDate}\n")
        stringBuilder.append("Content-Type: ${streamContent}\n")
        return stringBuilder.toString()
    }


    /* Getter for currently selected stream */
    fun getStreamUri(): String {
        if (streamUris.isNotEmpty()) {
            return streamUris[stream]
        } else {
            return String()
        }
    }


    /* Checks if a Station has the minimum required elements / data */
    fun isValid(): Boolean {
        return uuid.isNotEmpty() && name.isNotEmpty() && streamUris.isNotEmpty() && streamUris[stream].isNotEmpty() && modificationDate != Keys.DEFAULT_DATE && streamContent != Keys.MIME_TYPE_UNSUPPORTED
    }


    /* Get the correct Mime type - used for building a MediaItem  */
    fun getMediaType(): String {
        if (Keys.MIME_TYPES_MPEG.contains(streamContent)) {
            return MimeTypes.AUDIO_MPEG
        } else if (Keys.MIME_TYPES_AAC.contains(streamContent)) {
            return MimeTypes.AUDIO_AAC
        } else if (Keys.MIME_TYPES_HLS.contains(streamContent)) {
            return MimeTypes.APPLICATION_M3U8
        } else {
            return MimeTypes.AUDIO_UNKNOWN
        }
    }


    /* Creates a deep copy of a Station */
    fun deepCopy(): Station {
        return Station(uuid = uuid,
                       starred = starred,
                       name = name,
                       nameManuallySet = nameManuallySet,
                       streamUris = streamUris,
                       stream = stream,
                       streamContent = streamContent,
                       homepage = homepage,
                       image = image,
                       smallImage = smallImage,
                       imageColor = imageColor,
                       imageManuallySet = imageManuallySet,
                       remoteImageLocation = remoteImageLocation,
                       remoteStationLocation = remoteStationLocation,
                       modificationDate = modificationDate,
                       isPlaying = isPlaying,
                       radioBrowserStationUuid = radioBrowserStationUuid,
                       radioBrowserChangeUuid = radioBrowserChangeUuid,
                       codec = codec,
                       bitrate = bitrate)
    }
}
