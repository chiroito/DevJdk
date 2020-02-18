import jdk.jfr.FlightRecorder;
import jdk.jfr.Recording;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class RecordingSample {
    public static void main(String[] args) throws Exception{
        Path dumppath = Paths.get("dump.jfr");
        Recording r = new Recording();
        FlightRecorder.getFlightRecorder().getEventTypes().stream().forEach(e->r.enable(e.getName()));
        r.start();
        TimeUnit.SECONDS.sleep(1);
        r.dump(dumppath);
        r.close();
    }
}
