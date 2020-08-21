import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GetNetworkAddr {

    public static void main(String[] args) throws Exception{

        System.out.println(isContain("0.0.0.0", "0.0.0.1", 8));
        System.out.println(!isContain("0.0.0.0", "128.0.0.0", 8));
        System.out.println(!isContain("128.0.0.0", "127.255.255.255", 8));

        System.out.println(isContain("128.0.0.0", "128.0.0.1", 16));
        System.out.println(!isContain("128.0.0.0", "128.1.0.1", 16));
        System.out.println(!isContain("128.1.0.0", "128.0.255.255", 16));

        System.out.println(isContain("192.168.0.0", "192.168.0.1", 24));
        System.out.println(!isContain("192.168.0.0", "192.168.1.1", 24));
        System.out.println(!isContain("192.168.1.0", "192.168.0.255", 24));

        findIpAddr("192.168.1.0/24").stream().forEach(System.out::println);
    }

    public static List<InetAddress> findIpAddr(String networkAddrWithSubnet) {

        final String[] splittedNetworkAddr = networkAddrWithSubnet.split("/");
        final String networkAddr = splittedNetworkAddr[0];
        final String subnet = splittedNetworkAddr[1];
        final int subnetLength = Integer.parseInt(subnet);

        try {
            List<InetAddress> collect = NetworkInterface.networkInterfaces()
                    .flatMap(nic -> nic.getInterfaceAddresses().stream())
                    .map(intrfc -> intrfc.getAddress())
                    .filter(addr -> isIpv4(addr))
                    .filter(addr -> isContain(networkAddr, addr.getHostAddress(), subnetLength))
                    .collect(Collectors.toList());

            return collect;
        }catch(SocketException e){
            System.out.println("No Network Interface is found");
            throw new RuntimeException(e);
        }
    }

    private static boolean isIpv4(InetAddress addr){
        return addr.getAddress().length ==4;
    }

    public static boolean isContain(String networkAddrStr, String ipStr, int subnetLength)  {

        byte[] networkAddr = calcNetworkAddr(networkAddrStr, subnetLength);
        byte[] calcNetworkAddr = calcNetworkAddr(ipStr, subnetLength);

        return Arrays.compare(networkAddr, calcNetworkAddr) == 0;
    }

    private static byte[] calcNetworkAddr(String ip, int subnetLength)  {

        byte[] subnetMask = ByteBuffer.allocate(4).putInt(0xFFFFFFFF << (32-subnetLength)).array();

        byte[] addr = new byte[4];
        int i = 0;
        for(String octet : ip.split("[.]")){
            addr[i] = (byte)(Integer.parseInt(octet) & subnetMask[i]);
            i++;
        }

        return addr;
    }
}
