/*
 * Created by Patrick McQuery - 06/12/2023 - CS240 - Spring Quarter
 * 
 * A basic password manager, everything is stored in plain text so please do not put your real
 * information in. Uses a HashMap of LinkedLists to store account information, can handle multiple
 * accounts for the same website/app. You can add accounts, search for an account, edit accounts,
 * save your vault, and load your vault. I used https://patorjk.com/software/taag/ to generate the
 * ASCII text. The font used was Small Slant.
 */

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;

public class PasswordManager {
    private static HashMap<String, LinkedList<Account>> vault =
            new HashMap<String, LinkedList<Account>>();

    public static void main(String[] args) {
        boolean running = true;
        Scanner console = new Scanner(System.in);

        // main program loop
        while (running) {
            printLogo();
            System.out.println("\nWhat would you like to do?\n");
            System.out.println("[A]dd an entry");
            System.out.println("[S]earch for an entry; to view, remove, or edit");
            System.out.println("[E]xport vault");
            System.out.println("[I]mport vault");
            System.out.println("[Q]uit password manager\n");
            // Getting user input, reading the first character in lowercase
            String responseString = console.nextLine();
            char responseChar = responseString.toLowerCase().charAt(0);
            // switch/case to call the appropriate method depending on what the user wants to do.
            switch (responseChar) {
                case 'a':
                    add();
                    break;
                case 's':
                    search();
                    break;
                case 'e':
                    try {
                        exportVault();
                    } catch (IOException error) {
                        System.out.println(
                                "There was an error with saving your file. See error log for more information:\n"
                                        + error);
                    }
                    break;
                case 'i':
                    try {
                        importVault();
                    } catch (IOException error) {
                        System.out.println(
                                "There was an error with saving your file. See error log for more information:\n"
                                        + error);
                    }
                    break;
                case 'q':
                    System.out.println(
                            " ________             __                      __  ____      _ __  _                 ");
                    System.out.println(
                            "/_  __/ /  ___ ____  / /__  __ _____  __ __  / / / __/_ __ (_) /_(_)__  ___ _       ");
                    System.out.println(
                            " / / / _ \\/ _ `/ _ \\/  '_/ / // / _ \\/ // / /_/ / _/ \\ \\ // / __/ / _ \\/ _ `/ _ _ _ ");
                    System.out.println(
                            "/_/ /_//_/\\_,_/_//_/_/\\_\\  \\_, /\\___/\\_,_/ (_) /___//_\\_\\/_/\\__/_/_//_/\\_, / (_|_|_)");
                    System.out.println(
                            "                          /___/                                       /___/         ");
                    running = false;
                    break;
                // Any input not recognized generates an error, and the loop repeats
                default:
                    System.out.println("ERROR: Invalid response given, please try again.");
            }

        }
        console.close();
    }

    // Logo/Main Menu to help user understand where they are in the program
    public static void printLogo() {
        System.out.println(
                "   ___       __      _     __  _        __  __                                ___                                 __                                   ");
        System.out.println(
                "  / _ \\___ _/ /_____(_)___/ /_( )___   / / / /__  ___ ___ ______ _________   / _ \\___ ____ ____    _____  _______/ / __ _  ___ ____  ___ ____ ____ ____");
        System.out.println(
                " / ___/ _ `/ __/ __/ / __/  '_//(_-<  / /_/ / _ \\(_-</ -_) __/ // / __/ -_) / ___/ _ `(_-<(_-< |/|/ / _ \\/ __/ _  / /  ' \\/ _ `/ _ \\/ _ `/ _ `/ -_) __/");
        System.out.println(
                "/_/   \\_,_/\\__/_/ /_/\\__/_/\\_\\ /___/  \\____/_//_/___/\\__/\\__/\\_,_/_/  \\__/ /_/   \\_,_/___/___/__,__/\\___/_/  \\_,_/ /_/_/_/\\_,_/_//_/\\_,_/\\_, /\\__/_/   ");
        System.out.println(
                "                                                                                                                                        /___/          ");


        System.out.println("   __  ___     _        __  ___             ");
        System.out.println("  /  |/  /__ _(_)__    /  |/  /__ ___  __ __");
        System.out.println(" / /|_/ / _ `/ / _ \\  / /|_/ / -_) _ \\/ // /");
        System.out.println("/_/  /_/\\_,_/_/_//_/ /_/  /_/\\__/_//_/\\_,_/ ");

    }

