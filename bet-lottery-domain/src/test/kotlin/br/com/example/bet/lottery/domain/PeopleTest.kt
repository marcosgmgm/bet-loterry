package br.com.example.bet.lottery.domain

import br.com.example.bet.lottery.domain.events.BetCreated
import br.com.example.bet.lottery.domain.events.PeopleCreated
import br.com.example.bet.lottery.domain.events.BalanceCredited
import br.com.example.bet.lottery.domain.events.BalanceDebited
import org.junit.Assert.*
import org.junit.Test

/**
 * Created by marcosgm on 04/12/17
 */
class PeopleTest {

    @Test
    fun createPeopleSuccess() {
        createPeople()
    }

    @Test
    fun creditAndDebitBalance() {
        val people = createPeople()

        people.creditBalance(Balance(10))
        assertTrue(people.event is BalanceCredited)
        assertEquals(10, people.balance.value)
        people.creditBalance(Balance(20))
        assertTrue(people.event is BalanceCredited)
        assertEquals(30, people.balance.value)

        people.debitBalance(Balance(5))
        assertTrue(people.event is BalanceDebited)
        assertEquals(25, people.balance.value)
        people.debitBalance(Balance(15))
        assertTrue(people.event is BalanceDebited)
        assertEquals(10, people.balance.value)
    }

    @Test(expected = UnsupportedOperationException::class)
    fun failChangeBalanceToValueNegative() {
        val people = createPeople()
        people.debitBalance(Balance(5))
    }

    @Test
    fun betSuccess() {
        val people = createPeople()

        assertTrue(people.bets.isEmpty())

        people.creditBalance(Balance(10))
        assertTrue(people.event is BalanceCredited)
        assertEquals(10, people.balance.value)

        val bet = Bet()

        people.debitBalance(Balance(bet.valueBet.value))
        assertTrue(people.event is BalanceDebited)
        assertEquals(8, people.balance.value)

        people.addBet(Bet())
        assertTrue(people.event is BetCreated)
        assertFalse(people.bets.isEmpty())
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