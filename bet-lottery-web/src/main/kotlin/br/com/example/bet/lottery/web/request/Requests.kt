package br.com.example.bet.lottery.web.request

import org.hibernate.validator.constraints.NotBlank

/**
 * Created by marcosgm on 06/12/17
 */
class CreatePeopleRequest(@field:NotBlank val cpf: String?, @field:NotBlank val name: String?)

class CreateOrderRequest(@field:NotBlank val cpf: String?)