package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;

/**
 * Main block of a Deca program.
 *
 * @author gl24
 * @date 01/01/2023
 */
public abstract class AbstractMain extends Tree {

    private int stackSize;

    public void setStackSize(int stackSize) {
        this.stackSize = stackSize;
    }

    public int getStackSize() {
        return stackSize;
    }

    protected abstract void codeGenMain(DecacCompiler compiler);


    /**
     * Implements non-terminal "main" of [SyntaxeContextuelle] in pass 3 
     */
    protected abstract void verifyMain(DecacCompiler compiler) throws ContextualError;


    protected abstract void codeGenMainArm(DecacCompiler compiler);
}
