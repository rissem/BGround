package com.jukejuice;

import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InitDbServlet extends HttpServlet {
	private static final long serialVersionUID = 7262331411229610139L;
	
	@Override
	public void doGet (HttpServletRequest request, HttpServletResponse response)
	{
		DbPopulator dbPopulator = new DbPopulator();
		try {
			dbPopulator.init();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		Db db = new Db();
		try {
			db.createSongTable();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}