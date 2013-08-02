import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Customer extends Table{

	public Customer(Connection con) {
		super(con);
	}

	@Override
	void insert() {
		int                ccid = -1;     //1
		String			   cpassword = null; 	//2
		String			   cname = null; 	//3
		String			   caddress = null; 	//4
		int			       cphone = -1; 	//5
		
		PreparedStatement  ps;  

		try
		{
			ps = con.prepareStatement("INSERT INTO Item VALUES (?,?,?,?,?)");


			setNull(ps, askUser(in, "Customer ID: "), true, 1, ccid);

			setNull(ps, askUser(in, "Customer Password: "), false, 2, cpassword);
			
			setNull(ps, askUser(in, "Customer Name: "), false, 3, cname);

			setNull(ps, askUser(in, "Customer Address: "), false, 4, caddress);
			
			setNull(ps, askUser(in, "Customer Phone: "), false, 5, cphone);

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
