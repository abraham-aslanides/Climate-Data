import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CityListStatsTest {
    int[] years1 = {1995, 1996, 1997, 1998, 1999};
    int[] years2 = {2003, 2004, 2005, 2006, 2007};
    int[] years3 = {2011, 2012, 2013, 2014, 2015};
    int[] years4 = {2016, 2017, 2018, 2019, 2020};
    CityListStats city1 = new CityListStats(0, 0, years1);
    CityListStats city2 = new CityListStats(2000, 200, years2);
    CityListStats city3 = new CityListStats(40000, 4000, years3);
    CityListStats city4 = new CityListStats(300000, 10000, years4);

    @Test
    void startingIndex() {
        assertEquals(0, city1.startingIndex());
        assertEquals(2000, city2.startingIndex());
        assertEquals(40000, city3.startingIndex());
        assertEquals(300000, city4.startingIndex());
    }

    @Test
    void count() {
        assertEquals(0, city1.count());
        assertEquals(200, city2.count());
        assertEquals(4000, city3.count());
        assertEquals(10000, city4.count());
    }

    @Test
    void years() {
        assertArrayEquals(years1, city1.years());
        assertArrayEquals(years2, city2.years());
        assertArrayEquals(years3, city3.years());
        assertArrayEquals(years4, city4.years());
    }
}