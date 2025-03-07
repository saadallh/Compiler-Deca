package fr.ensimag.arm.pseudocode;

/**
 * Definition of registers for arm architecture
 *
 * @author aitdriss
 * @date 10/01/2023
 */

public class RegisterArm extends DValArm {
	private String name;
    protected RegisterArm(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name.toLowerCase();
    }

    /**
     * Global Base register
     */
    public static final RegisterArm GB = new RegisterArm("GB");
    /**
     * Link register : R14
     * a register which holds the address to return to when a function completes
     * a function that uses a branch with Link Instruction
     * store the return link
     */
    public static final RegisterArm LR = new RegisterArm("LR");
    /**
     * Stack Pointer : R13
     * Pointer to the active stack
     */
    public static final RegisterArm SP = new RegisterArm("SP");
    /** 
     * Program counter : R15
     * stores the memory address of the next instruction to be executed
     */
    public static final RegisterArm PC = new RegisterArm("PC");
    /** 
     * Application Program Status Register : R16
     */
    public static final RegisterArm CPSR = new RegisterArm("CPSR");
    /**
     * General Purpose Registers. Array is private because Java arrays cannot be
     * made immutable, use getR(i) to access it.
     * there are 13 general purpose registers.
     */
    private static final GPRegisterArm[] R = initRegisters();
    /**
     * General Purpose Registers
     */
    public static GPRegisterArm getR(int i) {
        return R[i];
    }
    /**
     * Convenience shortcut for R[0]
     */
    public static final GPRegisterArm R0 = R[0];
    /**
     * Convenience shortcut for R[1]
     */
    public static final GPRegisterArm R1 = R[1];
    static private GPRegisterArm[] initRegisters() {
        GPRegisterArm [] res = new GPRegisterArm[16];
        for (int i = 0; i <= 15; i++) {
            res[i] = new GPRegisterArm("R" + i, i);
        }
        return res;
    }
}
