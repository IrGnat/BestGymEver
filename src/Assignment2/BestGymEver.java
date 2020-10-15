package Assignment2;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Iryna Gnatenko
 * Date 10/12/2020
 * Time 1:20 PM
 * Project Sprint2
 */

public class BestGymEver {

    Path inFilePath = Paths.get("src/Assignment2/customers.txt");
    Path outFilePath = Paths.get("src/Assignment2/visitors.txt");

    // get a list with all gym members, try with resources
    public List<String> getMembersFromFile(Path inPath) {

        List<String> allMembers = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(inPath.toString()))) {
            while (sc.hasNextLine()) {
                allMembers.add(sc.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("The file is not found.");
            System.exit(0);

        } catch (Exception e) {
            System.out.println("Something went wrong.");
            e.printStackTrace();
            System.exit(0);
        }
        return allMembers;
    }

    // Checking if the customer is the current member and printing it out
    public String checkMembership(List<String> allMembers, String userInput) {

        for (String member : allMembers) {
            if (member.toLowerCase().contains(userInput.toLowerCase())) {

                //using indexOf() to find index of element member and get a name and personal number of the customer
                String membersInfo = allMembers.get(allMembers.indexOf(member));

                //using indexOf() to find index of element member, +1 to get to the line with pers number
                int positionInList = allMembers.indexOf(member) + 1;

                //parse String element to LocalDate element
                LocalDate membershipDate = LocalDate.parse(allMembers.get(positionInList));

                //using compareTo() to compare membership date to the current date
                int compDate = membershipDate.plusDays(365).compareTo(LocalDate.now());

                if (compDate < 0)
                    return "The customer is a former member. Please renew the membership.";
                else if (compDate >= 0) {
                    registerVisitor(membersInfo, LocalDate.now()); // writing to the file
                    return "The customer is a current member.";
                }
            }
        }
        return "The customer has never been a member at this gym.";
    }


    // using FileWriter to register visitors in the separate file, try with resources
    public void registerVisitor(String userInput, LocalDate date) {
        try (PrintWriter writeToFile = new PrintWriter(new BufferedWriter(new FileWriter(outFilePath.toString(), true)))) {
            writeToFile.write(userInput);
            writeToFile.write(" ");
            writeToFile.write(date.toString() + " \n");
            writeToFile.flush();

        } catch (IOException e) {
            System.out.println("Couldn't write to the file");
            e.printStackTrace();
        }
    }

    // get user input with Scanner
    public String getUserInput(String userInput) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter a name or personal number");

        while (true) {
            String input = sc.nextLine();

            if (input.isBlank()) {
                System.out.println("You didn't write anything. Please enter a name or personal number");
                continue;
            }
            return input;

        }
    }

    public Boolean testMembership;

    public BestGymEver(boolean testMembership) {
        this.testMembership = testMembership;

        if (!this.testMembership) {
            List<String> membersList = getMembersFromFile(inFilePath);
            System.out.println(checkMembership(membersList, getUserInput(null)));
        }
    }

    public static void main(String[] args) {
        BestGymEver gym = new BestGymEver(false);
    }
}

