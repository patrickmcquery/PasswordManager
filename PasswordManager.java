import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.Set;

public class PasswordManager {
    private static LinkedHashMap<String, Account> vault = new LinkedHashMap<String, Account>();

    public static void main(String[] args) {
        // PasswordManagerGUI gui = new PasswordManagerGUI();
        boolean running = true;
        boolean firstTime = true;
        Scanner console = new Scanner(System.in);
        while (running) {
            if (firstTime) {
                printIntro();
                firstTime = false;
            }
            System.out.println("\nWhat would you like to do?\n");
            System.out.println("[A]dd an entry");
            System.out.println("[S]earch for an entry; to view, remove, or edit");
            System.out.println("[E]xport vault");
            System.out.println("[I]mport vault");
            System.out.println("[Q]uit\n");
            String responseString = console.nextLine();
            char responseChar = responseString.toLowerCase().charAt(0);
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
                    System.out.println("Exiting gracefully.");
                    running = false;
                    break;
                default:
                    System.out.println("ERROR: Invalid response given, please try again.");
            }

        }
        console.close();
    }

    public static void printIntro() {
        System.out.println("\n+--------------------------------------------+");
        System.out.println("| Welcome to my (unsecure) password manager! |");
        System.out.println("+--------------------------------------------+");
    }

    public static void add() {
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
        vault.put(tempAccount.getAppName(), tempAccount);

    }

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

    public static void search() {
        System.out.println(
                "\nTo search for your account, please enter the website or application name.\n");
        Scanner console = new Scanner(System.in);
        String appName = console.nextLine();
        Account found = vault.get(appName);
        if (found == null) {
            System.out.println("\nERROR: Account not found.");
            return;
        }
        boolean accounting = true;
        while (accounting) {
            System.out.println("\n" + found.getString());
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
                    found.unHide();
                    break;
                case 'h':
                    found.hide();
                    break;
                case 'e':
                    edit(found);
                    break;
                case 'u':
                    copyUsername(found);
                    break;
                case 'p':
                    copyPassword(found);
                    break;
                case 'd':
                    if (vault.remove(found.getAppName(), found)) {
                        System.out.println("\nAccount deleted successfully.");
                        accounting = false;
                    } else {
                        System.out.println("There was a problem deleting this account.");
                    }

                    break;
                case 'q':
                    accounting = false;
                    found.hide();
                    break;
                default:
                    System.out.println("ERROR: Invalid response given, please try again.");
            }
        }
    }

    public static void copyUsername(Account account) {
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new StringSelection(account.getUsername()), null);

    }

    public static void copyPassword(Account account) {
        Toolkit.getDefaultToolkit().getSystemClipboard()
                .setContents(new StringSelection(account.getPassword()), null);

    }

    public static void edit(Account account) {
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
                case 'a':
                    System.out.print("Website/Application Name: ");
                    account.setAppName(console.nextLine());
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

    public static void exportVault() throws IOException {
        System.out.println("\nWhat file would you like to save to?\n");
        Scanner console = new Scanner(System.in);
        BufferedWriter writer = new BufferedWriter(new FileWriter(console.nextLine()));
        Set<String> keys = vault.keySet();
        for (String key : keys) {
            vault.get(key).unHide();
            writer.append(vault.get(key).getCSVString() + ",");
        }
        writer.close();
    }

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
            vault.put(appName, new Account(appName, username, password, notes));
        }
        scanner.close();
    }
}
