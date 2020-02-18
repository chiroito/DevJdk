import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class DurationFormat {

    public static void main(String[] args) throws Exception {
        Locale.setDefault(Locale.US);

        // Nanoseconds
        assertDuration("0 ns   ", "0 s");
        assertDuration("1 ns   ", "0.000001 ms");
        assertDuration("10 ns  ", "0.000010 ms");
        assertDuration("100 ns ", "0.000100 ms");
        assertDuration("999 ns ", "0.000999 ms");
        assertDuration("1000 ns", "0.00100 ms");
        assertDuration("1004 ns", "0.00100 ms");
        assertDuration("1005 ns", "0.00101 ms");

        // 10 us
        assertDuration("9 us 994 ns", "0.00999 ms");
        assertDuration("9 us 995 ns", "0.0100 ms");
        assertDuration("10 us      ", "0.0100 ms");
        assertDuration("10 us 49 ns", "0.0100 ms");
        assertDuration("10 us 50 ns", "0.0101 ms");

        // 100 us
        assertDuration("99 us 949 ns ", "0.0999 ms");
        assertDuration("99 us 950 ns ", "0.100 ms");
        assertDuration("100 us       ", "0.100 ms");
        assertDuration("100 us 499 ns", "0.100 ms");
        assertDuration("100 us 500 ns", "0.101 ms");

        // 1 ms
        assertDuration("999 us 499 ns       ", "0.999 ms");
        assertDuration("999 us 500 ns       ", "1.00 ms");
        assertDuration("1 ms                ", "1.00 ms");
        assertDuration("1 ms 4 us 999 ns    ", "1.00 ms");
        assertDuration("1 ms 5 us", "1.01 ms");

        // 10 ms
        assertDuration("9 ms 994 us 999 ns", "9.99 ms");
        assertDuration("9 ms 995 us       ", "10.0 ms");
        assertDuration("10 ms             ", "10.0 ms");
        assertDuration("10 ms 49 us 999 ns", "10.0 ms");
        assertDuration("10 ms 50 us 999 ns", "10.1 ms");

        // 100 ms
        assertDuration("99 ms 949 us 999 ns ", "99.9 ms");
        assertDuration("99 ms 950 us 000 ns ", "100 ms");
        assertDuration("100 ms              ", "100 ms");
        assertDuration("100 ms 499 us 999 ns", "100 ms");
        assertDuration("100 ms 500 us       ", "101 ms");

        // 1 second
        assertDuration("999 ms 499 us 999 ns  ", "999 ms");
        assertDuration("999 ms 500 us         ", "1.00 s");
        assertDuration("1 s                   ", "1.00 s");
        assertDuration("1 s 4 ms 999 us 999 ns", "1.00 s");
        assertDuration("1 s 5 ms              ", "1.01 s");

        // 10 seconds
        assertDuration("9 s 994 ms 999 us 999 ns ", "9.99 s");
        assertDuration("9 s 995 ms               ", "10.0 s");
        assertDuration("10 s                     ", "10.0 s");
        assertDuration("10 s 049 ms 999 us 999 ns", "10.0 s");
        assertDuration("10 s 050 ms              ", "10.1 s");

        // 1 minute
        assertDuration("59 s 949 ms 999 us 999 ns", "59.9 s");
        assertDuration("59 s 950 ms              ", "1 m 0 s");
        assertDuration("1 m 0 s                  ", "1 m 0 s");
        assertDuration("60 s 499 ms 999 us 999 ns", "1 m 0 s");
        assertDuration("60 s 500 ms              ", "1 m 1 s");

        // 10 minutes
        assertDuration("10 m 0 s", "10 m 0 s");

        // 1 hour
        assertDuration("59 m 59 s 499 ms 999 us 999 ns", "59 m 59 s");
        assertDuration("59 m 59 s 500 ms              ", "1 h 0 m");
        assertDuration("1 h 0 m                       ", "1 h 0 m");
        assertDuration("1 h 29 s 999 ms 999 us 999 ns ", "1 h 0 m");
        assertDuration("1 h 30 s                      ", "1 h 1 m");

        // 1 day
        assertDuration("23 h 59 m 29 s 999 ms 999 us 999 ns", "23 h 59 m");
        assertDuration("23 h 59 m 30 s                     ", "1 d 0 h");
        assertDuration("1 d 0 h                            ", "1 d 0 h");
        assertDuration("1 d 29 m 59 s 999 ms 999 us 999 ns ", "1 d 0 h");
        assertDuration("1 d 30 m                           ", "1 d 1 h");

        // 100 days
        assertDuration("100 d 13 h", "100 d 13 h");

        // 1000 days
        assertDuration("1000 d 13 h", "1000 d 13 h");


    }

    private static void assertDuration(String value, String expected) throws Exception {
        long nanos = parse(value);
        System.out.println(value + " == " + expected + " ? (" + nanos + " ns) ");

        String formatedString = Utils.formatDuration(Duration.ofNanos(nanos));

        if (formatedString.equals(expected)) {
            System.out.println("Success : " + formatedString);
        } else {
            System.err.println("Error : " + formatedString);
        }

        if (nanos != 0) {

            String nformatedString = Utils.formatDuration(Duration.ofNanos(-1*nanos));

            if (nformatedString.equals("-" + expected)) {
                System.out.println("Success : " + nformatedString);
            } else {
                System.err.println("Error : " + nformatedString);
            }
        }
    }

    private static long parse(String duration) throws Exception {
        String[] t = duration.trim().split(" ");
        long nanos = 0;
        for (int i = 0; i < t.length - 1; i += 2) {
            nanos += Long.parseLong(t[i]) * parseUnit(t[i + 1]);
        }
        return nanos;
    }

    private static long parseUnit(String unit) throws Exception {
        switch (unit) {
            case "ns":
                return 1L;
            case "us":
                return 1_000L;
            case "ms":
                return 1_000_000L;
            case "s":
                return 1_000_000_000L;
            case "m":
                return 60 * 1_000_000_000L;
            case "h":
                return 3600 * 1_000_000_000L;
            case "d":
                return 24 * 3600 * 1_000_000_000L;
        }
        throw new Exception("Test error. Unknown unit " + unit);
    }
}




