package cz.fi.muni.pa165.tier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;

public class PetDao {

	private DataSource ds = null;

	public void setDatasource(DataSource db) {
		this.ds = db;
	}
	
	public void update(Pet pet) {
		Connection con = null;
		try {
			con = ds.getConnection();
			con.setAutoCommit(false);
			PreparedStatement st = con
					.prepareStatement("UPDATE PETS SET ID = ?, NAME=?, TYPENAME=?, CAGE_FK=? WHERE ID = ? ");
			st.setInt(1, pet.getId());
			st.setString(2, pet.getName());
			st.setString(3, pet.getTypename());
			st.setObject(4, pet.getCageFk());
			st.setInt(5, pet.getId());
			st.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			DataSourceUtils.releaseConnection(con, ds);
		}
	}
	

	public void update(Pet pet) {
		Connection con = null;
		try {
			con = DataSourceUtils.getConnection(ds);
			PreparedStatement st = con
					.prepareStatement("UPDATE PETS SET ID = ?, NAME=?, TYPENAME=?, CAGE_FK=? WHERE ID = ? ");
			st.setInt(1, pet.getId());
			st.setString(2, pet.getName());
			st.setString(3, pet.getTypename());
			st.setObject(4, pet.getCageFk());
			st.setInt(5, pet.getId());
			st.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			DataSourceUtils.releaseConnection(con, ds);
		}
	}

	public Map<Integer, Pet> findAllPets() {
		HashMap<Integer, Pet> result = new HashMap<Integer, Pet>();
		try (Connection con = DataSourceUtils.getConnection(ds)) {
			Statement st = con.createStatement();
			ResultSet rs = st
					.executeQuery("select * FROM PETS WHERE CAGE_FK is not null");

			while (rs.next()) {
				Pet pet = new Pet();
				fillPet(pet, rs);
				result.put(pet.getId(), pet);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	private void fillPet(Pet result, ResultSet rs) throws SQLException {
		result.setId(rs.getInt("ID"));
		result.setName(rs.getString("NAME"));
		result.setTypename(rs.getString("TYPENAME"));
		result.setCageFk(rs.getInt("CAGE_FK"));
	}
}
