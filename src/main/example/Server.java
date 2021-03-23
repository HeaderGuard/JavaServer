package example;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import static example.Utils.error;

public class Server
{
    private static final int port = 8080;
    public static void main(String[] args)
    {
        try
        {
            //Initialize the server at IP address localhost on port 8080
            final HttpServer server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
            //Allow the server request 2 threads to run on
            server.setExecutor(Executors.newFixedThreadPool(2));
            //Static folder
            Utils.staticFolder(server, "client/");
            //API Endpoints
            server.createContext("/users/add", new Router("/users/add"));
            server.createContext("/users/find", new Router("/users/find"));
            server.createContext("/users/update", new Router("/users/update"));
            server.createContext("/users/delete", new Router("/users/delete"));
            //Start the server on a different thread
            server.start();
            System.out.println("Server started on " + port);
        }
        catch(Exception e)
        {
            error(e);
        }
    }
}
