package example;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Server
{
    private static String clientDir;
    //Server HttpServer object
    private static HttpServer server;
    private static final int port = 8080;
    public static void main(String[] args)
    {
        clientDir = "client/";
        createRoutes();
        try
        {
            //Allow the server request 2 threads to run on
            server.setExecutor(Executors.newFixedThreadPool(2));
            //Start the server on a different thread
            server.start();
            System.out.println("Server started on " + port);
        }
        catch(Exception e)
        {
            //Print the error and kill the server
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }
    public static void createRoutes()
    {
        try
        {
            //Initialize the server at IP address localhost on port 8080
            server = HttpServer.create(new InetSocketAddress("localhost", port), 0);

            //Files we need to serve
            server.createContext("/", new Router(route("index.html")));
            server.createContext("/ez", new Router(route("ez.html")));
            server.createContext("/styles/style.css", new Router(route("styles/style.css")));
            server.createContext("/javascript/components/Header.js", new Router(route("javascript/components/Header.js")));
            server.createContext("/javascript/test.js", new Router(route("javascript/test.js")));

            //API Endpoints
            server.createContext("/users/add", new Router("/users/add"));
            server.createContext("/users/find", new Router("/users/find"));
            server.createContext("/users/update", new Router("/users/update"));
            server.createContext("/users/delete", new Router("/users/delete"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }
    //just for convenience of not having to write client every time we route a file on the client
    public static String route(String file)
    {
        return clientDir + file;
    }
}
