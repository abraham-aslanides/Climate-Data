import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


/**
 * GlobalWeatherManager class implements the GlobalWeatherManagerInterface and Iterable interfaces
 * to manage a list of WeatherReading objects. The class also contains methods to retrieve data sets
 * by city, state, and country. It also calculates the linear regression slope for a given data set.
 *
 * @author Abraham Aslanides
 * @version CSC 143 Spring 2023, 5/1/2023
 */
public class GlobalWeatherManager implements GlobalWeatherManagerInterface, Iterable<WeatherReading>{

    /** ArrayList of WeatherReading objects */
    private ArrayList<WeatherReading> dataPoints;
    private ArrayList<PrecipitationReading> precipPoints;

    /**
     * Constructor for the GlobalWeatherManager object
     * @param tempFile  the temperature readings file to be read
     *
     * @throws FileNotFoundException if the file is not found
     */
    public GlobalWeatherManager(File tempFile) throws FileNotFoundException{

        Scanner input;

        input = new Scanner(tempFile).useDelimiter( ",|\\R" );

        dataPoints = new ArrayList<>();
        input.nextLine();
        while (input.hasNext()){
            dataPoints.add(new WeatherReading(input.next(),input.next(),input.next(),input.next(),
                    input.nextInt(),input.nextInt(),input.nextInt(),input.nextDouble()));
        }
        Collections.sort(dataPoints);

        input.close();
    }

    /**
     * Constructor for the GlobalWeatherManager object
     * @param tempFile      the temperature readings file to be read
     * @param precipFile    the precipitation readings file to be read
     *
     * @throws FileNotFoundException if either file is not found
     */
    public GlobalWeatherManager(File tempFile, File precipFile) throws FileNotFoundException{

        Scanner input;

        input = new Scanner(tempFile).useDelimiter( ",|\\R" );

        dataPoints = new ArrayList<>();
        input.nextLine();
        while (input.hasNext()){
            dataPoints.add(new WeatherReading(input.next(),input.next(),input.next(),input.next(),
                    input.nextInt(),input.nextInt(),input.nextInt(),input.nextDouble()));
        }
        Collections.sort(dataPoints);

        input.close();

        input = new Scanner(precipFile).useDelimiter( ",|\\R" );

        precipPoints = new ArrayList<>();

        //TODO: add code to read precipitation files

    }
    /**
     * Retrieves a count of readings
     *
     * @return count of readings
     */
    @Override
    public int getReadingCount() {

        return dataPoints.size();
    }

    /**
     * Retrieves the weather reading at the specified index.
     *
     * @param index the index for the desired reading; must be a valid element index.
     * @return the reading at the specified index.
     */
    @Override
    public WeatherReading getReading(int index) {

        return dataPoints.get(index);
    }

    /**
     * Retrieves a set of weather readings.
     *
     * @param index the index of the first reading; must be a valid index.
     * @param count the count of readings to potentially include.  Must be at least 1.  Must imply a valid range;
     *              index + count must be less than the total reading count.
     * @return an array of readings.
     */
    @Override
    public WeatherReading[] getReadings(int index, int count) {
        WeatherReading[] readings;
        if (count > 0 && index >= 0 && index + count <= this.getReadingCount()) {
            readings = new WeatherReading[count];

            // populates the array with the readings
            for (int position = 0; position < count; position++) {
                readings[position] = this.getReading(index + position);
            }
        } else {
            readings = new WeatherReading[0];
        }
        return readings;
    }

    /**
     * Retrieves a set of weather readings.
     *
     * @param index the index of the first reading.
     * @param count the count of readings to check for potential inclusion.  Must be at least 1.
     *              Must imply a valid range; index + count must be less than the total reading count.
     * @param month the month to filter; must be a valid month (1 to 12).
     * @param day   the day to filter; must be a valid day (1 to 31).
     * @return an array of readings matching the specified criteria.  Length will usually be smaller than
     * the count specified as a parameter, as each year will only have one matching day.
     */

    @Override
    public WeatherReading[] getReadings(int index, int count, int month, int day) {
        ArrayList<WeatherReading> filteredReadings = new ArrayList<>();

        for (int i = index; i < Math.min(index + count, this.getReadingCount()); i++) {
            WeatherReading reading = this.getReading(i);
            if (reading.month() == month && reading.day() == day) {
                filteredReadings.add(reading);
            }
        }

        return filteredReadings.toArray(new WeatherReading[0]);
    }

