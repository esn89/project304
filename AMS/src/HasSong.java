import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class HasSong extends Table{

	public HasSong(Connection con) {
		super(con);
	}

	@Override
	void insert() {
		int                iupc = -1;     //1
		String             sname = null;  //2
		PreparedStatement  ps;  

		try
		{
			ps = con.prepareStatement("INSERT INTO Item VALUES (?,?,?,?,?,?,?,?)");


			setNull(ps, askUser(in, "Item UPC: "), true, 1, iupc);

			setNull(ps, askUser(in, "Song Name: "), true, 2, sname);

			ps.executeUpdate();

			// commit work 
			con.commit();

			ps.close();
		} catch (IOException e) {
			System.out.println("IOException!");
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
			try {
				// undo the insert
				con.rollback(); 
			} catch (SQLException ex2) {
				System.out.println("Message: " + ex2.getMessage());
				System.exit(-1);
			}
		}

	}

	@Override
	void delete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	void display() {
		// TODO Auto-generated method stub
		
	}

}
