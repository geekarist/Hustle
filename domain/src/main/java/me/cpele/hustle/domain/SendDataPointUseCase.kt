package me.cpele.hustle.domain

import kotlinx.coroutines.*

class SendDataPointUseCase(private val dataPointRepository: DataPointRepository) {

    suspend fun executeAsync(timer: EggTimer): Deferred<Response> {
        val deferred = CompletableDeferred<Response>()
        try {
            timer.pause()
            val elapsedMillis = timer.elapsedMillis
            withContext(Dispatchers.IO) {
                dataPointRepository.insert(elapsedMillis)
                timer.reset()
                deferred.complete(Response("TODO: Send $elapsedMillis to Beeminder"))
            }
        } catch (e: CancellationException) {
            deferred.complete(Response("TODO: Sending cancelled"))
        } catch (e: Error) {
            deferred.complete(Response("TODO: Error"))
        }
        return deferred
    }

    data class Response(val message: String)
}
