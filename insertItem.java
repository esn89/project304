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

		// UPC
		System.out.print("\nItem UPC: ");
		iupc = Integer.parseInt(in.readLine());
		ps.setInt(1, iupc);

		// Title
		System.out.print("\nItem Title: ");
		ititle = in.readLine();
		if (ititle.length() == 0) {
			ps.setString(2, null);
		} else {
			ps.setString(2, ititle);
		}

		// Type
		System.out.print("\nItem Type: ");
		itype = in.readLine();
		if (itype.length() == 0) {
			ps.setString(3, null);
		} else {
			ps.setString(3, itype);
		}


		// Category
		System.out.print("\nItem Category: ");
		icategory = in.readLine();
		if (icategory.length == 0)
			ps.setString(4, null);
		else
			ps.setString(4, icategory);

		// Company
		System.out.print("\nItem Company: ");
		tempCompany = in.readLine();
		if (tempCompany.length() == 0)
			ps.setString(5, null);
		else
			ps.setString(5, icompany);

		// Year
		System.out.print("\nItem Year: ");
		ps.setInt(6, iyear);
		String tempYear = in.readLine();
		if (tempYear.length() == 0) {
			ps.setNull(6, java.sql.Types.INTEGER);
		} else {
			iyear = Integer.parseInt(tempYear);
			ps.setInt(6, iyear);
		}

		// Price
		System.out.print("\nItem Price: ");
		String tempPrice = in.readLine();
		if (tempPrice.length() == 0){
			ps.setNull(7, java.sql.Types.DOUBLE);
		} else {
			iprice = Double.parseDouble(tempPrice);
			ps.setDouble(7, iprice);
		}

		// Stock
		System.out.print("\nItem Stock: ");
		String tempStock = in.readLine();
		if (tempStock.length() == 0) {
			ps.setNull(8, java.sql.Types.BOOLEAN);
		} else {
			istock = Boolean.parseBoolean(tempStock);
			ps.setBoolean(8, istock);
		}
