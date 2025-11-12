package ticket.booking.system.entities;

import java.util.List;

public class User {
    private String name;
    private String hashPassword;
    private List<Ticket> ticketsBooked;
    private String userId;

    public User(){}

    public User(String name, String hashPassword, List<Ticket> ticketsBooked, String userId) {
        this.name = name;
        this.hashPassword = hashPassword;
        this.ticketsBooked = ticketsBooked;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public List<Ticket> getTicketsBooked() {
        return ticketsBooked;
    }

    public void setTicketsBooked(List<Ticket> ticketsBooked) {
        this.ticketsBooked = ticketsBooked;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void printTickets() {
        if (ticketsBooked == null || ticketsBooked.isEmpty()) {
            System.out.println("No tickets booked yet.");
            return;
        }

        for (Ticket ticket : ticketsBooked) {
            if (ticket != null) {
                System.out.println(ticket.getTicketInfo());
            }
        }
    }
}
