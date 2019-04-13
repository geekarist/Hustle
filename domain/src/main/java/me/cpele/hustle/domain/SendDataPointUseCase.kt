package me.cpele.hustle.domain

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

class SendDataPointUseCase(private val dataPointRepository: DataPointRepository) {

    suspend fun executeAsync(timer: EggTimer): Deferred<Response> {
        val deferred = CompletableDeferred<Response>()
        timer.pause()
        val elapsedMillis = timer.elapsedMillis
        try {
            dataPointRepository.insert(elapsedMillis)
            timer.reset()
            deferred.complete(Response("TODO: Send $elapsedMillis to Beeminder"))
        } catch (e: CancellationException) {
            deferred.complete(Response("TODO: Sending cancelled"))
        } catch (e: Error) {
            deferred.complete(Response("TODO: Error"))
        }
        return deferred
    }

    data class Response(val message: String)
}
