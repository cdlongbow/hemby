package org.emby.androidtv.ui.startup

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.emby.androidtv.auth.model.AuthenticatingState
import org.emby.androidtv.auth.model.AutomaticAuthenticateMethod
import org.emby.androidtv.auth.model.CredentialAuthenticateMethod
import org.emby.androidtv.auth.model.LoginState
import org.emby.androidtv.auth.model.QuickConnectState
import org.emby.androidtv.auth.model.Server
import org.emby.androidtv.auth.model.UnavailableQuickConnectState
import org.emby.androidtv.auth.model.UnknownQuickConnectState
import org.emby.androidtv.auth.model.User
import org.emby.androidtv.auth.repository.AuthenticationRepository
import org.emby.androidtv.auth.repository.ServerRepository
import org.jellyfin.sdk.Jellyfin
import org.jellyfin.sdk.model.DeviceInfo
import java.util.UUID

class UserLoginViewModel(
	@Suppress("UNUSED_PARAMETER") jellyfin: Jellyfin,
	private val serverRepository: ServerRepository,
	private val authenticationRepository: AuthenticationRepository,
	private val defaultDeviceInfo: DeviceInfo,
) : ViewModel() {
	private val _loginState = MutableStateFlow<LoginState?>(null)
	val loginState = _loginState.asStateFlow()

	var forcedUsername: String? = null

	private val _server = MutableStateFlow<Server?>(null)
	val server = _server.asStateFlow()

	private val _quickConnectState = MutableStateFlow<QuickConnectState>(UnknownQuickConnectState)
	val quickConnectState = _quickConnectState.asStateFlow()
	fun authenticate(server: Server, user: User): Flow<LoginState> =
		authenticationRepository.authenticate(server, AutomaticAuthenticateMethod(user))

	fun login(username: String, password: String) {
		val server = server.value ?: return
		_loginState.value = AuthenticatingState
		authenticationRepository.authenticate(server, CredentialAuthenticateMethod(username, password)).onEach {
			_loginState.value = it
		}.launchIn(viewModelScope)
	}

	fun clearLoginState() {
		_loginState.value = null
		_quickConnectState.value = UnknownQuickConnectState
	}

	suspend fun initiateQuickconnect() {
		_quickConnectState.emit(UnavailableQuickConnectState)
	}

	fun setServer(id: UUID?) {
		_server.value = serverRepository.storedServers.value
			.firstOrNull { it.id == id }
	}
}
