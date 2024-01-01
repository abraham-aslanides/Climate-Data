import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * GlobalWeatherManager class implements the GlobalWeatherManagerInterface and Iterable interfaces
 * to manage a list of WeatherReading objects. The class also contains methods to retrieve data sets
 * by city, state, and country. It also calculates the linear regression slope for a given data set.
 *
 * @author Abraham Aslanides
 * @version CSC 143 Spring 2023, 5/1/2023
 */

//TODO: Add a progress bar to show the progress of the methods that take a long time to run
//TODO: Add precipitation data to the GlobalWeatherManager class

//--------------------------------------------------------------------------------------------------------------------//
//  Contents
//  1. Instance Variables
//  2. Constructors
//  3. Get Methods
//  4. Set Methods / Data Processing Methods
//  5. Helper Methods
//  6. Key Generation Methods
//  7. Iterator / toString Methods
//--------------------------------------------------------------------------------------------------------------------//

public class GlobalWeatherManager implements GlobalWeatherManagerInterface, Iterable<WeatherReading>{

    //----------------------------------------------------------------------------------------------------------------//
    // Instance Variables
    //----------------------------------------------------------------------------------------------------------------//
    /** ArrayList of WeatherReading objects */
    private ArrayList<WeatherReading> dataPoints;

    /** HashMap of average temperatures for each city */
    public Map<String, Double> averageTemperaturesCache;

    //----------------------------------------------------------------------------------------------------------------//
    // Constructors
    //----------------------------------------------------------------------------------------------------------------//

    /**
     * Constructor for the GlobalWeatherManager object
     * @param tempFile  the temperature readings file to be read
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

        averageTemperaturesCache = new HashMap<>();

        input.close();
    }

    //----------------------------------------------------------------------------------------------------------------//
    // Get Methods
    //----------------------------------------------------------------------------------------------------------------//

    /**
     * Retrieves a count of readings
     * @return count of readings
     */
    @Override
    public int getReadingCount() {

        return dataPoints.size();
    }

    /**
     * Retrieves the weather reading at the specified index.
     * @param index the index for the desired reading; must be a valid element index.
     * @return the reading at the specified index.
     */
    @Override
    public WeatherReading getReading(int index) {

        return dataPoints.get(index);
    }

    /**
     * Retrieves a set of weather readings.
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
     * Retrieves the number of days in the specified month and year.
     * @param month the month of interest; must be a valid month (1 to 12).
     * @param year the year of interest; must be a valid year.
     * @return the number of days in the specified month and year.
     */
    private int getDaysInMonth(int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); // Calendar.MONTH is 0-based
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * Retrieves a set of all cities in the dataPoints ArrayList
     * @return a set of all cities in the dataPoints ArrayList
     * @throws IllegalArgumentException if the reading is null.
     */
    private Set<String> getAllCities() {
        Set<String> allCities = new HashSet<>();
        for (WeatherReading dataPoint : dataPoints) {
            String cityKey = dataPoint.country() + "-" + dataPoint.state() + "-" + dataPoint.city();
            allCities.add(cityKey);
        }
        return allCities;
    }

    /**
     * Retrieves key list statistics for the specified country/state/city.
     * Student note:  okay to use an additional ArrayList in this method.
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
     * Does a linear regression analysis on the data, using x = year and y = temperature.
     * Calculates the slope of a best-fit line using the Least Squares method.   For more information
     * on that method, see <a href="https://www.youtube.com/watch?v=P8hT5nDai6A">...</a>
     * Student note:  okay to use two additional ArrayLists in this method.
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

    //----------------------------------------------------------------------------------------------------------------//
    // Set Methods / Data Processing Methods
    //----------------------------------------------------------------------------------------------------------------//

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
            throw new IllegalArgumentException("Readings array cannot be null.");
        }

        Set<String> existingKeys = dataPoints.stream()
                .map(this::generateKey)
                .collect(Collectors.toSet());

        for (WeatherReading reading : readings) {
            if (reading == null) {
                throw new IllegalArgumentException("Reading cannot be null.");
            }
            String key = generateKey(reading);
            if (!existingKeys.contains(key)) {
                dataPoints.add(reading);
                existingKeys.add(key); // Add the key to the set of existing keys
            }
        }
        Collections.sort(dataPoints);
    }
    
    /**
     * Replaces and populates the dataPoints ArrayList with WeatherReading objects for missing dates
     * (01/01/1995 - 12/31/2019) and fills them with the average temperature for that city for that day and month.
     */

