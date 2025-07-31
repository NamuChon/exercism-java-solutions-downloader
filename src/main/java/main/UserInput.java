package main;

import java.util.*;

public class UserInput {

    public String userName;

    public UserInput() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Exercism username: ");
        userName = scan.nextLine();
    }
}