    /**
     * Adds Weather Readings to the dataPoints ArrayList
     * Ideally, this method should check for the following, but it is not required:
     *                  Must not contain readings with the same date and location as existing readings.
     *                  Must not contain readings with the same date and location as other readings in the parameter.
     * @param readings  the WeatherReading objects to be added
     *                  to the dataPoints ArrayList
     *                  Must not be null.
     *                  Must not contain null elements.
     *                  Must not contain duplicates.
     */

    public void addReadings(WeatherReading[] readings) {
        if (readings == null) {
            throw new IllegalArgumentException("Readings must not be null.");
        }

        Set<WeatherReading> uniqueReadings = new HashSet<>();

        for (WeatherReading reading : readings) {
            if (reading == null) {
                throw new IllegalArgumentException("Readings must not contain null elements.");
            }
            //If the reading is a duplicate, it will not be added to the set, and we skip it
            uniqueReadings.add(reading);
        }

        dataPoints.addAll(uniqueReadings);
        Collections.sort(dataPoints);
    }

    /**
     * Replaces and populates the dataPoints ArrayList with WeatherReading objects for missing dates
     * (01/01/1995 - 12/31/2019) and fills them with the average temperature for that city for that day and month.
     */

    //entries should have consecutive dates
    //Australia/South Pacific,Papua New Guinea,,Momote,1,1,1997,78.00
    //Australia/South Pacific,Papua New Guinea,,Momote,1,2,1997,78.00
    //Australia/South Pacific,Papua New Guinea,,Momote,1,3,1997,80.00

    public void populateMissingDates() {
        // Assuming dataPoints is already sorted by date and then city
        Set<String> existingDates = new HashSet<>();
        for (WeatherReading dataPoint : dataPoints) {
            String dateKey = dataPoint.year() + "-" + dataPoint.month() + "-" + dataPoint.day() + "-" + dataPoint.city();
            existingDates.add(dateKey);
        }

        ArrayList<WeatherReading> newReadings = new ArrayList<>();
        for (int year = 1995; year <= 2019; year++) {
            for (int month = 1; month <= 12; month++) {
                for (int day = 1; day <= getDaysInMonth(month, year); day++) {
                    Set<String> cityKeys = getAllCitiesForDate(year, month, day);
                    for (String cityKey : cityKeys) {
                        String[] parts = cityKey.split("-");
                        String country = parts[0];
                        String state = parts[1];
                        String city = parts[2];

                        String dateCityKey = year + "-" + month + "-" + day + "-" + city;
                        if (!existingDates.contains(dateCityKey)) {
                            CityListStats stats = getCityListStats(country, state, city);
                            if(stats != null) {
                                WeatherReading[] readingsForStats =
                                        getReadings(stats.startingIndex(), stats.count(), month, day);

                                double avgTemp = calculateAverageTemperature(readingsForStats);

                                WeatherReading newReading =
                                        new WeatherReading(
                                                ConvertReadings.returnRegion(country),
                                                country,
                                                state,
                                                city,
                                                month,
                                                day,
                                                year,
                                                avgTemp);
                                newReadings.add(newReading);
                                existingDates.add(dateCityKey);
                            }
                        }
                    }
                }
            }
        }
        dataPoints.addAll(newReadings);
        Collections.sort(dataPoints);
    }

    private Set<String> getAllCitiesForDate(int year, int month, int day) {
        Set<String> cityKeys = new HashSet<>();
        for (WeatherReading dataPoint : dataPoints) {
            if (dataPoint.year() == year && dataPoint.month() == month && dataPoint.day() == day) {
                String key = dataPoint.country() + "-" + dataPoint.state() + "-" + dataPoint.city();
                cityKeys.add(key);
            }
        }
        return cityKeys;
    }



