package htnk128.kotlin.ddd.sample.address.domain.model.owner

import reactor.core.publisher.Mono

/**
 * 住所の持ち主([Owner])ドメインの操作を提供するドメインサービス。
 */
interface OwnerService {

    fun find(ownerId: OwnerId): Mono<Owner>
}
