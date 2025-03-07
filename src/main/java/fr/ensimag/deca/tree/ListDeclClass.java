package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 *
 * @author gl24
 * @date 01/01/2023
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
    
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClass: start");
        //throw new UnsupportedOperationException("not yet implemented");

        // println on fait pas ce parcours jump ligne 37
        for (AbstractDeclClass e : getList()) {
        	e.verifyClass(compiler);
        }
        LOG.debug("verify listClass: end");
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
        for (AbstractDeclClass e : getList()) {
        	e.verifyClassMembers(compiler);
        }
    }
    
    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        //throw new UnsupportedOperationException("not yet implemented");
        for (AbstractDeclClass e : getList()) {
        	e.verifyClassBody(compiler);
        }
    }

    /**
     * First Pass Generating CodeGen for Virtual Table
     */
    public void codeGenListVirtualTable(DecacCompiler compiler){
        for(AbstractDeclClass e : this.getList()){
            e.codeGenVirtualTable(compiler);
        }
    }


    /**
     * Generate the methods to initialize the fields and define the functions
     * @param compiler
     */
    public void codeGenListFieldsMethods(DecacCompiler compiler){
        for(AbstractDeclClass e : this.getList()){
            e.codeGenFieldsMethods(compiler);
        }
    }



}
