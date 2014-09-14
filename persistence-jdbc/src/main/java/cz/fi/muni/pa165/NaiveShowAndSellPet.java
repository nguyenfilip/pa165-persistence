package cz.fi.muni.pa165;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Show all pets in the store and then sell one based on users input
 */
public class NaiveShowAndSellPet {
	
	public static void main(String[] args) {
		ApplicationContext appContext = new AnnotationConfigApplicationContext(
				DaoContext.class);
		
		DataSource ds = (DataSource) appContext.getBean("db");
		showAllPets(ds);
		sellPet(ds);
		showAllPets(ds);
		
	}
	private static void showAllPets(DataSource ds) {
		try (Connection con = ds.getConnection()) {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select PETS.ID,NAME, CAPACITY, REMAINING from PETS INNER JOIN CAGES  on PETS.CAGE_FK=CAGES.ID");
			
			while (rs.next()) {
				System.out.println(rs.getInt("ID") +": "+ rs.getString("NAME"));
				System.out.println("  CAGE: " + rs.getString("CAPACITY")+","+rs.getString("REMAINING"));
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void sellPet(DataSource ds) {
		Connection con= null;
		try {
			con = ds.getConnection();
			con.setAutoCommit(false);
			Statement st = con.createStatement();
			
			System.out.println("Which pet do you want to buy?");
			int petId =-1;
			try (Scanner s = new Scanner(System.in)){
				petId = s.nextInt();
			}
			ResultSet rs = st.executeQuery("SELECT CAGE_FK FROM PETS WHERE ID="+petId);
			rs.next();
			int cageId = rs.getInt("CAGE_FK");
			st.executeUpdate("UPDATE CAGES SET REMAINING=REMAINING+1 WHERE REMAINING < CAPACITY AND ID="+cageId);
			st.executeUpdate("UPDATE PETS SET CAGE_FK=NULL WHERE ID="+petId);
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
