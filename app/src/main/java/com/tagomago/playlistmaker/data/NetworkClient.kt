package com.tagomago.playlistmaker.data

import com.tagomago.playlistmaker.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}