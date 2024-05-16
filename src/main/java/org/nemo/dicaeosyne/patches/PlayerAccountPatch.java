package org.nemo.dicaeosyne.patches;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.nemo.dicaeosyne.Dicaeosyne;
import org.nemo.dicaeosyne.commands.CommandLogin;

import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;

public class PlayerAccountPatch {
    public PlayerAccountPatch() {
        java.util.logging.Logger.getLogger("org.mongodb").setLevel(Level.SEVERE);

        try {
            String uri = (String) ((JSONObject) (new JSONParser()).parse(new FileReader(".\\auth.json"))).get("uri");

            try (MongoClient mongoClient = MongoClients.create(uri)) {
                MongoDatabase database = mongoClient.getDatabase("dicaeosyne");
                MongoCollection<Document> collection = database.getCollection("accounts");

            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        Dicaeosyne.Plugin.getCommand("login").setExecutor(new CommandLogin());
    }
}
