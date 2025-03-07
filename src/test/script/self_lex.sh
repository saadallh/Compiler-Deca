cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# exemple de définition d'une fonction
test_lex_invalide () {
    # $1 = premier argument.
    if test_lex "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    then
        echo "Echec attendu pour test_lex sur $1."
    else
        echo "Succes inattendu de test_lex sur $1."
        exit 1
    fi
}    

test_context_valide () {
    # $1 = premier argument.
    if test_context "$1" 2>&1 | grep -q -e ":[0-9][0-9]*:"
    then
        echo "Echec inattendu de test_context sur $1."
        exit 1
    else
        echo "Succes attendu pour test_context sur $1."
    fi
}       

for cas_de_test in src/test/deca/syntax/invalid/*.deca
do
    test_lex_invalide "$cas_de_test"
done

for cas_de_test in src/test/deca/syntax/valid/*.deca
do
    test_lex_valide "$cas_de_test"
done