    /**
     * Retrieves key list statistics for the specified country/state/city.
     * Student note:  okay to use an additional ArrayList in this method.
     *
     * @param country the country of interest; must not be null or blank.
     * @param state   the state of interest; must not be null.
     * @param city    the city of interest; must not be null or blank.
     * @return the list stats for the specified city, or null if not found.
     */
    @Override
    public CityListStats getCityListStats(String country, String state, String city) {

        int min = 0;
        int max = dataPoints.size() - 1;

        while (min <= max) {
            int mid = (max + min) / 2;
            if (dataPoints.get(mid).country().equals(country)){ // found country
                int modifier = 1;
                while (mid + modifier <= max && dataPoints.get(mid + modifier).country().equals(country)){
                    modifier++;
                }
                max = mid + modifier - 1;
                modifier = 1;
                while (mid - modifier >= min && dataPoints.get(mid - modifier).country().equals(country)){
                    modifier++;
                }
                min = mid - modifier + 1;
                mid = (max + min) / 2;
                if (dataPoints.get(mid).state().equals(state)) {
                    modifier = 1;
                    while (mid + modifier <= max && dataPoints.get(mid + modifier).state().equals(state)){
                        modifier++;
                    }
                    max = mid + modifier - 1;
                    modifier = 1;
                    while (mid - modifier >= min && dataPoints.get(mid - modifier).state().equals(state)){
                        modifier++;
                    }
                    min = mid - modifier + 1;
                    mid = (max + min) / 2;
                    if (dataPoints.get(mid).city().equals(city)) {

                        HashSet<Integer> uniqueYears = new HashSet<>();
                        for (int index = 0; index < max - min + 1; index++) {
                            int year = dataPoints.get(min + index).year();
                            uniqueYears.add(year);
                        }
                        int[] uniqueYearsArray = new int[uniqueYears.size()];
                        int index = 0;
                        for (int year : uniqueYears) {
                            uniqueYearsArray[index++] = year;
                        }
                        return new CityListStats(min, max - min + 1, uniqueYearsArray);

                    } else if (dataPoints.get(mid).city().compareTo(city) < 0){
                        min = mid + 1; // too small
                    } else if (dataPoints.get(mid).city().compareTo(city) > 0){
                        max = mid - 1; // too large
                    }
                } else if (dataPoints.get(mid).state().compareTo(state) < 0){
                    min = mid + 1; // too small
                } else if (dataPoints.get(mid).state().compareTo(state) > 0){
                    max = mid - 1; // too large
                }
            } else if (dataPoints.get(mid).country().compareTo(country) < 0){
                min = mid + 1; // too small
            } else if (dataPoints.get(mid).country().compareTo(country) > 0){
                max = mid - 1; // too large
            }
        }

        return null;
    }


