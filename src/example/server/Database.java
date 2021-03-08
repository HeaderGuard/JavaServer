package example.server;

import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Database implements Runnable
{
    private final String endpoint;
    private final JSONObject json;
    private Connection connection;
    private final List<JSONObject> data;

    public Database(String endpoint, JSONObject json)
    {
        this.endpoint = endpoint;
        this.json = json;
        this.data = new ArrayList<>();
    }

    public void addUser()
    {
        String name = this.json.getString("name");
        int age = this.json.getInt("age");
        try
        {
            String query = "INSERT INTO users (name, age) VALUES ('"+ name + "', " + age + ");";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setQueryTimeout(10);
            statement.executeUpdate();
            System.out.println("User created");
            statement.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void findUser()
    {
        String name = json.getString("name");
        try
        {
            String query = "SELECT * FROM users WHERE name = '" + name + "';";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setQueryTimeout(10);
            ResultSet rs = statement.executeQuery();
            resultsToJSON(rs);
            System.out.println("Users found");
            statement.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void updateUser()
    {
        String name = this.json.getString("name");
        int newAge = this.json.getInt("age");
        try
        {
            String query = "UPDATE USERS SET age = " + newAge + " WHERE name = '" + name + "';";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setQueryTimeout(10);
            statement.executeUpdate();
            System.out.println("User updated");
            statement.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void deleteUser()
    {
        String name = this.json.getString("name");
        try
        {
            String query = "DELETE FROM USERS WHERE name = '" + name + "' RETURNING *;";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setQueryTimeout(10);
            ResultSet rs = statement.executeQuery();
            resultsToJSON(rs);
            System.out.println("User deleted");
            statement.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void resultsToJSON(ResultSet rs)
    {
        try
        {
            while(rs.next())
            {
                JSONObject result = new JSONObject();
                result.put("id", rs.getInt("id"));
                result.put("name", rs.getString("name"));
                result.put("age", rs.getInt("age"));
                data.add(result);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(-1);
        }
        for(JSONObject object : data)
        {
            System.out.println(object.getInt("id"));
            System.out.println(object.getString("name"));
            System.out.println(object.getInt("age"));
        }
    }

    public JSONObject getJson()
    {
        return json;
    }

    @Override
    public void run()
    {
        try
        {
            Dotenv dotenv = Dotenv.load();
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(Objects.requireNonNull(dotenv.get("DB_URL")), dotenv.get("DB_USER"), dotenv.get("DB_PASS"));
            System.out.println("Connected to Database on new thread");
            switch (endpoint)
            {
                case "/users/add":
                    addUser();
                    break;
                case "/users/find":
                    findUser();
                    break;
                case "/users/update":
                    updateUser();
                    break;
                case "/users/delete":
                    deleteUser();
                    break;
            }
            connection.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }
}
