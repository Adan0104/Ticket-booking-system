package ticket.booking.system.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.system.entities.Ticket;
import ticket.booking.system.entities.Train;
import ticket.booking.system.entities.User;
import ticket.booking.system.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserBookingService {
    private User user;

    private List<User> userList;

    private static final String USERS_PATH = "../localDb/users.json";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public UserBookingService(User newUser) throws IOException {
        this.user = newUser;
        loadUsers();
    }

    public UserBookingService() throws IOException {
        loadUsers();
    }

    public void loadUsers() throws IOException {
        File users = new File(USERS_PATH);
        userList = objectMapper.readValue(users, new TypeReference<List<User>>() {});
    }

    public Boolean loginUser(){
        Optional<User> foundUser = userList.stream().filter(newUser -> {
            return newUser.getName().equals(user.getName()) && UserServiceUtil.checkPassword(user.getPassword(),newUser.getHashPassword());
        }).findFirst();
        return foundUser.isPresent();
    }

    public Boolean signUp(User newUser){
        try{
            userList.add(newUser);
            saveUserListToFile();
            return Boolean.TRUE;
        }
        catch(IOException ex){
            return Boolean.FALSE;
        }
    }

    public void saveUserListToFile() throws IOException {
        File usersFile = new File(USERS_PATH);
        objectMapper.writeValue(usersFile,usersFile);
    }

    public void fetchBooking(){
        user.printTickets();
    }

    public boolean cancelBooking(String ticketId){
        try {
            if(ticketId == null || ticketId.isEmpty()) return Boolean.FALSE;

            boolean removed = user.getTicketsBooked().removeIf(ticket -> ticket.getTicketId().equals(ticketId));

            if(removed){
                System.out.println("Ticket with ID " + ticketId + " has been canceled");
            }
            else{
                System.out.println("No ticket found with ID " + ticketId);
            }
            return removed;
        }
        catch(Exception ex){
            System.err.println("Error while cancelling booking: " + ex.getMessage());
            return false;
        }
    }

    public List<Train> getTrains(String source, String destination){
        try{
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source,destination);
        }
        catch(IOException ex){
            return new ArrayList<>();
        }
    }

    public List<List<Integer>> fetchSeats(Train train){
        return train.getSeats();
    }

    public Boolean bookTrainSeat(Train train,int row,int seat){
        try {
            TrainService trainService = new TrainService();
            List<List<Integer>> seats = train.getSeats();
            if (row >= 0 && row < seats.size() && seat >= 0 && seat < seats.get(row).size()){
                if(seats.get(row).get(seat) == 0){
                    seats.get(row).set(seat,1);
                    train.setSeats(seats);
                    trainService.addTrain(train);
                    return true;
                }
                else {
                    return false;
                }
            }
            else{
                return false;
            }
        }
        catch(IOException e){
            return Boolean.FALSE;
        }
    }

}
