package sml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class BnzInstructionTest {

	Instruction instance;

	@Before
	public void setUp() throws Exception {
	}

//	@Test
	public void testOutInstructionConstructorLabelValidOpcode() {

//		instance = new BnzInstruction("label", LanguageOperation.div.name());
		assertNotNull(instance);
	}

//	@Test(expected = IllegalArgumentException.class)
	public void testOutInstructionConstructorLabelInValidOpcode() {

//		instance = new BnzInstruction("label", "InValidOpcode");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOutInstructionConstructorLabelInvalidRegister() {

		instance = new BnzInstruction("label", -1, "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOutInstructionConstructorLabelInvalidBranchLabel() {

		instance = new BnzInstruction("label", 0, null);
	}

	@Test
	public void testToStringConstructorLabelOpcode() {

		String expectedLabel = "label";
		String expectedOpCode = "bnz";
		int lhRegister = 0;
		String expectedBranchLabel = "branch label";
		String expectedToString = expectedLabel + ": " + expectedOpCode + " " + lhRegister + " <> 0 branch to "
				+ expectedBranchLabel;
		instance = new BnzInstruction(expectedLabel, lhRegister, expectedBranchLabel);
		String resultToString = instance.toString();
		assertEquals(expectedToString, resultToString);
	}
}
