package htnk128.kotlin.ddd.sample.dddcore.domain

/**
 * [DomainEvent]の出版インターフェース。
 */
interface DomainEventPublisher {

    /**
     * 指定されたドメインイベントを出版する。
     *
     * @param domainEvent ドメインイベント
     */
    fun publish(domainEvent: DomainEvent<*>)
}
