package test;

import model.EffectType;
import model.Player;
import model.Property;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Liu Yuyang
 */
public class PropertyTest {

    @Test
    public void testConstructorAndGetters() {
        Property property = new Property(1, "TestProperty", 500, 50);

        assertEquals(1, property.getPosition());
        assertEquals("TestProperty", property.getName());
        assertEquals(500, property.getPrice());
        assertEquals(50, property.getRent());
        assertFalse(property.isBought());
        assertNull(property.getOwner());
        assertNull(property.getEffectType());
    }

    @Test
    public void testSetters() {
        Property property = new Property(1, "TestProperty", 500, 50);

        property.getPosition();
        assertNotEquals(2, property.getPosition());

        property.setName("NewProperty");
        assertEquals("NewProperty", property.getName());

        property.setPrice(600);
        assertEquals(600, property.getPrice());

        property.setRent(60);
        assertEquals(60, property.getRent());

        property.setBought(true);
        assertTrue(property.isBought());

        Player mockOwner = new Player("MockPlayer", "Blue");
        property.setOwner(mockOwner);
        assertEquals(mockOwner, property.getOwner());
    }

    @Test
    public void testInitializeProperties() throws IOException {
        // Prepare a mock properties file covering all cases
        String mockFileName = "mockGameBoard.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(mockFileName))) {
            writer.write("1,Go,0,0\n");           // GO
            writer.write("2,Property1,100,10\n"); // NORMAL
            writer.write("4,Tax,0,0\n");          // INCOME_TAX
            writer.write("6,NoEffect1,0,0\n");    // NO_EFFECT
            writer.write("9,Chance1,0,0\n");      // CHANCE
            writer.write("11,NoEffect2,0,0\n");   // NO_EFFECT
            writer.write("13,Chance2,0,0\n");     // CHANCE
            writer.write("16,GoToJail,0,0\n");    // GO_TO_JAIL
            writer.write("19,Chance3,0,0\n");     // CHANCE
        }

        // Test the static method
        ArrayList<Property> properties = Property.initializeProperties(mockFileName);

        assertNotNull(properties);
        assertEquals(9, properties.size()); // 9 lines in the mock file

        // Validate effect types for specific positions
        assertEquals(EffectType.GO, properties.get(0).getEffectType());
        assertEquals(EffectType.NORMAL, properties.get(1).getEffectType());
        assertEquals(EffectType.INCOME_TAX, properties.get(2).getEffectType());
        assertEquals(EffectType.NO_EFFECT, properties.get(3).getEffectType());
        assertEquals(EffectType.CHANCE, properties.get(4).getEffectType());
        assertEquals(EffectType.NO_EFFECT, properties.get(5).getEffectType());
        assertEquals(EffectType.CHANCE, properties.get(6).getEffectType());
        assertEquals(EffectType.GO_TO_JAIL, properties.get(7).getEffectType());
        assertEquals(EffectType.CHANCE, properties.get(8).getEffectType());

        // Clean up the mock file
        new File(mockFileName).delete();
    }


    @Test
    public void testToString() {
        Property property = new Property(1, "TestProperty", 500, 50);
        assertEquals("1,TestProperty,500,50", property.toString());
    }
}

