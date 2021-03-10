package example;

//implement HttpHandler and HttpExchange is for out handle method
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

//File reading
import java.io.*;

//Specify UTF-8 just to be safe
import java.nio.charset.StandardCharsets;
//InputStreams, FileStreams, etc...
import java.util.Scanner;

//External dependency for JSON Objects, really just a more specialized HashMap
//import org.json.*;

public class Router implements HttpHandler
{
    //Path to file or api endpoint
    private final String path;

    public Router(String path)
    {
        this.path = path;
    }

    //Required by HttpHandler
    @Override
    public void handle(HttpExchange req) throws IOException
    {
        if(req.getRequestMethod().equals("GET"))
        {
            //Get code from the file and send it back as an OutputStream with a response code of 200
            String code = getFile();
            req.sendResponseHeaders(200, code.length());
            OutputStream stream = req.getResponseBody();
            stream.write(code.getBytes(StandardCharsets.UTF_8));
            stream.close();
        }
        else if(req.getRequestMethod().equals("POST"))
        {
            StringBuilder request = new StringBuilder();
            Scanner scanner = new Scanner(req.getRequestBody());
            while(scanner.hasNextLine())
            {
                request.append(scanner.nextLine());
            }
            JSONObject json = new JSONObject(request.toString());
            System.out.println(json.toString());
            if(path.contains("/users"))
            {
                //Create a Database Object passing the endpoint and json data
                Database db = new Database(path, json);
                //Create a new Thread to run what we need with the Database class
                Thread task = new Thread(db);
                //Call db.run() on a new thread
                task.start();
                try
                {
                    //We need to get the response JSON from the other thread so we join them and call a function
                    task.join();
                    //Create the response String data from the JSON and send as an OutputStream with res code of 200
                    String res = db.getJson().toString();
                    req.sendResponseHeaders(200, res.length());
                    OutputStream stream = req.getResponseBody();
                    stream.write(res.getBytes(StandardCharsets.UTF_8));
                    stream.close();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                    System.err.println(e.getMessage());
                    System.exit(-1);
                }
            }
        }
    }

    private String getFile()
    {
        //Read the file line by line
        StringBuilder code = new StringBuilder();
        try
        {
            Scanner reader = new Scanner(new File(path));
            while(reader.hasNextLine())
            {
                code.append(reader.nextLine());
            }
            reader.close();
        }
        catch (FileNotFoundException e)
        {
            //error handling
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
        return code.toString();
    }
}