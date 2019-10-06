package hello;

import java.nio.charset.Charset;
import java.util.function.Function;

public class Converter {
    Function<Integer, Integer> f= (Integer a) -> 1+a;
    Function<Integer, Integer> f2 = (Integer a) ->  f.apply(1)+a;
    public static void main(String[] args) {

        Charset charsetEBCDIC = Charset.forName("CP037");
        Charset charsetACSII = Charset.forName("US-ASCII");

        String ebcdic = "";
        System.out.println("String EBCDIC: " + ebcdic);
        System.out.println("String converted to ASCII: " + convertTO(ebcdic, charsetEBCDIC, charsetACSII));

        String ascII = "Jill,Doe";
        System.out.println("String ASCII: " + ascII);
        System.out.println("String converted to EBCDIC: " + convertTO(ascII, charsetACSII, charsetEBCDIC));

        System.out.println("java8" +new Converter().f2.apply(1));

    }

    public static String convertTO(String dados, Charset encondingFrom, Charset encondingTo) {
        return new String(dados.getBytes(encondingFrom), encondingTo);
    }
}
