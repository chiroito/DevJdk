import jdk.jfr.FlightRecorder;
import jdk.jfr.Recording;

import java.util.concurrent.TimeUnit;

public class Sleep {
    public static void main(String[] args) throws Exception{
        Recording r = new Recording();
        r.start();
        System.out.println("I sleep");
        TimeUnit.DAYS.sleep(1);
        r.stop();
    }
}
