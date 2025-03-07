package fr.ensimag.deca.tree;

import fr.ensimag.arm.pseudocode.*;
import fr.ensimag.arm.pseudocode.instructions.*;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import java.io.PrintStream;

import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import fr.ensimag.ima.pseudocode.instructions.CMP;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Deca Identifier
 *
 * @author gl24
 * @date 01/01/2023
 */
public class Identifier extends AbstractIdentifier {
    
    @Override
    protected void checkDecoration() {
        if (getDefinition() == null) {
            throw new DecacInternalError("Identifier " + this.getName() + " has no attached Definition");
        }
    }

    @Override
    public Definition getDefinition() {
        return definition;
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * ClassDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a class definition.
     */
    @Override
    public ClassDefinition getClassDefinition() {
        try {
            return (ClassDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a class identifier, you can't call getClassDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * MethodDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a method definition.
     */
    @Override
    public MethodDefinition getMethodDefinition() {
        try {
            return (MethodDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a method identifier, you can't call getMethodDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * FieldDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public FieldDefinition getFieldDefinition() {
        try {
            return (FieldDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a field identifier, you can't call getFieldDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * VariableDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public VariableDefinition getVariableDefinition() {
        try {
            return (VariableDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a variable identifier, you can't call getVariableDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a ExpDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public ExpDefinition getExpDefinition() {
        try {
            return (ExpDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a Exp identifier, you can't call getExpDefinition on it");
        }
    }

    @Override
    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    @Override
    public Symbol getName() {
        return name;
    }

    private Symbol name;

    public Identifier(Symbol name) {
        Validate.notNull(name);
        this.name = name;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	ExpDefinition def = localEnv.get(name);
    	if (def == null) {
    		throw new ContextualError("le type de la variable n'est pas déclaré", this.getLocation());
    	}
    	this.setDefinition(def);
    	this.setType(def.getType());
    	return this.getType();
    }

    /**
     * Implements non-terminal "type" of [SyntaxeContextuelle] in the 3 passes
     * @param compiler contains "env_types" attribute
     */
    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
    	try {
    		this.setDefinition(compiler.environmentType.defOfType(name));
    		this.setType(compiler.environmentType.defOfType(name).getType());
    		return this.getType();
    	} catch (Exception e) {
    		throw new ContextualError("type not declared in env", this.getLocation());
    	}
    	
    }
    
    
    private Definition definition;


    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(name.toString());
    }

    @Override
    String prettyPrintNode() {
        return "Identifier (" + getName() + ")";
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Definition d = getDefinition();
        if (d != null) {
            s.print(prefix);
            s.print("definition: ");
            s.print(d);
            s.println();
        }
    }



    @Override
    protected void codeGenPrint(DecacCompiler compiler, boolean hex){
        boolean set = setAdrField(compiler, null);//Sets the adress of the identifier when it is a field, returns true if it does
        if (this.getType().isInt()) {
            compiler.addInstruction(new LOAD(this.getExpDefinition().getOperand(), Register.R1));
            compiler.addInstruction(new WINT());
        } else if (this.getType().isFloat()) {
            compiler.addInstruction(new LOAD(this.getExpDefinition().getOperand(), Register.R1));
            if (hex) {
                compiler.addInstruction(new WFLOATX());
            }
            else {
                compiler.addInstruction(new WFLOAT());
            }
        }
        else{
            throw new DecacInternalError("Cannot print expression");
        }
        if (set){
            getExpDefinition().setOperand(null);//This means that the fields adress is no longer useful to keep
            compiler.freeReg();//We free the register storing the adress
        }
    }


    @Override
    protected DVal codeGenLoad(DecacCompiler compiler){
        GPRegister reg = compiler.getFreeReg();
        compiler.useReg();//Using thisReg
        boolean set = setAdrField(compiler, reg);
        compiler.addInstruction(new LOAD(getExpDefinition().getOperand(), reg));
        if (set){
            //In this case it is a field
            getExpDefinition().setOperand(null);
        }
        return reg;
    }



    @Override
    protected void codeGenBranch(DecacCompiler compiler, boolean b, Label label){
        boolean set = setAdrField(compiler, null);
        //TODO: push pop
        GPRegister reg = compiler.getFreeReg();
        compiler.addInstruction(new LOAD(getExpDefinition().getOperand(), reg));
        compiler.addInstruction(new CMP(0, reg));
        if (b){
            compiler.addInstruction(new BNE(label));
        }
        else {
            compiler.addInstruction(new BEQ(label));
        }

        if (set){
            getExpDefinition().setOperand(null);//This means that the fields adress is no longer useful to keep
            compiler.freeReg();//We free the register storing the adress
        }
    }


    @Override
    protected void codeGenInit(DecacCompiler compiler, DAddr adr){
        GPRegister reg = (GPRegister) codeGenLoad(compiler);
        compiler.addInstruction(new STORE(reg, adr));
    }



    @Override
    public boolean isIdent(){
        return true;
    }


    @Override
    protected void codeGenPrint(DecacCompiler compiler, boolean printHex, Identifier ident){
        //This means this is an instance of a class  and the ident is a field (Coming from a selection)
        if (!getExpDefinition().isField() | getExpDefinition().getOperand()!=null){
            //When it isn't a field its adress has already been set
            GPRegister reg = compiler.getFreeReg();
            compiler.useReg();
            compiler.addInstruction(new LOAD(getExpDefinition().getOperand(), reg));
            //ident adress is null
            ident.getExpDefinition().setOperand(new RegisterOffset(ident.getFieldDefinition().getIndex(), reg));
            ident.codeGenPrint(compiler, printHex);
            ident.getExpDefinition().setOperand(null);
            compiler.freeReg();
        }
        else {
            //this is a field we should set the adress by using -2(LB)
            GPRegister thisReg = compiler.getFreeReg();
            compiler.useReg();
            compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), thisReg));
            getExpDefinition().setOperand(new RegisterOffset(getFieldDefinition().getIndex(), thisReg));

            //todo: push pop

            GPRegister reg = compiler.getFreeReg();
            compiler.useReg();
            compiler.addInstruction(new LOAD(getExpDefinition().getOperand(), reg));
            //ident adress is null
            ident.getExpDefinition().setOperand(new RegisterOffset(ident.getFieldDefinition().getIndex(), reg));
            ident.codeGenPrint(compiler, printHex);
            ident.getExpDefinition().setOperand(null);
            compiler.freeReg();
            //setting the adress of this to null
            getExpDefinition().setOperand(null);
            compiler.freeReg();
        }

    }

    @Override
    protected boolean setAdrField(DecacCompiler compiler, GPRegister refReg){
        if (getExpDefinition().isField() && getExpDefinition().getOperand() == null){
            if (refReg == null) {
                GPRegister thisReg = compiler.getFreeReg();
                compiler.useReg();
                compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), thisReg));
                getExpDefinition().setOperand(new RegisterOffset(getFieldDefinition().getIndex(), thisReg));
            }
            else {
                compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), refReg));
                getExpDefinition().setOperand(new RegisterOffset(getFieldDefinition().getIndex(), refReg));
            }
            return true;
        }
        return false;
    }

