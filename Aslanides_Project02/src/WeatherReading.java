import java.util.Objects;

/**
 * Represents a weather reading
 * @author Abraham Aslanides
 * @version CSC 143 Spring 2023, 5/1/2023
 */
public class WeatherReading implements Comparable<WeatherReading>{

    /** Region of the city */
    private String region;
    /** Country of the city */
    private String country;
    /** State of the city */
    private String state;
    /** Name of the city */
    private String city;
    /** Month of the reading */
    private int month;
    /** Day of the reading */
    private int day;
    /** Year of the reading */
    private int year;
    /** Reading temperature */
    private double avgTemperature;

    /**
     * Represents a weather reading for a city
     * @param region            region of the city
     * @param country           country of the city
     * @param state             state of the city
     * @param city              name of the city
     * @param month             month of the reading
     * @param day               day of the reading
     * @param year              year of the reading
     * @param avgTemperature    reading temperature
     *
     */
    public WeatherReading(String region, String country, String state, String city, int month, int day, int year,
                          double avgTemperature){
        this.region = region;
        this.country = country;
        this.state = state;
        this.city = city;
        this.month = month;
        this.day = day;
        this.year = year;
        this.avgTemperature = avgTemperature;
    }

    /** Region get method
     * @return region of the city
     */
    public String region(){
        return region;
    }

    /** Country get method
     * @return country of the city
     */
    public String country(){
        return country;
    }

    /** State get method
     * @return state of the city
     */
    public String state(){
        return state;
    }

    /** City get method
     * @return name of the city
     */
    public String city(){
        return city;
    }

    /** Month get method
     * @return month of the reading
     */
    public int month(){
        return month;
    }

    /** Day get method
     * @return day of the reading
     */
    public int day(){
        return day;
    }

    /** Year get method
     * @return year of the reading
     */
    public int year(){
        return year;
    }

    /** Average temperature get method
     * @return reading temperature
     */
    public double avgTemperature(){
        return avgTemperature;
    }

    /** Region set method
     * @param region region of the city
     */
    public void setRegion(String region){
        this.region = region;
    }

    /** Country set method
     * @param country country of the city
     */
    public void setCountry(String country){
        this.country = country;
    }

    /** State set method
     * @param state state of the city
     */
    public void setState(String state){
        this.state = state;
    }

    /** City set method
     * @param city name of the city
     */
    public void setCity(String city){
        this.city = city;
    }

    /** Month set method
     * @param month month of the reading
     */
    public void setMonth(int month){
        this.month = month;
    }

    /** Day set method
     * @param day day of the reading
     */
    public void setDay(int day){
        this.day = day;
    }

    /** Year set method
     * @param year year of the reading
     */
    public void setYear(int year){
        this.year = year;
    }

    /** Average temperature set method
     * @param avgTemperature reading temperature
     */
    public void setAvgTemperature(double avgTemperature){
        this.avgTemperature = avgTemperature;
    }

    /**
     * Compares two WeatherReading objects
     * @param o     the object to be compared.
     * @return      0 if equal, -1 if less than, 1 if greater than
     */
    @Override
    public int compareTo(WeatherReading o) {

        int compareCountry = this.country.compareTo(o.country);
        if (compareCountry != 0) {
            return compareCountry;
        } else {
            int compareState = this.state.compareTo(o.state);
            if (compareState != 0) {
                return compareState;
            } else {
                int compareCity = this.city.compareTo(o.city);
                if (compareCity != 0) {
                    return compareCity;
                } else {
                    int compareYear = Integer.compare(this.year, o.year);
                    if (compareYear != 0) {
                        return compareYear;
                    } else {
                        int compareMonth = Integer.compare(this.month, o.month);
                        if (compareMonth != 0) {
                            return compareMonth;
                        } else {
                            return Integer.compare(this.day, o.day);
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks if two WeatherReading objects are equal
     * @param obj   the reference object with which to compare.
     * @return      true if this object is the same as the obj argument; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof WeatherReading o)) {
            return false;
        }

        return  Objects.equals(country, o.country) &&
                Objects.equals(state, o.state) &&
                Objects.equals(city, o.city) &&
                year == o.year &&
                month == o.month &&
                day == o.day;
    }

    /**
     * Returns a hash code value for the object
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(country, state, city, year, month, day);
    }

    @Override
    public String toString() {
        String info = "";
        info += city() + ", " + state() + ", " + country() + ", " + region() + "\n";
        info += month() + " / " + day() + " / " + year() + "\n";
        info += "Average temperature: " + avgTemperature() + "\n";
        return info;
    }
}
