import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class PurchaseItem extends Table{

	public PurchaseItem(Connection con) {
		super(con);
	}

	@Override
	void insert() {
		int                preceiptid = -1;     //1
		int                iupc = -1;           //2
		int                ccid = -1;           //3

		PreparedStatement  ps;  

		try
		{
			ps = con.prepareStatement("INSERT INTO Item VALUES (?,?,?)");


			setNull(ps, askUser(in, "Purchase Receipt ID: "), true, 1, preceiptid);

			setNull(ps, askUser(in, "Item UPC: "), false, 2, iupc);
			
			setNull(ps, askUser(in, "Customer ID: "), false, 3, ccid);

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
