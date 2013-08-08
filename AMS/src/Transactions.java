import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//import java.util.Date;
import java.sql.Date;

public class Transactions {
	Connection con;
	BufferedReader in = new BufferedReader((new InputStreamReader(System.in)));
	int receiptno = 555;		
	int returnId = 555;
	final int ORDERS_A_DAY = 10;

	public Transactions (Connection con) {
		this.con = con;
	}

	public static Date addDays(Date date, int numDays)		// helper function for adding a number of days to current date
	{
		date.setTime(date.getTime() + numDays * 1000 * 60 * 60 * 24);
		return date;
	}

	//---------------------------IN STORE PURCHASE-----------------------------------------------	

	public void inStorePurchase() throws SQLException, IOException, ParseException {
		Statement stmt = con.createStatement();
		Statement stmtt = con.createStatement();
		ResultSet rs;
		int iupc = -1;
		String purchaseDate = null;
		int quantity = -1;
		int creditno = -1;
		int cid = -1;
		String expiryDate = null;			

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
		purchaseDate = format.format(sqlDate);
		System.out.println("Current Date: " + purchaseDate); 

		//stmt.executeQuery("INSERT INTO Purchase VALUES (" + receiptno + ", " + sqlDate + ", " + cid + ", " + creditno + ", " + expiryDate + ", " + expectedDate + ", " + deliveredDate);

		System.out.println("Input valid Customer cid.");
		cid = Integer.parseInt(in.readLine());

		stmt.executeQuery("INSERT INTO Purchase (purchase_receiptid, purchase_date, customer_cid) VALUES ( purchase_receiptid_counter.nextval, to_date('" + purchaseDate + "','yyyy-MM-dd')" + ", " + cid + ")");
		rs = stmt.executeQuery("SELECT purchase_receiptid_counter.currval FROM Purchase");
		while (rs.next()) {
			receiptno = rs.getInt(1);		// get current counter for receiptno
		}

		do {	
			System.out.println("Please input upc of item.");
			iupc = Integer.parseInt(in.readLine());
			rs = stmt.executeQuery("SELECT item_title FROM Item WHERE item_upc = " + iupc);		
			rs.next();

			System.out.println("Insert Quantity.");
			quantity = Integer.parseInt(in.readLine()); 

			stmt.executeQuery("INSERT INTO PurchaseItem VALUES (" + receiptno + ", " + iupc + ", " + quantity + ")");
			System.out.println("Do you have more stuff? (Y/N)");
		} while ("Y".equals(in.readLine()));

		System.out.println("Are you paying by Credit Card? (Y/N) ");
		if ("Y".equals(in.readLine())) {
			System.out.println("Input Credit Card Number.");
			creditno = Integer.parseInt(in.readLine());
			System.out.println("Input Credit Card Expiry Date.");
			expiryDate = in.readLine();
			System.out.println("expiryDate:" + expiryDate);

			System.out.println("Clerk, was the customer's card approved?");
			if ("N".equals(in.readLine())) {
				System.out.println("Do you have cash?");
				if ("N".equals(in.readLine())) {
					stmt.executeQuery("DELETE PurchaseItem WHERE purchase_receiptId = " + receiptno);
					stmt.executeQuery("DELETE Purchase WHERE purchase_receiptId = " + receiptno);
					System.out.println("Purchase deleted");
					return;
				} else {
					creditno = -1;
					expiryDate = null;	
					System.out.println("check check.");

				}
			} 
			System.out.println("creditno = " + creditno + " expirydate: " + expiryDate);
			// update customer card # and card expiry date
			stmt.executeQuery("UPDATE Purchase SET custorder_card# = " + creditno + " WHERE purchase_receiptid = " + receiptno);		
			System.out.println("check 1");
			stmt.executeQuery("UPDATE Purchase SET custorder_expiryDate = to_date('" + expiryDate + "','MM/yy')" + " WHERE purchase_receiptid = " + receiptno);
			System.out.println("check 2");
		} 

		//stmt.executeQuery("INSERT INTO Purchase VALUES (" + receiptno + ", " + sqlDate + ", " + cid + ", " + creditno + ", " + expiryDate + ", " + expectedDate + ", " + deliveredDate);

		// update store stock
		rs = stmt.executeQuery("SELECT item_upc, purchaseitem_quantity FROM PurchaseItem WHERE purchase_receiptid = " + receiptno);

		while (rs.next()) {
			int upc = rs.getInt("item_upc");
			int qty = rs.getInt("purchaseitem_quantity");		
			stmtt.executeQuery("UPDATE Item SET item_stock = item_stock - " + qty + " WHERE item_upc = " + upc);

		}
	}

