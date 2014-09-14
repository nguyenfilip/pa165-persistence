package cz.fi.muni.pa165.tier;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class PetStoreView {

	private PetStoreService service = null;

	public PetStoreService getService() {
		return service;
	}

	public void setService(PetStoreService service) {
		this.service = service;
	}

	public void startInteraction() {
		System.out.println("************************************");
		System.out.println("Hello this is pet store service");

		System.out.println(service.getCageDao().find(1));
		Map<Integer, Pet> pets = service.getAllPets();
		printPets(pets);

		System.out.println("Which pet do you want to sell?");
		int petToSellId = -1;
		try (Scanner s = new Scanner(System.in)) {
			petToSellId = s.nextInt();
		}

		try {
			service.sellPet(pets.get(petToSellId));
		} catch (Exception ex) {
			System.out.println("Sell pet threw an exception!!");
		}
		pets = service.getAllPets();
		printPets(pets);
		System.out.println(service.getCageDao().find(1));
	}

	private void printPets(Map<Integer, Pet> pets) {
		for (Entry<Integer, Pet> entry : pets.entrySet()) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}
	}

}
