package example;

import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class Utils
{
    public static String readFile(final String path)
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
            error(e);
        }
        return code.toString();
    }

    public static String readStream(final InputStream is)
    {
        //Read the input stream
        StringBuilder data = new StringBuilder();
        try
        {
            Scanner reader = new Scanner(is);
            while(reader.hasNextLine())
            {
                data.append(reader.nextLine());
            }
            reader.close();
        }
        catch (Exception e)
        {
            error(e);
        }
        return data.toString();
    }

    //Get all the html css and javascript files we need and serve them if we have a directory make a recursive call
    public static void staticFolder(final HttpServer server, final String dir)
    {
        try
        {
            final String clientDir = "/client";
            File[] files = new File(dir).listFiles();
            assert files != null;
            for(File file : files)
            {
                boolean isDir = file.isDirectory();
                if(isDir)
                {
                    staticFolder(server, file.getPath());
                }
                String path = file.getPath();
                if(!path.contains("index.html") && !isDir)
                {
                    server.createContext("/" + path.substring(clientDir.length(), path.indexOf('.')), new Router(path));
                }
                else if(path.contains("index.html"))
                {
                    server.createContext("/", new Router(path));
                }
            }
        }
        catch (Exception e)
        {
            error(e);
        }
    }

    public static JSONObject getEnv(final String path)
    {
        return new JSONObject(readFile(path));
    }

    //error handling for debugging
    public static void error(final Exception e)
    {
        System.err.println(e.getMessage());
        e.printStackTrace();
        System.exit(-1);
    }
}
