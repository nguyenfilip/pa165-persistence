package cz.fi.muni.pa165;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import cz.fi.muni.pa165.tier.CageDao;
import cz.fi.muni.pa165.tier.PetDao;
import cz.fi.muni.pa165.tier.PetStoreService;
import cz.fi.muni.pa165.tier.PetStoreView;


@Configuration
//@ImportResource("classpath:spring-context.xml")
@EnableTransactionManagement
public class DaoContext {
	
	
	@Bean
	public PlatformTransactionManager transactionManager(){
		PlatformTransactionManager ptx = new DataSourceTransactionManager(db());
		return ptx;
	}
	
	@Bean
	public PetDao petDao(){
		PetDao petDao = new PetDao();
		petDao.setDatasource(db());
		return petDao;
	}
	
	@Bean
	public CageDao cageDao(){
		CageDao cageDao = new CageDao();
		cageDao.setDatasource(db());
		return cageDao;
	}
	
	@Bean
	public PetStoreService petstoreSservice(){
		PetStoreService service = new PetStoreService();
		service.setCageDao(cageDao());
		service.setPetDao(petDao());
		return service;
	}
	
	@Bean
	public PetStoreView view(){
		PetStoreView view = new PetStoreView();
		view.setService(petstoreSservice());
		return view;
	}
	
	@Bean 
	public JdbcTemplate dbTemplate(){
		JdbcTemplate template = new JdbcTemplate(db());
		return template;
	}


	@Bean
	public DataSource db(){
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		EmbeddedDatabase db = builder.setType(EmbeddedDatabaseType.DERBY).addScript("startup.sql").build();
		return db;
	}
}
