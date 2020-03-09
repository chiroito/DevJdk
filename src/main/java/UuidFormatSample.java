import java.util.UUID;

public class UuidFormatSample {
    public static void main(String[] args) throws Exception {
        testFromStringError("123456789-1234-1234-1234-123456789012");
        testFromStringError("123456789-2-3-4-5");
        testFromStringError("1-12345-3-4-5");
        testFromStringError("1-2-12345-4-5");
        testFromStringError("1-2-3-12345-5");
        testFromStringError("1-2-3-4-1234567890123");
    }

    private static void testFromStringError(String str) {
        try {
            UUID test = fromString(str);
            throw new RuntimeException("Should have thrown IAE");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    private static UUID fromString(String name) {
        int len = name.length();
        if (len > 36) {
            throw new IllegalArgumentException("UUID string too large");
        } else {
            int dash1 = name.indexOf(45, 0);
            int dash2 = name.indexOf(45, dash1 + 1);
            int dash3 = name.indexOf(45, dash2 + 1);
            int dash4 = name.indexOf(45, dash3 + 1);
            int dash5 = name.indexOf(45, dash4 + 1);

            int len1 = dash1;
            int len2 = dash2 - dash1 - 1;
            int len3 = dash3 - dash2 - 1;
            int len4 = dash4 - dash3 - 1;
            int len5 = len - dash4 - 1;

            if (len1 > 8 || len2 > 4 || len3 > 4 || len4 > 4 || len5 > 12) {
                throw new IllegalArgumentException("Invalid UUID string: " + name);
            }

            if (dash4 >= 0 && dash5 < 0) {
                long mostSigBits = Long.parseLong(name, 0, dash1, 16) & 4294967295L;
                mostSigBits <<= 16;
                mostSigBits |= Long.parseLong(name, dash1 + 1, dash2, 16) & 65535L;
                mostSigBits <<= 16;
                mostSigBits |= Long.parseLong(name, dash2 + 1, dash3, 16) & 65535L;
                long leastSigBits = Long.parseLong(name, dash3 + 1, dash4, 16) & 65535L;
                leastSigBits <<= 48;
                leastSigBits |= Long.parseLong(name, dash4 + 1, len, 16) & 281474976710655L;
                return new UUID(mostSigBits, leastSigBits);
            } else {
                throw new IllegalArgumentException("Invalid UUID string: " + name);
            }
        }
    }

}
