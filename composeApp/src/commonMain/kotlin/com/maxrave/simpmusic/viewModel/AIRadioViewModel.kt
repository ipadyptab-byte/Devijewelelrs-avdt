package com.maxrave.simpmusic.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxrave.domain.data.entities.SongEntity
import com.maxrave.domain.repository.YoutubeRepository
import com.maxrave.logger.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AIRadioViewModel(
    private val youtubeRepository: YoutubeRepository,
) : ViewModel() {

    private val tag = "AIRadioViewModel"

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<SongEntity>>(emptyList())
    val searchResults: StateFlow<List<SongEntity>> = _searchResults.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    private val _currentRadioSong = MutableStateFlow<SongEntity?>(null)
    val currentRadioSong: StateFlow<SongEntity?> = _currentRadioSong.asStateFlow()

    private val _radioQueue = MutableStateFlow<List<SongEntity>>(emptyList())
    val radioQueue: StateFlow<List<SongEntity>> = _radioQueue.asStateFlow()

    private val _isRadioActive = MutableStateFlow(false)
    val isRadioActive: StateFlow<Boolean> = _isRadioActive.asStateFlow()

    private val _relatedSongs = MutableStateFlow<List<SongEntity>>(emptyList())
    val relatedSongs: StateFlow<List<SongEntity>> = _relatedSongs.asStateFlow()

    /**
     * Search for songs based on query
     */
    fun searchSongs(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }

        viewModelScope.launch {
            _isSearching.value = true
            try {
                val result = youtubeRepository.search(query, "song", 20)
                _searchResults.value = result.map { it.toSongEntity() }
                Logger.d(tag, "Found ${_searchResults.value.size} songs for query: $query")
            } catch (e: Exception) {
                Logger.e(tag, "Search failed: ${e.message}")
                _searchResults.value = emptyList()
            } finally {
                _isSearching.value = false
            }
        }
    }

    /**
     * Start AI Radio with selected song
     * Get related songs and play them in sequence
     */
    fun startRadio(song: SongEntity) {
        viewModelScope.launch {
            _currentRadioSong.value = song
            _isRadioActive.value = true
            _radioQueue.value = emptyList()
            _relatedSongs.value = emptyList()

            try {
                // Get related songs for the selected song
                val relatedResult = youtubeRepository.getRelatedSongs(song.videoId, 20)
                val related = relatedResult.map { it.toSongEntity() }

                _relatedSongs.value = related
                _radioQueue.value = related.shuffled() // Shuffle for variety

                Logger.d(tag, "Started radio with song: ${song.title}, got ${related.size} related songs")
            } catch (e: Exception) {
                Logger.e(tag, "Failed to get related songs: ${e.message}")
            }
        }
    }

    /**
     * Stop the AI Radio
     */
    fun stopRadio() {
        _isRadioActive.value = false
        _currentRadioSong.value = null
        _radioQueue.value = emptyList()
        _relatedSongs.value = emptyList()
    }

    /**
     * Called when current song finishes playing
     * Play next song from queue
     */
    fun onSongFinished() {
        if (!_isRadioActive.value) return

        val queue = _radioQueue.value
        if (queue.isNotEmpty()) {
            val nextSong = queue.first()
            val newQueue = queue.drop(1)

            // Add current song to end of queue for loop
            _currentRadioSong.value?.let { current ->
                _radioQueue.value = newQueue + current
            }

            _currentRadioSong.value = nextSong
            Logger.d(tag, "Playing next song: ${nextSong.title}")
        } else {
            // Queue empty, try to get more related songs
            _currentRadioSong.value?.let { current ->
                loadMoreRelatedSongs(current.videoId)
            }
        }
    }

    /**
     * Load more related songs when queue is empty
     */
    private suspend fun loadMoreRelatedSongs(videoId: String) {
        try {
            val moreResult = youtubeRepository.getRelatedSongs(videoId, 20)
            val more = moreResult.map { it.toSongEntity() }

            if (more.isNotEmpty()) {
                _radioQueue.value = more.shuffled()
                val nextSong = _radioQueue.value.first()
                _radioQueue.value = _radioQueue.value.drop(1)
                _currentRadioSong.value = nextSong
                Logger.d(tag, "Loaded ${more.size} more related songs")
            }
        } catch (e: Exception) {
            Logger.e(tag, "Failed to load more songs: ${e.message}")
        }
    }

    /**
     * Skip to next song in radio
     */
    fun skipToNext() {
        onSongFinished()
    }

    /**
     * Change the base song and get new related songs
     */
    fun changeRadioSeed(song: SongEntity) {
        startRadio(song)
    }

    /**
     * Shuffle the radio queue
     */
    fun shuffleQueue() {
        _radioQueue.value = _radioQueue.value.shuffled()
    }
}
