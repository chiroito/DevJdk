import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * jdk.internal.vm.VMSupport#serializePropertiesToByteArray を修正するサンプルコード
 * -Dnormal=normal_val -D"space space=blank blank" -Dnonascii=あいうえお -Durl=http://openjdk.java.net/ -DwinDir="C:\"
 */
public class UnEscapeProperty {

    public static void main(String[] args) throws Exception {
        System.out.println("System.lineSeparator().getBytes().length = " + System.lineSeparator().getBytes().length);
        System.out.println("\"\\\\\".getBytes().length" + "\\".getBytes().length);

        System.out.println(new String(serializePropertiesToByteArray(System.getProperties())));
        System.out.println("");
        System.out.println("****************************************");
        System.out.println("");
        System.out.println(new String(serializePropertiesToByteArray2(System.getProperties())));
        System.out.println("");
        System.out.println("+++++++++++++++++++++++++++++++++++++++++");
        System.out.println("");
        System.out.println(new String(serializePropertiesToByteArray3(System.getProperties())));
        System.out.println("");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("");
        String result = new String(serializePropertiesToByteArray4(System.getProperties()));
        System.out.println(result);
        System.out.println("");

        String[] shouldNotContains = new String[]{"https\\://", "C\\:\\\\"};
        for (String shouldNotContainWord : shouldNotContains) {
            System.out.println(String.format("%s %b", shouldNotContainWord, OutputAnalyzer.shouldNotContain(result, shouldNotContainWord)));
        }

        String[] shouldContains = new String[]{"https://", "C:\\", "\\u3042\\u3044\\u3046\\u3048\\u304A", "C:\\", "blank blank", "normal_val", "\\n"};
        for (String shouldContainWord : shouldContains) {
            System.out.println(String.format("%s %b", shouldContainWord, OutputAnalyzer.shouldContain(result, shouldContainWord)));
        }
    }

    /*
    現在の実装
     */
    private static byte[] serializePropertiesToByteArray(Properties p) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(4096);

        Properties props = new Properties();

        // stringPropertyNames() returns a snapshot of the property keys
        Set<String> keyset = p.stringPropertyNames();
        for (String key : keyset) {
            String value = p.getProperty(key);
            props.put(key, value);
        }

        props.store(out, null);
        return out.toByteArray();
    }


    /*
    この実装だと改行コードとなる\r\nなど他のエスケープにも影響がある。
     */
    private static byte[] serializePropertiesToByteArray2(Properties p) throws IOException {
        byte[] output = null;

        try (
                ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
                OutputStreamWriter osw = new OutputStreamWriter(out);
                BufferedWriter bw = new BufferedWriter(osw);) {

            Set<String> keyset = p.stringPropertyNames();
            for (String key : keyset) {
                String value = p.getProperty(key);
                bw.write(key + "=" + value + System.lineSeparator());
            }

            bw.flush();
            output = out.toByteArray();
        }
        return output;
    }

    private static byte ESCAPE = '\\';

    private static byte[] serializePropertiesToByteArray3(Properties p) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(4096);

        Properties props = new Properties();

        // stringPropertyNames() returns a snapshot of the property keys
        Set<String> keyset = p.stringPropertyNames();
        for (String key : keyset) {
            String value = p.getProperty(key);
            props.put(key, value);
        }

        props.store(out, null);

        ByteArrayOutputStream unEscapedOut = new ByteArrayOutputStream(out.size());
        boolean isEscaping = false;

        for (byte character : out.toByteArray()) {
            if (isEscaping) {
                if (character != '\\' && character != ':') {
                    unEscapedOut.write(ESCAPE);
                }
                unEscapedOut.write(character);
                isEscaping = false;
            } else if (character == ESCAPE) {
                isEscaping = true;
            } else {
                unEscapedOut.write(character);
            }
        }

        return unEscapedOut.toByteArray();
    }

    private static byte[] serializePropertiesToByteArray4(Properties p) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out, "8859_1"));

        bw.write("#" + new Date().toString());
        bw.newLine();

        for (String key : p.stringPropertyNames()) {
            String val = p.getProperty(key);
            key = saveConvert(key);
            val = saveConvert(val);
            bw.write(key + "=" + val);
            bw.newLine();
        }
        bw.flush();

        return out.toByteArray();
    }

    private static String saveConvert(String theString) {
        int len = theString.length();
        StringBuilder outBuffer = new StringBuilder(len * 2);

        String replacedString = theString.replace("\t", "\\t").replace("\n", "\\n").replace("\r", "\\r").replace("\f", "\\f");

        String collect = replacedString.codePoints().mapToObj(cp -> {
            if ((cp < 0x20) || (cp > 0x7e)) {
                return String.format("\\u%04X", cp);
            } else {
                return Character.toString(cp);
            }
        }).collect(Collectors.joining());

        return collect;
    }
}
