package htnk128.kotlin.ddd.sample.address.adapter.gateway.messaging

import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressEvent
import htnk128.kotlin.ddd.sample.dddcore.domain.DomainEventPublisher
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class AddressEventSpringPublisher(
    private val eventPublisher: ApplicationEventPublisher
) : DomainEventPublisher<AddressEvent<*>> {

    override fun publish(domainEvent: AddressEvent<*>) {
        eventPublisher.publishEvent(domainEvent)
    }
}
