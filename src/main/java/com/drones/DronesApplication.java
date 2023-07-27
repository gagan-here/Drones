package com.drones;

import com.drones.service.DroneBatteryCheckTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DronesApplication {

    public static DroneBatteryCheckTask droneBatteryCheckTask;

    @Autowired
    public DronesApplication(DroneBatteryCheckTask droneBatteryCheckTask) {
        DronesApplication.droneBatteryCheckTask = droneBatteryCheckTask;
    }

    public static void main(String[] args) {
        SpringApplication.run(DronesApplication.class, args);


        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

        // Schedules a task to periodically check the battery level of a drone using a given executor service.
        executorService.scheduleAtFixedRate(droneBatteryCheckTask, 0, 1, TimeUnit.SECONDS);
    }

}
