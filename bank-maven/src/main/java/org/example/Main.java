package org.example;
import org.example.bank.*;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {

        ControllerREST controller = new ControllerREST();
        controller.startAPI();

    }
}