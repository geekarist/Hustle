package me.cpele.hustle.domain

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SendDataPointUseCase(private val dataPointRepositorySupplier: DataPointRepositorySupplier) {

    suspend fun execute(timer: EggTimer): Response =
        try {
            timer.pause()
            val elapsedMillis = timer.elapsedMillis
            withContext(Dispatchers.IO) {
                dataPointRepositorySupplier.get().insert(elapsedMillis)
            }
            timer.reset()
            Response("Data point sent: $elapsedMillis")
        } catch (e: CancellationException) {
            Response("Data point sending cancelled")
        } catch (e: Error) {
            Response("Error sending datapoint")
        }

    data class Response(val message: String)
}
