import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public abstract class Table {

	protected BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

	protected Connection con;  
	
	public Table(Connection con){
		this.con = con;
	}

	public void checkNull(ResultSet rs, String attribute) throws SQLException {
	    if (rs.wasNull())
	        System.out.printf("%-10.10s", " ");

	    else
	        System.out.printf("%-10.10s", attribute);

	}
	
	public String askUser ( BufferedReader in, String message) throws IOException
	{
		System.out.println("\n" + message + ": ");
		return in.readLine();
	}

	public void setNull (PreparedStatement ps, String line, boolean key, int position, double attribute) throws SQLException {
		if (line.length() == 0 && !key) {
			ps.setNull(position, java.sql.Types.DOUBLE);
		} else {
			attribute = Double.parseDouble(line);
			ps.setDouble(position, attribute);
		}
	}

	public void setNull (PreparedStatement ps, String line, boolean key, int position, int attribute) throws SQLException {
		if (line.length() == 0 && !key) {
			ps.setNull(position, java.sql.Types.INTEGER);
		} else {
			attribute = Integer.parseInt(line);
			ps.setInt(position, attribute);
		}
	}
	
	public void setNull (PreparedStatement ps, String line, boolean key, int position, Date attribute) throws SQLException {
		if (line.length() == 0 && !key) {
			ps.setDate(position, null);
		} else {
			ps.setDate(position, attribute);
		}
	}
	
	public void setNull (PreparedStatement ps, String line, boolean key, int position) throws SQLException {
		if (line.length() == 0 && !key) {
			ps.setString(position, null);
		} else {
			ps.setString(position, line);
		}
	}


	abstract void insert();

	abstract void delete();

	abstract void display();
}
