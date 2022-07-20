package htnk128.kotlin.ddd.sample.account.adapter.gateway.messaging

import htnk128.kotlin.ddd.sample.account.domain.model.account.AccountEvent
import htnk128.kotlin.ddd.sample.ddd.core.domain.DomainEventSubscriber
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class AccountEventSpringSubscriber : DomainEventSubscriber<AccountEvent<*>> {

    @EventListener
    override fun handleEvent(domainEvent: AccountEvent<*>) {
        println("type=${domainEvent.type}, account=${domainEvent.account}, occurredOn=${domainEvent.occurredOn}")
        // 何もしない。メールを送る、REST APIを叩く、どっかに通知を送るなどが考えられる
    }
}