    // Add method for user input
    public static void add() {
        System.out.println("   ___     __   __  ____     __          ");
        System.out.println("  / _ |___/ /__/ / / __/__  / /_______ __");
        System.out.println(" / __ / _  / _  / / _// _ \\/ __/ __/ // /");
        System.out.println("/_/ |_\\_,_/\\_,_/ /___/_//_/\\__/_/  \\_, / ");
        System.out.println("                                  /___/  ");
        Scanner console = new Scanner(System.in);
        Account tempAccount;
        System.out.print("Website/Application Name: ");
        String appName = console.nextLine();
        System.out.print("Username: ");
        String username = console.nextLine();
        System.out.print("Password: ");
        String password = console.nextLine();
        System.out.println("Would you like to add any notes to this account?\n\n[Y]es\n[N]o\n");
        String responseString = console.nextLine();
        char responseChar = responseString.toLowerCase().charAt(0);
        if (responseChar == 'n') {
            tempAccount = new Account(appName, username, password);
        } else {
            System.out.print("Notes: ");
            String notes = console.nextLine();
            tempAccount = new Account(appName, username, password, notes);
        }
        while (!confirm(tempAccount));
        if (vault.containsKey(tempAccount.getAppName())) {
            LinkedList<Account> accounts = vault.get(tempAccount.getAppName());
            accounts.add(tempAccount);
            vault.put(tempAccount.getAppName(), accounts);
        } else {
            LinkedList<Account> accounts = new LinkedList<Account>();
            accounts.add(tempAccount);
            vault.put(tempAccount.getAppName(), accounts);
        }

    }

    // add method for internal use
    public static void add(String appName, String username, String password, String notes) {
        Account tempAccount = new Account(appName, username, password, notes);
        if (vault.containsKey(tempAccount.getAppName())) {
            LinkedList<Account> accounts = vault.get(tempAccount.getAppName());
            accounts.add(tempAccount);
            vault.put(tempAccount.getAppName(), accounts);
        } else {
            LinkedList<Account> accounts = new LinkedList<Account>();
            accounts.add(tempAccount);
            vault.put(tempAccount.getAppName(), accounts);
        }
    }

    // Confirm whether the information is correct, if not, edit the account and loop until the user
    // is satisfied
    public static boolean confirm(Account account) {
        account.unHide();
        System.out.println("\n" + account.getString() + "\n\nIs this information correct?"
                + "\n\n[Y]es\n[N]o\n");
        account.hide();
        Scanner console = new Scanner(System.in);
        String responseString = console.nextLine();
        char responseChar = responseString.toLowerCase().charAt(0);
        if (responseChar == 'n') {
            edit(account);
            return false;
        }
        return true;
    }

    // Search for an account with user input
    public static void search() {
        System.out.println("   ____                 __ ");
        System.out.println("  / __/__ ___ _________/ / ");
        System.out.println(" _\\ \\/ -_) _ `/ __/ __/ _ \\");
        System.out.println("/___/\\__/\\_,_/_/  \\__/_//_/");



        System.out.println(
                "\nTo search for your account, please enter the website or application name.\n");

        System.out.println("Accounts found for these websites/applications:");
        Set<String> keys = vault.keySet();
        for (String key : keys) {
            LinkedList<Account> accounts = vault.get(key);
            if (accounts != null && accounts.size() > 0) {
                System.out.println(key);
            }
        }
        System.out.println();
        Scanner console = new Scanner(System.in);
        String appName = console.nextLine();
        LinkedList<Account> found = vault.get(appName);
        // If the user inputted app name does not exist we exit the method
        if (found == null || found.size() == 0) {
            System.out.println("ERROR: Account not found.");
            return;
        }
        int choice = 0;
        Account foundAccount;
        if (found.size() > 1) {
            System.out.println("\nMultiple accounts found for " + appName
                    + ".\nWhich one would you like to access?\n");
            for (Account account : found) {
                System.out.println(account.getUsername());
            }
            System.out.println("\nPlease enter the username correctly.\n");
            String username = console.nextLine();
            for (Account account : found) {
                if (account.getUsername().equals(username)) {
                    break;
                } else {
                    choice++;
                }
            }
        }
        foundAccount = found.get(choice);
        // Once the account is found the user has multiple options
        boolean accounting = true;
        while (accounting) {
            System.out.println("   ___                         __    ____                  __");
            System.out.println("  / _ |___________  __ _____  / /_  / __/__  __ _____  ___/ /");
            System.out.println(" / __ / __/ __/ _ \\/ // / _ \\/ __/ / _// _ \\/ // / _ \\/ _  / ");
            System.out.println(
                    "/_/ |_\\__/\\__/\\___/\\_,_/_//_/\\__/ /_/  \\___/\\_,_/_//_/\\_,_/  ");



            System.out.println("\n" + foundAccount.getString());
            System.out.println("\nWhat would you like to do with this account?\n");
            System.out.println("[S]how password");
            System.out.println("[H]ide password");
            System.out.println("[E]dit information");
            System.out.println("[U]sername - Copy");
            System.out.println("[P]assword - Copy");
            System.out.println("[D]elete account");
            System.out.println("[Q]uit search\n");
            String responseString = console.nextLine();
            char responseChar = responseString.toLowerCase().charAt(0);
            switch (responseChar) {
                case 's':
                    foundAccount.unHide();
                    break;
                case 'h':
                    foundAccount.hide();
                    break;
                case 'e':
                    edit(foundAccount);
                    break;
                case 'u':
                    copyUsername(foundAccount);
                    break;
                case 'p':
                    copyPassword(foundAccount);
                    break;
                case 'd':
                    found.remove(choice);
                    accounting = false;
                    break;
                case 'q':
                    accounting = false;
                    foundAccount.hide();
                    break;
                default:
                    System.out.println("ERROR: Invalid response given, please try again.");
            }
        }
    }

