package br.com.example.bet.lottery.domain.commands

import br.com.example.bet.lottery.domain.*

/**
 * Created by marcosgm on 06/12/17
 */
data class CreatePeople(val cpf: CPF, val name: Name)

data class CreditBalance(val cpf: CPF, val balance: Balance)

data class DebitBalance(val cpf: CPF, val balance: Balance)

data class CreateBetOrder(val cpf: CPF, val steps: LinkedHashMap<BetOrderStep.ID, BetOrderStep>)

data class UpdateStep(val betOrderId: BetOrderId, val step: BetOrderStep)

data class CreateBet(val betOrderId: BetOrderId)

data class DebitAmountPeople(val betOrderId: BetOrderId)

data class AddBetPeople(val betOrderId: BetOrderId)

data class FinishSuccessBetOrder(val betOrderId: BetOrderId)

data class FinishFailBetOrder(val betOrderId: BetOrderId)


