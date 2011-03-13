package com.jukejuice;

import java.io.File;
import java.util.List;

import org.junit.Test;

public class RemoteDbPopulatorTest {

	
	@Test
	public void testSync()
	{
		RemoteDbPopulator dbPopulator = new RemoteDbPopulator();
		List<File> files = dbPopulator.sync();
		System.out.println(files.size());
	}
}
