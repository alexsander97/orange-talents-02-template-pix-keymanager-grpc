package br.com.zup.edu.newPixKey

import br.com.zup.edu.RegisterPixKeyRequest
import br.com.zup.edu.KeyType
import java.lang.IllegalArgumentException


fun RegisterPixKeyRequest.validatesRequestToCreatePix() {
    when {
        idClient.isNullOrBlank() ->  throw IllegalArgumentException("id client not be null")
        keyType == null -> throw IllegalArgumentException("key type not be null")
        accountType == null -> throw IllegalArgumentException("account type not be null")
    }
    validatesKeyValueByKeyType()
}

fun RegisterPixKeyRequest.validatesKeyValueByKeyType() {
    if (keyValue.length > 77) {
        throw IllegalArgumentException("Key value must have a maximum length of 77 characters:")
    }

    when (keyType) {
        KeyType.CPF -> validatesCpf(keyValue)
        KeyType.PHONE -> validatesPhoneNumber(keyValue)
        KeyType.EMAIL -> validatesEmail(keyValue)
        KeyType.RANDOM_KEY -> validatesRandomKey()
    }
}


fun RegisterPixKeyRequest.validatesRandomKey() {
    if (!keyValue.isNullOrBlank()) {
        throw IllegalArgumentException("when the type of the key is random, the field key value must be null")
    }
}

fun validatesCpf(cpf: String) {
    if (cpf.isNullOrBlank()) {
        throw IllegalArgumentException("cpf is not be blank or null")
    }
    if (!cpf.matches("^[0-9]{11}$".toRegex())) {
        throw IllegalArgumentException("cpf must be valid")
    }
}

fun validatesPhoneNumber(phoneNumber: String) {
    if (phoneNumber.isNullOrBlank()) {
        throw IllegalArgumentException("phone number is not be blank or null")
    }
    if (!phoneNumber.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())) {
        throw IllegalArgumentException("phone number must be valid")
    }
}

fun validatesEmail(email: String) {
    if (email.isNullOrBlank()) {
        throw IllegalArgumentException("email is not be blank or null")
    }
    if(!email.matches("/^[a-z0-9.]+@[a-z0-9]+\\.[a-z]+\\.([a-z]+)?\$/i".toRegex())) {
        throw IllegalArgumentException("email must be valid")
    }
}
