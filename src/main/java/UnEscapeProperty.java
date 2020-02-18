
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * jdk.internal.vm.VMSupport#serializePropertiesToByteArray を修正するサンプルコード
 */
public class UnEscapeProperty {

    public static void main(String[] args) throws Exception{
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
        TimeUnit.HOURS.sleep(1);
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

        try(
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

        out.flush();

        byte[] escapedBytes = out.toByteArray();
        ByteArrayOutputStream unEscapedOut = new ByteArrayOutputStream(4096);
        boolean isEscaping = false;

        for(int escapedOffset = 0 ; escapedOffset < escapedBytes.length ; escapedOffset++){
            byte character = escapedBytes[escapedOffset];
            if(isEscaping){
                if (character != '\\' && character != ':') {
                    unEscapedOut.write(ESCAPE);
                }
                unEscapedOut.write(character);
                isEscaping = false;
            } else if(character == ESCAPE){
                isEscaping = true;
            } else {
                isEscaping = false;
                unEscapedOut.write(character);
            }
        }

        return unEscapedOut.toByteArray();
    }

}