	// ------------------------------------In STORE REFUND------------------------------------------------	

	public void inStoreRefund() throws SQLException, IOException, ParseException {

		Statement stmt = con.createStatement();
		Statement stmtt = con.createStatement();
		ResultSet rs;

		String rdate = null;

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
		rdate = format.format(sqlDate);		// gets return date or current date

		java.util.Date returnDate = format.parse(rdate);		// need Date returnDate for comparison later
		java.sql.Date sqlReturnDate = new java.sql.Date(returnDate.getTime());

		System.out.println("Return Date: " + rdate); // prints out 2013-08-15

		System.out.println("Please input the receipt id: ");
		int receiptId = Integer.parseInt(in.readLine());

		// check if receipt id exists
		rs = stmt.executeQuery("SELECT purchase_receiptId FROM PurchaseItem WHERE purchase_receiptId = " + receiptId);
		if (rs.next() == false) {

			System.out.println("Clerk, the receipt id does not exist, do you want to try again? (Y/N)");
			if ("Y".equals(in.readLine())) {
				inStoreRefund();
			}
			else {
				return;
			}

		}

		// check that purchase date is within 15 days 

		// get purchase date
		rs = stmt.executeQuery("SELECT purchase_date FROM Purchase WHERE purchase_receiptid = " + receiptId);

		while (rs.next()) {
			String pdate = rs.getString("purchase_date");
			if (rs.wasNull()) {
				System.out.println("Purchase date not found.");
				return;
			}

			//calculate when the last possible day for refund is 
			java.util.Date purchaseDate = format.parse(pdate);		// turned purchase date into a Date
			java.sql.Date sqlPurchaseDate = new java.sql.Date(purchaseDate.getTime());
			Date addedDate = addDays(sqlPurchaseDate, 15);
			//String fifteenDaysLater = format.format(addedDate);		
			if (sqlReturnDate.after(addedDate)) {			// if today is after the refund deadline date, then...
				System.out.println("Clerk, the refund deadline has passed. Customer is unable to do a refund anymore.");
				return;
			}
		}

		// get price of item that they would like to return
		String iprice;
		System.out.println("Please input item upc.");
		int iupc = Integer.parseInt(in.readLine());
		System.out.println("Please input quantity.");
		int iquantity = Integer.parseInt(in.readLine());
		rs = stmt.executeQuery("SELECT item_price FROM Item WHERE item_upc = " + iupc);
		rs.next();
		if (rs.getString(1) != null){
			iprice = rs.getString(1);		// multiply by quantity
			Double ipriceDouble = Double.parseDouble(iprice);
			ipriceDouble *= iquantity;
			iprice = "" + ipriceDouble;
		}
		else {
			System.out.println("Price cannot be found.");
			return;
		}

		// find out whether customer paid credit card or cash
		rs = stmt.executeQuery("SELECT custorder_card# FROM Purchase WHERE purchase_receiptid = " + receiptId);
		rs.next();
		String cardno = rs.getString(1);
		if (cardno != null){
			System.out.print("Clerk, refund $");
			System.out.print(iprice);
			System.out.println(" into credit card number " + cardno + " for item.");
		}
		else {
			System.out.print("Clerk, refund $");
			System.out.print(iprice);
			System.out.println(" for item in cash.");
		}

		stmt.executeQuery("INSERT INTO Return VALUES (return_retid_counter.nextval , to_date('" + rdate + "','yyyy-MM-dd')" + ", " + receiptId + ")");
		stmt.executeQuery("INSERT INTO ReturnItem VALUES (return_retid_counter.currval, " + iupc + ", " + iquantity + ")");   

		// each refund will consist of one type of item

		// update item storage
		stmt.executeQuery("UPDATE Item SET item_stock = item_stock + " + iquantity + " WHERE item_upc = " + iupc);

		System.out.println("Clerk, does the customer want to return another item?");
		if ("Y".equals(in.readLine())) {
			inStoreRefund();
		}
	}


