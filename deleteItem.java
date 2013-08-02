/*
 * deletes a branch
 */
private void deleteItem()
{
	int                iupc;
	PreparedStatement  ps;

	try
	{
		ps = con.prepareStatement("DELETE FROM Item WHERE item_upc = ?");

		System.out.print("\nItem UPC: ");
		iupc = Integer.parseInt(in.readLine());
		ps.setInt(1, iupc);

		int rowCount = ps.executeUpdate();

		if (rowCount == 0)
		{
			System.out.println("\nItem " + iupc + " does not exist!");
		}

		con.commit();

		ps.close();
	}
	catch (IOException e)
	{
		System.out.println("IOException!");
	}
	catch (SQLException ex)
	{
		System.out.println("Message: " + ex.getMessage());

		try
		{
			con.rollback();
		}
		catch (SQLException ex2)
		{
			System.out.println("Message: " + ex2.getMessage());
			System.exit(-1);
		}
	}
}
