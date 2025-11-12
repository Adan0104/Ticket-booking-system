package ticket.booking.system;

import ticket.booking.system.entities.Train;
import ticket.booking.system.entities.User;
import ticket.booking.system.services.UserBookingService;
import ticket.booking.system.util.UserServiceUtil;

import java.io.IOException;
import java.util.*;

public class App {
    public static void main(String[] args) {

        System.out.println("Running Train Booking System");
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        UserBookingService userBookingService;
        try{
            userBookingService = new UserBookingService();
        }
        catch(IOException ex){
            System.out.println("There is something wrong " + ex.getMessage());
            return;
        }
        Train trainSelectedForBooking = null;
        while(option != 7){
            System.out.println("Choose an option:");
            System.out.println("1. Sign up");
            System.out.println("2. Log in");
            System.out.println("3. Fetch bookings");
            System.out.println("4. Search trains");
            System.out.println("5. Book a seat");
            System.out.println("6. Cancel my booking");
            System.out.println("7. Exit the app");
            option = scanner.nextInt();
            switch (option){
                case 1:
                    System.out.println("Enter your name to sign up:");
                    String nameToSignUp = scanner.next();
                    System.out.println("Enter the password to sign up:");
                    String passwordToSignUp = scanner.next();
                    User userToSignUp = new User(nameToSignUp,
                            UserServiceUtil.hashPassword(passwordToSignUp),
                            new ArrayList<>(), UUID.randomUUID().toString());
                    userBookingService.signUp(userToSignUp);
                    break;

                case 2:
                    System.out.println("Enter the username to login:");
                    String nameToLogin = scanner.next();
                    System.out.println("Enter the password to login:");
                    String passwordToLogin = scanner.next();

                    boolean loginSuccessful = userBookingService.loginUser(nameToLogin, passwordToLogin);
                    if (loginSuccessful) {
                        System.out.println("Login successful!");
                    } else {
                        System.out.println("Login failed: Invalid credentials");
                    }
                    break;

                case 3:
                    System.out.println("Fetching your bookings:");
                    userBookingService.fetchBooking();
                    break;

                case 4:
                    System.out.println("Type your source station:");
                    String source = scanner.next();
                    System.out.println("Type your destination station:");
                    String dest = scanner.next();
                    List<Train> trains = userBookingService.getTrains(source,dest);

                    if(trains.isEmpty()){
                        System.out.println("No trains found for this route");
                        break;
                    }
                    int index = 1;
                    for(Train t : trains){
                        System.out.println(index + " Train id: " + t.getTrainId());
                        for(Map.Entry<String, String> entry : t.getStationTimes().entrySet()){
                            System.out.println("Station: " + entry.getKey() + " Time: " + entry.getValue());
                        }
                        index += 1;
                    }
                    System.out.println("0. None");
                    System.out.println("Select train option:");
                    int choice = scanner.nextInt();

                    if(choice == 0){
                        System.out.println("No train selected");
                        trainSelectedForBooking = null;
                    }
                    else if(choice > 0 && choice <= trains.size()){
                        trainSelectedForBooking = trains.get(choice - 1);
                        System.out.println("Train selected:");
                        System.out.println(trainSelectedForBooking.getTrainInfo());
                    }
                    break;

                case 5:
                    if(trainSelectedForBooking == null){
                        System.out.println("Please select a train first");
                        break;
                    }

                    System.out.println("Select a seat out of these seats:");
                    List<List<Integer>> seats = userBookingService.fetchSeats(trainSelectedForBooking);
                    for(List<Integer> row : seats){
                        for(Integer val : row){
                            System.out.print(val + " ");
                        }
                        System.out.println();
                    }
                    System.out.println("Select the seat by typing the row and column");
                    System.out.println("Enter the row");
                    int row = scanner.nextInt();
                    System.out.println("Enter the column");
                    int col = scanner.nextInt();
                    System.out.println("Booking your seat....");
                    Boolean booked = userBookingService.bookTrainSeat(trainSelectedForBooking, row, col);
                    if(booked.equals(Boolean.TRUE)){
                        System.out.println("Booked! Enjoy your journey");
                    }
                    else{
                        System.out.println("Can't book this seat");
                    }
                    break;

                case 6:
                    System.out.println("Enter ticket ID:");
                    String cancelTicketId = scanner.next();
                    userBookingService.cancelBooking(cancelTicketId);
                    break;

                case 7:
                    System.out.println("Exiting... Thank you for using the Train Booking System!");
                    break;

                default:
                    System.out.println("Invalid input. Exiting...");
                    break;
            }
        }
    }
}