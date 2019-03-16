package ch.heigvd.res.labio.impl.filters;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 * This class transforms the streams of character sent to the decorated writer.
 * When filter encounters a line separator, it sends it to the decorated writer.
 * It then sends the line number and a tab character, before resuming the write
 * process.
 *
 * Hello\n\World -> 1\Hello\n2\tWorld
 *
 * @author Olivier Liechti
 */
public class FileNumberingFilterWriter extends FilterWriter {
  private int number = 0;
  private boolean first = true;
  private static final Logger LOG = Logger.getLogger(FileNumberingFilterWriter.class.getName());

  public FileNumberingFilterWriter(Writer out) {
    super(out);
  }

  @Override
  public void write(String str, int off, int len) throws IOException {
    String value = "";
    str = str.substring(off, off + len);
    String[] test = str.split(" ");

    for(String a: test){
      if(number == 0 && a.charAt(a.length()-1) != '\n' && a.charAt(a.length()-1) != '\r') {
        value = ++number + "\t" + a + " ";
      } else if(number == 0 && (a.charAt(a.length()-1) == '\n' || a.charAt(a.length()-1) == '\r')) {
        value = ++number + "\t" + a + ++number + "\t";
      } else if(a.charAt(a.length()-1) == '\n' || a.charAt(a.length()-1) == '\r') {
        value += a + ++number + "\t";
      } else if(a.contains("\n")){
        value += a.substring(0, a.indexOf("\n")) + "\n" + ++number + "\t" + a.substring(a.indexOf("\n") + 1, a.length()) + " ";
      } else if (a.contains("\r")){
        value += a.substring(0, a.indexOf("\r")) + "\r" + ++number + "\t" + a.substring(a.indexOf("\r") + 1, a.length()) + " ";
      } else {
        value += a + " ";
      }
    }

    if(value.charAt(value.length() - 1) == ' '){
      value = value.substring(0, value.length() - 1);
    }

    super.write(value, 0, value.length());
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    String value = "";

    for(int i = off; i < len; ++i){
      if(number == 0){
        value = ++number + "\t" + cbuf[i];
      } else if (cbuf[i] == '\n'){
        value += "\n" + ++number + "\t";
      } else if (cbuf[i] == '\r'){
        value += "\r" + ++number + "\t";
      } else {
        value += cbuf[i];
      }
    }

    super.write(value, 0, value.length());

  }

  @Override
  public void write(int c) throws IOException {
    String value = "";

    if(number == 0){
      value = ++number + "\t" + (char)c;
    } else if((char)c == '\n'){
      value = "\n" + ++ number + "\t";
    } else {
      value = Character.toString((char)c);
    }

    super.write(value, 0, value.length());
  }

}