    @Override
    protected boolean setAdrField(DecacCompiler compiler, GPRegister refReg, Identifier ident){
        //This function is only called from a selection, so we are sure that
        if (!getExpDefinition().isField()){
            GPRegister reg = compiler.getFreeReg();
            compiler.addInstruction(new LOAD(getExpDefinition().getOperand(), reg));
            compiler.useReg();
            ident.getExpDefinition().setOperand(new RegisterOffset(ident.getFieldDefinition().getIndex(), reg));
        }
        else if (getExpDefinition().isField() && getExpDefinition().getOperand() != null){
            GPRegister reg = (GPRegister)( (RegisterOffset) getExpDefinition().getOperand() ).getRegister();
            compiler.addInstruction(new LOAD(getExpDefinition().getOperand(), reg));

            ident.getExpDefinition().setOperand(new RegisterOffset(ident.getFieldDefinition().getIndex(), reg));
        }
        else {
            GPRegister reg = compiler.getFreeReg();
            compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), reg));
            compiler.useReg();
            DAddr leftFieldAdr = new RegisterOffset(getFieldDefinition().getIndex(), reg);
            compiler.addInstruction(new LOAD(leftFieldAdr, reg));
            ident.getExpDefinition().setOperand(new RegisterOffset(ident.getFieldDefinition().getIndex(), reg));
        }
        return true;
    }



    @Override
    protected void codeGenInitArm(DecacCompiler compiler, OperandArm adr){
        GPRegisterArm reg1 = compiler.getFreeRegArm();
        compiler.useRegArm();
        GPRegisterArm reg2 = compiler.getFreeRegArm();//Implicit use and free
        compiler.addInstruction(new LDR(reg1, (LabelArm) adr));
        compiler.addInstruction(new STR(reg2, new RegisterOffsetArm(0, reg1)));
        compiler.freeRegArm();//free reg1
    }

    @Override
    protected DValArm codeGenLoadArm(DecacCompiler compiler){
        GPRegisterArm reg = compiler.getFreeRegArm();
        compiler.useRegArm();
        GPRegisterArm regTemp = compiler.getFreeRegArm();//Implicit use and free of this register
        compiler.addInstruction(new LDR(regTemp, (LabelArm) getExpDefinition().getOperandArm()));
        compiler.addInstruction(new LDRreg(reg, new RegisterOffsetArm(0, regTemp)));
        return reg;
    }


    @Override
    protected void codeGenBranchArm(DecacCompiler compiler, boolean b, LabelArm label){
        GPRegisterArm reg = compiler.getFreeRegArm();
        compiler.useRegArm();
        GPRegisterArm regTemp = compiler.getFreeRegArm();//Implicit use and free of this register
        compiler.addInstruction(new LDR(regTemp , (LabelArm) getExpDefinition().getOperandArm()));
        compiler.addInstruction(new LDRreg(reg, new RegisterOffsetArm(0, regTemp)));
        compiler.addInstruction(new ArmCMP(reg, new ImmediateIntegerArm(0)));
        if (b){
            compiler.addInstruction(new ArmBne(label));
        }
        else {
            compiler.addInstruction(new ArmBeq(label));
        }
        compiler.freeRegArm();
    }




    @Override
    protected void codeGenPrintArm(DecacCompiler compiler, boolean hex){
        compiler.addInstruction(new LDR(RegisterArm.getR(10), (LabelArm) getExpDefinition().getOperandArm()));
        if (!compiler.isDefined("fct_print_int")) {
            compiler.getFctProgArm().addInstruction(new PrintIntegerArm());
            compiler.addFunction("fct_print_int");
        }
        compiler.addInstruction(new ArmBl(new LabelArm("fct_print_int")));
    }

    @Override
    protected void codeGenPrintLNArm(DecacCompiler compiler, boolean hex){
        compiler.addInstruction(new LDR(RegisterArm.getR(10), (LabelArm) getExpDefinition().getOperandArm()));
        if (!compiler.isDefined("fct_print_int")) {
            compiler.getFctProgArm().addInstruction(new PrintIntegerArm());
            compiler.addFunction("fct_print_int");
        }
        compiler.addInstruction(new ArmBl(new LabelArm("fct_print_int")));




        StringLiteral newLine = new StringLiteral("");
        newLine.setLocation(getLocation());
        newLine.codeGenPrintLNArm(compiler, hex);
    }

}
