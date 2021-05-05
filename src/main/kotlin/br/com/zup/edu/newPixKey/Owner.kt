package br.com.zup.edu.newPixKey

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Owner(val name: String,
            val cpf: String,) {

    @Id
    @GeneratedValue
    val id: Long? = null
}