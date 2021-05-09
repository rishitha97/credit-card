import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CsvReader implements Record {
    private List<String> records;
    private List<String> output_records;
    public CsvReader() {
        records = new ArrayList<>();
        output_records = new ArrayList<>();
    }

    @Override
    public void fetch(String inputFilename) {
        try {
            records.addAll(Files.readAllLines(Paths.get(inputFilename)));
            if(records.get(0).toLowerCase().startsWith("card")){
                records.remove(0);
            }
        } catch (Exception e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    @Override
    public void process(String outputFilename){
        try {
            RecordIterator iterator = new ImplementationRI(records);

            output_records.add("CardNumber,CardType,Error");

            String record;
            String output;

            while (!iterator.isDone()) {
                record = iterator.currentString();

                if(record.equals("")){
                    output_records.add("null,Invalid,LineIsBlank");
                    iterator.next();
                    continue;
                }

                output = this.processEachRecord(record);

                output_records.add(output);
                iterator.next();
            }

            this.write(outputFilename);
        }catch(Exception e){
            System.out.println("Error while processing records:"+e);
        }

    }

    @Override
    public void write(String outputFilename){
        try {
            FileWriter fileWriter = new FileWriter(outputFilename);
            RecordIterator iterator = new ImplementationRI(output_records);

            while (!iterator.isDone()) {
                fileWriter.write(iterator.currentString() + "\n");
                iterator.next();
            }

            fileWriter.close();
        }catch(Exception e){
            System.out.println("Error:"+e);
        }
    }

    public String processEachRecord(String record){
        String output;
        String card_number = "";
        String card_type = "Invalid";
        String error_message = "None";
        Date expiry_date;

        card_number = record.split(",")[0];

        // Implementing Factory Method Pattern to get the Card Factory object
        CardVerify verify = new CardVerify();

        // Using the factory object to create the appropriate Object of Subclass of Card
        Card card = verify.createCard(card_number);

        //Finally validate the card and return the card_type
        if(card==null){
            error_message = "InvalidCardNumber";
            card_type = "Invalid";
            return (card_number + "," + card_type + "," + error_message);
        }

        output = card.CardTypeValidation(card_number);

        if(output.equals("Invalid")) {
            error_message = "InvalidCardNumber";
        }
        card_type = output;
        return (card_number + "," + card_type + "," + error_message);
    }
}
