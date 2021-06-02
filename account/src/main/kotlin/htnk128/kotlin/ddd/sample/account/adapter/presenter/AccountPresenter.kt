package htnk128.kotlin.ddd.sample.account.adapter.presenter

import htnk128.kotlin.ddd.sample.account.domain.model.account.Account
import htnk128.kotlin.ddd.sample.account.usecase.outputport.AccountPresenter
import htnk128.kotlin.ddd.sample.account.usecase.outputport.dto.AccountDTO
import htnk128.kotlin.ddd.sample.shared.usecase.outputport.dto.PaginationDTO
import org.springframework.stereotype.Component

@Component
class AccountPresenter : AccountPresenter {

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
}
