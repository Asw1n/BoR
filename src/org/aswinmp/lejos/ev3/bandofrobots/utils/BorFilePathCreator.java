package org.aswinmp.lejos.ev3.bandofrobots.utils;

import java.io.File;

/**
 * Utility class to create {@link File} objects. TODO find a better way to
 * inject MIDI files
 */
public class BorFilePathCreator {

  public static File assembleAbsoluteFileFromUserDir(final String subFolder, final String fileName) {
    final String fileSeparator = System.getProperty("file.separator");
    final String executionDir = System.getProperty("user.dir");
    return new File(String.format("%s%s%s%s%s", executionDir, fileSeparator, subFolder, fileSeparator, fileName));
  }

}
