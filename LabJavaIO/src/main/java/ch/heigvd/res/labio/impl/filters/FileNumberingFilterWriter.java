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
  private boolean isValueR = false;
  private static final Logger LOG = Logger.getLogger(FileNumberingFilterWriter.class.getName());

  public FileNumberingFilterWriter(Writer out) {
    super(out);
  }

  @Override
  public void write(String str, int off, int len) throws IOException {
    str = str.substring(off, off + len);

    // call function write to check caracter
    for(int i = 0; i < str.length(); ++i){
      this.write((int)str.charAt(i));
    }
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    // call function write to check caracter
    for(int i = off; i < off + len; ++i){
      this.write((int)cbuf[i]);
    }
  }

  @Override
  public void write(int c) throws IOException {
    String value = "";

    // for the first word
    if(number == 0){
      value = ++number + "\t" + (char)c;
    }
    // for all '\n'
    else if((char)c == '\n'){
      // if it's windows
      if(isValueR){
        value = "\r\n";
      }
      // if it's linux
      else {
        value = "\n";
      }
      value += ++number + "\t";
      isValueR = false;
    }
    // if it's MacOs
    else if((char)c == '\r'){
      isValueR = true;
    } else {
      // if the character before was \r
      if(isValueR){
        value = "\r" + ++number + "\t" + (char)c;
        isValueR = false;
      } else{
        value = Character.toString((char)c);
      }
    }
    super.write(value, 0, value.length());
  }

}