	//-------------------------------ONLINE PURCHASE------------------------------------------------------- 

	public void onlinePurchase () throws  IOException, SQLException, ParseException {
		int id; // customer id
		String pass; // customer password
		Statement stmt = con.createStatement(); // the universal statement
		Statement stmtB = con.createStatement(); // actually we needed another one
		PreparedStatement ps; // prepared statement for updating Purchase entry at the end
		ResultSet rs;  // ResultSet for the universal statement

		String category; // input category
		String title;   // input title
		String singer;  // input lead singer

		int upc = 0;          // upc of selected item
		int qty = 0;            // quantity desired of selected item
		int availableQty = 0;   // number of pieces available in store

		double total = 0.0;     // customer total

		int creditno;        // credit card number
		SimpleDateFormat format = new SimpleDateFormat("MM/yy");
		java.util.Date javaDate; 
		java.sql.Date date;   // credit card expiry date

		int outstandingOrders = 0;   // number of orders not delivered
		int daysUntilDelivery;      // estimated time until delivery
		java.sql.Date currDate;     // the current date

		boolean outOfItems = false;  // set if user goes through everything but does not select

		int receiptno;              // use to store receiptno gotten from sequence currval

		// get the user id and password
		do {
			System.out.println("Identify yourself.");
			id = Integer.parseInt(in.readLine());
			rs = stmt.executeQuery("SELECT * FROM Customer WHERE customer_cid = " + id);
		} while(!rs.next());

		do {
			System.out.println("Pass word?");
			pass = in.readLine();       
		} while (!rs.getString("customer_password").equals(pass));

		// insert a purchase tuple with the receiptid and cid
		stmt.executeQuery("INSERT INTO Purchase (purchase_receiptid, customer_cid) VALUES (purchase_receiptid_counter.nextval, " + id + ")");

		// for future use, get and store the current value of the receiptno
		rs = stmt.executeQuery("SELECT purchase_receiptid_counter.currval FROM Purchase");
		rs.next();
		receiptno = rs.getInt(1);

		// huge loop for selecting items
		do {
			System.out.println("Let's describe the item you want...");
			System.out.println("Category:");
			category = in.readLine();
			System.out.println("Title:");
			title = in.readLine();
			System.out.println("Leading Singer:");
			singer = in.readLine();

			// get list of items based on customer description
			if (category.isEmpty() && title.isEmpty() && singer.isEmpty()) {
				rs = stmt.executeQuery("SELECT * FROM Item MINUS SELECT * FROM Item WHERE item_stock = 0");
			} else if (title.isEmpty() && singer.isEmpty()) {
				rs = stmt.executeQuery("SELECT * FROM Item WHERE item_category = '" + category + "' MINUS SELECT * FROM Item WHERE item_stock = 0");  
			} else if (category.isEmpty() && singer.isEmpty()) {
				rs = stmt.executeQuery("SELECT * FROM Item WHERE title = '" + title + "' MINUS SELECT * FROM Item WHERE item_stock = 0");                
			} else if (category.isEmpty() && title.isEmpty()) {
				rs = stmt.executeQuery("SELECT * FROM Item I, LeadSinger L WHERE I.item_upc = L.item_upc AND singer_name = '" + singer + "' MINUS SELECT * FROM Item WHERE item_stock = 0");             
			} else if (category.isEmpty()) {
				rs = stmt.executeQuery("SELECT * FROM Item I, LeadSinger L WHERE I.item_upc = L.item_upc AND singer_name = '" + singer + "' AND item_title = '" + title + "' MINUS SELECT * FROM Item WHERE item_stock = 0");                  
			} else if (title.isEmpty()) {
				rs = stmt.executeQuery("SELECT * FROM Item I, LeadSinger L WHERE I.item_upc = L.item_upc AND singer_name = '" + singer + "' AND item_category = '" + category + "' MINUS SELECT * FROM Item WHERE item_stock = 0");                
			} else if (singer.isEmpty()) {
				rs = stmt.executeQuery("SELECT * FROM Item WHERE item_category = '" + category + "' AND item_title = '" + title + "' MINUS SELECT * FROM Item WHERE item_stock = 0");              
			}

			// go through list asking customer if this is the one they want
			do {
				if (!rs.next()) {
					outOfItems = true;
					break;
				}
				upc = rs.getInt("item_upc");
				System.out.println("Did you mean " + upc + ", " + rs.getString("item_title") + "?");

			} while ("N".equals(in.readLine()));

			if (outOfItems) {
				System.out.println("No more items matching the description. Want to start over?");
				outOfItems = false;
				continue;
			}


			// for item selected, insert tuple into PurchaseItem
			System.out.println("How many do you want?");
			qty = Integer.parseInt(in.readLine());

			// check how many are in stock and if not enough, ask customer if they want to settle for the available

			rs = stmt.executeQuery("SELECT item_stock FROM Item WHERE item_upc = " + upc);
			rs.next();

			availableQty = rs.getInt("item_stock");

			if (availableQty < qty) {
				System.out.println("We don't have that many. Is " + availableQty + " okay?"); 
				if ("Y".equals(in.readLine())) {
					qty = availableQty;
				}
			}

			try {
				stmt.executeUpdate("INSERT INTO PurchaseItem VALUES (" + receiptno + ", " + upc + ", " + qty + ")");
			} catch (SQLException e) {
				System.out.println("You already put this item in your cart previously. We will update your cart with the new quantity.");
				stmt.executeUpdate("UPDATE PurchaseItem SET purchaseItem_quantity = " + qty + " WHERE item_upc = " + upc + " AND purchase_receiptid = " + receiptno);
			}

			System.out.println("Do you want to buy more stuff?");
		} while ("Y".equals(in.readLine()));


		// print bill for customer
		rs = stmt.executeQuery("SELECT I.item_upc, purchaseItem_quantity, item_price FROM Item I, PurchaseItem R WHERE I.item_upc = R.item_upc AND R.purchase_receiptId = " + receiptno);

		while (rs.next()) {
			System.out.printf("%-10.10s", rs.getString("item_upc"));
			System.out.printf("%-10.10s", rs.getString("purchaseItem_quantity"));
			System.out.printf("%-10.10s", rs.getString("item_price"));
			total += (rs.getDouble("item_price")*rs.getInt("purchaseItem_quantity"));
			System.out.println("");
		}
		System.out.println("TOTAL:       " + Math.floor(total * 100) / 100);

		// get payment information
		System.out.println("Credit Card Number Please:");
		creditno = Integer.parseInt(in.readLine());
		System.out.println("Credit Card Expiry Date:");
		javaDate = format.parse(in.readLine());
		date = new java.sql.Date(javaDate.getTime());

		// finalize purchase by inserting the rest of the info into the tuple
		con.setAutoCommit(false);
		ps = con.prepareStatement("UPDATE Purchase SET purchase_date = ?, custorder_card# = ?, custorder_expiryDate = ?, purchase_expectedDate = ?, purchase_deliveredDate = ? WHERE purchase_receiptid = " + receiptno);
		currDate = new java.sql.Date( (new java.util.Date()).getTime());
		ps.setDate(1, currDate);  // sets current date
		ps.setInt(2, creditno);
		ps.setDate(3, date);

		// determine expected date of delivery
		rs = stmt.executeQuery("SELECT purchase_expectedDate, purchase_deliveredDate FROM Purchase");
		while (rs.next()) {
			rs.getDate("purchase_expectedDate");
			if (!rs.wasNull()) {
				rs.getDate("purchase_deliveredDate");
				if (rs.wasNull()) {
					outstandingOrders++;
				}
			}
		}
		daysUntilDelivery = outstandingOrders/ORDERS_A_DAY;
		ps.setDate(4, addDays(currDate, daysUntilDelivery));
		ps.setDate(5, null);

		ps.executeUpdate();
		con.commit();
		ps.close();
		con.setAutoCommit(true);

		System.out.println("Thank you for shopping at AMS. Your order should arrive in about " + daysUntilDelivery + " days.");

		// update store stock
		rs = stmt.executeQuery("SELECT item_upc, purchaseItem_quantity FROM PurchaseItem WHERE purchase_receiptid = " + receiptno);

		while (rs.next()) {         
			int q = rs.getInt("purchaseItem_quantity");
			int u = rs.getInt("item_upc");
			stmtB.executeQuery("UPDATE Item SET item_stock = item_stock - " + q + " WHERE item_upc = " + u);            
		}

	}

