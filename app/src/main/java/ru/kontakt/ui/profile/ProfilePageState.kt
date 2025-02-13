package ru.kontakt.ui.profile

sealed interface ProfilePageState {
    data object Unauthorized : ProfilePageState
    data class LoggedIn(val fullName: String, val profilePhotoUrl: String?) : ProfilePageState
}