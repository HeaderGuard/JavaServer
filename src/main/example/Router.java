package example;

//implement HttpHandler and HttpExchange is for out handle method
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

//File reading
import java.io.*;

//Specify UTF-8 just to be safe
import java.nio.charset.StandardCharsets;

import static example.Utils.error;
import static example.Utils.readFile;
import static example.Utils.readStream;

public class Router implements HttpHandler
{
    //Path to file or api endpoint
    private final String path;

    public Router(String path)
    {
        this.path = path;
    }

    @Override
    public void handle(HttpExchange req)
    {
        try
        {
            String reqMethod = req.getRequestMethod();
            if(reqMethod.equals("GET"))
            {
                String code = readFile(new File(path));
                if(code.length() == 0)
                {
                    JSONObject res = new JSONObject();
                    res.put("message", "endpoint not registered");
                    response(req, res, 404);
                }
                response(req, code, 200);
            }
            else if(reqMethod.equals("POST"))
            {
                JSONObject json = new JSONObject(readFile(req.getRequestBody()));
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
                        //Create a response from the JSON and send as an OutputStream with status code of 200
                        response(req, db.getJson(), 200);
                    }
                    catch (InterruptedException e)
                    {
                        error(e);
                    }
                }
                else
                {
                    JSONObject res = new JSONObject();
                    json.put("message", "endpoint not registered");
                    response(req, res, 404);
                }
            }
        }
        catch (Exception e)
        {
            error(e);
        }
    }

    //Response for files
    public void response(HttpExchange req, String code, int statusCode)
    {
        try
        {
            //Get code from the file and send it back as an OutputStream with a response code of 200
            req.sendResponseHeaders(statusCode, code.length());
            OutputStream os = req.getResponseBody();
            os.write(code.getBytes(StandardCharsets.UTF_8));
            os.close();
        }
        catch (IOException e)
        {
            error(e);
        }
    }

    //Response for JSON
    private void response(HttpExchange req, JSONObject json, int statusCode)
    {
        try
        {
            String res = json.toString();
            req.sendResponseHeaders(statusCode, res.length());
            OutputStream stream = req.getResponseBody();
            stream.write(res.getBytes(StandardCharsets.UTF_8));
            stream.close();
        }
        catch (IOException e)
        {
            error(e);
        }
    }
}