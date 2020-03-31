package shared.Request1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * gets all the information in that we need about the User to be able to access that specific User
 * just a bunch of getters and setters
 */
public class LoginRequest
{
    String userName;
    String password;

    public LoginRequest (String UserNameIn, String PasswordIn)
    {
        userName = UserNameIn;
        password = PasswordIn;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    /*public String serialize()
    {
        try
        {
            FileOutputStream file = new FileOutputStream(String.valueOf(this));
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(this);
            out.close();
            file.close();
        } catch (FileNotFoundException e)
        {
            System.out.println("FileNotFound, LoginRequest");
            e.printStackTrace();
        } catch (IOException e)
        {
            System.out.println("IOException, LoginRequest");
            e.printStackTrace();
        }
        return this.toString();
    }*/
}
