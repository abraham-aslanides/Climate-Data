import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;


/**
 * Converts NOAA data to a format that can be used by the program.
 */
public class ConvertReadings {

    //Format of the NOAA data is:
    //"STATION","NAME","LATITUDE","LONGITUDE","ELEVATION","DATE","TAVG"
    //"CMM00064860","GAROUA, CM","9.336","13.37","242.0","1995-01-01","76"
    //"AO000066160","LUANDA, AO","-8.85","13.233","74.0","2002-04-07","78"
    //"CGM00064210","NDJILI INTERNATIONAL, CG","-4.386","15.445","313.0","2008-09-06","78"
    //"ZI000067975","MASVINGO, ZI","-20.067","30.867","1095.0","2006-03-03","74"
    //"NGM00061091","MAGARIA, NG","12.983","8.933","403.0","1995-01-01","62"
    //"CD000064706","MOUNDOU, CD","8.567","16.067","429.0","2015-01-31","84"
    //"SU000062790","NYALA, SU","12.05","24.883","643.0","2003-05-07","95"
    //"BC008382520","GHANZI, BC","-21.7","21.65","1100.0","2014-09-15","77"
    //"GHM00065404","WA, GH","10.083","-2.508","323.1","1996-06-25","81"
    //"MLM00061265","AMBODEDJO, ML","14.513","-4.08","276.1","2000-07-10","88"
    //"LYM00062012","EL KHOMS, LY","32.633","14.3","22.0","2000-12-07","60"
    //"LIM00065660","MONROVIA ROBERTS INTERNATIONAL, LI","6.234","-10.362","9.4","2006-06-02","79"
    //"RWM00064387","KIGALI INTERNATIONAL, RW","-1.969","30.139","1481.0","1995-01-05","72"
    //"SL000061856","LUNGI, SL","8.617","-13.2","27.0","2006-12-03","82"
    //"NIM00065201","MURTALA MUHAMMED, NI","6.577","3.321","41.1","2001-04-20","83"
    //"ZA000067743","LIVINGSTONE, ZA","-17.817","25.817","986.0","2015-09-14","72"
    //"MI000067693","CHILEKA, MI","-15.683","34.967","767.0","2002-08-15","78"

    //Format of the data we want is:
    //Region, Country, State, City, Month, Day, Year, Average Temperature
    //Africa, Cameroon, , Garoua, 1, 1, 1995, 76.0
    //Africa, Angola, , Luanda, 4, 7, 2002, 78.0
    //Africa, Democratic Republic of the Congo, , Ndijili International, 9, 6, 2008, 78.0
    //Africa, Zimbabwe, , Masvingo, 3, 3, 2006, 74.0
    //Africa, Niger, , Magaria, 1, 1, 1995, 62.0
    //Africa, Chad, , Moundou, 1, 31, 2015, 84.0
    //Africa, Sudan, , Nyala, 5, 7, 2003, 95.0
    //Africa, Botswana, , Ghanzi, 9, 15, 2014, 77.0
    //Africa, Ghana, , Wa, 6, 25, 1996, 81.0
    //Africa, Mali, , Ambodedjo, 7, 10, 2000, 88.0
    //Africa, Libya, , El Khoms, 12, 7, 2000, 60.0
    //Africa, Liberia, , Monrovia Roberts International, 6, 2, 2006, 79.0
    //Africa, Rwanda, , Kigali International, 1, 5, 1995, 72.0
    //Africa, Sierra Leone, , Lungi, 12, 3, 2006, 82.0
    //Africa, Nigeria, , Murtala Muhammed, 4, 20, 2001, 83.0
    //Africa, Zambia, , Livingstone, 9, 14, 2015, 72.0
    //Africa, Malawi, , Chileka, 8, 15, 2002, 78.0



    /**
     * Reads a .csv of NOAA data and converts each line into WeatherReading objects and returns them in an array.
     * @param file the file to be read
     * @return an array of WeatherReading objects
     */
    public WeatherReading[] convertReadings(File file) throws FileNotFoundException {
        Map<String, String> countryNameMap = createCountryNameMap();
        Map<String, String> countryRegionMap = createCountryRegionMap();
        Map<String, String> stateNameMap = createStateNameMap(); // Map for state codes

        Scanner scanner = new Scanner(file);
        scanner.nextLine(); // Skip the first line of the file
        ArrayList<WeatherReading> readings = new ArrayList<>();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] lineArray = line.split("\",\"");

            if (lineArray.length < 7) {
                continue;
            }

            lineArray[0] = lineArray[0].replaceFirst("^\"", "");
            lineArray[lineArray.length - 1] = lineArray[lineArray.length - 1].replaceAll("\"$", "");

            String[] nameArray = lineArray[1].split(",");
            if (nameArray.length < 2) {
                continue;
            }

