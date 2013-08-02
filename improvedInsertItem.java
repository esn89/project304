private String askUser ( BufferedReader in, String message)
{
	System.out.println("\n" + message + ": ");
	return in.readLine();
}

private void setNull (PreparedStatement ps, String line, boolean key, int position, double attribute) {
	if (line.length() == 0 && !key) {
		ps.setNull(position, java.sql.Types.DOUBLE);
	} else {
		attribute = Double.parseDouble(line);
		ps.setDouble(position, attribute);
	}
}

private void setNull (PreparedStatement ps, String line, boolean key, int position, int attribute) {
	if (line.length() == 0 && !key) {
		ps.setNull(position, java.sql.Types.INTEGER);
	} else {
		attribute = Integer.parseInt(line);
		ps.setInt(position, attribute);
	}
}
private void setNull (PreparedStatement ps, String line, boolean key, int position, String attribute) {
	if (line.length() == 0 && !key) {
		ps.setString(position, null);
	} else {
		ps.setString(position, attribute);
	}
}

private void setNull (PreparedStatement ps, String line, boolean key, int position, boolean attribute) {
	if (line.length() == 0 && !key) {
		ps.setNull(position, java.sql.Types.BOOLEAN);
	} else {
		attribute = Boolean.parseBoolean(line);
		ps.setBoolean(position, attribute);
	}
}


// Inserts an item into the Item table
private void insertItem()
{
	int                iupc;     //1
	String             ititle;   //2
	String             itype;    //3
	String             icategory; //4
	String             icompany;  //5
	int                iyear;     //6
	double             iprice;    //7
	boolean            istock;    //8
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
