package sml;

import static sml.LanguageOperation.*;

/**
 * 
 * @author sbaird02
 *
 */
@InstructionType(out)
public class OutInstruction extends Instruction {
	
	private int register;

	public OutInstruction(String label, int register) {
		
		super(label, out.name());
		if (!isValidRegister(register)) {
			throw new IllegalArgumentException(String.format(ILLEGAL_REGISTER_MSG, register));
		}
		this.register = register;
	}

	@Override
	public void execute(Machine m) {
		
		System.out.println(String.format("r:%d = %d", register, m.getRegisters().getRegister(register)));
	}
	
    @Override
    public String toString() {    	
        return super.toString() + " register " + register;
    }
}
