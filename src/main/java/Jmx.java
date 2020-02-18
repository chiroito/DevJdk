import com.sun.tools.attach.VirtualMachine;
import jdk.management.jfr.FlightRecorderMXBean;
import jdk.management.jfr.RecordingInfo;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.lang.management.ManagementFactory;
import java.util.List;

/**
 * -Djdk.attach.allowAttachSelf=true
 * -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9000 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false
 */
public class Jmx {
    public static void main(String[] args) throws Exception {

        FlightRecorderMXBean bean = getFlighteRecorderMXBean();

        List<RecordingInfo> preCreateRecordings = bean.getRecordings();
        long recId = bean.newRecording();
        bean.closeRecording(recId);

        long selfPID = ManagementFactory.getRuntimeMXBean().getPid();
        FlightRecorderMXBean attachBean = getFlighteRecorderMXBean(selfPID);
        long attachRecId = attachBean.newRecording();
        List<RecordingInfo> attachRecordings = attachBean.getRecordings();
        System.out.println(attachRecordings);
        attachBean.closeRecording(attachRecId);

        String portString = System.getProperty("com.sun.management.jmxremote.port");
        String jmxURL = String.format("service:jmx:rmi:///jndi/rmi://localhost:%s/jmxrmi", portString);
        FlightRecorderMXBean remoteBean = getFlighteRecorderMXBean(jmxURL);
        long remoteRecId = remoteBean.newRecording();
        List<RecordingInfo> recordings = remoteBean.getRecordings();
        System.out.println(recordings);
        remoteBean.closeRecording(remoteRecId);

    }

    public static FlightRecorderMXBean getFlighteRecorderMXBean() {
        return ManagementFactory.getPlatformMXBean(FlightRecorderMXBean.class);
    }


    public static FlightRecorderMXBean getFlighteRecorderMXBean(String jmxServiceUrl) throws Exception {
        JMXConnector jmxConnector = JMXConnectorFactory.connect(new JMXServiceURL(jmxServiceUrl));
        MBeanServerConnection mBeanServerConnection = jmxConnector.getMBeanServerConnection();

        ObjectName objectName = new ObjectName("jdk.management.jfr:type=FlightRecorder");
        FlightRecorderMXBean flightRecorder = JMX.newMXBeanProxy(mBeanServerConnection, objectName, FlightRecorderMXBean.class);

        return flightRecorder;
    }

    private static final String LOCAL_CONNECTION_ADDRESS = "com.sun.management.jmxremote.localConnectorAddress";
    public static FlightRecorderMXBean getFlighteRecorderMXBean(long pid) throws Exception {

        VirtualMachine targetVM = VirtualMachine.attach("" + pid);
        String jmxServiceUrl = targetVM.getAgentProperties().getProperty(LOCAL_CONNECTION_ADDRESS);
        System.out.println(jmxServiceUrl);
        JMXServiceURL jmxURL = new JMXServiceURL(jmxServiceUrl);
        JMXConnector connector = JMXConnectorFactory.connect(jmxURL);
        MBeanServerConnection connection = connector.getMBeanServerConnection();

        ObjectName objectName = new ObjectName("jdk.management.jfr:type=FlightRecorder");
        FlightRecorderMXBean flightRecorder = JMX.newMXBeanProxy(connection, objectName, FlightRecorderMXBean.class);

        return flightRecorder;
    }

}
