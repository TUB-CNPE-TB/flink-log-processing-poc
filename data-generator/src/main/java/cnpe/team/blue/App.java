package cnpe.team.blue;

import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App {

    public static void main( String[] args ) throws UnknownHostException {
        int requestPerSecond = 100;
        String brokerAddress = args[0];
        String brokerPort = args[1];
        String brokerList = brokerAddress + ":" + brokerPort;

        ScheduledExecutorService exec = Executors.newScheduledThreadPool(4);
        exec.scheduleAtFixedRate(new Worker(requestPerSecond, brokerList), 0, 1, TimeUnit.SECONDS);
    }
}
