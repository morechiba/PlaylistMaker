package com.tagomago.playlistmaker.domain.impl

import com.tagomago.playlistmaker.domain.api.TrackInteractor
import com.tagomago.playlistmaker.domain.api.TrackRepository
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun search(expression: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {

           consumer.consume(repository.search(expression), repository.resultCode)

        }
    }
}