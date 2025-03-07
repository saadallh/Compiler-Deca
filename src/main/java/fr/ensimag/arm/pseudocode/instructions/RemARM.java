package fr.ensimag.arm.pseudocode.instructions;

import fr.ensimag.arm.pseudocode.FunctionsInstructionArm;
import fr.ensimag.arm.pseudocode.GPRegisterArm;
import fr.ensimag.arm.pseudocode.OperandArm;
import fr.ensimag.arm.pseudocode.TernaryInstructionArm;

import java.io.PrintStream;

/**
* ModuloArm: it is simply a matter of repeated subtraction
* ro = r1%r2
* @author aitdriss
* @date 23/01/2023
*/

public class RemARM extends FunctionsInstructionArm {
	public RemARM() {
		super();
	}

	 
	 @Override
		protected void display(PrintStream s){
		s.println("fct_rem_arm :");
		 s.println("#start of division algorithm to calculate the modulo");
		 s.println("substract_rem_arm:");
		 s.println("\t\tsubs r0, r0 , r1");
		 s.println("\t\tBPL substract_rem_arm");
		 s.println("\t\tadd r0, r0  ,r1");
		 s.println("#end of division algorithm to calculate the modulo");
		 s.println("\t\tbx lr");
	 }

	 @Override
		public boolean equals(Object o){
		if (o instanceof RemARM){
			return true;
		}
		return false;
	 }

}
