package fr.ensimag.deca.tree;

import fr.ensimag.arm.pseudocode.DValArm;
import fr.ensimag.arm.pseudocode.LabelArm;
import fr.ensimag.arm.pseudocode.OperandArm;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;

import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.instructions.LOAD;
import org.apache.commons.lang.Validate;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl24
 * @date 01/01/2023
 */
public abstract class AbstractExpr extends AbstractInst {
    /**
     * @return true if the expression does not correspond to any concrete token
     * in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        return false;
    }

    /**
     * Get the type decoration associated to this expression (i.e. the type computed by contextual verification).
     */
    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }
    private Type type;

    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
    }

    /**
     * Verify the expression for contextual error.
     * 
     * implements non-terminals "expr" and "lvalue" 
     *    of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  (contains the "env_types" attribute)
     * @param localEnv
     *            Environment in which the expression should be checked
     *            (corresponds to the "env_exp" attribute)
     * @param currentClass
     *            Definition of the class containing the expression
     *            (corresponds to the "class" attribute)
     *             is null in the main bloc.
     * @return the Type of the expression
     *            (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments 
     * 
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  contains the "env_types" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute            
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass, 
            Type expectedType)
            throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	Type t = this.verifyExpr(compiler, localEnv, currentClass);
    	if (t.sameType(expectedType)) {
    		return this;
    	}
    	if (expectedType.isFloat() && t.isInt()) {
    		ConvFloat res = new ConvFloat(this);
    		res.verifyExpr(compiler, localEnv, currentClass);
    		return res;
    	}
    	if (compiler.environmentType.subType(t, expectedType)) {
    		return this;
    	}
    	throw new ContextualError("Assignment Error", this.getLocation());
    }
    
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	this.verifyExpr(compiler, localEnv, currentClass);
    	
    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv
     *            Environment in which the condition should be checked.
     * @param currentClass
     *            Definition of the class containing the expression, or null in
     *            the main program.
     */
    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	Type t = this.verifyExpr(compiler, localEnv, currentClass);
    	if (!t.isBoolean()) {
    		throw new ContextualError("not boolean", this.getLocation());
    	}
    	this.setType(t);
    }

    /**
     * Generate code to print the expression
     * @param compiler
     */
    protected void codeGenPrint(DecacCompiler compiler, boolean hex) {
        throw new DecacInternalError("expression cannot be printed");
    }



    /**
     * Generate the code corresponding to the instruction
     * @param compiler
     * @param endIf : useful to store the endIf label in if instructions
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler, Label endIf) {
        throw new UnsupportedOperationException("no available code generation for this instruction");
    }




    /**
     * Generate assembly code for the initialization of the expression
     * @param compiler
     * @param adr
     */
    protected void codeGenInit(DecacCompiler compiler, DAddr adr){
        throw new DecacInternalError("Shouldn't be initialized");
    }

    /**
     * Generate assignment code for the expression
     * @param compiler
     * @param left: can be either identifier or selection
     *
     */
    protected void codeGenAssign(DecacCompiler compiler, AbstractLValue left){
        if (left.isIdent()){
            this.codeGenInit(compiler, ((Identifier)left).getExpDefinition().getOperand());
        }
        //todo:
    }


    /**
     * Loads the value of the expression in a register and sets it as used (increments the current register)
     * We need to update the register descriptor after no longer using it
     * @param compiler
     * @return register
     */
    protected DVal codeGenLoad(DecacCompiler compiler){
        throw new DecacInternalError("Cannot load the expression");
    }


    /**
     * Loads the value of the expression in R0,
     * Used as a return in assembly
     * @param compiler
     */
    protected void codeGenReturn(DecacCompiler compiler){
        GPRegister reg = (GPRegister) codeGenLoad(compiler);
        compiler.addInstruction(new LOAD(reg, Register.R0));
        compiler.freeReg();
    }



    /**
     * Generate branching operations
     * branch to this label if the exprBool == b
     * @param compiler
     * @param b : true or false/ compare the exprBool to this
     * @param label :
     */
    protected void codeGenBranch(DecacCompiler compiler, boolean b, Label label){
        throw new DecacInternalError("Expression cannot be used for boolean expressions");
    }


    /**
     *  Generate code to print ident depending on the type of the expression
     *  Useful for println(this.x) for example
     *  the expression in this case can be :
     * @param compiler
     * @param printHex
     * @param ident
     */
    protected void codeGenPrint(DecacCompiler compiler, boolean printHex, Identifier ident){
        throw new DecacInternalError("Not implemented yet");
    }


    /**
     * Sets the adresses of the expressions when necessary
     * case1: field in the form of : x (inside the scope of a class) -> use -2(LB) and index of field
     * case2: Selection this.x: same as case1
     * case3: c.x with c not a field, -> use @(c) and index of field
     * case4: c.x with c a field -> use -2(LB) and index of c and set @(C), use @(C) and index of field
     * The selection can be recursive
     * Should be called before using any function that uses the adresses of operands:
     * -->codeGenPrint, codeGenLoad, codeGenAssign, codeGenBranch, codeGenInit
     * !!This function locks a register if refReg is null, we need to use freeReg() after
     * @param compiler
     * @param refReg: Is the reference register use to construct the adress, if it's null we get a free register
     * @return if it has set the adress it returns true
     */
    protected boolean setAdrField(DecacCompiler compiler, GPRegister refReg){
        return false;
    }


    /**
     * Called when we are in a selection, useful for recursion
     * @param compiler
     * @param refReg
     * @param ident
     * @return
     */
    protected boolean setAdrField(DecacCompiler compiler, GPRegister refReg, Identifier ident){
        return false;
    }




    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }




    /*****************************Methods for arm generation**************************************/
    /**
     * Generate code to print the expression
     * @param compiler
     */
    protected void codeGenPrintArm(DecacCompiler compiler, boolean hex) {
        //throw new DecacInternalError("expression cannot be printed");

    }


    /**
     * Generate code to print the expression
     * @param compiler
     */
    protected void codeGenPrintLNArm(DecacCompiler compiler, boolean hex) {
        //throw new DecacInternalError("expression cannot be printed");

    }


    protected void codeGenInitArm(DecacCompiler compiler, OperandArm adr){
        throw new DecacInternalError("Not yet implemented");
    }


    protected void codeGenAssignArm(DecacCompiler compiler, Identifier identifier){
       codeGenInitArm(compiler, identifier.getExpDefinition().getOperandArm());
    }


    protected DValArm codeGenLoadArm(DecacCompiler compiler){
        throw new DecacInternalError("Not yet implemented");
    }



    /**
     * Generate the code corresponding to the instruction
     * @param compiler
     * @param endIf : useful to store the endIf label in if instructions
     */
    @Override
    protected void codeGenInstArm(DecacCompiler compiler, LabelArm endIf) {
        throw new UnsupportedOperationException("no available code generation for this instruction");
    }


    /**
     * Generate branching operations ( ARM version)
     * branch to this label if the exprBool == b
     * @param compiler
     * @param b : true or false/ compare the exprBool to this
     * @param label :
     */
    protected void codeGenBranchArm(DecacCompiler compiler, boolean b, LabelArm label){
        throw new DecacInternalError("Expression cannot be used for boolean expressions");
    }

}
