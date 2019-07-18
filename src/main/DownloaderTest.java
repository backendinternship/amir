package main;

import mockit.Expectations;
import mockit.MockUp;
import mockit.Mocked;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.Assert.fail;

public class DownloaderTest {

    @Test
    public void downloadRSS_wrongURL() {
        try {
            Files.delete(Paths.get("rss_test.xml"));
        } catch (IOException ignored) {
        }

        try {
            Downloader.getInstance().downloadRSS("rss_test.xml", "google.com");
            if (isValidXml("rss_test.xml")) {
                fail();
            }
        } catch (IOException ignored) {
        }
    }

    @Test
    public void downloadRSS_rightURL() {
        try {
            Files.delete(Paths.get("rss_test.xml"));
        } catch (IOException ignored) {
        }

        try {
            Downloader.getInstance().downloadRSS("rss_test.xml", Downloader.RSS_ADDRESS);
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
}