class Utils {

    private static final  Duration MiCRO_SECOND = Duration.ofNanos(1_000);
    private static final  Duration SECOND = Duration.ofSeconds(1);
    private static final  Duration MINUTE = Duration.ofMinutes(1);
    private static final  Duration HOUR = Duration.ofHours(1);
    private static final  Duration DAY = Duration.ofDays(1);
    private static final int NANO_SIGNIFICANT_FIGURES = 9;
    private static final int MILL_SIGNIFICANT_FIGURES = 3;
    private static final int DISPLAY_NANO_DIGIT = 3;
    private static final int BASE = 10;

    public static String formatDuration(Duration d) {
        Duration roundedDuration = roundDuration(d);
        if (roundedDuration.equals(Duration.ZERO)) {
            return "0 s";
        } else if(roundedDuration.isNegative()){
            return "-" + formatPositiveDuration(roundedDuration.abs());
        } else {
            return formatPositiveDuration(roundedDuration);
        }
    }

    private static String formatPositiveDuration(Duration d){
        if (d.compareTo(MiCRO_SECOND) < 0) {
            double outputMs = (double) d.toNanosPart() / 1_000_000;
            return String.format("%.6f ms",  outputMs);
        } else if (d.compareTo(SECOND) < 0) {
            // 10.1 ms
            int valueLength = countLength(d.toNanosPart());
            int outputDigit = NANO_SIGNIFICANT_FIGURES - valueLength;
            double outputMs = (double) d.toNanosPart() / 1_000_000;
            return String.format("%." + outputDigit + "f ms",  outputMs);
        } else if (d.compareTo(MINUTE) < 0) {
            // 10.0 s
            int valueLength = countLength(d.toSecondsPart());
            int outputDigit = MILL_SIGNIFICANT_FIGURES - valueLength;
            double outputSecond = d.toSecondsPart() + (double) d.toMillisPart() / 1_000;
            return String.format("%." + outputDigit + "f s",  outputSecond);
        } else if (d.compareTo(HOUR) < 0) {
            //1 m 1 s
            return String.format("%d m %d s",  d.toMinutesPart(), d.toSecondsPart());
        } else if (d.compareTo(DAY) < 0) {
            // 1 h 0 m
            return String.format("%d h %d m",  d.toHoursPart(), d.toMinutesPart());
        } else {
            //"100 d 13 h"
            return String.format("%d d %d h",  d.toDaysPart(), d.toHoursPart());
        }
    }

    private static int countLength(long value){
        return (int) Math.log10(value) + 1;
    }

    private static Duration roundDuration(Duration d) {
        if (d.equals(Duration.ZERO)) {
            return d;
        } else if(d.isNegative()){
            Duration roundedPositiveDuration = roundPositiveDuration(d.abs());
            return roundedPositiveDuration.negated();
        } else {
            return roundPositiveDuration(d);
        }
    }

    private static Duration roundPositiveDuration(Duration d){
        if (d.compareTo(MiCRO_SECOND) < 0) {
            return d;
        } else if (d.compareTo(SECOND) < 0) {
            // 10.1 ms
            int valueLength = countLength(d.toNanosPart());
            int roundValue = (int) Math.pow(BASE, valueLength - DISPLAY_NANO_DIGIT);
            long roundedNanos = Math.round((double) d.toNanosPart() / roundValue) * roundValue;
            return d.truncatedTo(ChronoUnit.SECONDS).plusNanos(roundedNanos);
        } else if (d.compareTo(MINUTE) < 0) {
            // 10.0 s
            int valueLength = countLength(d.toSecondsPart());
            int roundValue = (int) Math.pow(BASE, valueLength);
            long roundedMills = Math.round((double) d.toMillisPart() / roundValue) * roundValue;
            return d.truncatedTo(ChronoUnit.SECONDS).plusMillis(roundedMills);
        } else if (d.compareTo(HOUR) < 0) {
            //1 m 1 s
            return d.plusMillis(SECOND.dividedBy(2).toMillisPart()).truncatedTo(ChronoUnit.SECONDS);
        } else if (d.compareTo(DAY) < 0) {
            // 1 h 0 m
            return d.plusSeconds(MINUTE.dividedBy(2).toSecondsPart()).truncatedTo(ChronoUnit.MINUTES);
        } else {
            //"100 d 13 h"
            return d.plusMinutes(HOUR.dividedBy(2).toMinutesPart()).truncatedTo(ChronoUnit.HOURS);
        }
    }
}
