import jdk.jfr.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.regex.Pattern;

public class SettingControlSample {

    public static void main(String[] args) throws IOException {
        Recording r = new Recording();
        r.enable(HTTPRequest.class).with("uriFilter", "https://www.example.com/list/.*");
        r.start();

        HTTPRequest e1 = new HTTPRequest();
        e1.uri = "https://www.example.com/list/xxx";
        e1.begin();
        e1.commit();

        HTTPRequest e2 = new HTTPRequest();
        e2.uri = "https://www.example.com/post/";
        e2.begin();
        e2.commit();

        r.dump(Path.of("settingControl.jfr"));
        r.close();
    }

    final static class RegExpControl extends SettingControl {
        private Pattern pattern = Pattern.compile(".*");

        @Override
        public void setValue(String value) {
            this.pattern = Pattern.compile(value);
        }

        @Override
        public String combine(Set<String> values) {
            return String.join("|", values);
        }

        @Override
        public String getValue() {
            return pattern.toString();
        }

        public boolean matches(String s) {
            return pattern.matcher(s).find();
        }
    }

    final static class HTTPRequest extends Event {
        @Label("Request URI")
        protected String uri;

        @Label("Servlet URI Filter")
        @SettingDefinition
        protected boolean uriFilter(RegExpControl regExp) {
            return regExp.matches(uri);
        }
    }
}