	//--------------------------DAILY REPORT------------------------------------------------------------------------    

	public void dailyReport() throws ParseException, IOException, SQLException {
		int units = 0;
		double value = 0;
		double totalVal = 0;
		int totalUn = 0;
		String prevCat;      

		System.out.println("Please provide a date.");
		String date = in.readLine();

		ResultSet rs;

		Statement stmt = con.createStatement();

		stmt.executeQuery("CREATE VIEW QtyBought (upc, purchaseitem_quantity) AS " 
				+ "(SELECT item_upc, SUM(purchaseitem_quantity) "
				+ "FROM PurchaseItem R, Purchase P "
				+ "WHERE P.purchase_receiptid = R.purchase_receiptId AND P.purchase_date = to_date('" + date + "', 'yyyy-MM-dd')" 
				+ " GROUP BY item_upc)");

		rs = stmt.executeQuery("SELECT I.item_upc, item_category, item_price, Q.purchaseitem_quantity, item_price*purchaseitem_quantity as value "
				+ "FROM Item I, QtyBought Q "
				+ "WHERE I.item_upc = Q.upc "
				+ "GROUP BY item_category, I.item_upc, item_price, Q.purchaseitem_quantity");


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

		rs.next();
		prevCat = rs.getString("item_category");
		do {
			if (!rs.getString("item_category").equals(prevCat)) {
				System.out.println("");
				System.out.printf("%-10.10s", "total");
				System.out.printf("%-10.10s", units);
				System.out.printf("%-10.10s", value);  
				System.out.println();
				totalUn += units;
				totalVal += value;
				units = 0;
				value = 0;

			} 
			System.out.printf("%-10.10s", rs.getString("item_upc"));
			System.out.printf("%-10.10s", rs.getString("item_category"));
			prevCat = rs.getString("item_category");
			System.out.printf("%-10.10s", rs.getString("item_price"));
			units += rs.getInt("purchaseitem_quantity");
			System.out.printf("%-10.10s", rs.getString("purchaseitem_quantity"));
			value += rs.getDouble("value");
			System.out.printf("%-10.10s", rs.getString("value"));
			System.out.println();

		} while (rs.next());
		// have to repeat one more time for the last category (perhaps there is an easier way but isLast doesn't work in oracle, so I couldn't do that)
		System.out.println("");
		System.out.printf("%-10.10s", "total");
		System.out.printf("%-10.10s", units);
		System.out.printf("%-10.10s", value);  
		System.out.println();
		totalUn += units;
		totalVal += value;
		units = 0;
		value = 0;
		System.out.printf("%-20.10s", "total daily");
		System.out.printf("%-10.10s", totalUn);
		System.out.printf("%-10.10s", totalVal); 
		System.out.println("");

		stmt.executeQuery("DROP VIEW QtyBought");

	}

