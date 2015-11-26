package com.hcentive.cloudmanage;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.joda.time.DateTime;
import org.quartz.SchedulerException;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;


@Configuration
@EnableCaching
@EnableScheduling
@EnableJpaRepositories(basePackages = { "com.hcentive.cloudmanage.security" })
public class ScheduleConfig {

	/**
	 * Who ensures shutdown();
	 * 
	 * @return
	 * @throws SchedulerException
	 */
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean()
			throws SchedulerException {
		SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
		scheduler.setDataSource(getDatasource());

		Properties quartzProps = new Properties();
		// Use Persistence
		quartzProps.setProperty("org.quartz.jobStore.class",
				"org.quartz.impl.jdbcjobstore.JobStoreTX");
		// Use Driver for Database - MySQL has default
		quartzProps.setProperty("org.quartz.jobStore.driverDelegateClass",
				"org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
		// Table prefix
		quartzProps.setProperty("org.quartz.jobStore.tablePrefix", "QRTZ_");
		// True if JobDataMaps will always have strings and no objects.
		quartzProps.setProperty("org.quartz.jobStore.useProperties", "false");

		scheduler.setQuartzProperties(quartzProps);

		return scheduler;
	}

	@Bean
	public EntityManagerFactory entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		factory.setPackagesToScan("com.hcentive.cloudmanage.security");
		factory.setDataSource(getDatasource());
		factory.afterPropertiesSet();
		return factory.getObject();
	}

	@Bean
	public CacheManager cacheManager() {
		// The constructor is of type String...
		// Lazy for all objects in case its left null.
		// return new ConcurrentMapCacheManager("appAuthorityMapCache");
		return new EhCacheCacheManager(ehCacheCacheManager().getObject());
	}

	@Bean
	public EhCacheManagerFactoryBean ehCacheCacheManager() {
		EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
		cmfb.setConfigLocation(new ClassPathResource("ehcache.xml"));
		cmfb.setShared(true);
		return cmfb;
	}

	// The below can be skipped as EhCache comes with flushing!
	// comma separate evictions
	@Caching(evict = { @CacheEvict(value = "appAuthorityMapCache", allEntries = true) })
	// Scheduled at 60 seconds for testing.
	@Scheduled(cron = "60 * * * * ?")
	public void flushAllCaches() {
		// Marker - method
		System.out.println(new DateTime() + "Removed all Cache!");
	}

	@Bean
	public DataSource getDatasource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost:3306/quartz");
		ds.setUsername("quartz");
		ds.setPassword("quartz");
		return ds;
	}
}
