package ticket.booking.system.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ticket.booking.system.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TrainService {

    private List<Train> trainList;
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String TRAIN_DB_PATH = "../localDb/trains.json";

    public TrainService() throws IOException {
        File trains = new File(TRAIN_DB_PATH);
        trainList = objectMapper.readValue(trains, new TypeReference<List<Train>>() {});
    }

    private void saveTrainListToFile(){
        try{
            objectMapper.writeValue(new File(TRAIN_DB_PATH),trainList);
        } catch (IOException e) {
            System.err.println("Train details could not be saved");
            e.printStackTrace();
        }
    }

    public List<Train> searchTrains(String source, String destination) {
        return trainList.stream().filter(train -> validTrain(train, source, destination)).collect(Collectors.toList());
    }

    public boolean validTrain(Train train,String source,String destination){
        List<String> stationOrder = train.getStations();

        int sourceStation = stationOrder.indexOf(source);
        int destinationStation = stationOrder.indexOf(destination);

        return sourceStation != 1 && destinationStation != 1 && sourceStation < destinationStation;
    }
}
