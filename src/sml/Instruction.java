package sml;

/**
 * This class is the superclass of the classes for machine instructions
 *
 * @author sbaird02
 */

public abstract class Instruction {

	protected String label;
	protected String opcode;
	//
	private final int MAX_NUM_REGISTERS = 32;
	protected final String ILLEGAL_REGISTER_MSG = "An illegal register was specified: %d";
	//
	protected final String ILLEGAL_OPCODE_MSG = "An illegal language operation was specified: %s";

	// Constructor: an instruction with label l and opcode op
	// (op must be an operation of the language)

	public Instruction(String l, String op) {
		this.label = l;
		if (!isValidOpCode(op)) {
			throw new IllegalArgumentException(String.format(ILLEGAL_OPCODE_MSG, op));
		}
		this.opcode = op;
	}

	// = the representation "label: opcode" of this Instruction

	@Override
	public String toString() {
		return label + ": " + opcode;
	}

	// Execute this instruction on machine m.

	public abstract void execute(Machine m);

	/**
	 * Use the label to identify the instruction in the program
	 * @return label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Return true if the register is in the valid range.
	 * 
	 * @param register
	 *            register to test
	 * @return true if this is a valid register, false otherwise
	 */
	protected boolean isValidRegister(int register) {
		return (register >= 0 && register < MAX_NUM_REGISTERS);
	}

	/**
	 * Return true if the supplied op code is valid
	 * 
	 * @param op
	 *            a String representing the op code
	 * @return true if this is a valid op code, false otherwise
	 */
	protected boolean isValidOpCode(String op) {

		try {
			LanguageOperations.valueOf(op);
		} catch (IllegalArgumentException e) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Instruction other = (Instruction) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		return true;
	}
	
}
