package htnk128.kotlin.ddd.sample.address.domain.service.address

import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressOwner
import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressOwnerId
import reactor.core.publisher.Mono

/**
 * 住所の持ち主([AddressOwner])ドメインの操作を提供するドメインサービス。
 */
interface AddressOwnerOperator {

    fun find(addressOwnerId: AddressOwnerId): Mono<AddressOwner>
}
