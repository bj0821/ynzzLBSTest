package com.ynzz.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

/**
 * Created by ynzz on 2017/8/29.
 */

public class JsonTest {
    public static void parseObject() throws Exception
    {
        String data = "{\"name\":\"jason\",\"age\":23,\"sex\":\"男\"}";
        Gson gson = new Gson();
        StringReader reader = new StringReader(data);
        JsonReader jsonReader = new JsonReader(reader);
        jsonReader.beginObject();
        while(jsonReader.hasNext())
        {
            String attribute = jsonReader.nextName();
            String value = jsonReader.nextString();
            System.out.println(attribute+" : "+value);
        }
        jsonReader.endObject();
        reader.close();
        jsonReader.close();
    }
    public static void parseArray() throws Exception
    {
        String data = "[{\"name\":\"jason\",\"age\":23,\"sex\":\"男\"}," +
                "{\"name\":\"uqgming\",\"age\":23,\"sex\":\"男\"} ]";
        Gson gson = new Gson();
        StringReader reader = new StringReader(data);
        JsonReader jsonReader = new JsonReader(reader);
        jsonReader.beginArray();
        while(jsonReader.hasNext())
        {
            jsonReader.beginObject();
            System.out.println("学生--------");
            while(jsonReader.hasNext())
            {
                String attribute = jsonReader.nextName();
                String value = jsonReader.nextString();
                System.out.println(attribute+" : "+value);
            }
            jsonReader.endObject();
        }
        jsonReader.endArray();
        reader.close();
        jsonReader.close();
    }

    public static String getStringToListObject(){

        String jsonStr = "";
        Student stuQugming = new Student("屈光明",31,'男');
        Student stup = new Student("jason",22,'男');

        List<Student> list = new ArrayList<Student>();
        list.add(stuQugming);
        list.add(stup);

        Gson gson = new Gson();
        jsonStr = gson.toJson(list,new TypeToken<List<Student>>() {
        }.getType());

        return jsonStr;

    }

    public static List<Student> getListObjectFromJson(String jsonStr)
    {
        List<Student> list = null;
        Gson gson = new Gson();
        list = gson.fromJson(jsonStr,new TypeToken<List<Student>>() {
        }.getType());

        return list;
    }

    public static void main(String[] args) {
        try {
            String json = getStringToListObject();
            List<Student> list = getListObjectFromJson(json);
            for(Student stu:list)
            {
                System.out.println(stu);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
