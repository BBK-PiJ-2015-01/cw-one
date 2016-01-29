package sml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class SubInstructionTest {

	Instruction instance;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testOutInstructionConstructorLabelValidOpcode() {

		instance = new SubInstruction("label", LanguageOperations.sub.name());
		assertNotNull(instance);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOutInstructionConstructorLabelInValidOpcode() {

		instance = new SubInstruction("label", "InValidOpcode");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOutInstructionConstructorLabelInvalidR1Register() {

		instance = new SubInstruction("label", 0, -1, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOutInstructionConstructorLabelInvalidResultRegister() {

		instance = new SubInstruction("label", -1, 0, 0);
	}

	// @Test(expected = IllegalArgumentException.class)
	public void testOutInstructionConstructorLabelInvalidRegister() {

		instance = new OutInstruction("label", 32);
	}

	// @Test
	public void testToStringConstructorLabelOpcode() {

		String expectedLabel = "label";
		String expectedOpCode = "out";
		String expectedToString = expectedLabel + ": " + expectedOpCode + " register 0";
		instance = new OutInstruction(expectedLabel, expectedOpCode);
		String resultToString = instance.toString();
		assertEquals(expectedToString, resultToString);
	}

	// @Test
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
