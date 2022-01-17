package cnpe.team.blue.model;

import java.io.Serializable;

public class OutputRecord implements Serializable {

    private final String requestService;
    private final String responseService;
    private final String entity;

    private final boolean email;
    private final boolean name;
    private final boolean lastName;
    private final boolean phoneNumber;
    private final boolean address;
    private final boolean id;

    public OutputRecord(String requestService, String responseService, String entity, boolean email, boolean name, boolean lastName, boolean phoneNumber, boolean address, boolean id) {
        this.requestService = requestService;
        this.responseService = responseService;
        this.entity = entity;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.id = id;
    }

    public String getRequestService() {
        return requestService;
    }

    public String getResponseService() {
        return responseService;
    }

    public String getEntity() {
        return entity;
    }

    public boolean isEmail() {
        return email;
    }

    public boolean isName() {
        return name;
    }

    public boolean isLastName() {
        return lastName;
    }

    public boolean isPhoneNumber() {
        return phoneNumber;
    }

    public boolean isAddress() {
        return address;
    }

    public boolean isId() {
        return id;
    }

    @Override
    public String toString() {
        return "{" +
                "requestService:" + requestService +
                ", responseService:" + responseService +
                ", entity:" + entity +
                ", email:" + email +
                ", name:" + name +
                ", lastName:" + lastName +
                ", phoneNumber:" + phoneNumber +
                ", address:" + address +
                ", id:" + id +
                '}';
    }
}
