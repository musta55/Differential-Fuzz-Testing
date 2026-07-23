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
package com.datatorrent.stram.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jettison.json.JSONException;

/**
 * <p>Abstract FSPartFileAgent class.</p>
 *
 * @since 0.9.0
 */
public abstract class FSPartFileAgent
{
  private final Map<String, String> lastIndexLines = new HashMap<>();
  protected final StramAgent stramAgent;

  protected abstract IndexLine parseIndexLine(String line) throws JSONException;

  public FSPartFileAgent(StramAgent stramAgent)
  {
    this.stramAgent = stramAgent;
  }

  public void setLastIndexLine(String basePath, String line)
  {
    lastIndexLines.put(basePath, line);
  }

  protected static class IndexLine
  {
    public boolean isEndLine = false;
    public String partFile;
  }

  protected class IndexFileBufferedReader extends BufferedReader
  {
    private final String basePath;
    private boolean lastLineReturned = false;
    private String lastPartFile;

    IndexFileBufferedReader(InputStreamReader reader, String basePath)
    {
      super(reader);
      this.basePath = basePath;
    }

    public IndexLine readIndexLine() throws IOException, JSONException
    {
      String line = readNextLine();
      if (line == null) {
        return null;
      }
      IndexLine il = parseIndexLine(line);
      updateLastPartFile(il);
      return il;
    }

    private String readNextLine() throws IOException
    {
      String line = super.readLine();
      if (line == null && !lastLineReturned) {
        line = lastIndexLines.get(basePath);
        if (line != null) {
          lastLineReturned = true;
        }
      }
      return line;
    }

    private void updateLastPartFile(IndexLine il)
    {
      if (il.partFile != null && !il.partFile.equals(lastPartFile)) {
        lastPartFile = il.partFile;
      } else {
        lastIndexLines.remove(basePath);
      }
    }
  }

  public static String getNextPartFile(String partFile)
  {
    if (partFile == null) {
      return "part0.txt";
    }
    if (!isValidPartFileFormat(partFile)) {
      return null;
    }
    return incrementPartFileNumber(partFile);
  }

  private static boolean isValidPartFileFormat(String partFile)
  {
    return partFile.startsWith("part") && partFile.endsWith(".txt");
  }

  private static String incrementPartFileNumber(String partFile)
  {
    int number = Integer.parseInt(partFile.substring(4, partFile.length() - 4));
    return "part" + (number + 1) + ".txt";
  }
}