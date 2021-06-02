package htnk128.kotlin.ddd.sample.account.usecase.outputport

import htnk128.kotlin.ddd.sample.account.domain.model.account.Account
import htnk128.kotlin.ddd.sample.account.usecase.outputport.dto.AccountDTO
import htnk128.kotlin.ddd.sample.shared.usecase.outputport.dto.PaginationDTO

interface AccountPresenter {

    fun toDTO(account: Account): AccountDTO

    fun toDTO(accounts: List<Account>, count: Int, limit: Int, offset: Int): PaginationDTO<AccountDTO>
}
