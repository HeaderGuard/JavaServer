package example;

import java.util.HashMap;

public class JSONObject extends HashMap<String, Object>
{
    public JSONObject()
    {
        super();
    }
    public JSONObject(String json)
    {
        super();
        parseJSON(json);
    }
    public void parseJSON(String json)
    {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < json.length(); ++i)
        {
            switch(json.charAt(i))
            {
                case '{':
                case '}':
                case ',':
                    break;
                case '\"':
                {
                    ++i;
                    while(json.charAt(i) != '\"')
                    {
                        builder.append(json.charAt(i));
                        ++i;
                    }
                    //skip the " and :
                    i+=2;
                    //Store and empty the string builder
                    String key = builder.toString();
                    builder.delete(0, builder.length());
                    if(json.charAt(i) == '\"')
                    {
                        ++i;
                    }
                    while(json.charAt(i) != '\"' && json.charAt(i) != ',' && json.charAt(i) != '}')
                    {
                        builder.append(json.charAt(i));
                        ++i;
                    }
                    if(json.charAt(i) == '\"')
                    {
                        ++i;
                    }
                    this.put(key, builder.toString());
                } break;
            }
            builder.delete(0, builder.length());
        }
    }
    public int getInt(String key)
    {
        return Integer.parseInt(this.getString(key));
    }
    public String getString(String key)
    {
        return this.get(key).toString();
    }
    @Override
    public String toString()
    {
        StringBuilder jsonString = new StringBuilder("{");
        for(String key : this.keySet())
        {
            jsonString.append("\"").append(key).append("\":\"").append(this.get(key)).append("\"").append(',');
        }
        jsonString.deleteCharAt(jsonString.length()-1);
        jsonString.append('}');
        return jsonString.toString();
    }
}