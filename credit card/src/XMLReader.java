import java.io.*;
import java.util.Date;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXParseException;


public class XMLReader implements Record {
    private NodeList records;
    private DocumentBuilderFactory dbFactory;
    private DocumentBuilder dBuilder;
    Document outputDoc;
    public XMLReader() throws ParserConfigurationException {
        records = new NodeList() {
            @Override
            public Node item(int index) {
                return null;
            }

            @Override
            public int getLength() {
                return 0;
            }
        };

        dbFactory = DocumentBuilderFactory.newInstance();
        dBuilder = dbFactory.newDocumentBuilder();
    }

    @Override
    public void fetch(String inputFilename){
        try {
            File file = new File(inputFilename);

            // TO handle & operators
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuffer sb =  new StringBuffer();
            String line = null;
            while((line = br.readLine())!= null)
            {
                if(line.indexOf("&") != -1)
                {
                    line = line.replaceAll("&","");
                }
                String newline = System.getProperty("line.separator");
                sb.append(line).append(newline);
            }
            br.close();
            BufferedWriter bw = new BufferedWriter(new FileWriter(inputFilename));
            bw.write(sb.toString());
            bw.close();

            //Fetch the records
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            records = doc.getElementsByTagName("row");
        }catch(SAXParseException sax){
            System.out.println("Error with & or any special character:"+sax);

        }catch(Exception e){
            System.out.println("Error while fetching records");
        }
    }

    @Override
    public void process(String outputFilename){
        try {
            RecordIterator iterator = new ImplementationRI(records);
            outputDoc = dBuilder.newDocument();

            Element root = outputDoc.createElement("root");
            outputDoc.appendChild(root);

            Element element;
            String card_number = "";
            String output = "";
            String card_type = "Invalid";
            String error_message = "None";
            Date expiry_date;
            while (!iterator.isDone()) {
                element = (Element) iterator.currentNode();

                try {
                    card_number = element.getElementsByTagName("CardNumber").item(0).getTextContent();
                }catch(Exception e){
                    card_number="null";
                }
                // Implementing Factory Method Pattern to get the Card Factory object
                CardVerify verify = new CardVerify();

                // Using the factory object to create the appropriate Object of Subclass of Card
                Card card = CardVerify.createCard(card_number);

                //Finally validate the card and return the card_type
                if(card==null){
                    error_message = "InvalidCardNumber";
                    card_type = "Invalid";
                }else{
                    output = card.CardTypeValidation(card_number);
                    if (output.equals("Invalid")) {
                        error_message = "InvalidCardNumber";
                    }
                    card_type=output;
                }

                Element row = outputDoc.createElement("row");
                root.appendChild(row);

                Element cardNumber = outputDoc.createElement("CardNumber");
                cardNumber.appendChild(outputDoc.createTextNode(card_number));
                row.appendChild(cardNumber);

                Element cardType = outputDoc.createElement("CardType");
                cardType.appendChild(outputDoc.createTextNode(card_type));
                row.appendChild(cardType);

                Element error = outputDoc.createElement("Error");
                error.appendChild(outputDoc.createTextNode(error_message));
                row.appendChild(error);

                iterator.next();
            }

            this.write(outputFilename);
        }catch(Exception e){
            System.out.println("Error while processing records:"+e);

        }
    }

    @Override
    public void write(String outputFilename) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        outputDoc.setXmlStandalone(true);
        DOMSource domSource = new DOMSource(outputDoc);
        StreamResult streamResult = new StreamResult(new File(outputFilename));

        transformer.transform(domSource, streamResult);
    }
}
