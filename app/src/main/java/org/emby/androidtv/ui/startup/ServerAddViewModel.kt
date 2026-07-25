package org.emby.androidtv.ui.startup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.emby.androidtv.auth.model.ServerAdditionState
import org.emby.androidtv.auth.repository.ServerRepository

class ServerAddViewModel(
	private val serverRepository: ServerRepository,
) : ViewModel() {
	private val _state = MutableStateFlow<ServerAdditionState?>(null)
	val state = _state.asStateFlow()

	fun addServer(address: String) {
		serverRepository.addServer(address).onEach { state ->
			_state.value = state
		}.launchIn(viewModelScope)
	}
}
