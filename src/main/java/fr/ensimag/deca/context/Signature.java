package fr.ensimag.deca.context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fr.ensimag.deca.tools.SymbolTable.Symbol;

/**
 * Signature of a method (i.e. list of arguments)
 *
 * @author gl24
 * @date 01/01/2023
 */
public class Signature {
    List<Type> args = new ArrayList<Type>();

    public void add(Type t) {
        args.add(t);
    }
    
    public Type paramNumber(int n) {
        return args.get(n);
    }
    
    public int size() {
        return args.size();
    }
    
    public boolean sameSignature(Signature s) {
    	Iterator<Type> i = (s.args).iterator();
    	if(s.size() != this.size()) {
    		return false;
    	}
    	int j = 0;
    	while(i.hasNext()) {
    		if(!i.next().sameType(paramNumber(j))) {
    			return false;
    		}
    		j++;
    	}
    	return true;
    }

}
