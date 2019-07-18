package main;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UITest {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    @Mocked Downloader downloader;
    @Mocked DAO dao;
    @Mocked Record record;

    @Before
    public void setSystemOutputStream() {
        System.setOut(new PrintStream(output));
    }

    @Test
    public void handleInput_update() {
        UI.getInstance().handleInput("update");

        new Verifications(){
            {
                Downloader.getInstance().downloadAndParse(); times=1;
            }
        };
    }

    @Test
    public void handleInput_id_exists() {
        new Expectations(){
            {
                DAO.getInstance().getRecord(anyInt);
                result = record;
            }
        };

        UI.getInstance().handleInput("-1");

        new Verifications(){
            {
                DAO.getInstance().incrementViewCount(-1);
                record.toString();
            }
        };
    }

    @Test
    public void handleInput_id_dosNotExist() {
        new Expectations(){
            {
                DAO.getInstance().getRecord(anyInt);
                result = null;
            }
        };

        UI.getInstance().handleInput("-1");
        assertEquals("not found.\n", output.toString());
    }

    @Test
    public void handleInput_exit() {
        assertTrue(UI.getInstance().handleInput("exit"));
    }

    @Test
    public void handleInput_wrongCommand() {
        UI.getInstance().handleInput(":)");
        assertEquals("command not found\n", output.toString());
    }

    @Test
    public void startUI_all() {
        new Expectations(){
            {
                UI.getInstance().handleInput(anyString);
                result = true;
            }
        };
        System.setIn(new ByteArrayInputStream(":)\n".getBytes()));
        UI.getInstance().startUI();

        new Verifications(){
            {
                UI.getInstance().handleInput(":)");
            }
        };
    }
}
