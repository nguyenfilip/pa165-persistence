package cz.fi.muni.pa165.tier;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import cz.fi.muni.pa165.DaoContext;

/**
 * Show all pets in the store and then sell one based on users input
 */
public class PrimitiveTierMain {
	
	public static void main(String[] args) {
		ApplicationContext appContext = new AnnotationConfigApplicationContext(
				DaoContext.class);
		
		PetStoreView view = appContext.getBean("view",PetStoreView.class);
		view.startInteraction();
	}
	
	
}
