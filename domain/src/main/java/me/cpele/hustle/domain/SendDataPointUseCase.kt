package me.cpele.hustle.domain

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SendDataPointUseCase(private val dataPointRepository: DataPointRepository) {

    suspend fun execute(timer: EggTimer): Response =
        try {
            timer.pause()
            val elapsedMillis = timer.elapsedMillis
            withContext(Dispatchers.IO) { dataPointRepository.insert(elapsedMillis) }
            timer.reset()
            Response("Data point sent: $elapsedMillis")
        } catch (e: CancellationException) {
            Response("Data point sending cancelled")
        } catch (t: Throwable) {
            Response("Error sending datapoint: ${t.message}")
        }

    data class Response(val message: String)
}
