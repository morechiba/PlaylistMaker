package com.tagomago.playlistmaker.data

interface StorageClient<T> {

    fun saveData(data: T)
    fun getData(): T?
    fun clearData()

}