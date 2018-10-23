package htnk128.kotlin.spring.boot.ddd.sample.domain.model.account

import htnk128.kotlin.spring.boot.ddd.sample.domain.shared.SingleValueObject

class Name(override val value: String) : SingleValueObject<Name, String>()
