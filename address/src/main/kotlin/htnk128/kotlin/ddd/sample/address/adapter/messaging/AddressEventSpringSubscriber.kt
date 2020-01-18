package htnk128.kotlin.ddd.sample.address.adapter.messaging

import htnk128.kotlin.ddd.sample.address.domain.model.address.AddressEvent
import htnk128.kotlin.ddd.sample.dddcore.domain.DomainEventSubscriber
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class AddressEventSpringSubscriber : DomainEventSubscriber<AddressEvent<*>> {

    @EventListener
    override fun handleEvent(domainEvent: AddressEvent<*>) {
        println("type=${domainEvent.type}, address=${domainEvent.address}, occurredOn=${domainEvent.occurredOn}")
        // 何もしない。メールを送る、REST APIを叩く、どっかに通知を送るなどが考えられる
    }
}
