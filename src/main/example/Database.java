package example;

//You thought I was giving you my database login information
import io.github.cdimascio.dotenv.Dotenv;

//SQL
import java.sql.*;
//ArrayList of JSON Objects
import java.util.ArrayList;
import java.util.List;
//Make sure DB_URL isn't null
import java.util.Objects;

//Implementing runnable so we can multi thread
public class Database implements Runnable
{
    //To decide what function we want to call
    private final String endpoint;
    //To get the data sent by the request
    private final JSONObject json;
    //Connection to the database we need in almost every function
    private Connection connection;
    //If we want to send a list of JSON objects from database data
    private final List<JSONObject> data;

    public Database(String endpoint, JSONObject json)
    {
        this.endpoint = endpoint;
        this.json = json;
        this.data = new ArrayList<>();
    }

    //Does an SQL Query to add a user
    public void addUser()
    {
        String name = this.json.getString("name");
        int age = this.json.getInt("age");
        try
        {
            //Prepared statement is more clean
            PreparedStatement statement = connection.prepareStatement("INSERT INTO users (name, age) VALUES ('"+ name + "', " + age + ");");
            //Stop if we get nothing in 10 seconds
            statement.setQueryTimeout(10);
            statement.executeUpdate();
            System.out.println("User created");
            statement.close();
        }
        catch(Exception e)
        {
            error(e);
        }
    }

    public void findUser()
    {
        String name = json.getString("name");
        try
        {
            String query = "SELECT * FROM users WHERE name = '" + name + "';";
            PreparedStatement statement = connection.prepareStatement(query);
            //Stop if we get nothing in 10 seconds
            statement.setQueryTimeout(10);
            ResultSet rs = statement.executeQuery();
            resultsToJSON(rs);
            System.out.println("Users found");
            statement.close();
        }
        catch(Exception e)
        {
            error(e);
        }
    }

    private void updateUser()
    {
        String name = this.json.getString("name");
        int newAge = this.json.getInt("age");
        try
        {
            PreparedStatement statement = connection.prepareStatement("UPDATE USERS SET age = " + newAge + " WHERE name = '" + name + "';");
            //Stop if we get nothing in 10 seconds
            statement.setQueryTimeout(10);
            statement.execute();
            System.out.println("User updated");
            statement.close();
        }
        catch(Exception e)
        {
            error(e);
        }
    }

    private void deleteUser()
    {
        String name = this.json.getString("name");
        try
        {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM USERS WHERE name = '" + name + "' RETURNING *;");
            //Stop if we get nothing in 10 seconds
            statement.setQueryTimeout(10);
            resultsToJSON(statement.executeQuery());
            System.out.println("User deleted");
            statement.close();
        }
        catch(Exception e)
        {
            error(e);
        }
    }

    //Converts the result set into JSON
    private void resultsToJSON(ResultSet rs)
    {
        try
        {
            //Read from ResultSet as long as we have a result and store the data in a JSONObject then add that object to the ArrayList
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
            error(e);
        }
        //Prints out the result set
        for(JSONObject object : data)
        {
            System.out.println(object.getInt("id"));
            System.out.println(object.getString("name"));
            System.out.println(object.getInt("age"));
        }
    }

    //We have this so we can get our data in the Router function, specifically when we join threads
    public JSONObject getJson()
    {
        return json;
    }

    //We do this for the multithreading
    @Override
    public void run()
    {
        try
        {
            //Read the variables from our environment variables file
            Dotenv dotenv = Dotenv.load();
            //Find the JAR file for PostgreSQL
            Class.forName("org.postgresql.Driver");
            //Connect to the database
            connection = DriverManager.getConnection(Objects.requireNonNull(dotenv.get("DB_URL")), dotenv.get("DB_USER"), dotenv.get("DB_PASS"));
            System.out.println("Connected to Database on new thread");
            //Decide what function to call based on the API endpoint
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
            //close the database connection
            connection.close();
        }
        catch(Exception e)
        {
            error(e);
        }
    }
    private void error(Exception e)
    {
        e.printStackTrace();
        System.err.println(e.getMessage());
        System.exit(-1);
    }
}
