package br.com.example.bet.lottery.web

import br.com.example.bet.lottery.domain.Balance
import br.com.example.bet.lottery.domain.BetOrderId
import br.com.example.bet.lottery.domain.CPF
import br.com.example.bet.lottery.domain.commands.CreditBalance
import br.com.example.bet.lottery.domain.commands.LotteryCommandHandler
import br.com.example.bet.lottery.web.representation.BetOrderRepresentation
import br.com.example.bet.lottery.web.representation.PeopleRepresentation
import br.com.example.bet.lottery.web.request.CreateOrderRequest
import br.com.example.bet.lottery.web.request.CreatePeopleRequest
import br.com.example.bet.lottery.web.utils.toCommand
import br.com.example.bet.lottery.web.utils.toRepresentation
import br.com.example.bet.lottery.workflow.WorkflowManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * Created by marcosgm on 06/12/17
 */
@RestController
@RequestMapping("/bet-lottery")
class BetLotteryController @Autowired constructor(private val commandHandler: LotteryCommandHandler,
                                                  private val workflowManager: WorkflowManager) {

    @PostMapping("people", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE], consumes = [MediaType
            .APPLICATION_JSON_UTF8_VALUE])
    fun createPeople(@RequestBody @Valid request: CreatePeopleRequest ): ResponseEntity<PeopleRepresentation> {
        val command = request.toCommand()
        val people = commandHandler.handle(command)
        return ResponseEntity(people.toRepresentation(), HttpStatus.OK)
    }

    @GetMapping("people/{cpf}", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun getPeople(@PathVariable cpf: String): ResponseEntity<PeopleRepresentation> {
        val people = commandHandler.getPeople(CPF(cpf))
        return ResponseEntity(people.toRepresentation(), HttpStatus.OK)
    }

    @PatchMapping("people/{cpf}/{balance}", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun creditBalancePeople(@PathVariable cpf: String, @PathVariable balance: String): ResponseEntity<PeopleRepresentation> {
        val people = commandHandler.handle(CreditBalance(CPF(cpf), Balance(balance.toLong())))
        return ResponseEntity(people.toRepresentation(), HttpStatus.OK)
    }


    @PostMapping("order", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE], consumes = [MediaType
            .APPLICATION_JSON_UTF8_VALUE])
    fun createOrder(@RequestBody @Valid request: CreateOrderRequest): ResponseEntity<BetOrderRepresentation> {
        val command = request.toCommand()
        val order = workflowManager.start(command)
        return ResponseEntity(order.toRepresentation(), HttpStatus.OK)
    }

    @GetMapping("order/{id}", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun getOrder(@PathVariable id: String): ResponseEntity<BetOrderRepresentation> {
        val order = commandHandler.getBetOrder(BetOrderId(id))
        return ResponseEntity(order.toRepresentation(), HttpStatus.OK)
    }

}