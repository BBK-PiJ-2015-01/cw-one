package sml;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

/*
 * The translator of a <b>S</b><b>M</b>al<b>L</b> program.
 */
public class Translator {

	private static final String SRC = "src";
	// word + line is the part of the current line that's not yet processed
	// word has no whitespace
	// If word and line are not empty, line begins with whitespace
	private String line = "";
	private Labels labels; // The labels of the program being translated
	private ArrayList<Instruction> program; // The program to be created
	private String fileName; // source file of SML code
	//
	private final Map<LanguageOperation, Constructor<?>> instructionMap;
	//
	private final Set<String> requiredLabels;
	//
	// To allow primitive types to be passed in Constructor args
	//
	private final Map<String, Class<?>> primitiveBoxClasses;
	//
	private final String INSTRUCTION_CLASS_PACKAGE = "sml";

	{
		instructionMap = new HashMap<>();
		requiredLabels = new HashSet<>();
		primitiveBoxClasses = new HashMap<>();
		primitiveBoxClasses.put(int.class.getTypeName(), Integer.class);
		primitiveBoxClasses.put(double.class.getTypeName(), Double.class);
		primitiveBoxClasses.put(float.class.getTypeName(), Float.class);
		primitiveBoxClasses.put(long.class.getTypeName(), Long.class);
		primitiveBoxClasses.put(boolean.class.getTypeName(), Boolean.class);
		primitiveBoxClasses.put(char.class.getTypeName(), Character.class);
		primitiveBoxClasses.put(byte.class.getTypeName(), Byte.class);
		primitiveBoxClasses.put(short.class.getTypeName(), Short.class);
	}

	public Translator(String fileName) {
		this.fileName = SRC + "/" + fileName;
		populateInstructionMap();
	}

