package ticket.booking.system.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.system.entities.Ticket;
import ticket.booking.system.entities.Train;
import ticket.booking.system.entities.User;
import ticket.booking.system.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class UserBookingService {
    private User user;

    private List<User> userList;

    private static final String USERS_PATH = "src/main/java/ticket/booking/system/localDb/users.json";

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

    public Boolean loginUser(String userName,String password){
        Optional<User> foundUser = userList.stream().filter(u -> u.getName().equals(userName)).findFirst();
        if(foundUser.isPresent() && UserServiceUtil.checkPassword(password,foundUser.get().getHashPassword())){
            this.user = foundUser.get();
            return true;
        }
        else{
            return false;
        }
    }

    public Boolean signUp(User newUser){
        try{
            boolean exists = userList.stream()
                    .anyMatch(u -> u.getName().equals(newUser.getName()));

            if (exists) {
                System.out.println("Username already taken. Please choose another.");
                return false;
            }

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
        objectMapper.writeValue(usersFile,userList);
    }

    public void fetchBooking() {
        if (user == null) {
            System.out.println("No user logged in. Please login first.");
            return;
        }
        user.printTickets();
    }

    public boolean cancelBooking(String ticketId){
        try{
            if (ticketId == null || ticketId.isEmpty()) return false;

            Ticket ticketToCancel = user.getTicketsBooked().stream()
                    .filter(ticket -> ticket.getTicketId().equals(ticketId))
                    .findFirst()
                    .orElse(null);

            if (ticketToCancel == null) {
                System.out.println("No ticket found with ID " + ticketId);
                return false;
            }

            boolean removed  = user.getTicketsBooked().remove(ticketToCancel);
            if(!removed) return false;

            TrainService trainService = new TrainService();

            List<Train> trains = trainService.getTrainList();

            Train bookedTrain = trains.stream()
                    .filter(train -> train.getTrainId().equals(ticketToCancel.getTrain().getTrainId()))
                    .findFirst()
                    .orElse(null);

            if(bookedTrain != null){
                List<List<Integer>> seats = bookedTrain.getSeats();
                int row = ticketToCancel.getRow();
                int col = ticketToCancel.getCol();
                seats.get(row).set(col,0);
            }
            else{
                System.out.println("Train not found for ticket ID: " + ticketId);
                return false;
            }
            return false;
        }
        catch(Exception ex) {
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

    public Boolean bookTrainSeat(Train train, int row, int col) {
        try {
            List<List<Integer>> seats = train.getSeats();
            if (row < 0 || row >= seats.size() || col < 0 || col >= seats.get(row).size()) {
                System.out.println("Invalid seat position!");
                return false;
            }

            if (seats.get(row).get(col) == 1) {
                System.out.println("Seat already booked!");
                return false;
            }

            seats.get(row).set(col, 1);

            String source = train.getStations().get(0);
            String destination = train.getStations().get(train.getStations().size() - 1);

            Ticket newTicket = new Ticket(
                    UUID.randomUUID().toString(),
                    this.user.getUserId(),
                    source,
                    destination,
                    new Date().toString(),
                    train
            );

            newTicket.setRow(row);
            newTicket.setCol(col);

            user.getTicketsBooked().add(newTicket);

            for (int i = 0; i < userList.size(); i++) {
                if (userList.get(i).getUserId().equals(user.getUserId())) {
                    userList.set(i, user);
                    break;
                }
            }

            saveUserListToFile();
            TrainService trainService = new TrainService();
            trainService.updateTrain(train);
            System.out.println("Seat booked successfully!");
            return true;

        } catch (IOException e) {
            System.out.println("Error while booking: " + e.getMessage());
            return false;
        }
    }

}
