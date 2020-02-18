import java.time.Duration;

public class DurationSample {

    private static final Duration MICROSECOND = Duration.ofNanos(1000);
    private static final Duration MILLSECOND = Duration.ofMillis(1);
    private static final Duration SECOND = Duration.ofSeconds(1);
    private static final Duration MINUTE = Duration.ofMinutes(1);
    private static final Duration HOUR = Duration.ofHours(1);
    private static final Duration DAY = Duration.ofDays(1);

    public static void main(String[] args) {
//        Duration d = Duration.ofDays(1).plusHours(2).plusMinutes(34).plusSeconds(56).plusNanos(123456789);
//        Duration d = Duration.ofHours(2).plusMinutes(34).plusSeconds(56).plusNanos(123456789);
//        Duration d = Duration.ofMinutes(34).plusSeconds(56).plusNanos(123456789);
//        Duration d = Duration.ofSeconds(56).plusNanos(123456789);
        Duration d = Duration.ofNanos(123456789);

        StringBuilder durationStr = new StringBuilder();
        if(d.compareTo(DAY) > 0){
            durationStr.append(String.format("%d d ", d.toDaysPart()));
        }
        if(d.compareTo(HOUR) > 0){
            durationStr.append(String.format("%d h ", d.toHoursPart()));
        }
        if(d.compareTo(MINUTE) > 0){
            durationStr.append(String.format("%d m ", d.toMinutesPart()));
        }
        if(d.compareTo(SECOND) > 0){
            durationStr.append(String.format("%d s ", d.toSecondsPart()));
        }
        if(d.compareTo(MILLSECOND) > 0){
            durationStr.append(String.format("%d ms ", d.toNanosPart() / 1_000_000));
        }
        if(d.compareTo(MICROSECOND) > 0){
            durationStr.append(String.format("%d us ", (d.toNanosPart() / 1_000) % 1_000));
        }
        durationStr.append(String.format("%d ns", d.toNanosPart() % 1_000));
        println(durationStr.toString());


    }

    private static void println(String str){
        System.out.println(str);
    }
}
