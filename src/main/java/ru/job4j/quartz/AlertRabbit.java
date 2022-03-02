package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {

    public static void main(String[] args) {

        try {
            AlertRabbit ar = new AlertRabbit();
            try (Connection connect = ar.init()) {
                Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
                scheduler.start();
                JobDataMap data = new JobDataMap();
                data.put("connect", connect);
                JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
                SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(takeSeconds())
                    .repeatForever();
                Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
                scheduler.scheduleJob(job, trigger);
                Thread.sleep(10000);
                scheduler.shutdown();
            }

        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    public Connection init() {
        Connection connect = null;
        try {
            takeSettings();
            Class.forName(takeSettings().getProperty("driver-class-name"));
            connect = DriverManager.getConnection(
                    takeSettings().getProperty("url"),
                    takeSettings().getProperty("username"),
                    takeSettings().getProperty("password")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connect;
    }

    private static Properties takeSettings() {
        Properties config = null;
        try (InputStream in = AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            config = new Properties();
            config.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return config;
    }

    private static int takeSeconds() {
        if (takeSettings().getProperty("rabbit.interval") == null) {
            throw new IllegalArgumentException("Exception: argument is null!");
        }
        return Integer.parseInt(takeSettings().getProperty("rabbit.interval"));
    }

    public static class Rabbit implements Job {

        public Rabbit() {
            System.out.println("Rabbit dance here ...");
        }

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            Connection connect = (Connection) context.getJobDetail().getJobDataMap().get("connect");
            try (PreparedStatement statement =
                         connect.prepareStatement("insert into rabbit(created_date) values (?)")) {
                statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}