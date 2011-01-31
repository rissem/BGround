package com.jukejuice;

import java.io.File;

import org.apache.log4j.Logger;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.Tag;

public class SongFileInfo {
	private static final Logger log = Logger.getLogger(SongFileInfo.class);
	
	public AudioHeader header;
	public AudioFile f;
	public Tag tag;
	public String filename;
	
	public SongFileInfo(){}
	
	public SongFileInfo(String filename) 
	{
		try {
			f = AudioFileIO.read(new File(filename));
		} catch (Exception e) {
			log.error("", e);
		}
		header = f.getAudioHeader();
		tag = f.getTag();
		this.filename = filename;
	}
}
