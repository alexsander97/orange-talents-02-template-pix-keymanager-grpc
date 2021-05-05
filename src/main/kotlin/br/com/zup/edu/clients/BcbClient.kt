package br.com.zup.edu.clients

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client
import java.time.LocalDateTime
import br.com.zup.edu.newPixKey.BankAccount as PixKeyBankAccount
import br.com.zup.edu.newPixKey.Owner as PixKeyOwner
import br.com.zup.edu.AccountType as AccountTypeProto

@Client("\${bcbClient.url}")
interface BcbClient {

    @Post("/keys", produces = [MediaType.APPLICATION_XML], consumes = [MediaType.APPLICATION_XML])
    fun registerKey(@Body register: CreatePixKeyRequest): HttpResponse<CreatePixKeyResponse>

    @Delete("/keys/{key}", processes = [MediaType.APPLICATION_XML])
    fun removeKey(@PathVariable key: String, @Body request: DeletePixKeyRequest): HttpResponse<DeletePixKeyResponse>



}

data class DeletePixKeyRequest(val key: String, val participant: String)

data class DeletePixKeyResponse(val key: String,
                                val participant: String,
                                val deletedAt: LocalDateTime)

data class CreatePixKeyResponse(
    val keyType: BcbKeyType,
    val key: String,
    val bankAccount: BankAccount,
    val owner: Owner
)

data class CreatePixKeyRequest(
    val keyType: BcbKeyType,
    val key: String,
    val bankAccount: BankAccount,
    val owner: Owner
)

data class BankAccount(
    val participant: String,
    val branch: String,
    val accountNumber: String,
    val accountType: AccountType) {


    constructor(bankAccount: PixKeyBankAccount) : this(
        participant = bankAccount.ispb,
        branch = bankAccount.agency,
        accountNumber = bankAccount.number,
        accountType = bankAccount.accountType.convertToAccountType())

}

fun AccountTypeProto.convertToAccountType(): AccountType {
    if (this == AccountTypeProto.CONTA_POUPANCA) {
        return AccountType.SVGS
    }
    return AccountType.CACC
}

data class Owner(val type: PersonType, val name: String, val taxIdNumber: String) {

    constructor(owner: PixKeyOwner) : this(type = PersonType.NATURAL_PERSON, name = owner.name, taxIdNumber = owner.cpf)
}

enum class AccountType {
    CACC, SVGS;
}

enum class PersonType {
    NATURAL_PERSON, LEGAL_PERSON;
}

enum class BcbKeyType {
    CPF, CNPJ, PHONE, EMAIL, RANDOM;
}