    /**
     * Retrieves an iterator over all weather readings.
     *
     * @return strongly typed iterator for.
     */
    @Override
    public Iterator<WeatherReading> iterator() {
        return new Iterator<>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {

                return currentIndex < dataPoints.size();
            }

            @Override
            public WeatherReading next() {

                return dataPoints.get(currentIndex++);
            }

            @Override
            public void remove() {

                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * Does a linear regression analysis on the data, using x = year and y = temperature.
     * Calculates the slope of a best-fit line using the Least Squares method.   For more information
     * on that method, see <a href="https://www.youtube.com/watch?v=P8hT5nDai6A">...</a>
     * Student note:  okay to use two additional ArrayLists in this method.
     *
     * @param readings array of readings to analyze.  Should typically be readings for a single day over
     *                 a number of years; larger data sets will likely yield better results.  Ignores
     *                 temperature data of -99.0, a default value indicating no temperature data was present.
     *                 Must not be null and must contain at least two readings.
     * @return slope of best-fit line; positive slope indicates increasing temperatures.
     */
    @Override
    public double getTemperatureLinearRegressionSlope(WeatherReading[] readings) {
        if (readings == null || readings.length < 1) {
            throw new IllegalArgumentException("Readings must not be null and must contain at least two readings.");
        }
        // count number of -99.0 readings (no data)
        int ignores = 0;
        for (WeatherReading reading : readings) {
            if (reading.avgTemperature() == -99.0) {
                ignores++;
            }
        }
        // all readings are -99.0, so no data
        if (ignores == readings.length) {
            return -99.0;
        }

        Integer[] years = new Integer[readings.length - ignores];
        Double[] temperatures = new Double[readings.length - ignores];

        // populate years and temperatures arrays from readings
        int index = 0;
        for (WeatherReading reading : readings) {
            if (reading.avgTemperature() != -99.0) {
                years[index] = reading.year();
                temperatures[index] = reading.avgTemperature();
                index++;
            }
        }

        return calcLinearRegressionSlope(years, temperatures);
    }

    /**
     * Calculates the slope of the best-fit line calculated using the Least Squares method.  For more information
     * on that method, see <a href="https://www.youtube.com/watch?v=P8hT5nDai6A">...</a>
     *
     * @param years         an array of x values; must not be null and must contain at least two elements.
     * @param temperatures  an array of y values; must be the same length as the x array and must not be null.
     * @return the slope of the best-fit line
     */
    @Override
    public double calcLinearRegressionSlope(Integer[] years, Double[] temperatures) {
        if (years == null || temperatures == null || years.length < 2 || temperatures.length < 2) {
            throw new IllegalArgumentException("Arrays must not be null and must contain at least two elements");
        }
        if (years.length != temperatures.length) {
            throw new IllegalArgumentException("Arrays must have same length");
        }

        double sumYears = 0.0, sumY = 0.0, sumYearsY = 0.0, sumYearsYears = 0.0;

        for (int i = 0; i < years.length; i++) {
            sumYears += years[i];
            sumY += temperatures[i];
            sumYearsY += years[i] * temperatures[i];
            sumYearsYears += years[i] * years[i];
        }

        return (years.length * sumYearsY - sumYears * sumY) / (years.length * sumYearsYears - sumYears * sumYears);
    }

    /**
     * Replaces all temperature readings of -99.0 with the average for that city for that day and month.
     * All WeatherReadings[] in the dataPoints ArrayList are iterated through and if the avgTemperature is -99.0,
     * the temperature is replaced with the average for that city.
     * Note: This method can take 2-5 minutes to run, depending on your hardware.
     */

    public void replaceReadings() {
        for (WeatherReading dataPoint : dataPoints) {
            if (dataPoint.avgTemperature() == -99.0) {
                CityListStats stats = getCityListStats(dataPoint.country(), dataPoint.state(), dataPoint.city());
                if (stats != null) {
                    int index = stats.startingIndex();
                    int count = stats.count();

                    // Filter readings by month and day
                    WeatherReading[] sameDateReadings = getReadings(index, count, dataPoint.month(), dataPoint.day());
                    double avgTemp = calculateAverageTemperature(sameDateReadings);

                    if (avgTemp == -99.0) {
                        // If no valid average, try getting average for the entire month
                        WeatherReading[] sameMonthReadings = getReadings(index, count, dataPoint.month(), 1);
                        avgTemp = calculateAverageTemperature(sameMonthReadings);

                        // If still no valid average, try getting the temperature of the next day
                        if (avgTemp == -99.0) {
                            avgTemp = getTemperatureOfNextDay(dataPoint, index, count);
                        }
                    }

                    if (avgTemp != -99.0) {
                        dataPoint.setAvgTemperature(avgTemp);
                    }
                }
            }
        }
    }

    /**
     * Gets the temperature of the next day
     * @param dataPoint the WeatherReading object to get the next day for
     * @param index     the starting index to search from
     * @param count     the number of readings to search through
     * @return the temperature of the next day, or -99.0 if not found
     */

    private double getTemperatureOfNextDay(WeatherReading dataPoint, int index, int count) {
        int nextDay = dataPoint.day() + 1;
        int nextMonth = dataPoint.month();
        int nextYear = dataPoint.year();

        // Adjust for end of month
        if (nextDay > getDaysInMonth(nextMonth, nextYear)) {
            nextDay = 1;
            nextMonth++;
            // Adjust for end of year
            if (nextMonth > 12) {
                nextMonth = 1;
                nextYear++;
            }
        }

        for (int i = index; i < index + count && i < dataPoints.size(); i++) {
            WeatherReading reading = dataPoints.get(i);
            if (reading.day() == nextDay && reading.month() == nextMonth && reading.year() == nextYear
                    && reading.avgTemperature() != -99.0) {
                return reading.avgTemperature();
            }
        }
        return -99.0;
    }

    /**
     * Gets the number of days in a month
     * @param month the month to get the number of days for
     * @param year  the year to get the number of days for
     * @return the number of days in the month
     */

    private int getDaysInMonth(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); // Calendar.MONTH is 0-based
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * Calculates the average temperature for a set of WeatherReading objects
     * @param readings the set of WeatherReading objects to calculate the average temperature for
     * @return the average temperature for the set of WeatherReading objects, or -99.0 if not found
     */

    private double calculateAverageTemperature(WeatherReading[] readings) {
        double sum = 0.0;
        int instances = 0;
        for (WeatherReading reading : readings) {
            if (reading.avgTemperature() != -99.0) {
                sum += reading.avgTemperature();
                instances++;
            }
        }
        return (instances > 0) ? sum / instances : -99.0;
    }

    public String toString(){
        StringBuilder info = new StringBuilder();
        for (WeatherReading dataPoint : dataPoints) {
            info.append(dataPoint);
        }
        return info.toString();
    }

}
