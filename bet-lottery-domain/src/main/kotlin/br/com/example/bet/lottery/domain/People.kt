package br.com.example.bet.lottery.domain

import br.com.example.bet.lottery.domain.events.BetCreated
import br.com.example.bet.lottery.domain.events.PeopleCreated
import br.com.example.bet.lottery.domain.events.BalanceCredited
import br.com.example.bet.lottery.domain.events.BalanceDebited
import br.com.zup.eventsourcing.core.AggregateRoot
import br.com.zup.eventsourcing.core.Event

/**
 * Created by marcosgm on 04/12/17
 */

class People() : AggregateRoot() {

    lateinit var name: Name
    lateinit var balance: Balance
    var bets = arrayListOf<Bet>()

    constructor(cpf: CPF, name: Name) : this() {
        applyChange(
                PeopleCreated(
                        cpf = cpf,
                        name = name,
                        balance = Balance(value = 0)
                )
        )
    }

    override fun applyEvent(event: Event) {
        when (event) {
            is PeopleCreated -> apply(event)
            is BalanceCredited -> apply(event)
            is BalanceDebited -> apply(event)
            is BetCreated -> apply(event)
        }
    }

    private fun apply(event: PeopleCreated) {
        this.id = event.cpf
        this.name = event.name
        this.balance = event.balance
    }

    private fun apply(event: BalanceCredited) {
        this.balance = Balance(this.balance.value.plus(event.credit.value))
    }

    private fun apply(event: BalanceDebited) {
        this.balance = Balance(this.balance.value.minus(event.debit.value))
    }

    private fun apply(event: BetCreated) {
        this.bets.add(event.bet)
    }

    fun creditBalance(balance: Balance) {
        applyChange(
                BalanceCredited(credit = balance)
        )
    }

    fun debitBalance(balance: Balance) {
        if (this.balance.value.minus(balance.value) < 0) {
            throw UnsupportedOperationException("Balance can not be negative")
        }
        applyChange(
                BalanceDebited(debit = balance)
        )
    }

    fun addBet(bet: Bet) {
        applyChange(
                BetCreated(bet = bet)
        )
    }

}