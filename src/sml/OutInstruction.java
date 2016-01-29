package sml;

/**
 * 
 * @author sbaird02
 *
 */
public class OutInstruction extends Instruction {
	
	// Added as equivalent value in Registers has private access 
	private final int MAX_NUM_REGISTERS = 32;
	private final String ILLEGAL_REGISTER_MSG = "An illegal register was specified: %d";
	private int register;

	public OutInstruction(String l, String op) {
		super(l, op);
	}

	public OutInstruction(String label, int register) {
		
		super(label, "out");
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
    
    boolean isValidRegister(int register) {
    	return (register >= 0 && register < MAX_NUM_REGISTERS);
    }
}
