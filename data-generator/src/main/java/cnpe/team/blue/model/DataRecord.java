package cnpe.team.blue.model;

import java.io.Serializable;
import java.util.Random;
import java.util.UUID;

public class DataRecord implements Serializable {

    private final String email;

    private final String id;

    private final String name;

    private final String lastName;

    private final String address;

    private final String phoneNumber;

    private final String requestService;

    public DataRecord() {
        this.email = getRandomString();
        this.id = getRandomString();
        this.name = getRandomString();
        this.lastName = getRandomString();
        this.address = getRandomString();
        this.phoneNumber = getRandomString();
        this.requestService = getRandomRequestService();
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getRequestService() {
        return requestService;
    }

    private String getRandomRequestService() {
        return "newsletterService";
    }

    private String getRandomString() {
        Random randomGenerator = new Random();
        if (randomGenerator.nextBoolean()) {
            return UUID.randomUUID().toString();
        }
        else {
            return "";
        }
    }
}
