package main;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.fail;

public class ConfigReaderTest {
    @Before
    public void createTestConfigFile() {
        try {
            Files.delete(Paths.get("testconfig.txt"));
        } catch (IOException ignored) {
        }

        PrintStream ps = null;
        try {
            ps = new PrintStream(new FileOutputStream("testconfig.txt"));
        } catch (FileNotFoundException ignored) {
        }
        ps.println("a: 1");
        ps.println("b: 2");
    }

    @Test
    public void getFromConfigFile_inFile() {
        Assert.assertEquals(ConfigReader.getInstance().getFromConfigFile("a", "testconfig.txt"), "1");
        Assert.assertEquals(ConfigReader.getInstance().getFromConfigFile("b", "testconfig.txt"), "2");
    }

    @Test
    public void getFromConfigFile_notInFile() {
        Assert.assertTrue(ConfigReader.getInstance().getFromConfigFile("z", "testconfig.txt").isEmpty());
    }

    @Test
    public void getFromConfigFile_noFile() {
        try {
            Files.delete(Paths.get("testconfig.txt"));
        } catch (IOException ignored) { }

        try {
            Assert.assertTrue(ConfigReader.getInstance().getFromConfigFile("a","testconfig.txt").isEmpty());
        }catch (Exception e){
            fail();
        }
    }

    @After
    public void deleteTestConfigFile(){
        try {
            Files.delete(Paths.get("testconfig.txt"));
        } catch (IOException ignored) { }
    }
}
