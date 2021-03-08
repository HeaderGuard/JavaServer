package example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.json.*;

public class Router implements HttpHandler, Runnable
{
    private final String path;

    public Router(String path)
    {
        this.path = path;
    }

    @Override
    public void handle(HttpExchange req) throws IOException
    {
        if(req.getRequestMethod().equals("GET"))
        {
            String code = getFile();
            req.sendResponseHeaders(200, code.length());
            OutputStream stream = req.getResponseBody();
            stream.write(code.getBytes(StandardCharsets.UTF_8));
            stream.close();
        }
        else if(req.getRequestMethod().equals("POST"))
        {
            StringBuilder requestJSON = new StringBuilder();
            Scanner scanner = new Scanner(req.getRequestBody());
            while(scanner.hasNextLine())
            {
                requestJSON.append(scanner.nextLine());
            }
            JSONObject json = new JSONObject(requestJSON.toString());
            if(path.contains("/users"))
            {
                Database db = new Database(path, json);
                Thread task = new Thread(db);
                task.start();
                try
                {
                    task.join();
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

    public String getFile()
    {
        StringBuilder code = new StringBuilder();
        try
        {
            File file = new File(path);
            Scanner reader = new Scanner(file);
            while(reader.hasNextLine())
            {
                String line = reader.nextLine();
                code.append(line);
            }
            reader.close();
        }
        catch (FileNotFoundException e)
        {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
        return code.toString();
    }

    @Override
    public void run()
    {
        //
    }
}