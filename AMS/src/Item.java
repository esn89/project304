import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;


public class Item extends Table{
	


	public Item(Connection con) {
		super(con);
	}

	@Override
	void insert() {
		int                iupc = -1;     //1
	    String             ititle = null;   //2
	    String             itype = null;    //3
	    String             icategory = null; //4
	    String             icompany = null;  //5
	    int                iyear = -1;     //6
	    double             iprice = -1;    //7
	    boolean            istock = false;    //8
	    PreparedStatement  ps;  
	      
	    try
	    {
	      ps = con.prepareStatement("INSERT INTO Item VALUES (?,?,?,?,?,?,?,?)");


	      setNull(ps, askUser(in, "Item UPC: "), true, 1, iupc);
	      
	      setNull(ps, askUser(in, "Item Title: "), false, 2, ititle);
	      
	      setNull(ps, askUser(in, "Item Type: "), false, 3, itype);

	      setNull(ps, askUser(in, "Item Category: "), false, 4, icategory);
	                          
	      setNull(ps, askUser(in, "Item Company: "), false, 5, icompany);
	      
	      setNull(ps, askUser(in, "Item Year: "), false, 6, iyear);

	      setNull(ps, askUser(in, "Item Price: "), false, 7, iprice);
	      
	      setNull(ps, askUser(in, "Item Stock: "), false, 8, istock);

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
	    String             iupc;     //1
	    String             ititle;   //2
	    String             itype;    //3
	    String             icategory; //4
	    String             icompany;  //5
	    String             iyear;     //6
	    String             iprice;    //7
	    String             istock;    //8
	    Statement  stmt;
	    ResultSet  rs;

	    try
	    {
	        stmt = con.createStatement();

	        rs = stmt.executeQuery("SELECT * FROM Item");

	        // get info on ResultSet
	        ResultSetMetaData rsmd = rs.getMetaData();

	        // get number of columns
	        int numCols = rsmd.getColumnCount();

	        System.out.println(" ");

	        // display column names;
	        for (int i = 0; i < numCols; i++)
	        {
	            // get column name and print it

	            System.out.printf("%-15s", rsmd.getColumnName(i+1));
	        }

	        System.out.println(" ");

	        while(rs.next())
	        {
	            // for display purposes get everything from Oracle
	            // as a string

	            // simplified output formatting; truncation may occur

	            iupc = rs.getString("item_upc");
	            System.out.printf("%-10.10s", iupc);

	            ititle = rs.getString("item_title");
	            checkNull(rs, ititle);

	            itype = rs.getString("item_type");
	            checkNull(rs, itype);

	            icategory = rs.getString("item_category");
	            checkNull(rs, icategory);

	            icompany = rs.getString("item_company");
	            checkNull(rs, icompany);

	            iyear = rs.getString("item_year");
	            checkNull(rs, iyear);

	            iprice = rs.getString("item_price");
	            checkNull(rs, iprice);

	            istock = rs.getString("item_stock");
	            checkNull(rs, istock);

	        }

	        // close the statement;
	        // the ResultSet will also be closed
	        stmt.close();
	    }
	    catch (SQLException ex)
	    {
	        System.out.println("Message: " + ex.getMessage());
	    }
		
	}

}
