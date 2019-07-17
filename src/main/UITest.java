package main;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UITest {
    ByteArrayOutputStream output = new ByteArrayOutputStream();

    @Before
    public void setSystemOutputStream() {
        System.setOut(new PrintStream(output));
    }

    @Test
    public void handleInput_update() {
        UI.getInstance().handleInput("update");
        assertEquals("done\n", output.toString());
    }

    @Test
    public void handleInput_id_exists() {
        DAO.getInstance().insertRecord(new Record(-1,"test","test1","test2",3));
        UI.getInstance().handleInput("-1");
        assertEquals("id: -1\ntitle: test\ndes: test1\nlink: test2\nviewCount: 4\n",output.toString());
        DAO.getInstance().deleteRecord(-1);
    }

    @Test
    public void handleInput_id_dosNotExist() {
        DAO.getInstance().deleteRecord(-1);
        UI.getInstance().handleInput("-1");
        assertEquals("not found.\n",output.toString());
    }

    @Test
    public void handleInput_exit() {
        assertTrue(UI.getInstance().handleInput("exit"));
    }

    @Test
    public void handleInput_wrongCommand() {
        UI.getInstance().handleInput(":)");
        assertEquals("command not found\n",output.toString());
    }
}
