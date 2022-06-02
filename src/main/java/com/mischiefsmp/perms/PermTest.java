package com.mischiefsmp.perms;

import com.mischiefsmp.perms.permission.MischiefPermission;

import java.util.Scanner;

public class PermTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while(running) {
            System.out.print("Enter Permission to compare: ");
            MischiefPermission permission = new MischiefPermission(scanner.nextLine());
            while(true) {
                System.out.print("Permission/n/q: ");
                String input = scanner.nextLine();

                if(input.equals("n"))
                    break;
                else if(input.equals("q")) {
                    running = false;
                    break;
                }

                MischiefPermission p = new MischiefPermission(input);
                System.out.printf("%s == %s: %s\n", permission, p, permission.equals(p));
            }
        }
    }
}
