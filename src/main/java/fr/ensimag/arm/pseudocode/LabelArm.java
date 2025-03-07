package fr.ensimag.arm.pseudocode;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;

/**
 * Representation of a label in ARM code. The same structure is used for label
 * declaration (e.g. foo: instruction) or use (e.g. BRA foo).
 *
 * @author aitdriss
 * @date 10/01/2023
 */

public class LabelArm extends OperandArm {
	    @Override
	    public String toString() {

			return name;
	    }
	    
	    public LabelArm(String name) {
	        super();
	        // a vérifier label name length
			//TODO: Conditions for ARM  labels
	        //Validate.isTrue(name.length() <= 1024, "Label name too long, not supported by IMA");
	        //Validate.isTrue(name.matches("^[a-zA-Z][a-zA-Z0-9_.]*$"), "Invalid label name " + name);
	        this.name = name;
	    }
	    private String name;
		public void display(PrintStream s) {
			// TODO Auto-generated method stub
			s.println(this.toString() + ":");
			
		}
	}
