import java.util.Arrays;

/**
 * Represents the index, count, and years for which a city has WeatherReadings
 * @param startingIndex starting index of the city in the list
 * @param count         count of readings for the city
 * @param years         array of years for which the city has readings
 *
 * @author Abraham Aslanides
 * @version CSC 143 Spring 2023, 5/1/2023
 */
public record CityListStats(int startingIndex, int count, int[] years) {

    /**
     * Constructor with validation
     */
    public CityListStats{
        if (startingIndex < 0) {
            throw new IllegalArgumentException("Starting index must be greater than or equal to 0");
        }
        if (count < 0) {
            throw new IllegalArgumentException("Count must be greater than or equal to 0");
        }
        if (years == null) {
            throw new IllegalArgumentException("Years must not be null");
        }
        if (years.length == 0) {
            throw new IllegalArgumentException("Years must not be empty");
        }
    }

    /**
     * Compares two CityListStats objects
     * @param o    the other CityListStats object
     * @return     0 if equal, -1 if less than, 1 if greater than
     */
    public int compareTo(CityListStats o) {

        int compareStart = Long.compare(this.startingIndex, o.startingIndex);
        if (compareStart != 0) {
            return compareStart;
        } else {
            int compareCount = Integer.compare(this.count, o.count);
            if (compareCount != 0) {
                return compareCount;
            } else {
                return Arrays.compare(this.years, o.years);
            }
        }
    }

    @Override
    public String toString() {
        return "CityListStats\n" +
                "startingIndex: " + startingIndex + "\n" +
                "count: " + count + "\n" +
                "years: " + Arrays.toString(years) + "\n";
    }
}
