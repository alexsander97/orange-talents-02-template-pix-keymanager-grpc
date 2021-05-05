package br.com.zup.edu.newPixKey

import br.com.zup.edu.RegisterPixServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest

import org.junit.Test

@MicronautTest(transactional = false)
internal class PixKeyEndpointTest(val repository: PixKeyRepository,
                                  val grpcClient: RegisterPixServiceGrpc.RegisterPixServiceFutureStub) {


//    @Test
//    fun `deve registrar nova chave pix`() {
//
//    }
//
//    @Test
//    fun `nao deve registrar chave pix quando existe a chave`() {
//
//    }
//
//    @Test
//    fun `deve registrar chave pix quando nao encontrar dados do cliente`() {
//
//    }
//
//    @Test
//    fun `nao deve registrar chave pix quando parametros forem invalidos`() {
//
//    }


    @Factory
    class Clients {
        @Bean
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): RegisterPixServiceGrpc.RegisterPixServiceBlockingStub {
            return RegisterPixServiceGrpc.newBlockingStub(channel)
        }
    }

}