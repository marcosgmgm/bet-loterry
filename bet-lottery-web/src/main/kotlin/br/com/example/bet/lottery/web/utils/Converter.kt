package br.com.example.bet.lottery.web.utils

import br.com.example.bet.lottery.domain.*
import br.com.example.bet.lottery.domain.commands.CreatePeople
import br.com.example.bet.lottery.web.representation.PeopleRepresentation
import br.com.example.bet.lottery.web.request.CreatePeopleRequest
import br.com.example.bet.lottery.domain.commands.CreateBetOrder
import br.com.example.bet.lottery.web.representation.BetOrderRepresentation
import br.com.example.bet.lottery.web.request.CreateOrderRequest

/**
 * Created by marcosgm on 06/12/17
 */
fun CreatePeopleRequest.toCommand() =
        CreatePeople(
                cpf = CPF(this.cpf!!),
                name = Name(this.name!!)
        )

fun CreateOrderRequest.toCommand() =
       CreateBetOrder(
               cpf = CPF(this.cpf!!),
               steps = linkedMapOf()
       )

fun People.toRepresentation() =
        PeopleRepresentation(
                cpf = this.id.value,
                name = this.name.name,
                balance = this.balance.value,
                bets = this.bets.toRepresentation()
        )
fun BetOrder.toRepresentation() =
        BetOrderRepresentation(
                id = this.idAsString(),
                cpf = this.cpf.value,
                steps = this.steps.toRepresentation(),
                status = this.status.name,
                bet = this.bet?.toRepresentation()
        )


fun LinkedHashMap<BetOrderStep.ID, BetOrderStep>.toRepresentation() : LinkedHashMap<String, String> {
        val map = linkedMapOf<String, String>()
        this.forEach { t, u ->  map.put(t.value, u.status.name)}
        return map
}

fun Bet.toRepresentation() =
        BetOrderRepresentation.BetRepresentation(
                this.id.value,
                this.valueBet.value
        )


fun List<Bet>.toRepresentation() : List<PeopleRepresentation.BetRepresentation> {
       val list = arrayListOf<PeopleRepresentation.BetRepresentation>()
        this.forEach {
                list.add(PeopleRepresentation.BetRepresentation(it.id.value, it.valueBet.value))
        }
        return list
}
