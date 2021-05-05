package br.com.zup.edu.newPixKey

import br.com.zup.edu.AccountType
import br.com.zup.edu.KeyType
import br.com.zup.edu.newPixKey.enums.PixKeyType
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
class PixKey(

    val idClient: String,

    @Enumerated(EnumType.STRING)
    val keyType: PixKeyType,

    val keyValue: String,


    @OneToOne(cascade = [CascadeType.PERSIST])
    val bankAccount: BankAccount
) {

    @Id
    @GeneratedValue
    val id: Long? = null

    val createdAt: LocalDateTime = LocalDateTime.now()
}
