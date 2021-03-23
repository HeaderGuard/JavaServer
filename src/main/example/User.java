package example;

import java.util.UUID;

//Schema for users
public class User
{
    private UUID id;
    private String username;
    private String password;
    private int age;
    public User(){}

    public User(final String username, final String password, final int age)
    {
        setId();
        this.username = username;
        this.password = password;
        this.age = age;
    }

    public UUID getId()
    {
        return id;
    }

    public void setId()
    {
        id = UUID.randomUUID();
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
