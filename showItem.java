void checkNull(ResultSet rs, String attribute){
	if (rs.wasNull())
		System.out.printf("%-20.20s", " ");

	else
		System.out.printf("%-20.20s", attribute);

}

private void showItem()
{
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
