package com.ninety.nine.main.restbackend.iban

import com.ninety.nine.main.restbackend.data.Group
import com.ninety.nine.main.restbackend.data.Transfer
import org.iban4j.Iban
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class IbanService(
    private val ibanDao: IbanDao,
    private val ibanTransferDao: IbanTransferDao
) {
    fun get(): Flux<Group> {
        return ibanDao.get()
    }

    fun getLatestTransfersByIban(iban: Iban, start: Long, limit: Int): Flux<Transfer> {
        return ibanTransferDao.getLatestTransfersByIban(iban, start, limit)
    }
}