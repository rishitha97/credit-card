import javax.xml.transform.TransformerException;

public interface Record {
    void process(String outputFilename);

    void fetch(String inputFilename);

    void write(String outputFilename) throws TransformerException;
}