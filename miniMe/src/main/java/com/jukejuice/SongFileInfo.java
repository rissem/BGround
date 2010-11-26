package com.jukejuice;

import java.io.File;
import java.io.IOException;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

public class SongFileInfo {
	public AudioHeader header;
	public AudioFile f;
	public Tag tag;
	
	public SongFileInfo(){}
	
	public SongFileInfo(String filename) 
		throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException
	{
		f = AudioFileIO.read(new File(filename));
		header = f.getAudioHeader();
		tag = f.getTag();
	}
}
