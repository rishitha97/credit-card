import java.io.*;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class JsonReader implements Record {
    private JSONArray output_records;
    private JSONArray records;

    public JsonReader() {
        records = new JSONArray();
        output_records = new JSONArray();
    }

    @Override
    public void fetch(String inputFilename){
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(inputFilename))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            records = (JSONArray) obj;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(String outputFilename) {

        RecordIterator iterator = new ImplementationRI(records, records.size());
        JSONObject output;

        while(!iterator.isDone()){
            JSONObject record = iterator.currentObject();
            output = this.processEachRecord(record);
            output_records.add(output);
            iterator.next();
        }
        this.write(outputFilename);
    }

    public JSONObject processEachRecord(JSONObject record){
        String output;
        String card_type = "Invalid";
        String error_message = "None";
        String card_number;
        Date expiry_date;
        JSONObject row = new JSONObject();

        try {
            card_number = record.get("CardNumber").toString();
        }catch(Exception e){
            error_message = "InvalidCardNumber";
            card_type = "Invalid";
            row.put("CardNumber",0);
            row.put("CardType",card_type);
            row.put("Error",error_message);
            return row;
        }
        // Implementing Factory Method Pattern to get the Card Factory object
        CardVerify verify = new CardVerify();

        // Using the factory object to create the appropriate Object of Subclass of Card
        Card card = verify.createCard(card_number);

        //Finally validate the card and return the card_type
        if(card==null){
            error_message = "InvalidCardNumber";
            card_type = "Invalid";
            row.put("CardNumber",Long.parseLong(card_number));
            row.put("CardType",card_type);
            row.put("Error",error_message);
            return row;
        }

        output = card.CardTypeValidation(card_number);

        if(output.equals("Invalid")){
            error_message = "InvalidCardNumber";
        }
        card_type = output;
        row.put("CardNumber",Long.parseLong(card_number));
        row.put("CardType",card_type);
        row.put("Error",error_message);
        return row;
    }


    @Override
    public void write(String outputFilename){
        //Write JSON file
        try (FileWriter file = new FileWriter(outputFilename)) {

            file.write(output_records.toJSONString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
