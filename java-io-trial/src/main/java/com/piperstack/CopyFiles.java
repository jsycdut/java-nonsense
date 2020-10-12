package com.piperstack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

public class CopyFiles {
  void stream(File source, File dest) throws IOException {
    try (FileInputStream fis = new FileInputStream(source); FileOutputStream fos = new FileOutputStream(dest);) {
      byte[] buffer = new byte[1024];
      int offset = 0;
      while ((offset = fis.read(buffer)) != -1) {
        fos.write(buffer, 0, offset);
      }
    }
  }

  void channel(File source, File dest) throws IOException {
    try (RandomAccessFile rafForSource = new RandomAccessFile(source, "rw");
        RandomAccessFile rafForDest = new RandomAccessFile(dest, "rw");
        FileChannel input = rafForSource.getChannel();
        FileChannel output = rafForDest.getChannel();) {
      output.transferFrom(input, 0, input.size());
    }
  }

  void filesCopy(File source, File dest) throws IOException {
    Files.copy(source.toPath(), dest.toPath());
  }
}
