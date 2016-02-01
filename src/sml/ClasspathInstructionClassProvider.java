package sml;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Implementation of the InstructionClassProvider that walks the class path
 * looking for candidate classes. A valid Instruction is defined as one that
 * extends Instruction, has an InstructionType annotation and only one
 * Constructor
 * 
 * @author sbaird02
 *
 */
public class ClasspathInstructionClassProvider implements InstructionClassProvider {

	private final Collection<Class<?>> instructionClasses;

	Class<?> instructionSuperclass = Instruction.class; // The superclass

	private final String CLASS_SUFFIX_MATCHER = ".class";
	private final String FILE_SEPARATOR = System.getProperty("file.separator");
	private final String PACKAGE_SEPARATOR = ".";
	private final int EXPECTED_CONSTRUCTOR_COUNT = 1;

	{
		instructionClasses = new HashSet<>();
	}

	@Override
	public Collection<Class<?>> getClasses() {

		String path = System.getProperty("java.class.path");
		String sep = System.getProperty("path.separator");

		String[] pathElements = path.split(sep);
		for (String element : pathElements) {
			Path p = Paths.get(element);
			try {
				Files.walkFileTree(p, new ResolveFiles(element));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return instructionClasses;
	}

	/*
	 * The mehtod that tests for validity
	 */
	private void resolveValidFile(File f, String root) {

		// Test file for validity
		if (f.getName().endsWith(CLASS_SUFFIX_MATCHER)) {
			//
			String packagePrefix = f.getPath();
			// Remove the root if it has been supplied
			if (root != null) {
				packagePrefix = packagePrefix.substring(root.length() + (root.endsWith(FILE_SEPARATOR) ? 0 : 1));
			}
			// Change separators to '.'
			packagePrefix = packagePrefix.replaceAll(FILE_SEPARATOR, PACKAGE_SEPARATOR);
			// Strip off the '.class'
			String classBaseName = packagePrefix.substring(0, packagePrefix.length() - CLASS_SUFFIX_MATCHER.length());
			try {
				Class<?> c = Class.forName(classBaseName);
				// See doc for validity
				if (instructionSuperclass.equals(c.getSuperclass()) && c.isAnnotationPresent(InstructionType.class)
						&& c.getConstructors().length == EXPECTED_CONSTRUCTOR_COUNT) {
					instructionClasses.add(c);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	class ResolveFiles extends SimpleFileVisitor<Path> {

		private final String pathRoot;

		public ResolveFiles(String pathRoot) {
			super();
			Objects.requireNonNull(pathRoot);
			this.pathRoot = pathRoot;
			// TODO Auto-generated constructor stub
		}

		private final String JAR_SUFFIX_MATCHER = ".jar";

		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

			Objects.requireNonNull(file);
			Objects.requireNonNull(attrs);
			if (isJarFile(file)) {
				resolveJarFile(file.toFile());
			} else {
				resolveValidFile(file.toFile(), pathRoot);
			}
			return FileVisitResult.CONTINUE;
		}

		private void resolveJarFile(File file) {

			try (ZipFile jar = new ZipFile(file)) {
				Enumeration<? extends ZipEntry> jarEnum = jar.entries();
				while (jarEnum.hasMoreElements()) {
					ZipEntry z = (ZipEntry) jarEnum.nextElement();
					resolveValidFile(new File(z.getName()), null);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private boolean isJarFile(Path file) {
			return file == null ? false : file.toString().endsWith(JAR_SUFFIX_MATCHER);
		}

	}
}
