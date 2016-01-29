package sml;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

public class OutInstructionTest {
	
	OutInstruction instance;

	@Before
	public void setUp() throws Exception {
	}



	@Test
	public void testOutInstructionConstructorLabelOpcode() {
				
		instance = new OutInstruction("label", "opcode");
		assertNotNull(instance);
	}
	@Test
	public void testOutInstructionConstructorLabelRegister() {
				
		instance = new OutInstruction("label", 0);
		assertNotNull(instance);
	}
	@Test
	public void testToStringConstructorLabelOpcode() {
		
		String expectedLabel = "label";
		String expectedOpCode = "out";
		String expectedToString = expectedLabel + ": " + expectedOpCode + " register 0";
		instance = new OutInstruction(expectedLabel, expectedOpCode);
		String resultToString = instance.toString();
		assertEquals(expectedToString, resultToString);
	}
	@Test
	public void testToStringConstructorLabelRegister() {
		
		String expectedLabel = "label";
		String expectedOpCode = "out";
		int expectedRegister = 31;
		String expectedToString = expectedLabel + ": " + expectedOpCode + " register " +  expectedRegister;
		instance = new OutInstruction(expectedLabel, expectedRegister);
		String resultToString = instance.toString();
		assertEquals(expectedToString, resultToString);
	}
}
