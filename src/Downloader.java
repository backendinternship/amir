import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Downloader {
    public static void main(String[] args) {
        try {
            updateRSS();
            Database.updateRecords(parse());
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<Record> parse() throws ParserConfigurationException, IOException, SAXException {
        File file = new File("rss.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();
        NodeList nlist = doc.getElementsByTagName("item");

        ArrayList<Record> records = new ArrayList<>();
        for (int i = 0; i < nlist.getLength(); i++) {
            Element e = (Element) nlist.item(i);

            String title = e.getElementsByTagName("title").item(0).getTextContent();

            String des = e.getElementsByTagName("description").item(0).getTextContent();

            String link = e.getElementsByTagName("link").item(0).getTextContent();

            String idR = e.getElementsByTagName("guid").item(0).getTextContent();
            Matcher idMatcher = Pattern.compile("p=(\\d*)").matcher(idR);
            int id = idMatcher.find() ? Integer.valueOf(idMatcher.group(1)) : -1;

            records.add(new Record(id, title, des, link, 0));
        }

        return records;
    }

    private static void updateRSS() throws IOException {
        URL url = new URL("https://www.digitaltrends.com/feed/");
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream("rss.xml");
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }
}
