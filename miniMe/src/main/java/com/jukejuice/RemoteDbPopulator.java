package com.jukejuice;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.TagException;

public class RemoteDbPopulator {
	private String musicDirectory;
	private Logger log = Logger.getLogger(RemoteDbPopulator.class);

	/**
	 * find all of the mp3s and m4as in the musicDirectory, parse their ID3 tags and add them to the database
	 * @throws KeyNotFoundException 
	 * @throws UnsupportedEncodingException 
	 * @throws CannotReadException
	 * @throws IOException
	 * @throws TagException
	 * @throws ReadOnlyFileException
	 * @throws InvalidAudioFrameException
	 * @throws SQLException
	 */
	public void init () throws UnsupportedEncodingException, KeyNotFoundException
	{
		String[] mp3SongFilenames = new String[1];
		String[] m4aSongFilenames = new String[1];
		try {
			mp3SongFilenames = Util.exec("find " + getMusicDirectory() + " -name *mp3").split("\n");
			m4aSongFilenames = Util.exec("find " + getMusicDirectory() + " -name *m4a").split("\n");			
		} catch (IOException e) {
			log.error("", e);
		}
		
		String[] songFilenames = new String[mp3SongFilenames.length + m4aSongFilenames.length];
		System.arraycopy(mp3SongFilenames, 0, songFilenames, 0, mp3SongFilenames.length);
		System.arraycopy(m4aSongFilenames, 0, songFilenames, mp3SongFilenames.length, m4aSongFilenames.length);

		List<SongFileInfo> songInfos = new ArrayList<SongFileInfo>();
		for (String songFilename: songFilenames)
		{
			if (! "".equals(songFilename)) {
				try {
					SongFileInfo songFileInfo = new SongFileInfo(songFilename);
					StringBuffer url= new StringBuffer();
					url.append("http://localhost:8124/addSong");
					url.append("?artist=" + URLEncoder.encode(songFileInfo.tag.getFirst(FieldKey.ARTIST))); 				
					url.append("&title=" + URLEncoder.encode(songFileInfo.tag.getFirst(FieldKey.TITLE)));
					url.append("&album=" + URLEncoder.encode(songFileInfo.tag.getFirst(FieldKey.ALBUM)));
				}
				catch (Exception e)
				{
					log.error(e);
				}
				log.info("added song " + songFilename);

			}
		}
	}
	
	public String getMusicDirectory()
	{
		if (musicDirectory == null)
			musicDirectory = System.getProperty("user.home") + "/Music";			
		return musicDirectory;
	}
	
	public void setMusicDirectory(String musicDirectory)
	{
		this.musicDirectory = musicDirectory;
	}
}
