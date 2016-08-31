package Pack;

import org.json.simple.JSONArray;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;

interface UserRepozitory extends Serializable
{
    public void createUser() throws UserAlreadyExistsException;
    public User getUserFromDB(String username) throws UserNotFoundException;
    public void serializeUser() throws  ValidUserException;
}
class User implements UserRepozitory
{
    static final OutputProperties prop = new OutputProperties();

    private String username;
    private String password;
    private String usernameDBFolder;
    private TreeSet<String> contactsList;
    private TreeSet<String> friends;
    public String getUsername() {
        return username;
    }
    private String getPassword() {
        return password;
    }
    public String getUsernameDBFolder() {
        return usernameDBFolder;
    }
    public String getFriendsList()
    {
        String res = "";
        for (String x : friends)
        {
            res += " -> " + x  + "\n";
        }
        return res;
    }
    public String getTelNumbers()
    {
        String res = "";
        for(String x : contactsList)///////////////////
            res += " --> " + x + "\n";
        return res;
    }
    private String getFriendsTelNumbers() throws UserNotFoundException ///////////STUGEL
    {
        String res = "";
        int i = 1;
        for(String friend : friends)
        {
            User us = new User();
            us = us.getUserFromDB(friend);
            if(i == 1)
            {
                res += us.getTelNumbers() + "\n";
                continue;
            }
            res +=" --> " + us.getTelNumbers() + "\n";
        }
        return res;
    }
    public String showTelNumbers() throws UserNotFoundException ///////////STUGEL
    {
        String res = "";
        res += this.getTelNumbers() + "\n friends tel. numbers: \n";
        res += getFriendsTelNumbers();
        return res;
    }

    public void addTelNumber(String number)
    {
        this.contactsList.add(number);
    }
    private void createUserDBFile() throws UserAlreadyExistsException
    {
        // Creates a DB for User
        boolean newFile = false;
        File f = new File(this.usernameDBFolder);
        try {
             newFile = f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!newFile)
            throw new UserAlreadyExistsException();
    }
    @Override
    public void serializeUser() throws UserAlreadyExistsException
    {
        try
        {
            this.createUserDBFile();
        }
        catch (UserAlreadyExistsException e)
        {
            throw new UserAlreadyExistsException();
        }
        try
        {
            FileOutputStream fos = new FileOutputStream(this.usernameDBFolder);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.flush();
            oos.close();
        }
        catch(Throwable t)
        {
            t.printStackTrace();
        }
    }
    public void saveUser()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(this.usernameDBFolder);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.flush();
            oos.close();
        }
        catch(Throwable t)
        {
            t.printStackTrace();
        }
    }
    public User DeSerializeUser(String DBFolder) throws UserNotFoundException ////// STUGEL
    {
        User ts = null;
        try
        {
            FileInputStream fis = new FileInputStream(DBFolder);
            ObjectInputStream oin = new ObjectInputStream(fis);
            ts = (User) oin.readObject();
        }
        catch(FileNotFoundException FNFE)
        {
            throw new UserNotFoundException();
        }
        catch (IOException | ClassNotFoundException E)
        {
            E.printStackTrace();
        }
        return ts;
    }

    @Override
    public void createUser() throws UserAlreadyExistsException
    {
        Scanner scan = new Scanner(System.in);
        System.out.print(prop.getProperty("ProvideYourUsername"));
        this.username = scan.nextLine();
        System.out.print(prop.getProperty("ProvideYourPassword"));
        scan = new Scanner(System.in);
        this.password = scan.nextLine();
        this.usernameDBFolder = username + "DataBase.txt";
        this.contactsList = new TreeSet<String>();
        this.friends = new TreeSet<String>();
        try
        {
            this.serializeUser();
        }
        catch(UserAlreadyExistsException UAE)
        {
            System.out.println(prop.getProperty("UserAlreadyExists"));
            throw new UserAlreadyExistsException();
        }

    }

    public void friendRequest(User friend)
    {
        friend.friends.add(this.username);
    }
    public void deleteFriend(String selectedUsername) throws UserNotFoundException
    {
       // User us = new User();
        //us = us.getUserFromDB(selectedUsername);
        boolean isDeleted = this.friends.remove(selectedUsername);
        if(!isDeleted)
            throw new UserNotFoundException();
    }

    @Override
    public User getUserFromDB(String username) throws UserNotFoundException
    {
        this.usernameDBFolder = username + "DataBase.txt";
        User result  = DeSerializeUser(this.usernameDBFolder);
        return result;
    }
    public boolean checkPassword(String passWord)
    {
        return this.password.equals(passWord);
    }
}