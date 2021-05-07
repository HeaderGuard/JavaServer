package example;

import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

public class Utils
{
    public static <Stream> String readFile(final Stream stream)
    {
        //Read the file line by line
        StringBuilder code = new StringBuilder();
        try
        {
            Scanner reader = null;
            if(stream instanceof File)
            {
                reader = new Scanner((File) stream);
            }
            else if(stream instanceof InputStream)
            {
                reader = new Scanner((InputStream) stream);
            }
            while(true)
            {
                assert reader != null;
                if (!reader.hasNextLine()) break;
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
        return new JSONObject(readFile(new File(path)));
    }

    //error handling for debugging
    public static void error(final Exception e)
    {
        System.err.println(e.getMessage());
        e.printStackTrace();
        System.exit(-1);
    }
}
