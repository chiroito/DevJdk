import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertyReadCodePoint {

    public static void main(String[] args) throws Exception{
        System.out.println(getSystemProperties());
    }

    public static Properties getSystemProperties() throws IOException {
        InputStream in = null;
        Properties props = new Properties();
        try {
            in = executeCommand("properties");
            props.load(in);
        } finally {
            if (in != null) in.close();
        }
        return props;
    }

    private static InputStream executeCommand(String arg) throws IOException{
        InputStream fis = Files.newInputStream(Paths.get("src/main/resources/PropertyReadCodePoint.properties"));

        return fis;
    }

}