	private void populateInstructionMap() {

		Path instructionsPath = Paths.get(System.getProperty("user.dir")).resolve(SRC)
				.resolve(INSTRUCTION_CLASS_PACKAGE);
		Class<?> instructionSuperclass = Instruction.class; // The superclass
		File[] packageFiles = instructionsPath.toFile().listFiles();
		for (File f : packageFiles) {
			String classBaseName = INSTRUCTION_CLASS_PACKAGE + "." + f.getName().split("\\.")[0];
			try {
				Class<?> c = Class.forName(classBaseName);
				if (instructionSuperclass.equals(c.getSuperclass()) && c.isAnnotationPresent(InstructionType.class)) {
					// Add the appropriate constructor to the map
					InstructionType typeAnnotation = c.getAnnotation(InstructionType.class);
					LanguageOperation opCode = typeAnnotation.value();
					for (Constructor<?> cons : c.getConstructors()) {
						instructionMap.put(opCode, cons);
						break;
					}
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// translate the small program in the file into lab (the labels) and
	// prog (the program)
	// return "no errors were detected"
	public boolean readAndTranslate(Labels lab, ArrayList<Instruction> prog) {

		try (Scanner sc = new Scanner(new File(fileName))) {
			// Scanner attached to the file chosen by the user
			labels = lab;
			labels.reset();
			program = prog;
			program.clear();

			try {
				line = sc.nextLine();
			} catch (NoSuchElementException ioE) {
				// End of input file
				// return false;
			}

			// Each iteration processes line and reads the next line into line
			while (line != null) {
				// Store the label in label
				String label = scan();

				if (label.length() > 0) {
					Instruction ins = getInstruction(label);
					if (ins != null) {
						labels.addLabel(label);
						program.add(ins);
					}
				}

				try {
					line = sc.nextLine();
				} catch (NoSuchElementException ioE) {
					// End of input file
					// return false;
					break;
				}
			}
		} catch (IOException ioE) {
			System.out.println("File: IO error " + ioE.getMessage());
			return false;
		}
		// Check program validity with respect to branch statements
		final int LABEL_NOT_FOUND = -1;
		for (String label : requiredLabels) {
			if (labels.indexOf(label) == LABEL_NOT_FOUND) {
				throw new IllegalStateException(String.format("Invalid program: required label '%s' not found", label));
			}
		}
		return true;
	}

	// line should consist of an MML instruction, with its label already
	// removed. Translate line into an instruction with label label
	// and return the instruction
	public Instruction getInstruction(String label) {
		int s1; // Possible operands of the instruction
		int s2;
		int r;

		if (line.equals(""))
			return null;

		String[] args = line.split("\\s"); // split the line into arguments

		String ins = scan();
		switch (LanguageOperation.valueOf(ins)) {
		case add:
			r = scanInt();
			s1 = scanInt();
			s2 = scanInt();
			return new AddInstruction(label, r, s1, s2);
		case lin:
			r = scanInt();
			s1 = scanInt();
			return new LinInstruction(label, r, s1);
		case out:
			r = scanInt();
			return new OutInstruction(label, r);
		case sub:
			r = scanInt();
			s1 = scanInt();
			s2 = scanInt();
			return new SubInstruction(label, r, s1, s2);
		case mul:
			r = scanInt();
			s1 = scanInt();
			s2 = scanInt();
			return new MulInstruction(label, r, s1, s2);
		case div:
			r = scanInt();
			s1 = scanInt();
			s2 = scanInt();
			return new DivInstruction(label, r, s1, s2);
		case bnz:
			LanguageOperation opCode = null;
			try {
				opCode = LanguageOperation.valueOf(ins);
			} catch (IllegalArgumentException | NullPointerException e) {
				throw new IllegalStateException(String.format("Unknown instruction code: %s", ins));
			}
			if (!instructionMap.containsKey(opCode)) {
				throw new IllegalStateException(String.format("No instruction type for: %s", ins));
			}
			int argIndex = 0;
			Constructor<?> constructor = instructionMap.get(LanguageOperation.valueOf(ins));

			// List of constructor parameters
			Object[] oArgs = new Object[constructor.getParameterCount()];

			// Instruction arguments are supplied as Strings so they need
			// converting to their appropriate type
			for (Class<?> o : constructor.getParameterTypes()) {
				if (argIndex == 0) {
					oArgs[0] = label; // First arg is always the label stripped
										// out earlier
				} else {
					oArgs[argIndex] = convertStringto(args[argIndex + 1], o);
				}
				argIndex++;
			}
			// Generate the Instruction from its Constructor
			Instruction validInstruction = null;
			try {
				validInstruction = (Instruction) constructor.newInstance(oArgs);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
				return null;
			}
			// Update the set of all required labels
			requiredLabels.addAll(validInstruction.getRequiredLabels());
			return validInstruction;
		}

		// You will have to write code here for the other instructions.

		return null;
	}

	private Object convertStringto(String value, Class<?> t) {

		// Autobox the primitives
		if (t.isPrimitive()) {
			t = primitiveBoxClasses.get(t.getTypeName());
		}

		for (Constructor<?> tCon : t.getConstructors()) {
			if (tCon.getParameterCount() == 1 && tCon.getParameterTypes()[0].equals(String.class)) {
				try {
					return tCon.newInstance(value);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/*
	 * Return the first word of line and remove it from line. If there is no
	 * word, return ""
	 */
	private String scan() {
		line = line.trim();
		if (line.length() == 0)
			return "";

		int i = 0;
		while (i < line.length() && line.charAt(i) != ' ' && line.charAt(i) != '\t') {
			i = i + 1;
		}
		String word = line.substring(0, i);
		line = line.substring(i);
		return word;
	}

	// Return the first word of line as an integer. If there is
	// any error, return the maximum int
	private int scanInt() {
		String word = scan();
		if (word.length() == 0) {
			return Integer.MAX_VALUE;
		}

		try {
			return Integer.parseInt(word);
		} catch (NumberFormatException e) {
			return Integer.MAX_VALUE;
		}
	}
}