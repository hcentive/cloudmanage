package com.hcentive.cloudmanage;

import java.util.Properties;

import javax.sql.DataSource;

import org.quartz.SchedulerException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
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
	public DataSource getDatasource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost:3306/quartz");
		ds.setUsername("quartz");
		ds.setPassword("quartz");
		return ds;
	}
}
