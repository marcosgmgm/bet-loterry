package br.com.example.bet.lottery.domain.commands

import br.com.example.bet.lottery.domain.BetOrder
import br.com.example.bet.lottery.domain.BetOrderId
import br.com.example.bet.lottery.domain.CPF
import br.com.example.bet.lottery.domain.People
import br.com.zup.eventsourcing.core.RepositoryManager

/**
 * Created by marcosgm on 06/12/17
 */
class LotteryCommandHandler(val repositoryPeople: RepositoryManager<People>,
                            val repositoryOrder: RepositoryManager<BetOrder>) {

    fun handle(command: CreatePeople): People {
        val people = People(
                cpf = command.cpf,
                name = command.name
        )
        repositoryPeople.save(people)
        return people
    }

    fun handle(command: CreditBalance): People {
        val people = repositoryPeople.get(command.cpf)
        people.creditBalance(command.balance)
        repositoryPeople.save(people)
        return people
    }

    fun handle(command: DebitBalance): People {
        val people = repositoryPeople.get(command.cpf)
        people.debitBalance(command.balance)
        repositoryPeople.save(people)
        return people
    }


    fun handle(command: CreateBetOrder): BetOrder {
        val betOrder = BetOrder(
                cpf = command.cpf,
                steps = command.steps,
                betOrderId = BetOrderId()
        )
        repositoryOrder.save(betOrder)
        return betOrder
    }

    fun handle(command: UpdateStep): BetOrder {
        val betOrder = repositoryOrder.get(command.betOrderId)
        betOrder.updateStep(command.step.id, command.step.status)
        repositoryOrder.save(betOrder)
        return betOrder
    }

    fun handle(command: CreateBet): BetOrder {
        val betOrder = repositoryOrder.get(command.betOrderId)
        betOrder.createBet()
        repositoryOrder.save(betOrder)
        return betOrder
    }

    fun handle(command: DebitAmountPeople): BetOrder {
        val betOrder = repositoryOrder.get(command.betOrderId)
        val people = repositoryPeople.get(betOrder.cpf)
        betOrder.debitAmount(people)
        repositoryPeople.save(people)
        repositoryOrder.save(betOrder)
        return betOrder
    }

    fun handle(command: AddBetPeople): BetOrder {
        val betOrder = repositoryOrder.get(command.betOrderId)
        val people = repositoryPeople.get(betOrder.cpf)
        betOrder.addBetToPeople(people)
        repositoryPeople.save(people)
        repositoryOrder.save(betOrder)
        return betOrder
    }

    fun handle(command: FinishSuccessBetOrder): BetOrder {
        val betOrder = repositoryOrder.get(command.betOrderId)
        betOrder.finishBetOrderSuccess()
        repositoryOrder.save(betOrder)
        return betOrder
    }

    fun handle(command: FinishFailBetOrder): BetOrder {
        val betOrder = repositoryOrder.get(command.betOrderId)
        betOrder.failBetOrder()
        repositoryOrder.save(betOrder)
        return betOrder
    }

    fun getPeople(cpf: CPF) : People {
        return repositoryPeople.get(cpf)
    }

    fun getBetOrder(betOrderId: BetOrderId) : BetOrder {
        return repositoryOrder.get(betOrderId)
    }







}