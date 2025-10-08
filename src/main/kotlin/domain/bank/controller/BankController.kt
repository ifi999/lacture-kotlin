package org.example.domain.bank.controller

import org.example.domain.bank.service.BankService
import org.example.types.dto.Response
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping("/api/v1/bank")
class BankController (
    private val bankService: BankService
) {

    @PostMapping("/create/{ulid}")
    fun createAccount(
        @PathVariable("userUlid", required = true) ulid: String
    ) : Response<String> {
        return bankService.createAccount(ulid)
    }

    @GetMapping("/balance/{userUlid}/{accountUlid}")
    fun balance(
        @PathVariable("userUlid", required = true) userUlid: String,
        @PathVariable("accountUlid", required = true) accountUlid: String
    ) : Response<BigDecimal> {
        return bankService.balance(userUlid, accountUlid)
    }

    @PostMapping("/remove/{userUlid}/{accountUlid}")
    fun removeAccount(
        @PathVariable("userUlid", required = true) userUlid: String,
        @PathVariable("accountUlid", required = true) accountUlid: String
    ) : Response<String> {
        return bankService.removeAccount(userUlid, accountUlid)
    }

}