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

public class Router implements HttpHandler
{
    //Path to file or api endpoint
    private final String path;
    private final String contentType;
    public Router(final String path)
    {
        this.path = path;
        this.contentType = "application/json";
    }

    public Router(final String path, final String contentType)
    {
        this.path = path;
        this.contentType = contentType;
    }

    @Override
    public void handle(final HttpExchange req)
    {
        try
        {
            String reqMethod = req.getRequestMethod();
            if(reqMethod.equals("GET"))
            {
                String code = readFile(new File(path));
                if(code.length() == 0)
                {
                    response(req, "{message: endpoint not registered}", 404);
                }
                response(req, code, 200);
            }
            else if(reqMethod.equals("POST"))
            {
                final JSONObject json = new JSONObject(readFile(req.getRequestBody()));
                System.out.println(json.toString());
                if(path.contains("/users"))
                {
                    //Create a Database Object passing the endpoint and json data
                    final Database db = new Database(path, json);
                    //Create a new Thread to run what we need with the Database class
                    final Thread task = new Thread(db);
                    //Call db.run() on a new thread
                    task.start();
                    try
                    {
                        //We need to get the response JSON from the other thread so we join them and call a function
                        task.join();
                        //Create a response from the JSON and send as an OutputStream with status code of 200
                        response(req, db.getJson().toString(), 200);
                    }
                    catch (InterruptedException e)
                    {
                        error(e);
                    }
                }
                else
                {
                    response(req, "{message: endpoint not registered}", 404);
                }
            }
        }
        catch (Exception e)
        {
            error(e);
        }
    }

    //Response for files
    public void response(final HttpExchange req, final String res, final int statusCode)
    {
        try
        {
            //Get code from the file and send it back as an OutputStream with a response code of 200
            final OutputStream os = req.getResponseBody();
            req.getResponseHeaders().add("Content-Type", contentType);
            req.sendResponseHeaders(statusCode, res.length());
            os.write(res.getBytes(StandardCharsets.UTF_8));
            os.close();
        }
        catch (IOException e)
        {
            error(e);
        }
    }
}