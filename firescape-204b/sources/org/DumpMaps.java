/**
 * rsc 16-02-2016
 */

package org;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class DumpMaps {

  static byte[] mapPack;
  static byte[] memberMapPack;
  static byte[] landscapePack;
  static byte[] memberLandscapePack;

  public static void main(String[] args) {
    mapPack = readDataFile(Paths.get("maps", "archive", "maps63.jag"));
    memberMapPack = readDataFile(Paths.get("maps", "archive", "maps63.mem"));
    landscapePack = readDataFile(Paths.get("maps", "archive", "land63.jag"));
    memberLandscapePack = readDataFile(Paths.get("maps", "archive", "land63.mem"));

    for (int plane = 0; plane < 4; plane++) {
      for (int x = 0; x < 100; x++) {
        for (int y = 0; y < 100; y++) {
          dump(plane, x, y);
        }
      }
    }
  }

  static byte[] readDataFile(Path path) {
    int archiveSize = 0;
    int archiveSizeCompressed = 0;
    byte archiveData[] = null;
    try {
      java.io.InputStream inputstream = Files.newInputStream(path);
      DataInputStream datainputstream = new DataInputStream(inputstream);
      byte header[] = new byte[6];
      datainputstream.readFully(header, 0, 6);
      archiveSize = ((header[0] & 0xff) << 16) + ((header[1] & 0xff) << 8) + (header[2] & 0xff);
      archiveSizeCompressed = ((header[3] & 0xff) << 16) + ((header[4] & 0xff) << 8) + (header[5] & 0xff);
      int read = 0;
      archiveData = new byte[archiveSizeCompressed];
      while (read < archiveSizeCompressed) {
        int length = archiveSizeCompressed - read;
        if (length > 1000) {
          length = 1000;
        }
        datainputstream.readFully(archiveData, read, length);
        read += length;
      }
      datainputstream.close();
    } catch (IOException ignored) {
    }
    if (archiveSizeCompressed != archiveSize) {
      byte decompressed[] = new byte[archiveSize];
      BZLib.decompress(decompressed, archiveSize, archiveData, archiveSizeCompressed, 0);
      return decompressed;
    } else {
      return archiveData;
    }
  }

  static void dump(int plane, int x, int y) {
    byte[] tileDirection = new byte[2304];
    byte[] wallsNorthsouth = new byte[2304];
    byte[] wallsRoof = new byte[2304];
    byte[] terrainHeight = new byte[2304];
    byte[] terrainColour = new byte[2304];
    byte[] tileDecoration = new byte[2304];
    int[] wallsDiagonal = new int[2304];
    byte[] wallsEastwest = new byte[2304];
    String mapname = "m" + plane + x / 10 + x % 10 + y / 10 + y % 10;
    try {
      byte mapData[] = unpackData(mapname + ".hei", 0, landscapePack, null);
      if (mapData == null && memberLandscapePack != null) {
        mapData = unpackData(mapname + ".hei", 0, memberLandscapePack, null);
      }
      if (mapData != null && mapData.length > 0) {
        int off = 0;
        int lastVal = 0;
        for (int tile = 0; tile < 2304; ) {
          int val = mapData[off++] & 0xff;
          if (val < 128) {
            terrainHeight[tile++] = (byte) val;
            lastVal = val;
          }
          if (val >= 128) {
            for (int i = 0; i < val - 128; i++) {
              terrainHeight[tile++] = (byte) lastVal;
            }

          }
        }

        lastVal = 64;
        for (int tileY = 0; tileY < 48; tileY++) {
          for (int tileX = 0; tileX < 48; tileX++) {
            lastVal = terrainHeight[tileX * 48 + tileY] + lastVal & 0x7f;
            terrainHeight[tileX * 48 + tileY] = (byte) (lastVal * 2);
          }

        }

        lastVal = 0;
        for (int tile = 0; tile < 2304; ) {
          int val = mapData[off++] & 0xff;
          if (val < 128) {
            terrainColour[tile++] = (byte) val;
            lastVal = val;
          }
          if (val >= 128) {
            for (int i = 0; i < val - 128; i++) {
              terrainColour[tile++] = (byte) lastVal;
            }

          }
        }

        lastVal = 35;
        for (int tileY = 0; tileY < 48; tileY++) {
          for (int tileX = 0; tileX < 48; tileX++) {
            lastVal = terrainColour[tileX * 48 + tileY] + lastVal & 0x7f;// ??? wat
            terrainColour[tileX * 48 + tileY] = (byte) (lastVal * 2);
          }

        }
      } else {
        for (int tile = 0; tile < 2304; tile++) {
          terrainHeight[tile] = 0;
          terrainColour[tile] = 0;
        }
      }
      mapData = unpackData(mapname + ".dat", 0, mapPack, null);
      if (mapData == null && memberMapPack != null) {
        mapData = unpackData(mapname + ".dat", 0, memberMapPack, null);
      }
      if (mapData == null || mapData.length == 0) {
        throw new IOException();
      }
      int off = 0;
      for (int tile = 0; tile < 2304; tile++) {
        wallsNorthsouth[tile] = mapData[off++];
      }

      for (int tile = 0; tile < 2304; tile++) {
        wallsEastwest[tile] = mapData[off++];
      }

      for (int tile = 0; tile < 2304; tile++) {
        wallsDiagonal[tile] = mapData[off++] & 0xff;
      }

      for (int tile = 0; tile < 2304; tile++) {
        int val = mapData[off++] & 0xff;
        if (val > 0) {
          wallsDiagonal[tile] = val + 12000;// why??
        }
      }

      for (int tile = 0; tile < 2304; ) {
        int val = mapData[off++] & 0xff;
        if (val < 128) {
          wallsRoof[tile++] = (byte) val;
        } else {
          for (int i = 0; i < val - 128; i++) {
            wallsRoof[tile++] = 0;
          }

        }
      }

      int lastVal = 0;
      for (int tile = 0; tile < 2304; ) {
        int val = mapData[off++] & 0xff;
        if (val < 128) {
          tileDecoration[tile++] = (byte) val;
          lastVal = val;
        } else {
          for (int i = 0; i < val - 128; i++) {
            tileDecoration[tile++] = (byte) lastVal;
          }

        }
      }

      for (int tile = 0; tile < 2304; ) {
        int val = mapData[off++] & 0xff;
        if (val < 128) {
          tileDirection[tile++] = (byte) val;
        } else {
          for (int i = 0; i < val - 128; i++) {
            tileDirection[tile++] = 0;
          }

        }
      }

      mapData = unpackData(mapname + ".loc", 0, mapPack, null);
      if (mapData != null && mapData.length > 0) {
        off = 0;
        for (int tile = 0; tile < 2304; ) {
          int val = mapData[off++] & 0xff;
          if (val < 128) {
            wallsDiagonal[tile++] = val + 48000;
          } else {
            tile += val - 128;
          }
        }

      }

      ByteArrayOutputStream baos = new ByteArrayOutputStream(20736);
      DataOutputStream out = new DataOutputStream(baos);

      for (int tile = 0; tile < 2304; tile++) {
                /* here's how it's read:
                int val = 0;
                for (int tile = 0; tile < 2304; tile++) {
                    val = val + mapData[off++] & 0xff;
                    terrainHeight[chunk][tile] = (byte) val;
                }
                 */
        out.writeByte(terrainHeight[tile]);
      }
      for (int tile = 0; tile < 2304; tile++) {
                /* here's how it's read:
                int val = 0;
                for (int tile = 0; tile < 2304; tile++) {
                    val = val + mapData[off++] & 0xff;
                    terrainColour[chunk][tile] = (byte) val;
                }
                 */
        out.writeByte(terrainColour[tile]);
      }
      for (int tile = 0; tile < 2304; tile++) {
        out.writeByte(wallsNorthsouth[tile]);
      }
      for (int tile = 0; tile < 2304; tile++) {
        out.writeByte(wallsEastwest[tile]);
      }
      for (int tile = 0; tile < 2304; tile++) {
        out.writeShort(wallsDiagonal[tile]);
      }
      for (int tile = 0; tile < 2304; tile++) {
        out.writeByte(wallsRoof[tile]);
      }
      for (int tile = 0; tile < 2304; tile++) {
        out.writeByte(tileDecoration[tile]);
      }
      for (int tile = 0; tile < 2304; tile++) {
        out.writeByte(tileDirection[tile]);
      }

      out.flush();
      Files.write(Paths.get("maps", "files", mapname + ".jm"), baos.toByteArray(), StandardOpenOption.CREATE_NEW);

      System.out.println(mapname + ".jm written");
    } catch (IOException ioe) {
    }
  }

  static byte[] unpackData(String filename, int i, byte archiveData[], byte fileData[]) {
    int numEntries = (archiveData[0] & 0xff) * 256 + (archiveData[1] & 0xff);
    int wantedHash = 0;
    filename = filename.toUpperCase();
    for (int l = 0; l < filename.length(); l++) {
      wantedHash = (wantedHash * 61 + filename.charAt(l)) - 32;
    }

    int offset = 2 + numEntries * 10;
    for (int entry = 0; entry < numEntries; entry++) {
      int fileHash = (archiveData[entry * 10 + 2] & 0xff) * 0x1000000 +
                     (archiveData[entry * 10 + 3] & 0xff) * 0x10000 +
                     (archiveData[entry * 10 + 4] & 0xff) * 256 +
                     (archiveData[entry * 10 + 5] & 0xff);
      int fileSize = (archiveData[entry * 10 + 6] & 0xff) * 0x10000 +
                     (archiveData[entry * 10 + 7] & 0xff) * 256 +
                     (archiveData[entry * 10 + 8] & 0xff);
      int fileSizeCompressed = (archiveData[entry * 10 + 9] & 0xff) * 0x10000 +
                               (archiveData[entry * 10 + 10] & 0xff) * 256 +
                               (archiveData[entry * 10 + 11] & 0xff);
      if (fileHash == wantedHash) {
        if (fileData == null) {
          fileData = new byte[fileSize + i];
        }
        if (fileSize != fileSizeCompressed) {
          BZLib.decompress(fileData, fileSize, archiveData, fileSizeCompressed, offset);
        } else {
          for (int j = 0; j < fileSize; j++) {
            fileData[j] = archiveData[offset + j];
          }

        }
        return fileData;
      }
      offset += fileSizeCompressed;
    }

    return null;
  }
}
