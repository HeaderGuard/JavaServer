package example.server;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Server
{
    private static String clientDir;
    private static HttpServer server;

    public static void main(String[] args)
    {
        int port = 8080;
        clientDir = "client/";
        createRoutes();
        try
        {
            //Allow the server 2 threads to run on
            server.setExecutor(Executors.newFixedThreadPool(2));
            System.out.println("Server starting on " + port);
            server.start();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }
    public static void createRoutes()
    {
        int port = 8080;
        try
        {
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
    public static String route(String file)
    {
        return clientDir + file;
    }
}
