package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

abstract public class AbstractDeclParam extends Tree {


	protected abstract Type verifyDeclParam(DecacCompiler compiler) throws ContextualError;

	protected abstract EnvironmentExp verifyParam(DecacCompiler compiler) throws ContextualError;

	public abstract void setParamOperand(int paramOffset);

}
