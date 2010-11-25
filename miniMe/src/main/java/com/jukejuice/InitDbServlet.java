package com.jukejuice;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

public class InitDbServlet extends HttpServlet {
	private static final long serialVersionUID = 7262331411229610139L;
	
	@Override
	public void doGet (HttpServletRequest request, HttpServletResponse response)
	{
		PrintWriter out = null;
		try {
			out = response.getWriter();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try
        {
	        Class.forName("org.sqlite.JDBC");
	        Connection conn =
	          DriverManager.getConnection("jdbc:sqlite:test.db");
	        Statement stat = conn.createStatement();
	        stat.executeUpdate("drop table if exists people;");
	        stat.executeUpdate("create table people (name, occupation);");
	        PreparedStatement prep = conn.prepareStatement(
	          "insert into people values (?, ?);");
	
	        prep.setString(1, "Gandhi");
	        prep.setString(2, "politics");
	        prep.addBatch();
	        prep.setString(1, "Turing");
	        prep.setString(2, "computers");
	        prep.addBatch();
	        prep.setString(1, "Wittgenstein");
	        prep.setString(2, "smartypants");
	        prep.addBatch();
	
	        conn.setAutoCommit(false);
	        prep.executeBatch();
	        conn.setAutoCommit(true);
	
	        ResultSet rs = stat.executeQuery("select * from people;");
	        while (rs.next()) {
	          out.println("name = " + rs.getString("name"));
	          out.println("job = " + rs.getString("occupation"));
	        }
	        rs.close();
	        conn.close();
        }
        catch (Exception e)
        {
        	out.println(e.getStackTrace());
        }
		
		AudioFile f = null;
		try {
			f = AudioFileIO.read(new File("/Users/mike/Fatboy Slim vs 2Pac - California Skank (DJ BootOX pres Blaze Music).mp3"));
		} catch (CannotReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ReadOnlyFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAudioFrameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (f != null)
		{
			AudioHeader header = f.getAudioHeader();
			int length = header.getTrackLength();
			out.println("length = " + length);
			Tag tag = f.getTag();
			out.println(tag.getFirst(FieldKey.ARTIST));
			out.println(tag.getFirst(FieldKey.ALBUM));
			out.println(tag.getFirst(FieldKey.TITLE));
			out.println(tag.getFirst(FieldKey.COMMENT));
			out.println(tag.getFirst(FieldKey.YEAR));
			out.println(tag.getFirst(FieldKey.TRACK));
			out.println(tag.getFirst(FieldKey.DISC_NO));
			out.println(tag.getFirst(FieldKey.COMPOSER));
		}

		Process process = null;
		try {
			process = Runtime.getRuntime().exec("find /Users/mike/Music -name *mp3");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			out.println(convertStreamToString(process.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			process = Runtime.getRuntime().exec("find /Users/mike/Music -name *m4a");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			out.println(convertStreamToString(process.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public static String convertStreamToString(InputStream is)
	            throws IOException {
	        /*
	         * To convert the InputStream to String we use the
	         * Reader.read(char[] buffer) method. We iterate until the
	         * Reader return -1 which means there's no more data to
	         * read. We use the StringWriter class to produce the string.
	         */
	        if (is != null) {
	            Writer writer = new StringWriter();
	 
	            char[] buffer = new char[1024];
	            try {
	                Reader reader = new BufferedReader(
	                        new InputStreamReader(is, "UTF-8"));
	                int n;
	                while ((n = reader.read(buffer)) != -1) {
	                    writer.write(buffer, 0, n);
	                }
	           } finally {
	                is.close();
	            }
	            return writer.toString();
	        } else {       
	            return "";
	        }
	    }
		
	}