package br.com.example.bet.lottery.domain

import br.com.example.bet.lottery.domain.events.*
import br.com.zup.eventsourcing.core.AggregateRoot
import br.com.zup.eventsourcing.core.Event

/**
 * Created by marcosgm on 05/12/17
 */
class BetOrder() : AggregateRoot() {

    fun idAsString(): String = id.value
    lateinit var cpf: CPF
    lateinit var status: BetOrderStatus
    lateinit var steps: LinkedHashMap<BetOrderStep.ID, BetOrderStep>
    var bet: Bet? = null

    constructor(
            betOrderId: BetOrderId,
            cpf: CPF,
            steps: LinkedHashMap<BetOrderStep.ID, BetOrderStep>
    ): this() {
        applyChange(
                BetOrderCreated(
                        betOrderId = betOrderId,
                        cpf = cpf,
                        status = BetOrderStatus.PENDING,
                        steps = steps
                )
        )
    }

    override fun applyEvent(event: Event) {
        when (event) {
            is BetOrderCreated -> apply(event)
            is BetOrderStepUpdated -> apply(event)
            is BetOrderStatusUpdated -> apply(event)
            is BetOrderBetCreated -> apply(event)
        }
    }

    private fun apply(event: BetOrderCreated) {
        this.id = event.betOrderId
        this.cpf = event.cpf
        this.status = event.status
        this.steps = event.steps
    }

    private fun apply(event: BetOrderStepUpdated) {
        val step = steps[event.stepId]!!.copy(status = event.status)
        steps.put(event.stepId, step)
    }

    private fun apply(event: BetOrderStatusUpdated) {
        this.status = event.status
    }

    private fun apply(event: BetOrderBetCreated) {
        this.bet = event.bet
    }

    fun updateStep(stepId: BetOrderStep.ID, status: BetOrderStep.Status) {
        applyChange(BetOrderStepUpdated(stepId, status))
    }

    fun createBet() {
        applyChange(BetOrderStatusUpdated(status = BetOrderStatus.PROCESSING))
        applyChange(BetOrderBetCreated(bet = Bet()))
    }

    fun debitAmount(people: People) {
        people.debitBalance(Balance(this.bet!!.valueBet.value))
        applyChange(BetOrderBalancePeopleDebited(this.bet!!, this.cpf)) //NAO VOU TRATAR ESSE EVENTO, APENAS PARA REGISTRO
    }

    fun failBetOrder() {
        println("Failed to process Bet Order")
        applyChange(BetOrderStatusUpdated(status = BetOrderStatus.FAILED))
    }

    fun addBetToPeople(people: People) {
        people.addBet(this.bet!!)
        applyChange(BetOrderPeopleBetAdded()) //NAO VOU TRATAR ESSE EVENTO, APENAS PARA REGISTRO
    }

    fun finishBetOrderSuccess() {
        applyChange(BetOrderStatusUpdated(status = BetOrderStatus.FINISHED))
    }


}