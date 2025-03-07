package fr.ensimag.arm.pseudocode.instructions;

import java.io.PrintStream;

import fr.ensimag.arm.pseudocode.*;

/**
* ModuloArm: it is simply a matter of repeated subtraction
* ro = r1//r2
* Uses the register r9 for temporary calculations, it's value is restored at the end
* @author aitdriss
* @date 23/01/2023
*/

public class QuoARM extends FunctionsInstructionArm {
	public QuoARM() {
		super();
	}

	 
	 @Override
		protected void display(PrintStream s){
		s.println("fct_quo_arm:");
		 s.println("#start of division algorithm");
		 s.println("\t\tpush {r9}");
		 s.println("\t\tmov r9 , #0");
		 s.println("substract_quo_arm:");
		 s.println("\t\tsubs r0, r0 , r1");
		 s.println("\t\tadd r9, r9, #1");
		 s.println("\t\tBPL substract_quo_arm");
		 s.println("\t\tsub r9, r9, #1");
		 s.println("\t\tmov r0, r9");
		 s.println("#end of division algorithm");
		 s.println("\t\tpop {r9}");
		 s.println("\t\tbx lr");
	 }

}
