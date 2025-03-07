package fr.ensimag.deca.syntax;

import org.antlr.v4.runtime.ParserRuleContext;
/**
 *
 * @author gl24
 * @date 01/01/2023
 */

public class InvalidLitteralInt extends DecaRecognitionException {
	
    private static final long serialVersionUID = 4670163376041273741L;

    public InvalidLitteralInt(DecaParser recognizer, ParserRuleContext ctx) {
        super(recognizer, ctx);
    }

    @Override
    public String getMessage() {
        return "INT is way too big for 32 bits";
    }
}
