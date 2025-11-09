package ticket.booking.system.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.system.entities.Train;

import java.util.List;

public class TrainService {

    private List<Train> trainList;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String TRAIN_DB_PATH = "../localDb/trains.json";

    public TrainService(){
        File
    }
}
