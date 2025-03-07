cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# exemple de définition d'une fonction
test_context_invalide () {
    # $1 = premier argument.
    if test_context "$1" 2>&1 | grep -q -e "$1:[0-9][0-9]*:"
    then
        echo "Echec attendu pour test_context sur $1."
    else
        echo "Succes inattendu de test_context sur $1."
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


for cas_de_test in src/test/deca/context/invalid/*.deca
do
    test_context_invalide "$cas_de_test"
done

for cas_de_test in src/test/deca/context/valid/*.deca
do
    test_context_valide "$cas_de_test"
done

for cas_de_test in src/test/deca/context/invalid/provided/*.deca
do
    test_context_invalide "$cas_de_test"
done

for cas_de_test in src/test/deca/context/valid/provided/*.deca
do
    test_context_valide "$cas_de_test"
done
