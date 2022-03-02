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

    private static Properties config;

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
                    .withIntervalInSeconds(
                            Integer.parseInt(
                                    config.getProperty("rabbit.interval")))
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
            Class.forName(config.getProperty("driver-class-name"));
            connect = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connect;
    }

    private static void takeSettings() {
        try (InputStream in = AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            config = new Properties();
            config.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
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