package br.com.zup.edu.loadPix

import br.com.zup.edu.LoadPixRequest
import br.com.zup.edu.LoadPixResponse
import br.com.zup.edu.LoadPixServiceGrpc
import br.com.zup.edu.clients.BcbClient
import br.com.zup.edu.newPixKey.PixKeyRepository
import io.grpc.stub.StreamObserver
import javax.inject.Singleton

@Singleton
class LoadPixEndpoint(val repository: PixKeyRepository,
                      val bcbClient: BcbClient) : LoadPixServiceGrpc.LoadPixServiceImplBase() {


    override fun load(request: LoadPixRequest,
                      responseObserver: StreamObserver<LoadPixResponse>) {
    }
}


