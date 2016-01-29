package sml;

/**
 * 
 * @author sbaird02
 *
 */
public class OutInstruction extends Instruction {
	
	private int register;

	public OutInstruction(String l, String op) {
		super(l, op);
	}

	public OutInstruction(String label, int register) {
		super(label, "out");
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
