package br.com.zup.edu.newPixKey

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface PixKeyRepository : JpaRepository<PixKey, Long> {
    fun existsByKeyValue(keyValue: String): Boolean
}