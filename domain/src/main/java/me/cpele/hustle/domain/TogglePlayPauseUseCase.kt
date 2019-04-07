package me.cpele.hustle.domain

class TogglePlayPauseUseCase(private val stringProvider: StringProvider) {

    fun execute(request: TogglePlayPauseUseCase.Request): TogglePlayPauseUseCase.Response {
        val isPlaying = !request.wasPlaying
        val label =
            if (isPlaying) stringProvider.mainPause
            else stringProvider.mainPlay
        return Response(isPlaying, label)
    }

    data class Request(val wasPlaying: Boolean)
    data class Response(val playing: Boolean, val label: String)

    interface StringProvider {
        val mainPause: String
        val mainPlay: String
    }
}
