package htnk128.kotlin.ddd.sample.account.adapter.gateway.messaging

import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountEvent
import htnk128.kotlin.ddd.sample.dddcore.domain.DomainEventPublisher
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class AccountEventSpringPublisher(
    private val eventPublisher: ApplicationEventPublisher
) : DomainEventPublisher<AccountEvent<*>> {

    override fun publish(domainEvent: AccountEvent<*>) {
        eventPublisher.publishEvent(domainEvent)
    }
}
