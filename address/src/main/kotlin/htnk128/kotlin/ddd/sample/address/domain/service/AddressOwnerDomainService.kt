package htnk128.kotlin.ddd.sample.address.domain.service

import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressOwner
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressOwnerId
import reactor.core.publisher.Mono

/**
 * 住所の持ち主([AddressOwner])ドメインの操作を提供するドメインサービス。
 */
interface AddressOwnerDomainService {

    fun findOwner(addressOwnerId: AddressOwnerId): Mono<AddressOwner>
}
