import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        try {
            Properties config = loadConfig("C:\\Users\\liais\\workspace\\sd-back\\ds2023_30242_lupascu_liana_2_backend\\csvreader\\csvreader\\src\\main\\resources\\config.properties");
            Integer deviceId = Integer.parseInt(config.getProperty("device.id"));

            FileReader filereader = new FileReader("C:\\Users\\liais\\workspace\\sd-back\\ds2023_30242_lupascu_liana_2_backend\\csvreader\\sensor.csv");
            MessageSender messageSender = new MessageSender();
            CSVReader csvReader = new CSVReader(filereader);
            String[] nextRecord;
            Integer minutes=0;
            Integer hours = 0;
            Integer day= 1;
            while ((nextRecord = csvReader.readNext()) != null) {
                for (String cell : nextRecord) {
                    Double energyConsumption = Double.valueOf(cell);
                    minutes+=10;
                    if(minutes==60){
                        hours++;
                        minutes=0;
                    }
                    if(hours==24){
                        day++;
                        hours=0;
                    }
                    LocalDateTime timestamp = LocalDateTime.of(2023, 1, day, hours, minutes, 0);
                    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                    String date = timestamp.format(formatter);

                    String message = energyConsumption +"," + date + "," + deviceId;
                    messageSender.sendMessage(message);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Properties loadConfig(String filePath) throws IOException {
        Properties properties = new Properties();
        try (FileReader fileReader = new FileReader(filePath)) {
            properties.load(fileReader);
        }
        return properties;
    }
}
