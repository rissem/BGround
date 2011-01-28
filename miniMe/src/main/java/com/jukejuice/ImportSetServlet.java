package com.jukejuice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

public class ImportSetServlet 
	extends HttpServlet
{
	private static final Logger log = Logger.getLogger(ImportSetServlet.class);
	private static final long serialVersionUID = 5785824172360567085L;

	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	{
		if (! req.getLocalAddr().equals(req.getRemoteAddr()))
			return;
		resp.setContentType("text/html");
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		String setName = null;
		List<String> filenames = new ArrayList<String>();
		try {
			List<FileItem> items = upload.parseRequest(req);
			//there should only be one file
			for (FileItem item: items)
			{
				filenames = getFilesFromITunesPlaylist(item.getInputStream());
				if (item.isFormField() && "setName".equals(item.getFieldName()))
				{
					Reader r = new InputStreamReader(item.getInputStream(), "UTF-8");
					BufferedReader bufferedReader = new BufferedReader(r);
					setName = bufferedReader.readLine();
				}
			}
		} catch (FileUploadException e1) {
			log.error("", e1);
		} catch (IOException e) {
			log.error("", e);
		}
		Db db = new Db(); 
				
		//does a set by this name already exist?
		SongSet songSet = db.findSetByName(setName);
		if (songSet == null)
		{
			db.createSet(setName);
			songSet = db.findSetByName(setName);
		}
		db.emptySet(songSet);
		db.addFilenamesToSet(filenames, songSet.getId());
	}
	
	public static List<String> getFilesFromITunesPlaylist(InputStream inputStream) throws IOException
	{
		Reader r = new InputStreamReader(inputStream, "UTF-16");
		Writer w = new OutputStreamWriter(new FileOutputStream("/tmp/tempITunesPlaylist.txt"), "UTF-8");
		char[] buffer = new char[2048];
		int read;
		while ((read = r.read(buffer)) != -1) {
			w.write(buffer, 0, read);
		}
		r.close();
		w.flush();
		w.close();
		File file = new File("/tmp/tempITunesPlaylist.txt");
		
		List<String> filenames = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] fields = line.split("\\t");
				// now convert from apple's weird colon delimited file paths
				// to a normal file path
				String applePath = "";
				try {
					applePath = fields[26];
				} catch (Exception e) {
					System.out.println("Exception reading line " + line);
				}

				StringBuffer path = new StringBuffer();
				String[] pathSegments = applePath.split(":");
				//skip the first meaningless Macintosh HD field
				for (int i = 1; i < pathSegments.length; i ++) {
					path.append("/" + pathSegments[i]);
				}
				if (! "".equals(path))
					filenames.add(new String(path.toString().getBytes("utf-8"), "utf-8"));
			}
			return filenames;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return new ArrayList<String>();
		} catch (IOException e) {
			e.printStackTrace();
			return new ArrayList<String>();
		}		
	}	
}
