package sml;

import java.util.Optional;
import static sml.LanguageOperation.*;

/**
 * An instruction to branch to a labelled instruction if the contents of the
 * specified register is not zero
 * 
 * @author sbaird02
 *
 */
@InstructionType(bnz)
public class BnzInstruction extends Instruction {

	private int testRegister;
	private String branchLabel;
	
	private final String ILLEGAL_BRANCH_LABEL_MSG = "The branch label cannot be null";
	private final String UNKNOWN_BRANCH_LABEL_MSG = "Unable to locate the branch label: %s";
	//
	

//	public BnzInstruction(String l, String op) {
//		super(l, op);
//	}

	/**
	 * Construct the Instruction supplying test register and branch label. The
	 * validity of the branch label is not tested at construction
	 * 
	 * @param label
	 *            the program label
	 * @param testRegister
	 *            an integer whose value will be compared to zero
	 * @param branchLabel
	 *            the label to branch to if the condition is satisfied
	 * @throws IllegalArgumentException
	 *             if the testRegister is invalid or the branch label is null
	 */
	public BnzInstruction(String label, int testRegister, String branchLabel) {

		super(label, bnz.name());
		if (!isValidRegister(testRegister)) {
			throw new IllegalArgumentException(String.format(ILLEGAL_REGISTER_MSG, testRegister));
		}
		this.testRegister = testRegister;
		if (branchLabel == null) {
			throw new IllegalArgumentException(ILLEGAL_BRANCH_LABEL_MSG);
		}
		this.branchLabel = branchLabel;
		requiredLabels.add(branchLabel);
	}

	@Override
	public void execute(Machine m) {

		if (m.getRegisters().getRegister(testRegister) != 0) {
			Optional<Instruction> opt = m.getProg().stream().filter(i -> branchLabel.equals(i.getLabel())).findFirst();
			if (!opt.isPresent()) {
				throw new IllegalStateException(String.format(UNKNOWN_BRANCH_LABEL_MSG, branchLabel));
			}
			m.setPc(m.getProg().indexOf(opt.get()));
		}
	}

	@Override
	public String toString() {
		return String.format("%s %d <> 0 branch to %s", super.toString(), testRegister, branchLabel);
	}
}
