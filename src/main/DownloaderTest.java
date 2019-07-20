package main;

import mockit.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.fail;

public class DownloaderTest {
    @After @Before
    public void deleteRssTest(){
        try {
            Files.delete(Paths.get("rss_test.xml"));
        } catch (IOException ignored) {
        }
    }

    @Test
    public void downloadRSS_wrongURL() {
        try {
            Downloader.getInstance().downloadRSS("rss_test.xml", new URL("google.com"));
            if (isValidXml("rss_test.xml")) {
                fail();
            }
        } catch (IOException ignored) {
        }
    }

    @Test
    public void downloadRSS_rightURL() {
        try {
            Downloader.getInstance().downloadRSS("rss_test.xml", Paths.get("sampleRss4Test.xml").toUri().toURL());
            if (!isValidXml("rss_test.xml")) {
                fail();
            }
        } catch (IOException ignored) {
            fail();
        }
    }

    private boolean isValidXml(String address) {
        try (FileInputStream fis = new FileInputStream(address)) {
            String content = new String(fis.readAllBytes());
            if (!(content.contains("item") && content.contains("link") && content.contains("rss"))) {
                return false;
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Test
    public void parse_all() {
        downloadRSS_rightURL();
        try {
            ArrayList<Record> records = Downloader.getInstance().parse("rss_test.xml");
            if (records.size() == 0) {
                fail();
            }
            for (Record record : records) {
                Assert.assertTrue(record.id > 0);
                Assert.assertFalse(record.title.isEmpty());
                Assert.assertFalse(record.des.isEmpty());
                Assert.assertFalse(record.link.isEmpty());
                Assert.assertEquals(0, record.viewCount);
            }
        } catch (ParserConfigurationException | IOException | SAXException | NullPointerException e) {
            fail();
        }
    }

//    @Mocked Downloader testedDownloader;
//    @Mocked DAO dao;
//    @Mocked Record record;
//    @Test
//    public void downloadAndParse_all(){
//        new Expectations(){
//            {
//                try {
//                    Downloader.getInstance().parse(Downloader.XML_FILE);
//                } catch (ParserConfigurationException | IOException | SAXException ignored) {
//                }
//                result = Arrays.asList(record);
//            }
//        };
//
//        testedDownloader.downloadAndParse();
//
//        new Verifications(){
//            {
//                testedDownloader.downloadAndParse();
//
//                try {
//                    testedDownloader.downloadRSS(anyString,(URL)any);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                try {
//                    testedDownloader.parse(anyString);
//                } catch (ParserConfigurationException | IOException | SAXException e) {
//                    e.printStackTrace();
//                }
//
//                DAO.getInstance().insertRecord(record);
//            }
//        };
//    }
}
