package br.com.example.bet.lottery.domain

import br.com.zup.eventsourcing.core.AggregateId
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.KeyDeserializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.util.*

/**
 * Created by marcosgm on 05/12/17
 */
class BetOrderId(value: String = "BET-"+UUID.randomUUID().toString()) : AggregateId(value)

enum class BetOrderStatus{
    PENDING,
    PROCESSING,
    FINISHED,
    FAILED
}

data class BetOrderStep(val id: ID, val status: Status = BetOrderStep.Status.PENDING) {

    data class ID(val value: String)

    class IDSerializer : JsonSerializer<ID>() {
        override fun serialize(id: ID, gen: JsonGenerator, serializers: SerializerProvider?) = gen.writeFieldName(id.value)
    }

    class IDDeserializer : KeyDeserializer() {
        override fun deserializeKey(key: String, p1: DeserializationContext?): Any = ID(key)
    }

    enum class Status {
        PENDING,
        PROCESSING,
        PROCESSED,
    }
}