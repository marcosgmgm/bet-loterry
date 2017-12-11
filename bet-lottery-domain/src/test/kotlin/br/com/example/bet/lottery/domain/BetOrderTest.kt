package br.com.example.bet.lottery.domain

import br.com.example.bet.lottery.domain.events.*
import org.junit.Assert.*

import org.junit.Test

/**
 * Created by marcosgm on 05/12/17
 */
class BetOrderTest {

    @Test
    fun createBetOrderSuccess() {
        createBetOrder()
    }

    @Test
    fun updateStep() {
        val betOrder = createBetOrder()
        val id = betOrder.steps.keys.elementAt(0)
        assertEquals(BetOrderStep.Status.PENDING, betOrder.steps[id]!!.status)
        betOrder.updateStep(id, BetOrderStep.Status.PROCESSED)
        assertTrue(betOrder.event is BetOrderStepUpdated)
        assertEquals(BetOrderStep.Status.PROCESSED, betOrder.steps[id]!!.status)
    }

    @Test
    fun createBet() {
        val betOrder = createBetOrder()
        assertNull(betOrder.bet)
        betOrder.createBet()
        assertTrue(betOrder.event is BetOrderBetCreated)
        assertNotNull(betOrder.bet)
    }

    @Test
    fun debitAmount() {
        val people = createPeople()
        people.creditBalance(Balance(10))
        val betOrder = createBetOrder(cpf = people.id as CPF)
        betOrder.createBet()
        betOrder.debitAmount(people)
        assertTrue(betOrder.event is BetOrderBalancePeopleDebited)
        assertTrue(people.event is BalanceDebited)
        assertEquals(8, people.balance.value)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun debitAmountFailed() {
        val people = createPeople()
        val betOrder = createBetOrder(cpf = people.id as CPF)
        betOrder.createBet()
        betOrder.debitAmount(people)
    }

    @Test
    fun failBetOrder() {
        val betOrder = createBetOrder()
        betOrder.failBetOrder()
        assertTrue(betOrder.event is BetOrderStatusUpdated)
        assertEquals(BetOrderStatus.FAILED, betOrder.status)
    }

    @Test
    fun addBetToPeople() {
        val people = createPeople()
        people.creditBalance(Balance(10))
        assertTrue(people.bets.isEmpty())
        val betOrder = createBetOrder(cpf = people.id as CPF)
        betOrder.createBet()
        betOrder.addBetToPeople(people)
        assertTrue(betOrder.event is BetOrderPeopleBetAdded)
        assertTrue(people.event is BetCreated)
        assertFalse(people.bets.isEmpty())
    }

    @Test
    fun finishBetOrderSuccess() {
        val betOrder = createBetOrder()
        betOrder.finishBetOrderSuccess()
        assertTrue(betOrder.event is BetOrderStatusUpdated)
        assertEquals(BetOrderStatus.FINISHED, betOrder.status)
    }


    private fun createFakeSteps() : LinkedHashMap<BetOrderStep.ID, BetOrderStep> {
        val steps  = linkedMapOf<BetOrderStep.ID, BetOrderStep>()
        val step1 = BetOrderStep(id = BetOrderStep.ID("STEP_1"))
        val step2 = BetOrderStep(id = BetOrderStep.ID("STEP_2"))
        val step3 = BetOrderStep(id = BetOrderStep.ID("STEP_3"))
        steps.put(step1.id, step1)
        steps.put(step2.id, step2)
        steps.put(step3.id, step3)
        return steps
    }

    private fun createBetOrder(cpf: CPF = CPF("07253424638")) : BetOrder {
        val betOrder =  BetOrder(
                cpf = CPF("07253424638"),
                betOrderId = BetOrderId(),
                steps = createFakeSteps()
        )
        assertTrue(betOrder.event is BetOrderCreated)
        assertEquals(BetOrderStatus.PENDING, betOrder.status)
        return betOrder
    }

    private fun createPeople() : People {
        val people = People(
                cpf = CPF("07253424638"),
                name = Name("Marcos Guimarães")
        )
        assertTrue(people.event is PeopleCreated)
        assertEquals("07253424638", people.id.value)
        assertEquals("Marcos Guimarães", people.name.name)
        assertEquals(0, people.balance.value)
        return people
    }


}