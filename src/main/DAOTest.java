package main;

import mockit.Expectations;
import mockit.Mocked;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DAOTest {
    @Before
    public void insertTestData(){
        DAO.getInstance().deleteRecord(-1);
        DAO.getInstance().insertRecord(new Record(-1,"test","a test news","test.com",2));
    }

    @Test
    public void getRecord_recordExists(){
        Record record = DAO.getInstance().getRecord(-1);
        assertEquals(record.title,"test");
        assertEquals(record.des,"a test news");
        assertEquals(record.link,"test.com");
        assertEquals(record.viewCount,2);
    }

    @Test
    public void getRecord_recordDoesNotExist(){
        DAO.getInstance().deleteRecord(-2);
        try {
            DAO.getInstance().deleteRecord(-2);
        }catch (Exception e){
            fail();
        }
        assertNull(DAO.getInstance().getRecord(-2));
    }

    @Test
    public void deleteRecord_recordExists(){
        DAO.getInstance().deleteRecord(-1);
        assertNull(DAO.getInstance().getRecord(-1));
    }

    @Test
    public void deleteRecord_recordDoesNotExist(){
        try {
            DAO.getInstance().deleteRecord(-1);
            DAO.getInstance().deleteRecord(-1);
        }catch (Exception e){
            fail();
        }
    }

    @Test
    public void incrementViewCount_zeroAndOneAndMore(){
        DAO.getInstance().deleteRecord(-1);
        DAO.getInstance().insertRecord(new Record(-1,"test","a test news","test.com",0));

        Record record = DAO.getInstance().getRecord(-1);
        assertEquals(record.viewCount,0);
        DAO.getInstance().incrementViewCount(-1);
        record = DAO.getInstance().getRecord(-1);
        assertEquals(record.viewCount,1);
        DAO.getInstance().incrementViewCount(-1);
        record = DAO.getInstance().getRecord(-1);
        assertEquals(record.viewCount,2);
    }

    @Test
    public void incrementViewCount_recordDoesNotExist(){
        DAO.getInstance().deleteRecord(-1);
        try {
            DAO.getInstance().incrementViewCount(-1);
        }catch (Exception e){
            fail();
        }
    }

    @After
    public void deleteTestRecord(){
        DAO.getInstance().deleteRecord(-1);
    }
}
