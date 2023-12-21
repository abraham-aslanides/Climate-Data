import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

/**
 * Main class for the GlobalWeatherManager
 *
 * @author Abraham Aslanides
 * @version CSC 143 Spring 2023, 5/1/2023
 */
public class Main {

    /**
     * Main method
     * @param args command line arguments
     * @throws FileNotFoundException if the file is not found
     */

    public static void main(String[] args) throws FileNotFoundException {

//--------------------------------------------------------------------------------------------------------------------//
//  GlobalWeatherManager code
//--------------------------------------------------------------------------------------------------------------------//

        GlobalWeatherManager dataPoints = new GlobalWeatherManager(new File("raw_data/city_temperature.csv"));

        // Replaces all temperature readings of -99.0 with the linear regression slope for that city
        dataPoints.replaceReadings();

//--------------------------------------------------------------------------------------------------------------------//
//  Debugging code
//--------------------------------------------------------------------------------------------------------------------//
//
//        // Gets the linear regression slope for Muscat, Oman for June 21st
//        System.out.println("Linear Regression Slope: ");
//        System.out.println(dataPoints.getTemperatureLinearRegressionSlope(dataPoints.getReadings(
//                dataPoints.getCityListStats("Oman", "", "Muscat").startingIndex(),
//                dataPoints.getCityListStats("Oman", "", "Muscat").count())));
//
//        // Prints the CityListStats for Muscat, Oman
//        System.out.println(dataPoints.getCityListStats("Oman", "", "Muscat"));
//
//        // Prints the WeatherReadings for Muscat, Oman for every summer solstice (June 21st)
//        System.out.println(Arrays.toString(dataPoints.getReadings(979559,8915, 6, 21)));
//
//--------------------------------------------------------------------------------------------------------------------//
//  Africa file cleaning code
//--------------------------------------------------------------------------------------------------------------------//

        // Adds new africa readings from the NOAA data
        WeatherReading[] africa = new ConvertReadings().convertReadings(new File("raw_data/africa_temperature.csv"));
        WeatherReading[] africa2 = new ConvertReadings().convertReadings(new File("raw_data/africa_temperature2.csv"));

        // Combines the two arrays and skips the null values
        WeatherReading[] africaNew = new WeatherReading[africa.length + africa2.length];
        int j = 0;
        for (WeatherReading reading : africa) {
            if (reading != null) {
                africaNew[j] = reading;
                j++;
            }
        }
        for (WeatherReading weatherReading : africa2) {
            if (weatherReading != null) {
                africaNew[j] = weatherReading;
                j++;
            }
        }

        // Check for null values in the new array and identify position
        for (int i = 0; i < africaNew.length; i++) {
            if (africaNew[i] == null) {
                System.out.println(i);
            }
        }

        // Adds the combined array to the GlobalWeatherManager
        dataPoints.addReadings(africaNew);

        // Writes the new Africa data to a new file
        WeatherDataCSVWriter.writeToCSV(africaNew, "africa_city_temperature.csv");

//--------------------------------------------------------------------------------------------------------------------//
//  North America file cleaning code
//--------------------------------------------------------------------------------------------------------------------//

        // Adds new northAmerica readings from the NOAA data
        WeatherReading[] northAmerica = new ConvertReadings().convertReadings(new File(
                "raw_data/north_america_temperature.csv"));
        WeatherReading[] northAmerica2 = new ConvertReadings().convertReadings(new File(
                "raw_data/north_america_temperature2.csv"));

        // Combines the two arrays and skips the null values
        WeatherReading[] northAmericaNew = new WeatherReading[northAmerica.length + northAmerica2.length];
        j = 0;
        for (WeatherReading reading : northAmerica) {
            if (reading != null) {
                northAmericaNew[j] = reading;
                j++;
            }
        }
        for (WeatherReading weatherReading : northAmerica2) {
            if (weatherReading != null) {
                northAmericaNew[j] = weatherReading;
                j++;
            }
        }

        // Check for null values in the new array and identify position
        for (int i = 0; i < northAmericaNew.length; i++) {
            if (northAmericaNew[i] == null) {
                System.out.println(i);
            }
        }

        // Adds the combined array to the GlobalWeatherManager
        dataPoints.addReadings(northAmericaNew);

        // Writes the new North America data to a new file
        WeatherDataCSVWriter.writeToCSV(northAmericaNew, "north_america_city_temperature.csv");
//--------------------------------------------------------------------------------------------------------------------//
//  South America file cleaning code
//--------------------------------------------------------------------------------------------------------------------//

        // Adds new South America readings from the NOAA data
        WeatherReading[] southAmerica = new ConvertReadings().convertReadings(new File("raw_data/south_america_temperature.csv"));
        WeatherReading[] southAmericaNew;
        try {
            WeatherReading[] southAmerica2 = new ConvertReadings().convertReadings(new File("raw_data/south_america_temperature2.csv"));

            // Combines the two arrays and skips the null values
            southAmericaNew = new WeatherReading[southAmerica.length + southAmerica2.length];
            j = 0;
            for (WeatherReading reading : southAmerica) {
                if (reading != null) {
                    southAmericaNew[j] = reading;
                    j++;
                }
            }
            for (WeatherReading weatherReading : southAmerica2) {
                if (weatherReading != null) {
                    southAmericaNew[j] = weatherReading;
                    j++;
                }
            }

            // Check for null values in the new array and identify position
            for (int i = 0; i < southAmericaNew.length; i++) {
                if (southAmericaNew[i] == null) {
                    System.out.println(i);
                }
            }
        } catch (FileNotFoundException e) {
            southAmericaNew = southAmerica;
        }

        // Adds the combined array to the GlobalWeatherManager
        dataPoints.addReadings(southAmericaNew);

        // Writes the new South America data to a new file
        WeatherDataCSVWriter.writeToCSV(southAmericaNew, "south_america_city_temperature.csv");


//--------------------------------------------------------------------------------------------------------------------//
//  Europe file cleaning code
//--------------------------------------------------------------------------------------------------------------------//

        // Adds new europe readings from the NOAA data
        WeatherReading[] europe = new ConvertReadings().convertReadings(new File("raw_data/europe_temperature.csv"));
        WeatherReading[] europeNew;
        try {
            WeatherReading[] europe2 = new ConvertReadings().convertReadings(new File("raw_data/europe_temperature2.csv"));

            // Combines the two arrays and skips the null values
            europeNew = new WeatherReading[europe.length + europe2.length];
            j = 0;
            for (WeatherReading reading : europe) {
                if (reading != null) {
                    europeNew[j] = reading;
                    j++;
                }
            }
            for (WeatherReading weatherReading : europe2) {
                if (weatherReading != null) {
                    europeNew[j] = weatherReading;
                    j++;
                }
            }

            // Check for null values in the new array and identify position
            for (int i = 0; i < europeNew.length; i++) {
                if (europeNew[i] == null) {
                    System.out.println(i);
                }
            }
        } catch (FileNotFoundException e) {
            europeNew = europe;
        }

        // Adds the combined array to the GlobalWeatherManager
        dataPoints.addReadings(europeNew);

        // Writes the new Europe data to a new file
        WeatherDataCSVWriter.writeToCSV(europeNew, "europe_city_temperature.csv");

//--------------------------------------------------------------------------------------------------------------------//
//  Asia file cleaning code
//--------------------------------------------------------------------------------------------------------------------//
        // Adds new Asia readings from the NOAA data
        WeatherReading[] asia = new ConvertReadings().convertReadings(new File("raw_data/asia_temperature.csv"));
        WeatherReading[] asiaNew;
        try {
            WeatherReading[] asia2 = new ConvertReadings().convertReadings(new File("raw_data/asia_temperature2.csv"));

            // Combines the two arrays and skips the null values
            asiaNew = new WeatherReading[asia.length + asia2.length];
            j = 0;
            for (WeatherReading reading : asia) {
                if (reading != null) {
                    asiaNew[j] = reading;
                    j++;
                }
            }
            for (WeatherReading weatherReading : asia2) {
                if (weatherReading != null) {
                    asiaNew[j] = weatherReading;
                    j++;
                }
            }

            // Check for null values in the new array and identify position
            for (int i = 0; i < asiaNew.length; i++) {
                if (asiaNew[i] == null) {
                    System.out.println(i);
                }
            }
        } catch (FileNotFoundException e) {
            asiaNew = asia;
        }

        // Adds the combined array to the GlobalWeatherManager
        dataPoints.addReadings(asiaNew);

        // Writes the new Asia data to a new file
        WeatherDataCSVWriter.writeToCSV(asiaNew, "asia_city_temperature.csv");

//--------------------------------------------------------------------------------------------------------------------//
//
//--------------------------------------------------------------------------------------------------------------------//
//
//        // Replaces and populates the dataPoints ArrayList with WeatherReading objects for missing dates
//        dataPoints.populateMissingDates();
//
//--------------------------------------------------------------------------------------------------------------------//
//  Debugging code
//--------------------------------------------------------------------------------------------------------------------//
//

        //Prints the cityListStats for the city of Ravnik, Slovenia
        System.out.println(dataPoints.getCityListStats("Slovenia", "", "Ravnik"));

//--------------------------------------------------------------------------------------------------------------------//
//  Final file writing code
//--------------------------------------------------------------------------------------------------------------------//

        // Writes the new data to a new file
        WeatherDataCSVWriter.writeToCSV(dataPoints.getReadings(0,dataPoints.getReadingCount()),
                "cleaned_city_temperature.csv");
    }
}
