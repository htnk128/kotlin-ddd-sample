package htnk128.kotlin.ddd.sample.address.domain.service

import htnk128.kotlin.ddd.sample.address.domain.model.address.AccountId
import htnk128.kotlin.ddd.sample.address.domain.model.address.Owner
import reactor.core.publisher.Mono

/**
 * 住所の持ち主([Owner])ドメインの操作を提供するドメインサービス。
 */
interface OwnerDomainService {

    fun find(accountId: AccountId): Mono<Owner>
}
