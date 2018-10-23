package htnk128.kotlin.spring.boot.ddd.sample

import org.jetbrains.exposed.spring.SpringTransactionManager
import org.springframework.context.annotation.*
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.*
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
class ExposedConfiguration(val dataSource: DataSource) : TransactionManagementConfigurer {

    @Bean
    override fun annotationDrivenTransactionManager(): PlatformTransactionManager =
        SpringTransactionManager(dataSource)
}
