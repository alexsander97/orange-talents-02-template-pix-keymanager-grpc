package br.com.zup.edu.removePixKey

import br.com.zup.edu.RemovePixKeyRequest
import br.com.zup.edu.RemovePixKeyResponse
import br.com.zup.edu.RemovePixServiceGrpc
import br.com.zup.edu.clients.BcbClient
import br.com.zup.edu.clients.DeletePixKeyRequest
import br.com.zup.edu.newPixKey.PixKey
import br.com.zup.edu.newPixKey.PixKeyRepository
import io.grpc.Status
import io.grpc.stub.StreamObserver
import java.lang.Exception
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemovePixKeyEndpoint(@Inject val pixKeyRepository: PixKeyRepository,
                           @Inject val bcbClient: BcbClient) : RemovePixServiceGrpc.RemovePixServiceImplBase() {


    override fun remove(request: RemovePixKeyRequest, responseObserver: StreamObserver<RemovePixKeyResponse>) {
        val possiblePixKey = pixKeyRepository.findById(request.pixId.toLong())

        if (!possiblePixKey.isPresent) {
            responseObserver.onError(
                Status.NOT_FOUND
                    .withDescription("pix id not found")
                    .asRuntimeException())
            return
        }


        if (!request.belongsToTheCustomer()) {
            responseObserver.onError(
                Status.PERMISSION_DENIED
                    .withDescription("pix id not found")
                    .asRuntimeException())
            return
        }

        val success = try {
            pixKeyRepository.deleteById(request.pixId.toLong())
            registryOnBcb(possiblePixKey.get())
            true
        } catch (e: Exception) {
            responseObserver.onError(Status.INTERNAL
                .withDescription("an unexpected error happened")
                .withCause(e)
                .asRuntimeException())
            return
        }


        responseObserver.onNext(
            RemovePixKeyResponse.newBuilder()
            .setSuccess(success)
            .build())


        responseObserver.onCompleted()
    }

    fun registryOnBcb(pixkey: PixKey) {
        val request: DeletePixKeyRequest = DeletePixKeyRequest(key = pixkey.keyValue, pixkey.bankAccount.ispb)

        val search = bcbClient.removeKey(key = pixkey.keyValue, request = request).getBody().get()
    }


    fun RemovePixKeyRequest.belongsToTheCustomer(): Boolean {
        val pixKeyEntity: Optional<PixKey> = pixKeyRepository.findById(pixId.toLong())
        if (pixKeyEntity.get().idClient == idClient) {
            return true
        }
        return false
    }
}
