package br.com.zup.edu.newPixKey

import br.com.zup.edu.AccountType
import javax.persistence.*

@Entity
class BankAccount(val institutionName: String,
                  val ispb: String,
                  val agency: String,
                  val number: String,

                  @Enumerated(EnumType.STRING)
                  var accountType: AccountType,

                  @OneToOne(cascade = [CascadeType.PERSIST])
                  val owner: Owner){

    @Id
    @GeneratedValue
    val id: Long? = null

}