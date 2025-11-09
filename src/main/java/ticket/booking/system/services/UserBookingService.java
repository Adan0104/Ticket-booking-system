package ticket.booking.system.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.system.entities.User;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserBookingService {
    private User user;

    private List<User> userList;

    private static final String USERS_PATH = "../localDb/users.json";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    UserBookingService(User newUser) throws IOException {
        this.user = newUser;
        File users = new File(USERS_PATH);
        userList = objectMapper.readValue(users,new TypeReference<List<User>>() {});
    }

    public Boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter(newUser -> {
            return newUser.getName().equals(user.getName()) && UserServiceUtil.getPassword(user.getPassword())
        }).findFirst();
        return foundUser.isPresent();
    }

}
