import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Convertor {
    String apiKey;
    String urlString;

    public Convertor(){
        apiKey  = "04cad6402207e826dc0e9e8b";
        urlString = "https://v6.exchangerate-api.com/v6/" + apiKey;
    }

    public static void main(String[] args) {
        Convertor c = new Convertor();
        System.out.println(c.getConversionRate("GBP", "PKR"));
    }

    /**
     * Converts the JSON to a map - in order to easily work with JSON objects
     * @param jsonString json object as String
     * @return Map of json object
     */
    public static Map<String,Object> jsonToMap(String jsonString){
        Map<String,Object> map = new Gson().fromJson(
                jsonString, new TypeToken<HashMap<String,Object>>(){}.getType()
        );
        return map;
    }

    /**
     * @return map of currency codes and currency names
     */
    public Map<String, Object> getSupportedCodes(){
        urlString += "/codes";

        try{
            //Requesting data from the API
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            //Accessing supported currencies
            JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) connection.getContent()));
            JsonArray codes = root.getAsJsonObject().getAsJsonArray("supported_codes");

            Map<String, Object> supportedCodesMap = jsonToMap(codes.toString());
            return supportedCodesMap;

        } catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Returns the conversion rate between base currency and target currency
     * @param baseCurrency converting from this currency
     * @param targetCurrency converting to this currency
     * @return the conversion rate
     */
    public double getConversionRate(String baseCurrency, String targetCurrency){
        urlString += "pair/" + baseCurrency +"/" + targetCurrency;

        try{
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) connection.getContent()));
            double conversionRate = root.getAsJsonObject().getAsJsonPrimitive("conversion_rate").getAsDouble();
            return conversionRate;

        } catch (Exception e){
            System.out.println(e.getMessage());
            return 0;
        }
    }

}