            // Capitalize only the first letter of the city name
            String city = nameArray[0].trim().toLowerCase();
            city = city.substring(0, 1).toUpperCase() + city.substring(1);

            String[] codeArray = nameArray[1].trim().split(" ");
            String state = "";
            String country = "Unknown Country";
            String region = "Unknown Region";

            if (codeArray.length == 1) {
                // Only country code is present
                country = countryNameMap.getOrDefault(codeArray[0], "Unknown Country");
                region = countryRegionMap.getOrDefault(country, "Unknown Region");
            } else if (codeArray.length == 2) {
                // Both state and country codes are present
                state = stateNameMap.getOrDefault(codeArray[0], "");
                country = countryNameMap.getOrDefault(codeArray[1], "Unknown Country");
                region = countryRegionMap.getOrDefault(country, "Unknown Region");
            }

            String dateString = lineArray[5].trim();
            String[] dateArray = dateString.split("-");
            if (dateArray.length != 3) {
                continue;
            }

            int year = Integer.parseInt(dateArray[0]);
            int month = Integer.parseInt(dateArray[1]);
            int day = Integer.parseInt(dateArray[2]);
            double avgTemp = Double.parseDouble(lineArray[6].trim());

            readings.add(new WeatherReading(region, country, state, city, month, day, year, avgTemp));
        }

        return readings.toArray(new WeatherReading[0]);
    }


    private static Map<String, String> createStateNameMap() {
        Map<String, String> map = new HashMap<>();
        map.put("AL", "Alabama");
        map.put("AK", "Alaska");
        map.put("AZ", "Arizona");
        map.put("AR", "Arkansas");
        map.put("CA", "California");
        map.put("CO", "Colorado");
        map.put("CT", "Connecticut");
        map.put("DE", "Delaware");
        map.put("FL", "Florida");
        map.put("GA", "Georgia");
        map.put("HI", "Hawaii");
        map.put("ID", "Idaho");
        map.put("IL", "Illinois");
        map.put("IN", "Indiana");
        map.put("IA", "Iowa");
        map.put("KS", "Kansas");
        map.put("KY", "Kentucky");
        map.put("LA", "Louisiana");
        map.put("ME", "Maine");
        map.put("MD", "Maryland");
        map.put("MA", "Massachusetts");
        map.put("MI", "Michigan");
        map.put("MN", "Minnesota");
        map.put("MS", "Mississippi");
        map.put("MO", "Missouri");
        map.put("MT", "Montana");
        map.put("NE", "Nebraska");
        map.put("NV", "Nevada");
        map.put("NH", "New Hampshire");
        map.put("NJ", "New Jersey");
        map.put("NM", "New Mexico");
        map.put("NY", "New York");
        map.put("NC", "North Carolina");
        map.put("ND", "North Dakota");
        map.put("OH", "Ohio");
        map.put("OK", "Oklahoma");
        map.put("OR", "Oregon");
        map.put("PA", "Pennsylvania");
        map.put("PR", "Puerto Rico");
        map.put("RI", "Rhode Island");
        map.put("SC", "South Carolina");
        map.put("SD", "South Dakota");
        map.put("TN", "Tennessee");
        map.put("TX", "Texas");
        map.put("UT", "Utah");
        map.put("VT", "Vermont");
        map.put("VA", "Virginia");
        map.put("WA", "Washington");
        map.put("WV", "West Virginia");
        map.put("WI", "Wisconsin");
        map.put("WY", "Wyoming");

        return map;
    }

    private static Map<String, String> createCountryNameMap() {
        Map<String, String> map = new HashMap<>();
        // Populate the map with country codes and corresponding country names
        map.put("CM", "Cameroon");
        map.put("AO", "Angola");
        map.put("CG", "Democratic Republic of the Congo");
        map.put("ZI", "Zimbabwe");
        map.put("NG", "Niger");
        map.put("CD", "Chad");
        map.put("SU", "Sudan");
        map.put("BC", "Botswana");
        map.put("GH", "Ghana");
        map.put("ML", "Mali");
        map.put("LY", "Libya");
        map.put("LI", "Liberia");
        map.put("RW", "Rwanda");
        map.put("SL", "Sierra Leone");
        map.put("NI", "Nigeria");
        map.put("ZA", "Zambia");
        map.put("MI", "Malawi");
        map.put("CF", "Democratic Republic of the Congo");
        map.put("US", "US");
        map.put("PR", "Puerto Rico");
        map.put("CA", "Canada");
        map.put("MX", "Mexico");
        map.put("CU", "Cuba");
        map.put("BD", "Bermuda");
        map.put("JM", "Jamaica");
        map.put("MD", "Moldova");
        map.put("SI", "Slovenia");
        map.put("FR", "France");
        map.put("LH", "Lithuania");
        map.put("EN", "Estonia");
        map.put("GG", "Georgia");
        map.put("MJ", "Montenegro");
        map.put("GR", "Greece");
        map.put("LU", "Luxembourg");
        map.put("DA", "Denmark");
        map.put("UK", "United Kingdom");
        map.put("PO", "Portugal");
        map.put("CY", "Cyprus");
        map.put("RI", "Serbia");
        map.put("EI", "Ireland");
        map.put("GL", "Greenland");
        map.put("AJ", "Azerbaijan");
        map.put("TH", "Thailand");
        map.put("AM", "Armenia");
        map.put("TI", "Tajikistan");
        map.put("SA", "Saudi Arabia");
        map.put("ER", "Eritrea");
        map.put("IS", "Israel");
        map.put("AF", "Afghanistan");
        map.put("BG", "Bangladesh");
        map.put("IR", "Iran");
        map.put("PP", "Papua New Guinea");
        map.put("IZ", "Iraq");
        map.put("CB", "Cambodia");
        map.put("PE", "Peru");
        map.put("PA", "Paraguay");
        map.put("CI", "Chile");
        map.put("EC", "Ecuador");
        map.put("ES", "El Salvador");
        map.put("FS", "French Southern and Antarctic Lands");
        map.put("GY", "Guyana");
        map.put("FG", "French Guiana");
        map.put("TD", "Trinidad and Tobago");

        return map;
    }


    public static String returnRegion(String country) {
        Map<String, String> countryRegionMap = createCountryRegionMap();
        return countryRegionMap.getOrDefault(country, "Unknown Region");
    }
    private static Map<String, String> createCountryRegionMap() {
        Map<String, String> map = new HashMap<>();
        // Populate the map with country names and corresponding regions
        map.put("Cameroon", "Africa");
        map.put("Angola", "Africa");
        map.put("Democratic Republic of the Congo", "Africa");
        map.put("Zimbabwe", "Africa");
        map.put("Niger", "Africa");
        map.put("Chad", "Africa");
        map.put("Sudan", "Africa");
        map.put("Botswana", "Africa");
        map.put("Ghana", "Africa");
        map.put("Mali", "Africa");
        map.put("Libya", "Africa");
        map.put("Liberia", "Africa");
        map.put("Rwanda", "Africa");
        map.put("Sierra Leone", "Africa");
        map.put("Nigeria", "Africa");
        map.put("Zambia", "Africa");
        map.put("Malawi", "Africa");
        map.put("United States", "North America");
        map.put("US", "North America");
        map.put("Puerto Rico", "South/Central America & Carribean");
        map.put("Canada", "North America");
        map.put("Mexico", "North America");
        map.put("Cuba", "South/Central America & Carribean");
        map.put("Bermuda", "South/Central America & Carribean");
        map.put("Jamaica", "South/Central America & Carribean");
        map.put("Moldova", "Europe");
        map.put("Slovenia", "Europe");
        map.put("France", "Europe");
        map.put("Lithuania", "Europe");
        map.put("Estonia", "Europe");
        map.put("Georgia", "Europe");
        map.put("Montenegro", "Europe");
        map.put("Greece", "Europe");
        map.put("Luxembourg", "Europe");
        map.put("Denmark", "Europe");
        map.put("United Kingdom", "Europe");
        map.put("Portugal", "Europe");
        map.put("Cyprus", "Europe");
        map.put("Serbia", "Europe");
        map.put("Ireland", "Europe");
        map.put("Azerbaijan", "Middle East");
        map.put("Thailand", "Asia");
        map.put("Armenia", "Middle East");
        map.put("Tajikistan", "Asia");
        map.put("Saudi Arabia", "Middle East");
        map.put("Eritrea", "Africa");
        map.put("Israel", "Middle East");
        map.put("Afghanistan", "Asia");
        map.put("Bangladesh", "Asia");
        map.put("Iran", "Middle East");
        map.put("Papua New Guinea", "Australia/South Pacific");
        map.put("Iraq", "Middle East");
        map.put("Cambodia", "Asia");
        map.put("Peru", "South/Central America & Carribean");
        map.put("Paraguay", "South/Central America & Carribean");
        map.put("Chile", "South/Central America & Carribean");
        map.put("Ecuador", "South/Central America & Carribean");
        map.put("El Salvador", "South/Central America & Carribean");
        map.put("French Southern and Antarctic Lands", "Additional Territories");
        map.put("Guyana", "South/Central America & Carribean");
        map.put("French Guiana", "South/Central America & Carribean");
        map.put("Trinidad and Tobago", "South/Central America & Carribean");
        map.put("United States Minor Outlying Islands", "Additional Territories");

        return map;
    }

}
