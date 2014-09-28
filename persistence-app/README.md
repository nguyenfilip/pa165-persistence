
## Seminar 01 23.9.2014 
Your first tasks will be related to module persistence-app. It will teach you basic work with JPA in Java SE application. 

**Task 01** Your first task is to locate and download JPA 2.1 specification (JSR 338). It is a PDF file. 
   
**Task 02** Try to run cz.fi.muni.pa165.AppMain from NetBeans 

**Task 03** Run the main method also  from command line (using Maven exec:java 
target) see documentation for Maven here http://mojo.codehaus.org/exec-maven-plugin/java-mojo.html

**Task 04** Add configuration property to persistence.xml so that Hibernate writes all generated SQL statements to console. See A.2 section of
https://docs.jboss.org/hibernate/orm/4.3/devguide/en-US/html_single/ . Rerun **Task 03** to confirm that you can now see the SQL

**Task 05** Modify AppMain method. Create two more pets to be stored in the database and persist them. Rerun **Task 03**

**Task 06** Our application can store Pets. However, the pets cannot be yet assigned to Cages. The persistence-app project contains cz.fi.muni.pa165.entity.Cage class that should represent the Cage entity. However JPA annotations are missing. Add the annotations. For now, leave the "private Cage cage" field and do not map or modify it.

**Task 07** Verify that a Cage can be created, stored and retrieved from the database. To do this, persist two cages and implement new method AppMain.findAllCages(EntityManagerFactory emf). 

**Task 08** Wait. There is a bug in Cage entity. Its a nasty one, because our application works well so far. I belive you will find the bug if you take a look into specification. Some methods that JPA specification requires are missing. Hint: look through bulletpoints on page 30 in the specification (section 2.4).

**Task 09** Adding relationships. Now we have cages and we have Pets. We would 
like to assign a Cage to a Pet. To be able to do so you need to map the relationship. In Pet entity, remove @Transient and replace it with @ManyToOne. Rerun **Task 03** to verify everything works fine so far. Now in your main method use setCage on some of your pets to add the Pet to the Cage. 

**Task 10** To understand transient instances, try code such as this: 
```java
		em.getTransaction().begin();
		Pet pet = new Pet();
		pet.setName("Small Rat");
		Cage cage = new Cage();
		cage.setDescription("SMALL CAGE");
		pet.setCage(cage);
		em.persist(pet);
		em.getTransaction().commit();
```

You will get TransientPropertyValueException. Why is that? Its because your Cage is not persisted. Fix this.

**Task 11** We now can set a Cage for a Pet. Let's output this fact to the console. Modify your AppMain.printAllPets to not only show all Pets but also for each Pet print out the cage in which it is accomodated.

**Task 12** Perfect, now we have unidirectional relationship where Pet has a reference to a Cage. We would like to create this relationship bidirectional (so that Cage also knows all Pets in itself). Open Cage entity and remove @Transient annotation. Instead add @OneToMany(mappedBy="cage").

**Task 13** Make sure that in your main method you create at least 2 Cages and at least 4 Pets and that you distribute these Pets among the Cages. 

**Task 14** Modify your AppMain.printAllCages to print all pets in the Cage.

**Task 15** Make sure you maintain runtime consistency of your relationships. 
It means that when you add a cage to a pet through Pet.setCage() method, you also add the Pet to the collection held in the Cage entity. In other words, you maintain the relationship from both sides. This is very important and JPA spec requires the developer to do this!

**Task 16** You can see that the output shows that Pets have birthDate also with time. This information is really not necessary. Change the annotation on birthDate field in Pet so that the date is stored in the database only witth Year, Month, Date components.  

**Task 17** Quiz. You can check your answers after you take the quiz in file answ-q1.md 
 1. What is owning side of a relationship? Read through JPA spec, section 2.9 to answer this question. 
 2. Which side of the relationshiop in our example is the owning side?
 3. What is the main configuration file for JPA in your application?
 4. Where is the following text used and what is the effect of it (use Hibernate dev guide to find answer)? "hibernate.format_sql"
 5. What is hibernate.hbm2ddl.auto property in persistence.xml file?

 
**Task 18** As homework, solve the following https://is.muni.cz/auth/el/1433/podzim2014/PA165/um/cv/2012_PA165_cv02.pdf in a new Java project. Consider downloading the .pdf file in case you are not able to copy the xml snippet from the browser pdf reader.
 


