package server.web.casa.utils.scheduler

import org.quartz.Job
import org.quartz.spi.JobFactory
import org.quartz.spi.TriggerFiredBundle
import org.springframework.beans.factory.config.AutowireCapableBeanFactory
import org.springframework.context.ApplicationContext
import org.springframework.scheduling.quartz.SpringBeanJobFactory
import org.springframework.stereotype.Component

//@Component
//class AutowiringSpringBeanJobFactory(
//    private val beanFactory: AutowireCapableBeanFactory
//) : JobFactory {
//
//    override fun newJob(bundle: TriggerFiredBundle, scheduler: org.quartz.Scheduler?): Job {
//        val job = bundle.jobDetail.jobClass.getDeclaredConstructor().newInstance()
//        beanFactory.autowireBean(job) // injecte automatiquement les @Autowired
//        return job
//    }
//}

//@Component
//class AutowiringSpringBeanJobFactory(applicationContext: ApplicationContext) : SpringBeanJobFactory() {
//    init {
//        setApplicationContext(applicationContext)
//    }
//}



@Component
class SpringJobFactory(
    private val autowireCapableBeanFactory: AutowireCapableBeanFactory
) : SpringBeanJobFactory() {

    override fun createJobInstance(bundle: TriggerFiredBundle): Any {
        val jobInstance = super.createJobInstance(bundle)
        autowireCapableBeanFactory.autowireBean(jobInstance)
        return jobInstance
    }
}