package br.com.zup.edu.clients

import br.com.zup.edu.newPixKey.BankAccount
import br.com.zup.edu.newPixKey.Owner
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.client.annotation.Client
import br.com.zup.edu.AccountType

@Client("\${erpItauClient.url}")
interface ErpItauClient {

    @Get("/{clientId}/contas")
    fun search(@PathVariable clientId: String, @QueryValue tipo: AccountType): HttpResponse<ClientDataResponse>
}

data class ClientDataResponse(
    val tipo: AccountType,
    val instituicao: Instituicao,
    val agencia: String,
    val numero: String,
    val titular: Titular
)



data class Instituicao(val nome: String, val ispb: String)

data class Titular(val id: String, val nome: String, val cpf: String)

fun ClientDataResponse.toModel(): BankAccount{
    return BankAccount(
        institutionName = instituicao.nome,
        ispb = instituicao.ispb,
        owner = Owner(name = titular.nome, cpf = titular.cpf),
        agency = agencia,
        number = numero,
        accountType = tipo)
}