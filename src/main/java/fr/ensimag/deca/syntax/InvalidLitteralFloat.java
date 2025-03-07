package fr.ensimag.deca.syntax;

import org.antlr.v4.runtime.ParserRuleContext;

public class InvalidLitteralFloat extends DecaRecognitionException {
    private static final long serialVersionUID = 4670163376041273741L;

    public InvalidLitteralFloat(DecaParser recognizer, ParserRuleContext ctx) {
        super(recognizer, ctx);
    }

    @Override
    public String getMessage() {
        return "FLOAT assigned can't be rounded either is way too big or way too small";
    }

}
