package edu.ncsu.csc.itrust2.unit;

import edu.ncsu.csc.itrust2.models.persistent.Lockout;
import org.junit.Test;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests the Lockout object
 *
 * @author dbryan
 */
public class LockoutTest {

    @Test
    public void testLockout(){
        Long testTime = (Long) System.currentTimeMillis();
        Lockout l = new Lockout("dbryan", testTime);

        assertEquals("dbryan", l.getUsername());
        assertEquals(testTime, l.getTimestamp());

        l.setUsername("newUser");
        assertEquals("newUser", l.getUsername());

        testTime = (Long) System.currentTimeMillis();
        l.setTimestamp(testTime);
        assertEquals(testTime, l.getTimestamp());


        testTime = (Long) System.currentTimeMillis();
        Lockout l1 = new Lockout("dbryan", testTime);
        l1.save();
        Lockout l2 = new Lockout("dbryan", testTime + 1);
        l2.save();
        Lockout l3 = new Lockout("dbryan", testTime + 2);
        l3.save();

        List<Lockout> list = Lockout.getUserLockouts("dbryan");
        assertEquals(list.get(0).getTimestamp(), l3.getTimestamp());
        assertEquals(list.get(1).getTimestamp(), l2.getTimestamp());
        assertEquals(list.get(2).getTimestamp(), l1.getTimestamp());

    }
}
