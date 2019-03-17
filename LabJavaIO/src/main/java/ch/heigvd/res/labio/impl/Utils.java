package ch.heigvd.res.labio.impl;

import java.awt.*;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.ArrayList;

/**
 *
 * @author Olivier Liechti
 */
public class Utils {

  private static final Logger LOG = Logger.getLogger(Utils.class.getName());

  /**
   * This method looks for the next new line separators (\r, \n, \r\n) to extract
   * the next line in the string passed in arguments. 
   * 
   * @param lines a string that may contain 0, 1 or more lines
   * @return an array with 2 elements; the first element is the next line with
   * the line separator, the second element is the remaining text. If the argument does not
   * contain any line separator, then the first element is an empty string.
   */
  public static String[] getNextLine(String lines) {

    ArrayList<String> tableString = new ArrayList<String>();
    String tmp = "";

    for(int i = 0; i < lines.length(); ++i){
      tmp += lines.charAt(i);
      // if there is a new line
      if(lines.charAt(i) == '\r' || lines.charAt(i) == '\n'){
          // if the new line is in the end of string
          if(i + 1 != lines.length() && lines.charAt(i + 1) == '\n'){
              tmp += lines.charAt(++i);
          }
          tableString.add(tmp);
          tmp = "";
      }
    }

    if(tableString.size() == 0){
        tableString.add("");
        tableString.add(tmp);
    } else if(tableString.size() == 1){
      tableString.add("");
    }

    // used for the return
    String[] stringArray = tableString.toArray(new String[tableString.size()]);

    return stringArray;
  }

}
