package htnk128.kotlin.ddd.sample.account.application.service

import htnk128.kotlin.ddd.sample.account.application.dto.AccountDTO
import htnk128.kotlin.ddd.sample.account.domain.model.account.Account
import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountInvalidDataStateException
import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountInvalidRequestException
import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountNotFoundException
import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountUpdateFailedException
import htnk128.kotlin.ddd.sample.account.domain.model.address.AddressInvalidRequestException
import htnk128.kotlin.ddd.sample.shared.application.exception.ApplicationException

fun Account.toDTO(): AccountDTO =
    AccountDTO(
        accountId.id(),
        name.toValue(),
        namePronunciation.toValue(),
        email.toValue(),
        password.format(),
        createdAt.toEpochMilli(),
        deletedAt?.toEpochMilli(),
        updatedAt.toEpochMilli()
    )

fun Throwable.error(): Throwable =
    when (this) {
        is AccountInvalidRequestException -> ApplicationException(type, 400, message, this)
        is AddressInvalidRequestException -> ApplicationException(type, 400, message, this)
        is AccountNotFoundException -> ApplicationException(type, 404, message, this)
        is AccountInvalidDataStateException -> ApplicationException(type, 409, message, this)
        is AccountUpdateFailedException -> ApplicationException(type, 500, message, this)
        else -> ApplicationException(message, this)
    }
