
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalWeatherManagerTest {

    static GlobalWeatherManager dataPoints;
    static CityListStats list1;
    static CityListStats list2;
    static CityListStats list3;
    static WeatherReading reading1;
    static WeatherReading reading2;
    static WeatherReading reading3;
    static WeatherReading reading4;
    static WeatherReading reading5;
    static WeatherReading reading6;
    static WeatherReading reading7;
    static WeatherReading reading8;
    static WeatherReading reading9;
    static WeatherReading reading10;
    static WeatherReading reading11;
    static WeatherReading[] readings1;
    static WeatherReading[] readings2;
    static WeatherReading[] readings3;
    static WeatherReading[] readings4;


    static {

        try {
        dataPoints = new GlobalWeatherManager(new File("city_temperature.csv"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        list1 = new CityListStats(0, 9265, new int[]{1995, 1996, 1997, 1998, 1999, 2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020});
        list2 = new CityListStats(1572970, 15334, new int[]{1995, 1996, 1997, 1998, 1999, 2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020});
        list3 = new CityListStats(2868837, 9265, new int[]{1995, 1996, 1997, 1998, 1999, 2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020});

        //Tirana, , Albania, Europe 1 / 1 / 1995 Average temperature: -99.0
        //Tirana, , Albania, Europe 1 / 2 / 1995 Average temperature: -99.0
        //Tirana, , Albania, Europe 1 / 3 / 1995 Average temperature: -99.0

        reading1 = new WeatherReading("Europe", "Albania", "", "Tirana", 1, 1, 1995, -99.0);
        reading2 = new WeatherReading("Europe", "Albania", "", "Tirana", 1, 2, 1995, -99.0);
        reading3 = new WeatherReading("Europe", "Albania", "", "Tirana", 1, 3, 1995, -99.0);

        //Jacksonville, Florida, US, North America 7 / 8 / 1996 Average temperature: 81.2
        //Jacksonville, Florida, US, North America 7 / 9 / 1996 Average temperature: 79.6
        //Jacksonville, Florida, US, North America 7 / 10 / 1996 Average temperature: 81.2

        reading4 = new WeatherReading("North America", "US", "Florida", "Jacksonville", 7, 8, 1996, 81.2);
        reading5 = new WeatherReading("North America", "US", "Florida", "Jacksonville", 7, 9, 1996, 79.6);
        reading6 = new WeatherReading("North America", "US", "Florida", "Jacksonville", 7, 10, 1996, 81.2);

        //Belgrade, , Yugoslavia, Europe 1 / 24 / 2014 Average temperature: 37.0
        //Belgrade, , Yugoslavia, Europe 1 / 25 / 2014 Average temperature: 28.3
        //Belgrade, , Yugoslavia, Europe 1 / 26 / 2014 Average temperature: 19.1

        reading7 = new WeatherReading("Europe", "Yugoslavia", "", "Belgrade", 1, 24, 2014, 37.0);
        reading8 = new WeatherReading("Europe", "Yugoslavia", "", "Belgrade", 1, 25, 2014, 28.3);
        reading9 = new WeatherReading("Europe", "Yugoslavia", "", "Belgrade", 1, 26, 2014, 19.1);

        //Tirana, , Albania, Europe 1 / 1 / 1996 Average temperature: -99.0
        //Tirana, , Albania, Europe 1 / 1 / 1997 Average temperature: -99.0

        reading10 = new WeatherReading("Europe", "Albania", "", "Tirana", 1, 1, 1996, -99.0);
        reading11 = new WeatherReading("Europe", "Albania", "", "Tirana", 1, 1, 1997, -99.0);

        readings1 = new WeatherReading[]{reading1, reading2, reading3};
        readings2 = new WeatherReading[]{reading4, reading5, reading6};
        readings3 = new WeatherReading[]{reading7, reading8, reading9};
        readings4 = new WeatherReading[]{reading1, reading10, reading11};
    }

    @Test
    void testGetReadingCount() {
        assertEquals(2885064, dataPoints.getReadingCount());
    }

    @Test
    void testGetReading() {
        assertEquals(0, dataPoints.getReading(0).compareTo(reading1));
        assertEquals(0, dataPoints.getReading(1).compareTo(reading2));
        assertEquals(0, dataPoints.getReading(2).compareTo(reading3));
        assertEquals(0, dataPoints.getReading(1579275).compareTo(reading4));
        assertEquals(0, dataPoints.getReading(1579276).compareTo(reading5));
        assertEquals(0, dataPoints.getReading(1579277).compareTo(reading6));
        assertEquals(0, dataPoints.getReading(2875800).compareTo(reading7));
        assertEquals(0, dataPoints.getReading(2875801).compareTo(reading8));
        assertEquals(0, dataPoints.getReading(2875802).compareTo(reading9));

    }

    @Test
    void testGetReadings() {
        assertArrayEquals(readings1, dataPoints.getReadings(0,3));
        assertArrayEquals(readings2, dataPoints.getReadings(1579275,3));
        assertArrayEquals(readings3, dataPoints.getReadings(2875800,3));
        assertArrayEquals(readings4, dataPoints.getReadings(0,800, 1, 1));
    }

    @Test
    void testGetCityListStats() {
        assertEquals(0, list1.compareTo(dataPoints.getCityListStats("Albania", "", "Tirana")));
        assertEquals(0, list2.compareTo(dataPoints.getCityListStats("US", "Florida", "Jacksonville")));
        assertEquals(0, list3.compareTo(dataPoints.getCityListStats("Yugoslavia", "", "Belgrade")));
    }

    @Test
    void testGetTemperatureLinearRegressionSlope(){
        assertEquals(-4.599999999996119, dataPoints.getTemperatureLinearRegressionSlope(dataPoints.getReadings(0,4000, 1, 1)));
        assertEquals(1.8279069767409373, dataPoints.getTemperatureLinearRegressionSlope(dataPoints.getReadings(0,4000, 1, 2)));

    }

    @Test
    void testCalcLinearRegressionSlope(){
        assertEquals(1.0, dataPoints.calcLinearRegressionSlope(new Integer[]{1,2,3}, new Double[]{1.0,2.0,3.0}));
        assertEquals(-1.0, dataPoints.calcLinearRegressionSlope(new Integer[]{1,2,3}, new Double[]{3.0,2.0,1.0}));
        assertEquals(0.0, dataPoints.calcLinearRegressionSlope(new Integer[]{1,2,3}, new Double[]{2.0,2.0,2.0}));
        assertEquals(1.0, dataPoints.calcLinearRegressionSlope(new Integer[]{1,2,3}, new Double[]{0.0,4.0,2.0}));
        assertEquals(-1.0, dataPoints.calcLinearRegressionSlope(new Integer[]{1,2,3}, new Double[]{0.0,-4.0,-2.0}));
    }
}