package com.jukejuice;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

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
		} catch (CannotReadException e) {
			log.error("", e);
		} catch (IOException e) {
			log.error("", e);
		} catch (TagException e) {
			log.error("", e);
		} catch (ReadOnlyFileException e) {
			log.error("", e);
		} catch (InvalidAudioFrameException e) {
			log.error("", e);
		}
		header = f.getAudioHeader();
		tag = f.getTag();
		this.filename = filename;
	}
}
