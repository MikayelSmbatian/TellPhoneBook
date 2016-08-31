package Pack;

import jdk.nashorn.internal.ir.WhileNode;

import java.io.*;
import java.util.Scanner;
import java.util.TreeSet;


public class Address
{
    static final OutputProperties prop = new OutputProperties();
    static Scanner scan = new Scanner(System.in);
    static void SignUp()
    {
        User u = new User();
        try
        {
            u.createUser();
        }
        catch (UserAlreadyExistsException e)
        {
            SignUp();
        }
    } //// kaskats
    static void showTelNumbers(User us)
    {
        try
        {
            System.out.println(us.showTelNumbers());
        } catch (UserNotFoundException e)
        {
            e.printStackTrace();
        }

    }
    static void Exit()
    {
        System.out.println(prop.get("GoodBye"));
        try
        {
            Thread.sleep(2000);
        }
        catch (InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
        System.exit(1);
    }
    static void addTelNumb(User us)
    {
        System.out.print(prop.get("ProvideAnyNumber"));
        scan = new Scanner(System.in);
        us.addTelNumber(scan.nextLine());
    }
    static void addFriend(User us)
    {
        System.out.print(prop.get("ProvideUserToSendFriendRequest"));
        scan = new Scanner(System.in);
        String friendUsername = scan.nextLine();
        User friend = new User();
        try
        {
            friend = friend.getUserFromDB(friendUsername);
        }
        catch (UserNotFoundException e)
        {
            System.out.println(prop.get("UserIsNotFound"));
            return;
        }
        us.friendRequest(friend);
        friend.saveUser();
    }
    public static void signOut(User us)
    {
        us.saveUser();
        System.out.println(prop.get("ChangesHasSaved"));
        System.out.println(prop.get("GoodBye"));
    }
    static void ShowFriendsList(User us)
    {
        if(us.getFriendsList().isEmpty())
        {
            System.out.println(prop.getProperty("YouHaveNoFriends") + "\n");
            return;
        }
        System.out.println(prop.getProperty("YourFriendsListYouCanSeeBelow") + us.getFriendsList());
        System.out.println(prop.getProperty("FriendLISTMenu"));
        String command = scan.nextLine();
        handleFriendPageCommand(command,us);
    }
    static void handleFriendPageCommand(String command, User us)
    {
        command = command.toUpperCase();
        switch (command)
        {
            case "BACK":
            {
                return;
            }
            case "DELETE FRIEND":
            {
                System.out.print(prop.getProperty("WriteUsernameToDelete"));
                String selectedUsername = scan.nextLine();
                try
                {
                    us.deleteFriend(selectedUsername);
                }
                catch (UserNotFoundException e)
                {
                    System.out.println("User " + selectedUsername + " is not found in your friends list");
                    System.out.println(prop.getProperty("Friend LIST Menu"));
                    command = scan.nextLine();
                    handleFriendPageCommand(command,us);
                }
            }
        }
    }
    static void handleSignedInCommand(String command, User us) throws Exception
    {
        command = command.toUpperCase();
        switch (command)
        {
            case "ADD TEL NUMBER":
            {
               addTelNumb(us);
                break;
            }
            case "ADD FRIEND":
            {
                addFriend(us);
                break;
            }
            case "SHOW TEL NUMBERS":
            {
                showTelNumbers(us);
                break;
            }
            case "SIGN OUT":
            {
                signOut(us);
                throw new Exception();
            }
            case "FRIENDS":
            {
                ShowFriendsList(us);
                break;
            }
            default:
            {
                SignedInHelp();
            }
        }
    }
    static void signedIn(User us)
    {
        System.out.println(prop.get("YouHaveSuccessfullySignedIn"));
        while(true)
        {
            System.out.println(prop.get("SignedInHANDLE"));
            String command = scan.nextLine();
            try {
                handleSignedInCommand(command, us);
            } catch (Exception e)
            {
                break;
            }
        }
    }
    static void SignedInHelp()
    {
        System.out.println(prop.get("SignedInHELP"));
    }
    static void SignIn()//<---------------------------
    {
        User us = new User();
        scan = new Scanner(System.in);
        System.out.print(prop.get("ProvideYourUsername"));
        String usName = scan.nextLine();
        try
        {
            us = us.getUserFromDB(usName);
        }
        catch (UserNotFoundException e)
        {
            System.out.println(prop.get("UserIsNotFound"));
            SignIn();
        }
        scan = new Scanner(System.in);
        System.out.print(prop.get("ProvideYourPassword"));
        String passWord = scan.nextLine();
        boolean isCorrectPassword = us.checkPassword(passWord);
        int mistakes = 1;
        while(!isCorrectPassword)
        {
            if(mistakes > 3)
            {
                System.out.println(prop.get("Mistakes3"));
                Exit();
            }
            System.out.println(prop.get("IncorrectPassword"));
            scan = new Scanner(System.in);
            System.out.print(prop.get("ProvideYourPassword"));
            passWord = scan.nextLine();
            ++mistakes;
            isCorrectPassword = us.checkPassword(passWord);
        }
        signedIn(us);
    }
    static void help()
    {
        System.out.println(prop.get("HELP"));
    }
    static void handleCommand(String command)
    {
        command = command.toUpperCase();
        switch (command)
        {
            case "SIGN IN":
            {
                SignIn();
                break;
            }
            case "SIGN UP":
            {
                SignUp();
                break;
            }
            case "EXIT":
            {
                Exit();
                break;
            }
            default:
            {
                help();
            }
        }
    }
    public static void  main(String [] args) throws UserNotFoundException {

        while (true)
        {
            System.out.println(prop.getProperty("HANDLE"));
            String command = scan.nextLine();
            scan = new Scanner(System.in);
            handleCommand(command);
        }
    }

}
