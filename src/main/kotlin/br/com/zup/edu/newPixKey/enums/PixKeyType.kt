package br.com.zup.edu.newPixKey.enums


import br.com.zup.edu.clients.BcbKeyType
import br.com.zup.edu.KeyType as KeyTypeProto

enum class PixKeyType() {
    CPF,
    PHONE,
    EMAIL,
    RANDOM_KEY;
}

fun PixKeyType.convertToKeyTypeBcb(): BcbKeyType {
    return BcbKeyType.valueOf(this.name)
}

fun KeyTypeProto.convertKeyTypeProtoToPixKeyType(): PixKeyType {
    return PixKeyType.valueOf(this.name)
}