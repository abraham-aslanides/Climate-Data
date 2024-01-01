import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Objects;

/**
 * Main class for the GlobalWeatherManager
 *
 * @author Abraham Aslanides
 * @version CSC 143 Spring 2023, 5/1/2023
 */
public class Main {

    /** Default Constructor */
    public Main() {}

    /**
     * Main method
     * @param args command line arguments
     * @throws FileNotFoundException if the file is not found
     */

    public static void main(String[] args) throws FileNotFoundException {

    //----------------------------------------------------------------------------------------------------------------//
    //  GlobalWeatherManager code
    //----------------------------------------------------------------------------------------------------------------//

        GlobalWeatherManager dataPoints = new GlobalWeatherManager(new File("raw_data/city_temperature.csv"));

        String outputFile = "cleaned_city_temperature.csv";

        File dir = new File("raw_data");
        if (dir.isDirectory() && dir.canRead()) {
            File[] files = Arrays.stream(Objects.requireNonNull(dir.listFiles()))
                    // Excludes hidden files
                    .filter(file -> !file.getName().equals("city_temperature.csv") && !file.getName().startsWith("."))
                    .toArray(File[]::new);
            if (files.length == 0) {
                System.out.println("No NOAA files to process");

            } else {
            // Now process additional NOAA temperature data files excluding the initial file
            System.out.println(Arrays.toString(files));
            dataPoints.processNOAATempData(files);
            }
        } else {
            System.out.println("The path provided does not denote a directory, or it cannot be read.");
        }

        // Replaces and populates the dataPoints ArrayList with WeatherReading objects for missing dates
        dataPoints.populateMissingDates();

        // Replaces all temperature readings of -99.0 with the linear regression slope for that city
        // This is done to avoid null values in the data, which are represented by -99.0
        dataPoints.replaceReadings();

        // Removes invalid data points from the dataPoints ArrayList
        dataPoints.removeInvalidReadings();

        // Writes the new data to a new file
        WeatherDataCSVWriter.writeToCSV(dataPoints.getReadings(0,dataPoints.getReadingCount()),
                "cleaned_city_temperature.csv");
    }
}
