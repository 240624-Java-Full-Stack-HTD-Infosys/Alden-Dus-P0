package org.example;
import org.example.bank.*;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        ControllerREST controller = new ControllerREST();
        controller.startAPI();

    }
}