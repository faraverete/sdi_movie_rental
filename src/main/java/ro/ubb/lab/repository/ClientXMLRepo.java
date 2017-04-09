package ro.ubb.lab.repository;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ro.ubb.lab.domain.Client;
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
import java.util.Optional;

/**
 * Created by horatiu on 27.03.2017.
 */
public class ClientXMLRepo extends InMemoryRepository<Long, Client> {
    private String fileName;

    public ClientXMLRepo(Validator<Client> validator, String fn) throws ParserConfigurationException, SAXException, IOException {
        super(validator);
        fileName = fn;
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
                Client client = createClientFromNode(node);
                super.save(client);
            }
        }

    }

    private static Client createClientFromNode(Node node)
    {
        Long id = Long.parseLong(((Element) node).getAttribute("id"));
        String name = getTextContentByTagName((Element) node, "name");


        Client client = new Client(name);
        client.setId(id);

        return client;

    }

    private static String getTextContentByTagName(Element node, String tagName)
    {
        NodeList nodes = node.getElementsByTagName(tagName);
        Node nodeWithTextContent = nodes.item(0);
        System.out.println(nodeWithTextContent);
        System.out.println("aici");
        String textContent = nodeWithTextContent.getTextContent();
        System.out.println(textContent);
        return textContent;
    }


    private void saveData(Client client) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fileName);
        Element root = doc.getDocumentElement();

        Node clientNode = createProblemNode(doc, root, client);
        root.appendChild(clientNode);

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

    private static Node createProblemNode(Document doc, Element root, Client client)
    {
        Node clientNode = doc.createElement("client");

        ((Element) clientNode).setAttribute("id", ""+client.getId());
        appendChildNodeWithTextContent(doc, clientNode, "name", client.getName());

        return clientNode;
    }

    private static void appendChildNodeWithTextContent(Document doc, Node parent, String tagName, String text )
    {
        Node node = doc.createElement(tagName);
        node.setTextContent(text);

        parent.appendChild(node);
    }

    @Override
    public Optional<Client> save(Client client){
        Optional<Client> p = super.save(client);

        try{
            saveData(client);
        }
        catch (ParserConfigurationException | IOException | TransformerException |SAXException  e ){
            e.printStackTrace();
        }
        return p;
    }

    @Override
    public Optional<Client> delete(Long id)
    {

        Optional<Client> client =super.delete(id);
        try{
            saveAll();
        }
        catch (IOException| ParserConfigurationException | TransformerException | SAXException e){
            e.printStackTrace();
        }
        return client;
    }

    @Override
    public Optional<Client> update(Client client){
        Optional<Client> p = super.update(client);
        try{
            saveAll();
        }
        catch (IOException| ParserConfigurationException | TransformerException | SAXException e){
            e.printStackTrace();
        }

        return p;
    }
}

