package ro.ubb.lab.repository;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ro.ubb.lab.domain.Movie;
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
public class MovieXMLRepo extends InMemoryRepository<Long, Movie> {

    private String fileName;


    public MovieXMLRepo (Validator<Movie> validator, String fn) throws IOException, SAXException, ParserConfigurationException {
        super(validator);
        fileName = fn;
        loadData();
    }

    /**
     * Loads the data from the XML file
     *
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     *
     */
    private void loadData() throws ParserConfigurationException, IOException, SAXException {

        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fileName);
        Element root = doc.getDocumentElement();

        NodeList nodes = root.getChildNodes();

        for (int i =0; i<nodes.getLength(); i++)
        {
            Node node = nodes.item(i);
            if (node instanceof Element)
            {
                Movie movie = createMovieFromNode(node);
                super.save(movie);
            }
        }

    }

    /**
     * Creates a student from a node
     * @param node must not be null
     * @return movie- the movie entity
     */
    private static Movie createMovieFromNode(Node node)
    {
        Long id = Long.parseLong(((Element) node).getAttribute("id"));
        String name = getTextContentByTagName((Element) node, "name");
        String genre = getTextContentByTagName((Element) node, "genre");

        Movie movie = new Movie(name, genre);
        movie.setId(id);

        return movie;

    }

    /**
     * Returns the text content of a node by a given tag
     * @param node must not be null
     * @param tagName
     * @return textContent -the text content of the given tag in the node
     */
    private static String getTextContentByTagName(Element node, String tagName)
    {
        NodeList nodes = node.getElementsByTagName(tagName);
        Node nodeWithTextContent = nodes.item(0);
        String textContent = nodeWithTextContent.getTextContent();
        return textContent;
    }

    /**
     * Saves the given data to the xml file by appending
     * @param movie must not be null
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws TransformerException
     */
    private void saveData(Movie movie) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fileName);
        Element root = doc.getDocumentElement();

        Node movieNode = createMovieNode(doc, root, movie);
        root.appendChild(movieNode);

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(new DOMSource(doc), new StreamResult(fileName));

    }



    /**
     * Saves all the data from the memory in the xml file overwriting it
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws TransformerException
     * @throws SAXException
     */
    private void saveAll() throws ParserConfigurationException, IOException, TransformerException, SAXException {
        Path path = Paths.get(fileName);
        String string = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                "<movies></movies>";
        Files.write(path, string.getBytes());

        super.findAll().forEach(this::save);
    }

    /**
     * Creates a node for the given movie
     * @param doc must not be null
     * @param root
     * @param movie must not be null
     * @return studNode
     */
    private static Node createMovieNode(Document doc, Element root, Movie movie)
    {
        Node movieNode = doc.createElement("movie");

        ((Element) movieNode).setAttribute("id", ""+movie.getId());
        appendChildNodeWithTextContent(doc, movieNode, "name", movie.getName());
        appendChildNodeWithTextContent(doc, movieNode, "group", ""+movie.getGenre());

        return movieNode;
    }

    /**
     * Appends the given node to the root
     * @param doc must not be null
     * @param parent must not me null
     * @param tagName must not be null
     *
     * @param text
     */
    private static void appendChildNodeWithTextContent(Document doc, Node parent, String tagName, String text )
    {
        Node node = doc.createElement(tagName);
        node.setTextContent(text);

        parent.appendChild(node);
    }

    @Override
    public Optional<Movie> save(Movie movie){
        Optional<Movie> s = super.save(movie);

        try{
            saveData(movie);
        }
        catch (ParserConfigurationException | IOException | TransformerException |SAXException e){
            e.printStackTrace();
        }
        return s;
    }

    @Override
    public Optional<Movie> delete(Long id)
    {
        Optional<Movie> s =super.delete(id);
        try{
            saveAll();
        }
        catch (IOException| ParserConfigurationException | TransformerException | SAXException e){
            e.printStackTrace();
        }
        return s;
    }

    @Override
    public Optional<Movie> update(Movie student){
        Optional<Movie> s = super.update(student);
        try{
            saveAll();
        }
        catch (IOException| ParserConfigurationException | TransformerException | SAXException e){
            e.printStackTrace();
        }

        return s;
    }

}
