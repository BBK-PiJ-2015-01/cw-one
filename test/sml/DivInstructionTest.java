package sml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class DivInstructionTest {

	Instruction instance;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testOutInstructionConstructorLabelValidOpcode() {

		instance = new DivInstruction("label", LanguageOperations.div.name());
		assertNotNull(instance);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOutInstructionConstructorLabelInValidOpcode() {

		instance = new DivInstruction("label", "InValidOpcode");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOutInstructionConstructorLabelInvalidR1Register() {

		instance = new DivInstruction("label", 0, -1, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOutInstructionConstructorLabelInvalidR2Register() {

		instance = new DivInstruction("label", 0, 0, -1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOutInstructionConstructorLabelInvalidResultRegister() {

		instance = new DivInstruction("label", -1, 0, 0);
	}

	@Test
	public void testToStringConstructorLabelOpcode() {

		String expectedLabel = "label";
		String expectedOpCode = "div";
		int lhRegister = 0;
		int rhRegister = 2;
		int resultRegister = 1;
		String expectedToString = expectedLabel + ": " + expectedOpCode + " " + lhRegister + " / " + rhRegister + " to "
				+ resultRegister;
		instance = new DivInstruction(expectedLabel, resultRegister, lhRegister, rhRegister);
		String resultToString = instance.toString();
		assertEquals(expectedToString, resultToString);
	}

	@Test
	public void testToStringConstructorLabelRegister() {

		String expectedLabel = "label";
		String expectedOpCode = "div";
		int lhRegister = 0;
		int rhRegister = 0;
		int resultRegister = 0;
		String expectedToString = expectedLabel + ": " + expectedOpCode + " " + lhRegister + " / " + rhRegister + " to "
				+ resultRegister;
		instance = new DivInstruction(expectedLabel, expectedOpCode);
		String resultToString = instance.toString();
		assertEquals(expectedToString, resultToString);
	}
}
