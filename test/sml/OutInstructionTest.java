package sml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class OutInstructionTest {

	Instruction instance;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testOutInstructionConstructorLabelValidOpcode() {

		instance = new OutInstruction("label", LanguageOperations.out.name());
		assertNotNull(instance);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOutInstructionConstructorLabelInValidOpcode() {

		instance = new OutInstruction("label", "InValidOpcode");
	}

	@Test
	public void testOutInstructionConstructorLabelRegister() {

		instance = new OutInstruction("label", 0);
		assertNotNull(instance);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOutInstructionConstructorLabelInvalidRegister() {

		instance = new OutInstruction("label", 32);
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
		String expectedToString = expectedLabel + ": " + expectedOpCode + " register " + expectedRegister;
		instance = new OutInstruction(expectedLabel, expectedRegister);
		String resultToString = instance.toString();
		assertEquals(expectedToString, resultToString);
	}
}
