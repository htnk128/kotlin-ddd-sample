package htnk128.kotlin.ddd.sample.ddd.core.domain

/**
 * [DomainEvent]の購読インターフェース。
 */
interface DomainEventSubscriber<T : DomainEvent<*>> {

    /**
     * ドメインイベントを購読しハンドリングする。
     *
     * @param domainEvent ドメインイベント
     */
    fun handleEvent(domainEvent: T)
}