    public void populateMissingDates() {
        Set<String> existingDates = new HashSet<>();
        for (WeatherReading dataPoint : dataPoints) {
            String dateCityKey = generateKey(dataPoint);
            existingDates.add(dateCityKey);
        }

        Set<String> allCities = getAllCities();
        ArrayList<WeatherReading> newReadings = new ArrayList<>();
        int addedCount = 0; // Debugging counter

        for (int year = 1995; year <= 2019; year++) {
            for (int month = 1; month <= 12; month++) {
                for (int day = 1; day <= getDaysInMonth(month, year); day++) {
                    for (String cityKey : allCities) {
                        String dateCityKey = year + "-" + month + "-" + day + "-" + cityKey;
                        if (!existingDates.contains(dateCityKey)) {
                            String[] parts = cityKey.split("-");
                            String country = parts[0];
                            String state = parts[1];
                            String city = parts[2];
                            double avgTemp = -99.0; // or calculate average if needed
                            WeatherReading newReading = new WeatherReading(
                                    ConvertReadings.returnRegion(country),
                                    country, state, city, month, day, year, avgTemp
                            );
                            newReadings.add(newReading);
                            existingDates.add(dateCityKey);
                            addedCount++; // Increment debug counter
                            // Debugging output
                            System.out.println("Adding missing date: " + dateCityKey);
                        }
                    }
                }
            }
        }

        System.out.println("New Readings Added: " + addedCount); // Debugging output

        // Add newReadings to dataPoints and sort
        dataPoints.addAll(newReadings);
        Collections.sort(dataPoints);

        System.out.println("Total Readings After Addition: " + dataPoints.size()); // Debugging output
    }

    /**
     * Processes NOAA temperature data files excluding the initial file
     * @param files the NOAA temperature data files to be processed
     */
    public void processNOAATempData(File[] files){
        for (File file : files) {
            try {
                // Convert readings from the current file
                WeatherReading[] newReadings = new ConvertReadings().convertReadings(file);
                // Add new readings to the dataPoints
                addReadings(newReadings);
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + file.getName());
            }
        }

