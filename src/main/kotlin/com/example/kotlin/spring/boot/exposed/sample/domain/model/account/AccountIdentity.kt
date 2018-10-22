package com.example.kotlin.spring.boot.exposed.sample.domain.model.account

import com.example.kotlin.spring.boot.exposed.sample.domain.model.StringIdentity
import java.util.UUID

class AccountIdentity(override val value: String) : StringIdentity<AccountIdentity>() {

    companion object {

        fun generate(): AccountIdentity = AccountIdentity(UUID.randomUUID().toString())
    }
}
