package hello;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.function.Function;

public class Converter {
    Function<Integer, Integer> f= (Integer a) -> 1+a;
    Function<Integer, Integer> f2 = (Integer a) ->  f.apply(1)+a;
    public static void main(String[] args) {
      String idTelematique = String.format("0%1s", "ss");
        System.out.println("idT: " + idTelematique);
        Charset charsetEBCDIC = Charset.forName("CP037");
        Charset charsetACSII = Charset.forName("US-ASCII");


        String ebcdic = "|";
        System.out.println("String EBCDIC: " + ebcdic);
        System.out.println("String converted to ASCII: " + convertTO(ebcdic, charsetEBCDIC, charsetACSII));

        String ascII = "Jill \\n Doe";
        System.out.println("String ASCII: " + ascII);
        System.out.println("String converted to EBCDIC: " + convertTO(ascII, charsetACSII, charsetEBCDIC));
        System.out.println("String ASCII: " + ascII);
        System.out.println("java8" +new Converter().f2.apply(1));


        try {
            System.out.println(new String("0A".getBytes("utf-8"), "CP1047"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

// string with hexadecimal EBCDIC codes
        String sb = "|";
        int countOfHexValues = sb.length() / 2;
        byte[] bytes = new byte[countOfHexValues];
        for(int i = 0; i < countOfHexValues; i++) {
            int hexValueIndex = i * 2;
// take one hexadecimal string value
            String hexValue = sb.substring(hexValueIndex, hexValueIndex + 2);
// convert it to a byte
            bytes[i] = (byte) (Integer.parseInt(hexValue, 16) & 0xFF);
        }
// constructs a String by decoding bytes as EBCDIC
        try {
        String string = new String(bytes, "CP1047");
            System.out.println("string "+ string+ "string");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static String convertTO(String dados, Charset encondingFrom, Charset encondingTo) {
        return new String(dados.getBytes(encondingFrom), encondingTo);
    }
}
