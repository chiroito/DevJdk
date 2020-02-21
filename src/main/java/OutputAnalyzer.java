import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OutputAnalyzer {

    public static boolean shouldContain(String str, String notExpectedString) {
        return str.contains(notExpectedString);
    }

    public static boolean shouldMatch(String str, String regexp) {
        Pattern pattern = Pattern.compile(regexp, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    public static boolean shouldNotContain(String str, String notExpectedString) {
        return !shouldContain(str, notExpectedString);
    }

    public static boolean shouldNotMatch(String str, String regexp) {
        return !shouldMatch(str, regexp);
    }

}