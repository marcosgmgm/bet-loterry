package br.com.example.bet.lottery.domain.events

import br.com.example.bet.lottery.domain.CPF
import br.com.example.bet.lottery.domain.Name
import br.com.example.bet.lottery.domain.Balance
import br.com.example.bet.lottery.domain.Bet
import br.com.zup.eventsourcing.core.Event

/**
 * Created by marcosgm on 04/12/17
 */
data class PeopleCreated(val cpf: CPF, val name: Name, val balance: Balance) : Event()

data class BalanceCredited(val credit: Balance) : Event()

data class BalanceDebited(val debit: Balance) : Event()

data class BetCreated(val bet: Bet) : Event()

