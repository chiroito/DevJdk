public class TempDirSample {

    public static void main(String[] args) {
        System.out.println(assertEqual(removeSeparator("tmp", "\\"), "tmp"));
        System.out.println(assertEqual(removeSeparator("tmp\\", "\\"), "tmp"));
        System.out.println(assertEqual(removeSeparator("tmp/", "/"), "tmp"));
    }

    private static String removeSeparator(String path, String separator){
        if(path.endsWith(separator)){
            return path.substring(0, path.length()-separator.length());
        }
        return path;
    }

    private static boolean assertEqual(String expected, String actual){
        return expected.equals(actual);
    }
}
