import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Objects;

/**
 * This class handles reading and updating data from the gui.
 * Reads the amount to be converted and the currencies to convert to and from
 * Uses Convertor class to get the converted amount and displays to user.
 * Handles any error made by the user
 */
public class GuiController {
    @FXML
    TextField amount;
    @FXML
    ComboBox<String> baseCurrencyComboBox;
    @FXML
    ComboBox<String> targetCurrencyComboBox;
    @FXML
    Label reuslt;

    Convertor convertor;
    Map<String, String> currencyCodes;
    private static final DecimalFormat df = new DecimalFormat("0.00");


    public GuiController(){
       convertor = new Convertor();
       currencyCodes = convertor.getSupportedCodes();
   }

   @FXML
    public void initialize(){
        baseCurrencyComboBox.setEditable(true);
        targetCurrencyComboBox.setEditable(true);
        baseCurrencyComboBox.getItems().addAll(currencyCodes.values());
        targetCurrencyComboBox.getItems().addAll(currencyCodes.values());
   }

   @FXML
    public void handleConversion(Event event){
       String baseCur = getCurrencyCode(true);
       String targetCur = getCurrencyCode(false);

       double amountConverted = 0;
       double amountAsInteger = checkAmountEnteredValue();

       if(amountAsInteger < 0){
           return;
       }
       if(checkComboBoxValues(baseCur, targetCur) < 0){
           return;
       }

       double conversionRate = convertor.getConversionRate(baseCur, targetCur);
       amountConverted = amountAsInteger * conversionRate;

       //Display Result
       reuslt.setText(df.format(amountAsInteger) + " " + currencyCodes.get(baseCur) + "s = " + df.format(amountConverted) + " " + currencyCodes.get(targetCur) +"s");
   }

   private int checkComboBoxValues(String baseCurrency, String targetCurrency){
       if(baseCurrency == null || targetCurrency == null){
           Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.setContentText("Cannot Convert without selecting a two currencies");
           alert.show();
           return -1;
       }
       return 0;
   }

    /**
     * Checks if a valid amount has been entered
     * If a negative number or anything other than a number is entered, -1 is returned.
     * Otherwise, the amount to be converted is returned as double
     * @return amount entered as a number
     */
   private double checkAmountEnteredValue(){
       if(amount.getText() == null){
           Alert alert = new Alert(Alert.AlertType.WARNING);
           alert.setContentText("Amount cannot be empty!");
           alert.show();
           return -1;
       }else {
           try{
               double amountInt = Double.parseDouble(amount.getText());
               if(amountInt < 0){
                   Alert alert = new Alert(Alert.AlertType.WARNING);
                   alert.setContentText("Cannot convert negative currencies!");
                   alert.show();
                   return -1;
               }
               return amountInt;
           } catch (NumberFormatException ex){
               Alert alert = new Alert(Alert.AlertType.WARNING);
               alert.setContentText("Enter a valid amount - needs to be a number");
               alert.show();
               return -1;
           }
       }
   }

   private String getCurrencyCode(boolean isBaseCurrency){
       String currencyName = isBaseCurrency ? baseCurrencyComboBox.getValue() : targetCurrencyComboBox.getValue();
       String currencyCode = null;
       for(Map.Entry<String, String> entry: currencyCodes.entrySet()){
           if(Objects.equals(currencyName, entry.getValue())){
               currencyCode = entry.getKey();
           }
       }
       return currencyCode;
   }



}
