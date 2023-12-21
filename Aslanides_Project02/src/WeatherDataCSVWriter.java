import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class WeatherDataCSVWriter {

    public static void writeToCSV(WeatherReading[] array, String fileName) {
        try (FileWriter fileWriter = new FileWriter(fileName);
             PrintWriter printWriter = new PrintWriter(fileWriter)) {

            // Write the header of the CSV file
            printWriter.println("Region,Country,State,City,Month,Day,Year,AvgTemperature");

            // Iterate over the array and write the data of each WeatherReading
            for (WeatherReading weatherReading : array) {
                // Ensure that weatherReading is not null to avoid NullPointerException
                if (weatherReading != null) {
                    // Create a CSV formatted string from the WeatherReading object's data
                    String line = String.format("%s,%s,%s,%s,%d,%d,%d,%.2f",
                            weatherReading.region(),
                            weatherReading.country(),
                            weatherReading.state(),
                            weatherReading.city(),
                            weatherReading.month(),
                            weatherReading.day(),
                            weatherReading.year(),
                            weatherReading.avgTemperature());

                    // Write the string to the file
                    printWriter.println(line);
                }
            }

            System.out.println("Data has been written to " + fileName);
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the CSV file: " + e.getMessage());
        }
    }
}

