import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Return extends Table{

	public Return(Connection con) {
		super(con);
	}

	@Override
	void insert() {
		int                rid = -1;     //1
		String			   rdate = null; 	//2
		int			       preceiptid = -1; //3
		
		PreparedStatement  ps;  

		try
		{
			ps = con.prepareStatement("INSERT INTO Item VALUES (?,?,?");


			setNull(ps, askUser(in, "Return ID: "), true, 1, rid);

			setNull(ps, askUser(in, "Return Date: "), false, 2, rdate);
			
			setNull(ps, askUser(in, "Purchase Receipt ID: "), false, 3, preceiptid);

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
