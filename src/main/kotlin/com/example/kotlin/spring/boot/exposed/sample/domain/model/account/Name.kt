package com.example.kotlin.spring.boot.exposed.sample.domain.model.account

import com.example.kotlin.spring.boot.exposed.sample.domain.shared.SingleValueObject

class Name(override val value: String) : SingleValueObject<Name, String>()
