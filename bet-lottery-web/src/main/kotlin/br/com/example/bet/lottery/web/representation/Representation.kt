package br.com.example.bet.lottery.web.representation

import br.com.example.bet.lottery.domain.BetOrderStep

/**
 * Created by marcosgm on 06/12/17
 */
class PeopleRepresentation(val cpf: String, val name: String,
                           val balance: Long, val bets: List<BetRepresentation>) {
    class BetRepresentation(val id: String, val value: Long)
}

class BetOrderRepresentation(val id: String, val cpf: String,
                             val status: String, val steps: LinkedHashMap<String, String>,
                             val bet: BetRepresentation?) {

    class BetRepresentation(val id: String, val value: Long)
}

