package br.com.example.bet.lottery.domain.events

import br.com.example.bet.lottery.domain.*
import br.com.zup.eventsourcing.core.Event
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize

/**
 * Created by marcosgm on 05/12/17
 */
data class BetOrderCreated(
        val betOrderId: BetOrderId,
        val cpf: CPF,
        val status: BetOrderStatus,
        @JsonSerialize(keyUsing = BetOrderStep.IDSerializer::class)
        @JsonDeserialize(keyUsing = BetOrderStep.IDDeserializer::class)
        val steps: LinkedHashMap<BetOrderStep.ID, BetOrderStep>
) : Event()

data class BetOrderStepUpdated(
        val stepId: BetOrderStep.ID,
        val status: BetOrderStep.Status) : Event()

data class BetOrderStatusUpdated(val status: BetOrderStatus) : Event()

data class BetOrderBetCreated(val bet: Bet) : Event()

data class BetOrderBalancePeopleDebited(val bet: Bet, val cpf: CPF) : Event()

class BetOrderPeopleBetAdded : Event()
