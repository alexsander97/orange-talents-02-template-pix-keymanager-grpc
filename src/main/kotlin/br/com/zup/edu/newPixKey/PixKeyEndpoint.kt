package br.com.zup.edu.newPixKey

import br.com.zup.edu.KeyType
import br.com.zup.edu.RegisterPixKeyRequest
import br.com.zup.edu.RegisterPixKeyResponse
import br.com.zup.edu.RegisterPixServiceGrpc
import br.com.zup.edu.clients.*
import br.com.zup.edu.newPixKey.enums.convertKeyTypeProtoToPixKeyType
import br.com.zup.edu.newPixKey.enums.convertToKeyTypeBcb
import io.grpc.Status
import io.grpc.stub.StreamObserver
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PixKeyEndpoint(@Inject val pixKeyRepository: PixKeyRepository,
                     @Inject val bcbClient: BcbClient,
                     @Inject val erpItauClient: ErpItauClient) : RegisterPixServiceGrpc.RegisterPixServiceImplBase() {

    override fun create(request: RegisterPixKeyRequest, responseObserver: StreamObserver<RegisterPixKeyResponse>) {
        try {
            request.validatesRequestToCreatePix()
        } catch (e: IllegalArgumentException) {
            responseObserver.onError(
                Status.INVALID_ARGUMENT
                    .withDescription(e.message)
                    .withCause(e)
                    .asRuntimeException()
            )
            return
        }

        if (pixKeyRepository.existsByKeyValue(request.keyValue)) {
            responseObserver.onError(
                Status.ALREADY_EXISTS
                    .withDescription("key value already exists")
                    .asRuntimeException()
            )
            return
        }

        val search = erpItauClient.search(request.idClient, request.accountType).getBody().get()
        val bankAccount = search.toModel()

        val pixKey = request.toModel(bankAccount)


        val entity: PixKey = try {
            registryOnBcb(pixKey)
            pixKeyRepository.save(pixKey)
        } catch (e: Exception) {
            responseObserver.onError(
                Status.INTERNAL
                    .withCause(e)
                    .withDescription("an unexpected error happened: " + e.message)
                    .asRuntimeException()
            )
            return
        }

        responseObserver.onNext(
            RegisterPixKeyResponse.newBuilder()
                .setId(entity.id.toString())
                .build()
        )

        responseObserver.onCompleted()
    }


    fun registryOnBcb(pixkey: PixKey) {
        val request: CreatePixKeyRequest = CreatePixKeyRequest(keyType = pixkey.keyType.convertToKeyTypeBcb(),
            key = pixkey.keyValue,
            bankAccount = BankAccount(bankAccount = pixkey.bankAccount),
            owner =  Owner(pixkey.bankAccount.owner))

        val search = bcbClient.registerKey(request).getBody().get()
    }

    fun RegisterPixKeyRequest.toModel(bankAccount: BankAccount): PixKey {
        bankAccount.accountType = this.accountType
        if (keyType == KeyType.RANDOM_KEY) {
            return PixKey(idClient, keyType.convertKeyTypeProtoToPixKeyType(), UUID.randomUUID().toString(), bankAccount)
        }
        return PixKey(idClient, keyType.convertKeyTypeProtoToPixKeyType(), keyValue, bankAccount)
    }

}