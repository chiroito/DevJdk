
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * jdk.internal.vm.VMSupport#serializePropertiesToByteArray を修正するサンプルコード
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
        System.out.println(new String(serializePropertiesToByteArray4(System.getProperties())));
        System.out.println("");
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
        int bufLen = len * 2;
        if (bufLen < 0) {
            bufLen = Integer.MAX_VALUE;
        }
        StringBuilder outBuffer = new StringBuilder(bufLen);

        String replacedString = theString.replaceAll("\t", "\\\\t").replaceAll("\n", "\\\\n").replaceAll("\r", "\\\\r").replaceAll("\f", "\\\\f");

        for (int x = 0; x < replacedString.length(); x++) {
            char aChar = replacedString.charAt(x);

            if (((aChar < 0x0020) || (aChar > 0x007e))) {
                outBuffer.append('\\');
                outBuffer.append('u');
                outBuffer.append(toHex((aChar >> 12) & 0xF));
                outBuffer.append(toHex((aChar >> 8) & 0xF));
                outBuffer.append(toHex((aChar >> 4) & 0xF));
                outBuffer.append(toHex(aChar & 0xF));
            } else {
                outBuffer.append(aChar);
            }
        }

        return outBuffer.toString();
    }

    private static final char[] hexDigit = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    private static char toHex(int nibble) {
        return hexDigit[(nibble & 0xF)];
    }
}
