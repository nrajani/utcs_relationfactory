package run;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import entity_expansion.DateMatcher;
/**
 * Date normalization, to TIMEX2, to be run after offset finding.
 * @author Benjamin Roth
 *
 */
public class DateNormalizer {
  public static void main(String[] args) throws IOException {
    if (args.length != 2) {
      // TODO: for 2013 format responses:
      // System.out.println("DateNormalizer <infile> <outfile> <TAC_year>");      
      System.out.println("DateNormalizer <infile> <outfile>");
      return;
    }
    String inFile = args[0];
    String outFile = args[1];
    
    BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
    BufferedReader br = new BufferedReader(new FileReader(inFile));

    for (String line; (line = br.readLine()) != null;) {
      //SF_ENG_001      per:title       lsv     XIN_ENG_20070422.0195.LDC2009T13        Representative  0       0       0       0       1.0
      String[] lineParts = line.split("\t", 6);
      // TODO: if (TAC_year = "2013") {...} ?
      boolean isNIL = lineParts[3].equals("NIL");
      if (isNIL || !lineParts[1].contains("date")) {
        bw.append(line);
      } else {
        String fill = lineParts[4];
        String canonicalFill = DateMatcher.normalize_TIMEX2(fill);
        if (canonicalFill.equals("XXXX-XX-XX")) {
          canonicalFill = fill;
        }
        bw.append(lineParts[0] + "\t" + lineParts[1] + "\t" + lineParts[2] + 
            "\t" + lineParts[3] + "\t" + canonicalFill + "\t" + lineParts[5]);
      }
      bw.newLine();
    }
    br.close();
    bw.close();
  }
}
