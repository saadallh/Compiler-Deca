package fr.ensimag.arm.pseudocode;

import java.io.PrintStream;

/**
 * ARM instruction
 *
 * @author ouazzmoh
 * @date 10/01/2023
 */
public abstract class InstructionArm {

    public String getName() {
        return this.getClass().getSimpleName().toLowerCase();
    }

    abstract void displayOperands(PrintStream s);

    abstract protected void display(PrintStream s);
}
