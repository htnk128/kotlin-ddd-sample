package htnk128.kotlin.ddd.sample.ddd.core.domain

/**
 * [DomainEvent]の出版インターフェース。
 */
interface DomainEventPublisher<T : DomainEvent<*>> {

    /**
     * 指定されたドメインイベントを出版する。
     *
     * @param domainEvent ドメインイベント
     */
    fun publish(domainEvent: T)
}
