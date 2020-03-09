public class JpsCommandLine {
    public static void main(String[] args) {
        System.out.println(test("", mainClass("")));
        System.out.println(test("test-jar-with-dependencies.jar", mainClass("target\\test-jar-with-dependencies.jar")));
        System.out.println(test("Launcher", mainClass("org.jetbrains.jps.cmdline.Launcher C:/Users/c_hir/AppData/Local/JetBrains/Toolbox/apps/IDEA-U/ch-0/192.6817.14/lib/jps-model.jar;C:/Users/c_hir/AppData/Local/JetBrains/Toolbox/apps/IDEA-U/ch-0/192.6817.14/lib/protobuf-java-3.5.1.jar;C:/Users/c_hir/AppData/Local/JetBrains/Toolbox/apps/IDEA-U/ch-0/192.6817.14/lib/platform-api.jar;C:/Users/c_hir/AppData/Local/JetBrains/Toolbox/apps/IDEA-U/ch-0/192.6817.14/lib/log4j.jar;C:/Users/c_hir/AppData/Local/JetBrains/Toolbox/apps/IDEA-U/ch-0/192.6817.14/lib/httpclient-4.5.9.jar;C:/Users/c_hir/AppData/Local/JetBrains/Toolbox/apps/IDEA-U/ch-0/192.6817.14/lib/slf4j-api-1.7.25.jar;C:/Users/c_hir/AppData/Local/JetBrains/Toolbox/apps/IDEA-U/ch-0/192.6817.14/lib/commons-logging-1.2.jar;C:/Users/c_hir/AppData/Local/JetBrains/Toolbox/apps/IDEA-U/ch-0/192.6817.14/plugins/java/lib/plexus-interpolation-1.21.jar;C:/Users/c_hir/AppData/Local/JetBrains/Toolbox/apps/IDEA-U/ch-0/192.6817.14/lib/nanoxml-2.2.3.jar;C:/Users/c_hir/AppData/Local/JetBrains/Toolbox/apps/IDEA-U/ch-0/192.6817.14/p")));
        System.out.println(test("test-jar-with-dependencies.jar", mainClass("C:\\Program Files\\test\\test-jar-with-dependencies.jar")));
        System.out.println(test("test-jar-with-dependencies.jar", mainClass("Program Files\\test\\test-jar-with-dependencies.jar")));
        System.out.println(test("test-jar-with-dependencies.jar", mainClass("test.d\\test-jar-with-dependencies.jar")));
        System.out.println(test("test-jar-with-dependencies.jar", mainClass("Program Files\\test.d\\test-jar-with-dependencies.jar")));
    }

    private static boolean test(String expect, String actual) {
        return expect.equals(actual);
    }

    private static String mainClass(String cmdLine) {
        int firstSpace = cmdLine.indexOf(32);
        if (firstSpace > 0) {

            cmdLine = cmdLine.substring(0, firstSpace);
        }
        int lastSlash = cmdLine.lastIndexOf("/");
        int lastBackslash = cmdLine.lastIndexOf("\\");
        int lastSeparator = lastSlash > lastBackslash ? lastSlash : lastBackslash;
        if (lastSeparator > 0) {
            cmdLine = cmdLine.substring(lastSeparator + 1);
        }

        int lastPackageSeparator = cmdLine.lastIndexOf(46);
        if (lastPackageSeparator > 0) {
            String lastPart = cmdLine.substring(lastPackageSeparator + 1);
            return lastPart.equals("jar") ? cmdLine : lastPart;
        } else {
            return cmdLine;
        }
    }
}
