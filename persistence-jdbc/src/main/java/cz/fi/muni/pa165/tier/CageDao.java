package cz.fi.muni.pa165.tier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;

public class CageDao {

	private DataSource ds = null;

	public void setDatasource(DataSource db) {
		this.ds = db;
	}

	public void update(Cage cage) {
		Connection con = null;
		try {
			con = DataSourceUtils.getConnection(ds);
			PreparedStatement st = con
					.prepareStatement("UPDATE CAGES SET ID=?, DESCRIPTION=?, CAPACITY=?, REMAINING=? WHERE ID = ?");
			st.setInt(1, cage.getId());
			st.setString(2, cage.getDescription());
			st.setInt(3, cage.getCapacity());
			st.setInt(4, cage.getRemaining());
			st.setInt(5, cage.getId());
			st.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			DataSourceUtils.releaseConnection(con, ds);
		}
	}

	public Cage find(int id) {
		Cage result = new Cage();
		Connection con = null;
		try {
			con = DataSourceUtils.getConnection(ds);
			PreparedStatement st = con
					.prepareStatement("select * FROM CAGES where ID=?");
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			rs.next();
			result.setId(rs.getInt("ID"));
			result.setDescription(rs.getString("DESCRIPTION"));
			result.setRemaining(rs.getInt("REMAINING"));
			result.setCapacity(rs.getInt("CAPACITY"));

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DataSourceUtils.releaseConnection(con, ds);
		}
		return result;
	}
}
