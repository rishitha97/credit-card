public class Main {
    public static void main(String args[]){
        try {
            Content fc=new Content();

            //Read input file name
            String inputFilename = "com/"+args[0];

            //Read the output file name
            String outputFilename = "com/"+args[1];

            fc.parse(inputFilename, outputFilename);
        }catch(Exception e){
            System.out.print("Error: "+e);
        }
    }
}
