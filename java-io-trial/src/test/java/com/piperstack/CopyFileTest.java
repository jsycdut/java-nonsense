package com.piperstack;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class CopyFileTest {
  private File source;
  private File copyByStream;
  private File copyByFiles;
  private File copyByChannel;
  private CopyFiles copyFiles;

  @Before
  public void setup() {
    copyFiles = new CopyFiles();
    source = new File(this.getClass().getResource("/input-file.txt").getFile());
    copyByStream = new File(this.getClass().getResource("/copyByStream.txt").getFile());
    copyByChannel = new File(this.getClass().getResource("/copyByChannel.txt").getFile());
    copyByFiles = new File(this.getClass().getResource("/copyByFiles.txt").getFile());
  }

  @Test
  public void testStream() throws IOException {
    copyFiles.stream(source, copyByStream);
    assertTrue(copyByStream.length() > 0);
  }

  @Test
  public void testChannel() throws IOException {
    copyFiles.channel(source, copyByChannel);
  }

  @Test
  public void testCopy() throws IOException {
    copyByFiles.delete();
    copyFiles.filesCopy(source, copyByFiles);
  }
}
