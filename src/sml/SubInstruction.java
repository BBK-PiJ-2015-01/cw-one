package sml;

/**
 * An instruction to subtract the contents of two registers and store the result
 * in another register
 * 
 * @author sbaird02
 *
 */
public class SubInstruction extends Instruction {

	private int register1;
	private int register2;
    private int resultRegister;


	public SubInstruction(String l, String op) {
		super(l, op);
	}

	public SubInstruction(String label,  int resultRegister, int register1, int register2) {

		super(label, LanguageOperations.sub.name());
		if (!isValidRegister(resultRegister)) {
			throw new IllegalArgumentException(String.format(ILLEGAL_REGISTER_MSG, resultRegister));
		}
		this.resultRegister = resultRegister;
//		if (!isValidRegister(register1)) {
//			throw new IllegalArgumentException(String.format(ILLEGAL_REGISTER_MSG, register1));
//		}
		this.register1 = register1;
		this.register2 = register2;
	}

	@Override
	public void execute(Machine m) {

//		System.out.println(String.format("r:%d = %d", register, m.getRegisters().getRegister(register)));
	}

//	@Override
//	public String toString() {
//		return super.toString() + " register " + register;
//	}
}
