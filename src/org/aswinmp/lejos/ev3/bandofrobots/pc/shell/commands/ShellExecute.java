package org.aswinmp.lejos.ev3.bandofrobots.pc.shell.commands;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * Annotation interface for a shell command execute method.
 * 
 * @author mpscholz
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ ElementType.METHOD })
public @interface ShellExecute {

}
