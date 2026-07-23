/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.datatorrent.bufferserver.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>DiskStorage class.</p>
 *
 * @since 0.3.2
 */
public class DiskStorage implements Storage
{
  private static final Logger logger = LoggerFactory.getLogger(DiskStorage.class);
  final String basePath;
  int uniqueIdentifier;

  public DiskStorage(String baseDirectory)
  {
    basePath = baseDirectory;
    logger.info("Using {} as the basepath for spooling.", basePath);
  }

  public DiskStorage() throws IOException
  {
    Path tempDir = Files.createTempDirectory("msp");
    basePath = tempDir.toString();
    logger.info("Using {} as the basepath for spooling.", basePath);
  }

  @Override
  public Storage getInstance() throws IOException
  {
    return new DiskStorage(basePath);
  }

  public static String normalizeFileName(String name)
  {
    StringBuilder sb = new StringBuilder(1024);
    for (char c : name.toCharArray()) {
      if (Character.isLetterOrDigit(c)) {
        sb.append(c);
      } else {
        sb.append('-');
      }
    }
    return sb.toString();
  }

  @Override
  public int store(String identifier, byte[] bytes, int startingOffset, int endingOffset)
  {
    int lUniqueIdentifier;
    String normalizedFileName = normalizeFileName(identifier);
    File directory = new File(basePath, normalizedFileName);
    if (directory.exists()) {
      File identityFile = new File(directory, "identity");
      if (identityFile.isFile()) {
        try {
          byte[] stored = java.nio.file.Files.readAllBytes(identityFile.toPath());
          if (Arrays.equals(stored, identifier.getBytes())) {
            synchronized (this) {
              lUniqueIdentifier = ++this.uniqueIdentifier;
            }
          } else {
            throw new IllegalStateException("Collision in identifier name, please ensure that the slug for " +
                "the identifiers is different");
          }
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
      } else {
        throw new IllegalStateException("Identity file is hijacked!");
      }
    } else {
      if (directory.mkdirs()) {
        File identity = new File(directory, "identity");
        try {
          java.nio.file.Files.write(identity.toPath(), identifier.getBytes());
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
      } else {
        throw new RuntimeException("directory " + directory.getAbsolutePath() + " could not be created!");
      }
      lUniqueIdentifier = ++this.uniqueIdentifier;
    }

    try {
      return writeFile(bytes, startingOffset, endingOffset, directory, lUniqueIdentifier);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public void discard(String identifier, int uniqueIdentifier)
  {
    String normalizedFilename = normalizeFileName(identifier);
    File directory = new File(basePath, normalizedFilename);
    if (directory.exists()) {
      File identityFile = new File(directory, "identity");
      if (identityFile.isFile()) {
        try {
          byte[] stored = java.nio.file.Files.readAllBytes(identityFile.toPath());
          if (Arrays.equals(stored, identifier.getBytes())) {
            File deletionFile = new File(directory, String.valueOf(uniqueIdentifier));
            if (deletionFile.exists() && deletionFile.isFile()) {
              if (!deletionFile.delete()) {
                throw new RuntimeException("File " + deletionFile.getPath() + " could not be deleted!");
              }
            } else {
              throw new RuntimeException("File " + deletionFile.getPath() + " either is non existent or not a file!");
            }
          } else {
            throw new RuntimeException("Collision in the identifier name, please ensure that the slugs for " +
                "the identifiers are different");
          }
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
      } else {
        throw new RuntimeException(identityFile + " is not a file!");
      }
    } else {
      throw new RuntimeException("directory " + directory.getPath() + " does not exist!");
    }
  }

  @Override
  public byte[] retrieve(String identifier, int uniqueIdentifier)
  {
    String normalizedFilename = normalizeFileName(identifier);
    File directory = new File(basePath, normalizedFilename);
    if (directory.exists()) {
      File identityFile = new File(directory, "identity");
      if (identityFile.isFile()) {
        try {
          byte[] stored = java.nio.file.Files.readAllBytes(identityFile.toPath());
          if (Arrays.equals(stored, identifier.getBytes())) {
            File filename = new File(directory, String.valueOf(uniqueIdentifier));
            if (filename.exists() && filename.isFile()) {
              return java.nio.file.Files.readAllBytes(filename.toPath());
            } else {
              throw new RuntimeException("File " + filename.getPath() + " either is non existent or not a file!");
            }
          } else {
            throw new RuntimeException("Collision in the identifier name," +
                " please ensure that the slugs for the identifiers [" + identifier + "], and [" +  new String(stored) +
                "] are different.");
          }
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
      } else {
        throw new RuntimeException(identityFile + " is not a file!");
      }
    } else {
      throw new RuntimeException("directory " + directory.getPath() + " does not exist!");
    }
  }

  protected int writeFile(final byte[] bytes, final int startingOffset, final int endingOffset, final File directory,
      final int number) throws IOException
  {
    try (FileOutputStream stream = new FileOutputStream(new File(directory, String.valueOf(number)))) {
      stream.write(bytes, startingOffset, endingOffset - startingOffset);
    }
    return number;
  }
}