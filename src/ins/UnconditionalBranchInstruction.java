package ins;

import java.util.Optional;

import sml.Instruction;
import sml.InstructionType;
import sml.Machine;

import static sml.LanguageOperation.*;

/**
 * An instruction to branch to a labelled instruction 
 * 
 * @author sbaird02
 *
 */
@InstructionType(brn)
public class UnconditionalBranchInstruction extends Instruction {

	private String branchLabel;

	private final String ILLEGAL_BRANCH_LABEL_MSG = "The branch label cannot be null";
	private final String UNKNOWN_BRANCH_LABEL_MSG = "Unable to locate the branch label: %s";

	/**
	 * Construct the Instruction supplying a branch label. The
	 * validity of the branch label is not tested at construction
	 * 
	 * @param label
	 *            the program label
	 * @param branchLabel
	 *            the label to branch to if the condition is satisfied
	 * @throws IllegalArgumentException
	 *             if the  branch label is null
	 */
	public UnconditionalBranchInstruction(String label, String branchLabel) {

		super(label, brn.name());
		if (branchLabel == null) {
			throw new IllegalArgumentException(ILLEGAL_BRANCH_LABEL_MSG);
		}
		this.branchLabel = branchLabel;
		requiredLabels.add(branchLabel);
	}

	@Override
	public void execute(Machine m) {

		Optional<Instruction> opt = m.getProg().stream().filter(i -> branchLabel.equals(i.getLabel())).findFirst();
		if (!opt.isPresent()) {
			throw new IllegalStateException(String.format(UNKNOWN_BRANCH_LABEL_MSG, branchLabel));
		}
		m.setPc(m.getProg().indexOf(opt.get()));
	}

	@Override
	public String toString() {
		return String.format("%s branch to %s", super.toString(), branchLabel);
	}
}
