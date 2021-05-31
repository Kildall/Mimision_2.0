package com.kildall.mimision2.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kildall.mimision2.Mimision2;
import org.bukkit.ChatColor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class Messages {
    private static final File messagesDir = Mimision2.getInstance().getDataFolder();
    private static final File messagesFile = new File(Mimision2.getInstance().getDataFolder(), "messages.yml");
    private static JSONObject messages = new JSONObject();

    //Lee el archivo de mensajes y devuelve JSONObject con los contenidos
    private static JSONObject getAllMessages() {
        //Si el archivo ya fue leido
        if(!messages.isEmpty()){
            return (JSONObject) messages.get("mensajes");
        }
        //Si el archivo existe
        if (messagesFileExists()) {
            //JSON parser (Permite leer el archivo)
            JSONParser jsonParser = new JSONParser();

            //Si se puede leer el archivo
            try (FileReader messagesContent = new FileReader(messagesFile)) {

                //Leer y devolver el JSONObject
                JSONObject rawMessages = (JSONObject) jsonParser.parse(messagesContent);
                //Guardar objeto
                messages = rawMessages;

                return (JSONObject) rawMessages.get("mensajes");

            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        } else {
            createDefaultFile();

        }
        return null;
    }
    public static String getMessage(String key) {

        //Si los mensajes ya fueron obtenidos
        if(!messages.isEmpty()){
            JSONObject tempList = (JSONObject) messages.get("mensajes");
            return tempList.get(key).toString();
        }

        //Si el archivo existe
        if (messagesFileExists()) {
            //JSON parser (Permite leer el archivo)
            JSONParser jsonParser = new JSONParser();

            //Si se puede leer el archivo
            try (FileReader messagesContent = new FileReader(messagesFile)) {

                //Leer y devolver el JSONObject
                JSONObject rawMessages = (JSONObject) jsonParser.parse(messagesContent);

                JSONObject messages = (JSONObject) rawMessages.get("mensajes");

                return colorize(messages.get(key).toString());

            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        } else {
            createDefaultFile();

        }
        return null;
    }

    public static void setMessage(String key, String content){
        JSONObject prevMessages = getAllMessages();

        if (prevMessages == null || !prevMessages.containsKey(key)){
            return;
        }

        JSONObject newMessages = (JSONObject) prevMessages.put(key,content);
        messages.put(key,content);

        try(FileWriter file = new FileWriter(messagesFile)){

            file.write(newMessages.toJSONString());

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void createDefaultFile(){

        //Default Values
        JSONObject messages = new JSONObject();
        messages.put("Voto Eliminado", "&cSe ha eliminado tu voto.");
        messages.put("Voto", "&6Lindo boto uwu");
        messages.put("Prefijo", "&l&3Mimision » ");
        messages.put("Durmio Bien", "&l&dHas realizado la mimision uwu");
        messages.put("Dormir Cancelado", "&cKpo anda a mimir, no rompas los huevos.");
        messages.put("Jugador AFK", "l&4se fue a cagar uwu");
        messages.put("Jugador no AFK", "&l&dte extrañamos bb");
        messages.put("Votacion Repetida", "&cNo sos tan importante como para votar dos veces pt");
        messages.put("Iniciar Votacion", "&6Veo que tenes Noni");
        messages.put("Votar Muchos", "&l&dVamos a mimir? Uwu");
        messages.put("Votar Pocos", "&l&dVamos a mimir? Podemos hacerlo pegaditos Uwu");
        messages.put("Es De Dia", "&cFlaco sos boludo o masticas vidrio no ves que es de dia todavia.");
        messages.put("Necesitas Cama", "&cTodo bien pero donde pensas dormir si no tenes cama");
        messages.put("Error al Cancelar", "&cNo podes cancelar algo que no existe. Asi como el amor de tu vieja");
        messages.put("Titulo AFK", "&5Las personas que estan AFK ahora mismo son: ");
        messages.put("No hay AFKs", "&cActualmente no hay nadie afk");
        messages.put("No Existe", "&cEste jugador no existe");

        //Parent value
        JSONObject parent = new JSONObject();
        parent.put("mensajes", messages);

        System.out.println(messagesFile.toString());
        //Intentar crear el archivo, y sin importar el resultado, escribir los contenidos.
        try{
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            messagesDir.mkdirs();
            messagesFile.createNewFile();
            FileWriter file = new FileWriter(messagesFile);
            file.write(gson.toJson(parent));
            file.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static boolean messagesFileExists(){
        if(messagesFile.exists()){
            return true;
        } else {
            return false;
        }
    }

    private void reloadMessages(){
        messages = new JSONObject();
        getAllMessages();
    }

    private static String colorize(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
