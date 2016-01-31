package sml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class MulInstructionTest {

	Instruction instance;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testOutInstructionConstructorLabelValidOpcode() {

		instance = new MulInstruction("label", LanguageOperation.mul.name());
		assertNotNull(instance);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOutInstructionConstructorLabelInValidOpcode() {

		instance = new MulInstruction("label", "InValidOpcode");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOutInstructionConstructorLabelInvalidR1Register() {

		instance = new MulInstruction("label", 0, -1, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOutInstructionConstructorLabelInvalidR2Register() {

		instance = new MulInstruction("label", 0, 0, -1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOutInstructionConstructorLabelInvalidResultRegister() {

		instance = new MulInstruction("label", -1, 0, 0);
	}

	@Test
	public void testToStringConstructorLabelOpcode() {

		String expectedLabel = "label";
		String expectedOpCode = "mul";
		int lhRegister = 0;
		int rhRegister = 2;
		int resultRegister = 1;
		String expectedToString = expectedLabel + ": " + expectedOpCode + " " + lhRegister + " * " + rhRegister + " to "
				+ resultRegister;
		instance = new MulInstruction(expectedLabel, resultRegister, lhRegister, rhRegister);
		String resultToString = instance.toString();
		assertEquals(expectedToString, resultToString);
	}

	@Test
	public void testToStringConstructorLabelRegister() {

		String expectedLabel = "label";
		String expectedOpCode = "mul";
		int lhRegister = 0;
		int rhRegister = 0;
		int resultRegister = 0;
		String expectedToString = expectedLabel + ": " + expectedOpCode + " " + lhRegister + " * " + rhRegister + " to "
				+ resultRegister;
		instance = new MulInstruction(expectedLabel, expectedOpCode);
		String resultToString = instance.toString();
		assertEquals(expectedToString, resultToString);
	}
}
