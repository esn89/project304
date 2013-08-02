import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Purchase extends Table{

	public Purchase(Connection con) {
		super(con);
	}

	@Override
	void insert() {
		int                preceiptid = -1;     //1
		String             pdate = null;  //2
		int                ccard = -1;     //3
		String             cexprdate = null;  //4
		String             pexpdate = null;  //5
		String             pdelidate = null;  //6
		PreparedStatement  ps;  

		try
		{
			ps = con.prepareStatement("INSERT INTO Item VALUES (?,?,?,?,?,?)");


			setNull(ps, askUser(in, "Purchase Receipt ID: "), true, 1, preceiptid);

			setNull(ps, askUser(in, "Purchase Date: "), false, 2, pdate);
			
			setNull(ps, askUser(in, "Credit Card #: "), false, 3, ccard);
			
			setNull(ps, askUser(in, "Credit Card Expiry Date: "), false, 4, cexprdate);
			
			setNull(ps, askUser(in, "Expected Delivery Date: "), false, 5, pexpdate);
			
			setNull(ps, askUser(in, "Actual Delivery Date: "), false, 6, pdelidate);

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
