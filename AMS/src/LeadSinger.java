import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LeadSinger extends Table{

	public LeadSinger(Connection con) {
		super(con);
	}

	@Override
	void insert() {
		int                iupc = -1;     //1
	    String             lsname = null;  //2
	    PreparedStatement  ps;  
	      
	    try
	    {
	      ps = con.prepareStatement("INSERT INTO LeadSinger VALUES (?,?)");


	      setNull(ps, askUser(in, "Item UPC: "), true, 1, iupc);
	      
	      setNull(ps, askUser(in, "LeadSinger Name: "), true, 2, lsname);

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
