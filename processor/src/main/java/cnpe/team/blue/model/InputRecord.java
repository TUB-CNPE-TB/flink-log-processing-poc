package cnpe.team.blue.model;

import java.io.Serializable;
import java.util.UUID;

public class InputRecord implements Serializable {

    private final String email;

    private final String id;

    private final String name;

    private final String lastName;

    private final String address;

    private final String phoneNumber;

    private final String requestService;

    public InputRecord(String email, String id, String name, String lastName, String address, String phoneNumber, String requestService) {
        this.email = email;
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.requestService = requestService;
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
}
