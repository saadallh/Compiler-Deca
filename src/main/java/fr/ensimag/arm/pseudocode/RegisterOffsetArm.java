package fr.ensimag.arm.pseudocode;

import java.io.PrintStream;

import fr.ensimag.arm.pseudocode.DAddrArm;
import fr.ensimag.arm.pseudocode.RegisterArm;

/**
 * Operand representing a register indirection with offset, e.g. 42(R3).
 *
 * @author Ensimag
 * @date 01/01/2023
 */
public class RegisterOffsetArm extends DAddrArm {
    public int getOffset() {
        return offset;
    }
    public RegisterArm getRegister() {
        return register;
    }
    private final int offset;
    private final RegisterArm register ;
    public RegisterOffsetArm(int offset, RegisterArm register) {
        super();
        this.offset = offset;
        this.register = register;
    }
    @Override
    public String toString() {
        return "[" + register + "]";
    }
    
}
