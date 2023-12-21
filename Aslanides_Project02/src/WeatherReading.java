import java.util.Objects;

/**
 * Represents a weather reading
 * @author Abraham Aslanides
 * @version CSC 143 Spring 2023, 5/1/2023
 */
public class WeatherReading implements Comparable<WeatherReading>{

    private String region;
    private String country;
    private String state;
    private String city;
    private int month;
    private int day;
    private int year;
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

    public String region(){
        return region;
    }

    public String country(){
        return country;
    }
    public String state(){
        return state;
    }
    public String city(){
        return city;
    }
    public int month(){
        return month;
    }
    public int day(){
        return day;
    }
    public int year(){
        return year;
    }
    public double avgTemperature(){
        return avgTemperature;
    }

    public void setRegion(String region){
        this.region = region;
    }
    public void setCountry(String country){
        this.country = country;
    }
    public void setState(String state){
        this.state = state;
    }
    public void setCity(String city){
        this.city = city;
    }
    public void setMonth(int month){
        this.month = month;
    }
    public void setDay(int day){
        this.day = day;
    }
    public void setYear(int year){
        this.year = year;
    }
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
