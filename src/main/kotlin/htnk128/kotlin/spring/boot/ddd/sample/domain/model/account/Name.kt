package htnk128.kotlin.spring.boot.ddd.sample.domain.model.account

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonValue
import htnk128.kotlin.spring.boot.ddd.sample.domain.shared.SingleValueObject

class Name(
    @get:JsonValue
    @get:JsonIgnore
    override val value: String
) : SingleValueObject<Name, String>()
