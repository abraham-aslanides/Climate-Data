
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WeatherReadingTest {

    WeatherReading reading0 = new WeatherReading("Australia/South Pacific", "Australia", "", "Sydney", 6, 29, 1995, 47.1);
    WeatherReading reading1 = new WeatherReading("Australia/South Pacific", "Australia", "", "Sydney", 6, 29, 1995, 47.1);
    WeatherReading reading2 = new WeatherReading("Australia/South Pacific", "Australia", "", "Sydney", 6, 30, 1995, 48.4);
    WeatherReading reading3 = new WeatherReading("Australia/South Pacific", "Australia", "", "Sydney", 7, 1, 1995, 47.0);
    WeatherReading reading4 = new WeatherReading("Europe", "United Kingdom", "","London" ,8,27,2003,66.4);
    WeatherReading reading5 = new WeatherReading("Europe", "United Kingdom", "","London" ,8,28,2003,60.9);
    WeatherReading reading6 = new WeatherReading("Europe", "United Kingdom", "","London" ,8,29,2003,58.3);
    WeatherReading reading7 = new WeatherReading("Middle East", "Saudi Arabia", "","Dhahran" ,7,20,2011,98.4);
    WeatherReading reading8 = new WeatherReading("Middle East", "Saudi Arabia", "","Dhahran" ,7,21,2011,100.6);
    WeatherReading reading9 = new WeatherReading("Middle East", "Saudi Arabia", "","Dhahran" ,7,22,2011,101.8);

    @Test
    void compareTo() {
        assertEquals(0, reading0.compareTo(reading1));
        assertEquals(0, reading1.compareTo(reading0));
        assertEquals(-1, reading0.compareTo(reading2));
        assertEquals(1, reading2.compareTo(reading0));
        assertEquals(-1, reading0.compareTo(reading3));
        assertEquals(1, reading3.compareTo(reading0));

    }

    @Test
    void testEquals() {
        assertEquals(reading0, reading1);
        assertEquals(reading1, reading0);
        assertNotEquals(reading0, reading2);
        assertNotEquals(reading2, reading0);
        assertNotEquals(reading0, reading3);
        assertNotEquals(reading3, reading0);

    }

    @Test
    void testHashCode() {
        assertEquals(reading0.hashCode(), reading1.hashCode());
        assertNotEquals(reading0.hashCode(), reading2.hashCode());
        assertNotEquals(reading0.hashCode(), reading3.hashCode());
    }

    @Test
    void testToString() {
    }

    @Test
    void region() {
        assertEquals("Australia/South Pacific", reading0.region());
        assertEquals("Australia/South Pacific", reading1.region());
        assertEquals("Australia/South Pacific", reading2.region());
        assertEquals("Australia/South Pacific", reading3.region());

    }

    @Test
    void country() {
        assertEquals("Australia", reading0.country());
        assertEquals("Australia", reading1.country());
        assertEquals("Australia", reading2.country());
        assertEquals("Australia", reading3.country());
        assertEquals("United Kingdom", reading4.country());
        assertEquals("United Kingdom", reading5.country());
        assertEquals("United Kingdom", reading6.country());
        assertEquals("Saudi Arabia", reading7.country());
        assertEquals("Saudi Arabia", reading8.country());
        assertEquals("Saudi Arabia", reading9.country());
    }

    @Test
    void state() {
        assertEquals("", reading0.state());
        assertEquals("", reading1.state());
        assertEquals("", reading2.state());
        assertEquals("", reading3.state());
        assertEquals("", reading4.state());
        assertEquals("", reading5.state());
        assertEquals("", reading6.state());
        assertEquals("", reading7.state());
        assertEquals("", reading8.state());
        assertEquals("", reading9.state());
    }

    @Test
    void city() {
        assertEquals("Sydney", reading0.city());
        assertEquals("Sydney", reading1.city());
        assertEquals("Sydney", reading2.city());
        assertEquals("Sydney", reading3.city());
        assertEquals("London", reading4.city());
        assertEquals("London", reading5.city());
        assertEquals("London", reading6.city());
        assertEquals("Dhahran", reading7.city());
        assertEquals("Dhahran", reading8.city());
        assertEquals("Dhahran", reading9.city());
    }

    @Test
    void month() {
        assertEquals(6, reading0.month());
        assertEquals(6, reading1.month());
        assertEquals(6, reading2.month());
        assertEquals(7, reading3.month());
        assertEquals(8, reading4.month());
        assertEquals(8, reading5.month());
        assertEquals(8, reading6.month());
        assertEquals(7, reading7.month());
        assertEquals(7, reading8.month());
        assertEquals(7, reading9.month());
    }

    @Test
    void day() {
        assertEquals(29, reading0.day());
        assertEquals(29, reading1.day());
        assertEquals(30, reading2.day());
        assertEquals(1, reading3.day());
        assertEquals(27, reading4.day());
        assertEquals(28, reading5.day());
        assertEquals(29, reading6.day());
        assertEquals(20, reading7.day());
        assertEquals(21, reading8.day());
        assertEquals(22, reading9.day());
    }

    @Test
    void year() {
        assertEquals(1995, reading0.year());
        assertEquals(1995, reading1.year());
        assertEquals(1995, reading2.year());
        assertEquals(1995, reading3.year());
        assertEquals(2003, reading4.year());
        assertEquals(2003, reading5.year());
        assertEquals(2003, reading6.year());
        assertEquals(2011, reading7.year());
        assertEquals(2011, reading8.year());
        assertEquals(2011, reading9.year());
    }

    @Test
    void avgTemperature() {
        assertEquals(47.1, reading0.avgTemperature());
        assertEquals(47.1, reading1.avgTemperature());
        assertEquals(48.4, reading2.avgTemperature());
        assertEquals(47.0, reading3.avgTemperature());
        assertEquals(66.4, reading4.avgTemperature());
        assertEquals(60.9, reading5.avgTemperature());
        assertEquals(58.3, reading6.avgTemperature());
        assertEquals(98.4, reading7.avgTemperature());
        assertEquals(100.6, reading8.avgTemperature());
        assertEquals(101.8, reading9.avgTemperature());
    }
}