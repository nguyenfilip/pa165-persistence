package cz.fi.muni.pa165;

import java.util.Arrays;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	ApplicationContext appContext = new AnnotationConfigApplicationContext(DaoContext.class);
    	PersonDao personDao = appContext.getBean("personDao",PersonDao.class);
    	System.out.println(personDao.savePerson().getId());
    	System.out.println(Arrays.toString(personDao.findAllPeople().toArray()));
    	
    	
//    	DogDao dogDao = appContext.getBean("dogDao",DogDao.class);
//    	System.out.println(dogDao.savePerson().getId());
    }
}