	public void addItems () throws IOException, SQLException {
		int iupc;
		Statement stmt = con.createStatement();
		ResultSet rs;

		System.out.println("Please input item UPC.");

		iupc = Integer.parseInt(in.readLine());

		rs = stmt.executeQuery("SELECT item_upc FROM Item WHERE item_upc = " + iupc);
		rs.next();

		if (rs.getString(1) != null) {
			System.out.print("Is ");
			rs = stmt.executeQuery("SELECT item_title FROM Item WHERE item_upc = " + iupc);
			rs.next();
			System.out.print(rs.getString(1));
			System.out.println(" the item you meant? (Y/N)");
			if ("N".equals(in.readLine())) {
				addItems();
			} else {
				Item item = new Item(con);
				item.update(in, iupc);
			}

		} else {
			System.out.println("This item does not exist in the database. Do you want to add it? (Y/N)");
			if ("N".equals(in.readLine())) {
				addItems();
			} else {
				Table item = new Item(con);
				item.insert();
			}
		}		
	}

	
    public void processDelivery() throws NumberFormatException, IOException, SQLException {
        int receiptid;
        System.out.println("Please input receiptid of order.");
        receiptid = Integer.parseInt(in.readLine());
        Statement stmt = con.createStatement();
        java.sql.Date currDate =  new java.sql.Date( (new java.util.Date()).getTime());
        
        stmt.executeUpdate("UPDATE Purchase SET purchase_deliveredDate = " + currDate + " WHERE purchase_receiptid = " + receiptid);
    }


}
