package com.aliMezher.websocket.debugging.mongoConfiguration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

//import javax.annotation.PreDestroy;

@Configuration
@Slf4j
public class MongoShutdownConfig
//this class will enable the automatic deletion of the database when the application is stopped
{

    @Autowired
    private MongoClient mongoClient;
//    MongoClient is an injected bean by spring boot that represents the mongodb client (that is our mongodb instance that we will connect to in this project)

    @Value("${spring.data.mongodb.database}")
//    the above annotation will take the value of the property "spring.data.mongodb.database" from the application.properties file and store it in the variable "databaseName"
    private String databaseName;

    @PreDestroy
//    the above annotation means just before the stopping of the application, this method will be called
    public void dropDatabase() {
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        database.drop();
        log.info("Database '{}' dropped.", databaseName);
    }
}

