package htnk128.kotlin.ddd.sample.account.adapter.presenter

import htnk128.kotlin.ddd.sample.account.domain.model.account.Account
import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountInvalidDataStateException
import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountInvalidRequestException
import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountNotFoundException
import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountUpdateFailedException
import htnk128.kotlin.ddd.sample.account.domain.model.addressbook.AddressBookInvalidRequestException
import htnk128.kotlin.ddd.sample.account.usecase.outputport.dto.AccountDTO
import htnk128.kotlin.ddd.sample.shared.usecase.ApplicationException
import htnk128.kotlin.ddd.sample.shared.usecase.outputport.dto.PaginationDTO
import org.springframework.stereotype.Component

@Component
class AccountPresenter : htnk128.kotlin.ddd.sample.account.usecase.outputport.AccountPresenter {

    override fun toDTO(account: Account): AccountDTO {
        return AccountDTO(
            account.accountId.id(),
            account.name.value(),
            account.namePronunciation.value(),
            account.email.value(),
            account.password.format(),
            account.createdAt.toEpochMilli(),
            account.deletedAt?.toEpochMilli(),
            account.updatedAt.toEpochMilli()
        )
    }

    override fun toDTO(accounts: List<Account>, count: Int, limit: Int, offset: Int): PaginationDTO<AccountDTO> {
        return PaginationDTO(
            count,
            limit,
            offset,
            accounts.map { toDTO(it) }
        )
    }

    override fun error(t: Throwable): Throwable {
        return when (t) {
            is AccountInvalidRequestException -> ApplicationException(t.type, 400, t.message, t)
            is AddressBookInvalidRequestException -> ApplicationException(t.type, 400, t.message, t)
            is AccountNotFoundException -> ApplicationException(t.type, 404, t.message, t)
            is AccountInvalidDataStateException -> ApplicationException(t.type, 409, t.message, t)
            is AccountUpdateFailedException -> ApplicationException(t.type, 500, t.message, t)
            else -> ApplicationException(t.message, t)
        }
    }
}
