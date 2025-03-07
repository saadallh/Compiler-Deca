

#! /bin/sh

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:./src/main/bin:../global/bin/ima"$PATH"
target_dir=$(pwd)/src/test/deca/codegen/
target_dir2=$(pwd)

Ara_okan () {
	Var=0

	for ligne in `cat ./"$1.deca"` 
	do  
	   if $ligne 2>&1 | grep -q -e "Resultats:"
	   then
	   	Var=1
	   fi
	   if $ligne 2>&1 | grep -q -e "Historique"
	   then
	   	Var=2
	   fi
	   if $ligne 2>&1 | grep -q -e "{"
	   then
	   	Var=2
	   fi
	   if [ $Var = 1 ]
	   then

	   		echo $ligne
	   fi
	   if [ $Var = 2 ]
	   then
	   	break
	   fi
	done > res.txt

	chmod +x res.txt
	sed '1d' res.txt > res2.txt
	sed '$d' res2.txt > res3.txt
	sed '1d' res3.txt > res4.txt
	sed '$d' res4.txt > res5.txt


	for ligne in `cat ./res5.txt` 
	do 
		   if ! $ligne 2>&1 | grep -q -e "//"
	   		then
	   	   		echo $ligne
	   	   fi
	done > res6.txt

	rm res.txt
	rm res2.txt
	rm res3.txt
	rm res4.txt
	rm res5.txt

}


test_decac_valid () {
    decac "$1.deca"
    ima "$1.ass"  > "$1.res" 2>&1 
    if ! diff -q "$1.res" "res6.txt" ; then
        echo "Resultat non attendu"
    else
        echo "Success attendu"
    fi
}

test_decac_invalid(){
    decac "$1.deca"
    ima "$1.ass" > "$1.res"
    cat "$1.res" 
    echo "Erreur attendu"
}




for cas_de_test in src/test/deca/codegen/valid/*.deca
do
    direction_fichier_sans_deca="${cas_de_test%.*}"
    Ara_okan "$direction_fichier_sans_deca" 
    test_decac_valid "$direction_fichier_sans_deca"
    rm res6.txt
done

for cas_de_test in src/test/deca/codegen/valid/provided/*.deca
do
    direction_fichier_sans_deca="${cas_de_test%.*}"
    Ara_okan "$direction_fichier_sans_deca" 
    test_decac_valid "$direction_fichier_sans_deca"
    rm res6.txt
done


for cas_de_test in src/test/deca/codegen/invalid/*.deca
do
    direction_fichier_sans_deca="${cas_de_test%.*}"
    test_decac_invalid "$direction_fichier_sans_deca" 
done


find $target_dir -name "*.res" -delete
find $target_dir2 -name "*.ass" -delete






