import java.io.File;

public class Content {
    public void parse(String inputFilename, String outputFilename){
        String input_file_extension = inputFilename.substring(inputFilename.lastIndexOf(".") + 1).toLowerCase();
        String output_file_extension = outputFilename.substring(outputFilename.lastIndexOf(".") + 1).toLowerCase();
        if(!input_file_extension.equals(output_file_extension)){
            System.out.println("Please enter the same type of files.");
            return;
        }
        try {
            Record record;
            if (inputFilename.endsWith(".csv")) {
                record = new CsvReader();
            } else if (inputFilename.endsWith(".json")) {
                record = new JsonReader();
            } else if (inputFilename.endsWith(".xml")) {
                record = new XMLReader();
            } else {
                System.out.print(input_file_extension + " is not a supported file type");
                System.out.println("Please Enter a Valid file type");
                return;
            }

            record.fetch(inputFilename);

            File file = new File(outputFilename);
            file.createNewFile();

            record.process(outputFilename);
        }catch(Exception e){
            System.out.print("Error while Parsing File:"+e);
        }
    }
}
