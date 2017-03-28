package ro.ubb.lab.repository;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ro.ubb.lab.domain.Rental;
import ro.ubb.lab.domain.validators.Validator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.Date;
import java.util.Optional;

/**
 * Created by horatiu on 27.03.2017.
 */
public class RentalXMLRepo extends InMemoryRepository<Long,Rental> {
    private String fileName;

    public RentalXMLRepo(Validator<Rental> validator, String fn) throws IOException, SAXException, ParserConfigurationException {
        super(validator);
        fileName =fn;
        loadData();
    }

    private void loadData() throws ParserConfigurationException, IOException, SAXException {

        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fileName);
        Element root = doc.getDocumentElement();

        NodeList nodes = root.getChildNodes();

        for (int i =0; i<nodes.getLength(); i++)
        {
            Node node = nodes.item(i);
            if (node instanceof Element)
            {
                Rental rental = createRentalFromNode(node);
                super.save(rental);
            }
        }

    }

    private static Rental createRentalFromNode(Node node)
    {
        Long id = Long.parseLong(((Element) node).getAttribute("id"));
        Long mid = Long.parseLong(getTextContentByTagName((Element) node, "movieID"));
        Long cid = Long.parseLong(getTextContentByTagName((Element) node, "clientID"));


        Rental rental = new Rental(mid, cid);
        rental.setId(id);

        return rental;

    }

    private static String getTextContentByTagName(Element node, String tagName)
    {
        NodeList nodes = node.getElementsByTagName(tagName);
        Node nodeWithTextContent = nodes.item(0);
        String textContent = nodeWithTextContent.getTextContent();
        return textContent;
    }

    private void saveData(Rental rental) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fileName);
        Element root = doc.getDocumentElement();

        Node rentalNode = createRentalNode(doc, root, rental);
        root.appendChild(rentalNode);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(new DOMSource(doc), new StreamResult(fileName));

    }

    private void saveAll() throws ParserConfigurationException, IOException, TransformerException, SAXException {
        Path path = Paths.get(fileName);
        String string = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<movies></movies>";
        Files.write(path, string.getBytes());

        super.findAll().forEach(this::save);
    }

    private static Node createRentalNode(Document doc, Element root, Rental rental)
    {
        Node rentalNode = doc.createElement("rental");

        ((Element) rentalNode).setAttribute("id", ""+rental.getId());
        appendChildNodeWithTextContent(doc, rentalNode, "movieID", ""+rental.getMovieID());
        appendChildNodeWithTextContent(doc, rentalNode, "clientID", ""+rental.getClientID());

        return rentalNode;
    }

    private static void appendChildNodeWithTextContent(Document doc, Node parent, String tagName, String text )
    {
        Node node = doc.createElement(tagName);
        node.setTextContent(text);

        parent.appendChild(node);
    }

    @Override
    public Optional<Rental> save(Rental rental){
        Optional<Rental> a = super.save(rental);

        try{
            saveData(rental);
        }
        catch (ParserConfigurationException | IOException | TransformerException |SAXException e ){
            e.printStackTrace();
        }
        return a;
    }

    @Override
    public Optional<Rental> delete(Long id)
    {

        Optional<Rental> a =super.delete(id);
        try{
            saveAll();
        }
        catch (IOException| ParserConfigurationException | TransformerException | SAXException e){
            e.printStackTrace();
        }
        return a;
    }

    @Override
    public Optional<Rental> update(Rental rental){
        Optional<Rental> a = super.update(rental);
        try{
            saveAll();
        }
        catch (IOException| ParserConfigurationException | TransformerException | SAXException e){
            e.printStackTrace();
        }

        return a;
    }
}