    // Copy username to clipboard
    public static void copyUsername(Account account) {
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new StringSelection(account.getUsername()), null);

    }

    // Copy password to clipboard
    public static void copyPassword(Account account) {
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new StringSelection(account.getPassword()), null);

    }

    // Edit account with user input
    public static void edit(Account account) {
        System.out.println("   ____   ___ __  _             ___                         __ ");
        System.out.println("  / __/__/ (_) /_(_)__  ___ _  / _ |___________  __ _____  / /_");
        System.out.println(" / _// _  / / __/ / _ \\/ _ `/ / __ / __/ __/ _ \\/ // / _ \\/ __/");
        System.out
                .println("/___/\\_,_/_/\\__/_/_//_/\\_, / /_/ |_\\__/\\__/\\___/\\_,_/_//_/\\__/ ");
        System.out.println("                      /___/                                    ");



        account.unHide();
        boolean editing = true;
        while (editing) {
            System.out.println("\n" + account.getString() + "\n\nWhat would you like to edit?");
            System.out.println("[A]pp or website name");
            System.out.println("[U]sername");
            System.out.println("[P]assword");
            System.out.println("[N]otes");
            System.out.println("[Q]uit editing");
            Scanner console = new Scanner(System.in);
            String responseString = console.nextLine();
            char responseChar = responseString.toLowerCase().charAt(0);
            switch (responseChar) {
                // Due to how the accounts are stored, if the user wishes to change the app name
                // we must delete the old account entry and create a new one with th updated name
                case 'a':
                    System.out.print("Website/Application Name: ");
                    String oldAppName = account.getAppName();
                    account.setAppName(console.nextLine());
                    LinkedList<Account> accounts = vault.get(oldAppName);
                    int i = 0;
                    for (Account tempAccount : accounts) {
                        if (tempAccount.getUsername().equals(account.getUsername())) {
                            accounts.remove(i);
                            break;
                        } else {
                            i++;
                        }
                    }
                    add(account.getAppName(), account.getUsername(), account.getPassword(),
                            account.getNotes());
                    break;
                case 'u':
                    System.out.print("Username: ");
                    account.setUsername(console.nextLine());
                    break;
                case 'p':
                    System.out.print("Password: ");
                    account.setPassword(console.nextLine());
                    break;
                case 'n':
                    System.out.print("WARNING: This will overwrite your current notes!\nNotes: ");
                    account.setNotes(console.nextLine());
                    break;
                case 'q':
                    editing = false;
                    account.hide();
                    break;
                default:
                    System.out.println("ERROR: Invalid response given, please try again.");
            }
        }
    }

    // Writes all account information into a plain text file for later use.
    public static void exportVault() throws IOException {
        System.out.println("\nWhat file would you like to save to?\n");
        Scanner console = new Scanner(System.in);
        BufferedWriter writer = new BufferedWriter(new FileWriter(console.nextLine()));
        Set<String> keys = vault.keySet();
        for (String key : keys) {
            LinkedList<Account> accounts = vault.get(key);
            for (Account account : accounts) {
                account.unHide();
                writer.append(account.getCSVString() + ",");
            }
        }
        writer.close();
    }

    // Imports account information that was previously saved by the user.
    public static void importVault() throws IOException {
        System.out.println("\nWhat file would you like to open?\n");
        Scanner console = new Scanner(System.in);
        Scanner scanner = new Scanner(new File(console.nextLine()));
        scanner.useDelimiter(",");

        while (scanner.hasNext()) {
            String appName = scanner.next();
            String username = scanner.next();
            String password = scanner.next();
            String notes = scanner.next();
            add(appName, username, password, notes);
        }
        scanner.close();
    }
}
