package fr.ensimag.deca.tree;

import fr.ensimag.arm.pseudocode.LabelArm;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;

/**
 * Variable declaration
 *
 * @author gl24
 * @date 01/01/2023
 */
public abstract class AbstractDeclVar extends Tree {
    
    /**
     * Implements non-terminal "decl_var" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains "env_types" attribute
     * @param localEnv 
     *   its "parentEnvironment" corresponds to the "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to 
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to 
     *      the synthetized attribute
     * @param currentClass 
     *          corresponds to the "class" attribute (null in the main bloc).
     */    
    protected abstract void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;


    /**
     * Stores the variable in the stack and sets its adress to varOffset(refReg)
     * @param compiler
     * @param varOffset
     * @param refReg
     */
    protected abstract void codeGenDeclVariable(DecacCompiler compiler, int varOffset, Register refReg);



    protected void codeGenDeclVariableArm(DecacCompiler compiler, LabelArm lab) {
    }


}

