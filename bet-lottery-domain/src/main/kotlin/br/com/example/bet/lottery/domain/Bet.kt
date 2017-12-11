package br.com.example.bet.lottery.domain

import java.util.*
import java.util.stream.IntStream

/**
 * Created by marcosgm on 04/12/17
 */
class Bet(val id: BetId = BetId(), val valueBet: BetValue = BetValue()) {

    data class BetId(val value: String = UUID.randomUUID().toString())

    data class BetValue(val value: Long = 2)

}