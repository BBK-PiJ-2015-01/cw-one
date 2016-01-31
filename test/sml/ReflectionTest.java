package sml;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

public class ReflectionTest {

	@Test
	public void testReflectionOnClassList() {

		Path currentPath = Paths.get(System.getProperty("user.dir")).resolve("src/sml");
		Class<?> instructionSuperclass = Instruction.class;
		Constructor<?>[] instCon = instructionSuperclass.getConstructors();
		File[] packageFiles = currentPath.toFile().listFiles();
		for (File f : packageFiles) {
			String className = "sml." + f.getName().split("\\.")[0];
			try {
				Class<?> c = Class.forName(className);
				if (instructionSuperclass.equals(c.getSuperclass())) {
					System.out.println(className + " is an " + instructionSuperclass.getSimpleName());
					for (Constructor con : c.getConstructors()) {
						for (Parameter param : con.getParameters()) {
							System.out.println(con.getName() + " " + param.getType().getSimpleName());
						}
					}
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