        Collections.sort(dataPoints);
    }

    /**
     * Removes all readings with a temperature of -99.0 from the dataPoints ArrayList
     */
    public void removeInvalidReadings() {
        Iterator<WeatherReading> iterator = dataPoints.iterator();

        int removedCount = 0; // Debugging counter

        while (iterator.hasNext()) {
            WeatherReading reading = iterator.next();
            if (reading.avgTemperature() == -99.0) {
                iterator.remove();
                removedCount++;
            }
        }
        System.out.println("Invalid readings removed: " + removedCount);
        System.out.println("Remaining data count: " + dataPoints.size());
    }


    /**
     * Replaces all temperature readings of -99.0 with the average for that city for that day and month.
     * All WeatherReadings[] in the dataPoints ArrayList are iterated through and if the avgTemperature is -99.0,
     * the temperature is replaced with the average for that city.
     */

    public void replaceReadings() {
        // Ensure averages are computed first
        precomputeAverageTemperatures();

        int replacedCount = 0; // Counter for debugging
        int missedCount = 0; // Counter for missed replacements

        for (WeatherReading dataPoint : dataPoints) {
            if (dataPoint.avgTemperature() == -99.0) {
                String cacheKey = generateDayKey(dataPoint);

                if (averageTemperaturesCache.containsKey(cacheKey)) {
                    Double avgTemp = averageTemperaturesCache.get(cacheKey);

                    dataPoint.setAvgTemperature(avgTemp);
                    replacedCount++;
                } else {
                    missedCount++;
                }
            }
        }

        System.out.println("Replaced -99.0 readings count: " + replacedCount);
        System.out.println("Missed replacements count: " + missedCount);

        Collections.sort(dataPoints);
    }

    //----------------------------------------------------------------------------------------------------------------//
    // Helper Methods
    //----------------------------------------------------------------------------------------------------------------//

    /**
     * Calculates the slope of the best-fit line calculated using the Least Squares method.  For more information
     * on that method, see <a href="https://www.youtube.com/watch?v=P8hT5nDai6A">...</a>
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
     * Precomputes the average temperatures for each city for each day and month.
     * This method is called by replaceReadings() to ensure that the averages are computed before replacing
     * the -99.0 readings.
     */
    private void precomputeAverageTemperatures() {
        Map<String, List<Double>> tempAccumulator = new HashMap<>();
        Map<String, List<Double>> monthlyTempAccumulator = new HashMap<>();

        // Accumulate temperatures for daily and monthly, including -99.0 readings
        for (WeatherReading reading : dataPoints) {
            String dailyKey = generateDayKey(reading);
            String monthlyKey = generateMonthKey(reading);

            tempAccumulator.computeIfAbsent(dailyKey, k -> new ArrayList<>()).add(reading.avgTemperature());
            monthlyTempAccumulator.computeIfAbsent(monthlyKey, k -> new ArrayList<>()).add(reading.avgTemperature());
        }

        // Compute averages
        for (String dailyKey : tempAccumulator.keySet()) {
            List<Double> dailyTemperatures = tempAccumulator.get(dailyKey);
            double avgDailyTemp = dailyTemperatures.stream()
                    .filter(temp -> temp != -99.0)  // Filter out -99.0 before averaging
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(-99.0);

            if (avgDailyTemp == -99.0) { // No valid daily average, try monthly
                String monthlyKey = generateMonthKeyFromDayKey(dailyKey);
                List<Double> monthlyTemperatures = monthlyTempAccumulator.getOrDefault(monthlyKey, new ArrayList<>());
                double avgMonthlyTemp = monthlyTemperatures.stream()
                        .filter(temp -> temp != -99.0)  // Filter out -99.0 before averaging
                        .mapToDouble(Double::doubleValue)
                        .average()
                        .orElse(-99.0);

                avgDailyTemp = avgMonthlyTemp;  // Use monthly average as fallback
            }

            if (avgDailyTemp != -99.0) {
                averageTemperaturesCache.put(dailyKey, avgDailyTemp);
            }
        }

        // Debug
        System.out.println("Computed averages for " + averageTemperaturesCache.size() + " unique keys");
    }

    //----------------------------------------------------------------------------------------------------------------//
    // Key Generation Methods
    //----------------------------------------------------------------------------------------------------------------//

    /**
     * Generates a key for the specified reading.
     * @param reading the reading for which to generate a key; must not be null.
     *                Must not contain null elements.
     *                Must not contain duplicates.
     * @return the key for the specified reading.
     * @throws IllegalArgumentException if the reading is null.
     */
    private String generateKey(WeatherReading reading) {
        if (reading == null) {
            throw new IllegalArgumentException("Reading cannot be null.");
        }
        return reading.year() + "-" + reading.month() + "-" + reading.day() + "-" +
                reading.country() + "-" + reading.state() + "-" + reading.city();
    }

    /**
     * Generates a key for the specified reading.
     * Granularity is "country-state-city-month"
     * @param reading the reading for which to generate a key; must not be null.
     *                Must not contain null elements.
     *                Must not contain duplicates.
     * @return the key for the specified reading.
     * @throws IllegalArgumentException if the reading is null.
     */
    private String generateMonthKey(WeatherReading reading) {
        if (reading == null) {
            throw new IllegalArgumentException("Reading cannot be null.");
        }
        return reading.country() + "-" + reading.state() + "-" + reading.city() + "-" +
                reading.month();
    }

    /**
     * Converts a month key from a day key for the specified reading.
     * Granularity is "country-state-city-month"
     * @param dayKey the reading for which to generate a key; must not be null.
     *                Must not contain null elements.
     *                Must not contain duplicates.
     * @return the key for the specified reading.
     * @throws IllegalArgumentException if the reading is null.
     */
    private String generateMonthKeyFromDayKey(String dayKey) {
        if (dayKey == null) {
            throw new IllegalArgumentException("Reading cannot be null.");
        }
        // Remove the day part from the daily key
        int lastDashIndex = dayKey.lastIndexOf("-");
        return dayKey.substring(0, lastDashIndex);
    }

    /**
     * Generates a key for the specified reading.
     * Granularity is "country-state-city-month-day"
     * @param reading the reading for which to generate a key; must not be null.
     *                Must not contain null elements.
     *                Must not contain duplicates.
     * @return the key for the specified reading.
     * @throws IllegalArgumentException if the reading is null.
     */
    private String generateDayKey(WeatherReading reading) {
        if (reading == null) {
            throw new IllegalArgumentException("Reading cannot be null.");
        }
        return reading.country() + "-" + reading.state() + "-" + reading.city() + "-" +
                reading.month() + "-" + reading.day();
    }

    //----------------------------------------------------------------------------------------------------------------//
    // Iterator / toString Methods
    //----------------------------------------------------------------------------------------------------------------//
    /**
     * Retrieves an iterator over all weather readings.
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

    public String toString(){
        StringBuilder info = new StringBuilder();
        for (WeatherReading dataPoint : dataPoints) {
            info.append(dataPoint);
        }
        return info.toString();
    }

}
