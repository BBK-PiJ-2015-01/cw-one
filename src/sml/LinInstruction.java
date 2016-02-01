package sml;

import static sml.LanguageOperation.*;

/**
 * This class ....
 *
 * @author someone
 */
@InstructionType(lin)
public class LinInstruction extends Instruction {
    private int register;
    private int value;

    public LinInstruction(String label, int register, int value) {
        super(label, lin.name());
        this.register = register;
        this.value = value;

    }

    @Override
    public void execute(Machine m) {
        m.getRegisters().setRegister(register, value);
    }

    @Override
    public String toString() {
        return super.toString() + " register " + register + " value is " + value;
    }
}
