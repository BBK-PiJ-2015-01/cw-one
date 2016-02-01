package sml;

import java.util.Collection;

/**
 * Interface to provide appropriate Instruction classes for reflection
 * 
 * @author sbaird02
 *
 */

/**
 * Get a collection of appropriate Instruction classes. Validity of these
 * classes is defined by the implementation
 * 
 * @author sbaird02
 *
 */
public interface InstructionClassProvider {

	Collection<Class<?>> getClasses();

}
