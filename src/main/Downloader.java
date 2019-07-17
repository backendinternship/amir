package main;

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

    public static final String TITLE_TAG = "title";
    public static final String DESCRIPTION_TAG = "description";
    public static final String LINK_TAG = "link";
    public static final String GUID_TAG = "guid";
    public static final String GUID_REGEX = "p=(\\d*)";
    public static final String XML_FILE = "rss.xml";
    public static final String RSS_ADDRESS = "https://www.digitaltrends.com/feed/";
    public static final String ITEM_TAG = "item";
    private static Downloader instance = new Downloader();

    private Downloader() {
    }

    public static void main(String[] args) { Downloader.getInstance().downloadAndParse(); }

    public static Downloader getInstance() {
        return instance;
    }

    public void downloadAndParse() {
        try {
            downloadRSS(XML_FILE, RSS_ADDRESS);
            for (Record record : parse(XML_FILE)) {
                DAO.getInstance().insertRecord(record);
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Record> parse(String xmlFile) throws ParserConfigurationException, IOException, SAXException {
        File file = new File(xmlFile);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(file);
        doc.getDocumentElement().normalize();
        NodeList nlist = doc.getElementsByTagName(ITEM_TAG);

        ArrayList<Record> records = new ArrayList<>();
        for (int i = 0; i < nlist.getLength(); i++) {
            Element e = (Element) nlist.item(i);

            String title = e.getElementsByTagName(TITLE_TAG).item(0).getTextContent();

            String des = e.getElementsByTagName(DESCRIPTION_TAG).item(0).getTextContent();

            String link = e.getElementsByTagName(LINK_TAG).item(0).getTextContent();

            String idR = e.getElementsByTagName(GUID_TAG).item(0).getTextContent();
            Matcher idMatcher = Pattern.compile(GUID_REGEX).matcher(idR);
            int id = idMatcher.find() ? Integer.valueOf(idMatcher.group(1)) : -1;

            records.add(new Record(id, title, des, link, 0));
        }

        return records;
    }

    public void downloadRSS(String xmlFile, String adress) throws IOException {
        URL url = new URL(adress);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(xmlFile);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }
}
