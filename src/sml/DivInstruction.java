package sml;

import static sml.LanguageOperation.*;

/**
 * An instruction to integer divide the contents of two registers and store the result
 * in another register
 * 
 * @author sbaird02
 *
 */
@InstructionType(div)
public class DivInstruction extends Instruction {

	private int register1;
	private int register2;
	private int resultRegister;

	public DivInstruction(String l, String op) {
		super(l, op);
	}

	/**
	 * Construct the Instruction supplying registers for division
	 * 
	 * @param label
	 *            the program label
	 * @param resultRegister
	 *            an integer specifying the register to hold the result
	 * @param register1
	 *            an integer specifying the register which holds the first value
	 * @param register2
	 *            an integer specifying the register which holds the second
	 *            value
	 * @throws IllegalArgumentException
	 *             if any of the supplied registers is invalid
	 */
	public DivInstruction(String label, int resultRegister, int register1, int register2) {

		super(label, div.name());
		if (!isValidRegister(resultRegister)) {
			throw new IllegalArgumentException(String.format(ILLEGAL_REGISTER_MSG, resultRegister));
		}
		this.resultRegister = resultRegister;
		if (!isValidRegister(register1)) {
			throw new IllegalArgumentException(String.format(ILLEGAL_REGISTER_MSG, register1));
		}
		this.register1 = register1;
		if (!isValidRegister(register2)) {
			throw new IllegalArgumentException(String.format(ILLEGAL_REGISTER_MSG, register2));
		}
		this.register2 = register2;
	}

	@Override
	public void execute(Machine m) {

		int value1 = m.getRegisters().getRegister(register1);
		int value2 = m.getRegisters().getRegister(register2);
		m.getRegisters().setRegister(resultRegister, value1 / value2);	 // Allow divide by zero exception
	}

	@Override
	public String toString() {
		return String.format("%s %d / %s to %d", super.toString(), register1, register2, resultRegister);
	}
}
