package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

public abstract class AbstractDeclField extends Tree {

	protected abstract EnvironmentExp verifyDeclField(DecacCompiler compiler, Symbol className,
			Symbol superClass, int j) throws ContextualError;

	protected abstract void verifyField(DecacCompiler compiler, Symbol className,
			EnvironmentExp env) throws ContextualError;


	protected abstract void codeGenDeclField(DecacCompiler compiler);

	public abstract AbstractIdentifier getVarName();

	public abstract AbstractInitialization getInitialization();

}
