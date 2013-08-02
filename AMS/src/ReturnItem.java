import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class ReturnItem extends Table{

	public ReturnItem(Connection con) {
		super(con);
	}

	@Override
	void insert() {
		int                rretid = -1;         //1
		int			       iupc = -1;          //2
		int			       riquantity = -1;    //3		
		
		PreparedStatement  ps;  

		try
		{
			ps = con.prepareStatement("INSERT INTO Item VALUES (?,?,?");


			setNull(ps, askUser(in, "Return ID: "), true, 1, rretid);

			setNull(ps, askUser(in, "Item UPC: "), false, 2, iupc);
			
			setNull(ps, askUser(in, "Return Item Quantity: "), false, 3, riquantity);

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
