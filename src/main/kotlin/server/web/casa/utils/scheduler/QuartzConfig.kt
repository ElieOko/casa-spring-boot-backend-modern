package server.web.casa.utils.scheduler

import org.quartz.Scheduler
import org.quartz.impl.StdSchedulerFactory
import org.springframework.context.annotation.*

//@Configuration
//class QuartzConfig {
//    @Bean
//    fun scheduler(): Scheduler {
//        val factory = StdSchedulerFactory().apply {
//            // éventuellement passer des Properties
//        }
//
//        val scheduler = factory.scheduler
//        scheduler.start()
//        return scheduler
//    }
//}
import org.quartz.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.scheduling.quartz.SpringBeanJobFactory

@Configuration
class QuartzConfig(
    private val springJobFactory: SpringJobFactory
) {

    @Bean
    fun schedulerFactoryBean(): SchedulerFactoryBean {
        val factory = SchedulerFactoryBean()
        factory.setJobFactory(springJobFactory)
        return factory
    }
}

