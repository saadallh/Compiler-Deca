package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

/**
 * List of expressions (eg list of parameters).
 *
 * @author gl24
 * @date 01/01/2023
 */
public class ListExpr extends TreeList<AbstractExpr> {


    @Override
    public void decompile(IndentPrintStream s) {
        //throw new UnsupportedOperationException("Not yet implemented");
        int c = 0;
        for (AbstractExpr i : getList()) {
            if( c == 0 ){
                if(i != null){
                    i.decompile(s);
                    c = c + 1;
                }
                // else{
                //     s.print();
                // }
            }
            else{
                if(i != null){
                    s.print(",");
                    i.decompile(s);
                }
                // else{
                //     s.print();
                // }
                
            }         
        }
    }
}
