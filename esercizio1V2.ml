(* Esercizio 1 - Pieraccini Francesco 475944 *)

(*  Ricerca binding nell'ambiente  *)

let rec lookup (var, env) =
match env with
  (nome, valore) :: env1 -> if nome = var then valore else lookup (var,env1)
  | [] -> failwith "Not found" ;;

(* Sintassi *)

(* Identificatori memorizzati come stringhe *)
type ide = string

(* Tipi di dati memorizzabili *)
and eval =
| Int of int
| Bool of bool
| Funval of efun
| Tuple of tuple
| String of string
| Unbound

and efun = exp * env

(* Ambiente - lista di bindings (coppie <nome, valore>) *)
and env = ( ide * eval ) list

and exp =
Ide of ide

| Val of eval

(* Operazioni su Interi *)

| Prod of exp * exp | Somm of exp * exp | Diff of exp * exp | Ug of exp * exp | MinUg of exp * exp

(* Operazioni su Booleani *)
| Or of exp * exp | And of exp * exp | Not of exp

(* Condizionale, Blocco Let, Dichiarazione di funzione, Applicazione Funzionale *)
| Ifthenelse of exp * exp * exp

| Let of ide * exp * exp

| Fun of ide * exp

| Appl of exp * exp

(* Operazioni su Tuple *)

| ExpTup of exp list        (*  Espressione tupla                   *)

| EatI of exp * int         (*  Accesso elemento tupla              *)

| EfstI of exp * int        (*  Selezione elementi tupla            *)

| EugE of exp * exp         (*  Confronto tra tuple                 *)

| ApplT of exp * exp        (*  Applicazione di funzione a tupla    *)


and tuple =
  E of eval
| Elts of eval * tuple


(* Type Checker che controlla che un valore (semantico) sia di un dato tipo *)

let typecheck(tipo , valore) = match tipo with
| "int" -> (match valore with
| Int(u) -> true
| _ -> false)
| "bool"-> (match valore with
| Bool(u) -> true
| _ -> false)
| _ -> failwith ("Not a valid type")

(* Operazioni primitive *)

let ug (x,y) = if typecheck("int",x) && typecheck("int",y)
then ( match (x,y) with
| (Int(u), Int(w)) -> Bool(u = w)
| (_, _) -> failwith ("Not a valid type") )
else failwith ("type error")

let minug (x,y) = if typecheck("int",x) && typecheck("int",y)
then ( match (x,y) with
| (Int(u), Int(w)) -> Bool(u <= w)
| (_, _) -> failwith ("Not a valid type") )
else failwith ("type error")

let somm (x,y) = if typecheck("int",x) && typecheck("int",y)
then ( match (x,y) with
| (Int(u), Int(w)) -> Int(u+w)
| (_, _) -> failwith ("Not a valid type") )
else failwith ("type error")

let diff (x,y) = if typecheck("int",x) && typecheck("int",y)
then ( match (x,y) with
| (Int(u), Int(w)) -> Int(u-w)
| (_, _) -> failwith ("Not a valid type") )
else failwith ("type error")

let prod (x,y) = if typecheck("int",x) && typecheck("int",y)
then ( match (x,y) with
| (Int(u), Int(w)) -> Int(u*w)
| (_, _) -> failwith ("Not a valid type") )
else failwith ("type error")

let et (x,y) = if typecheck("bool",x) && typecheck("bool",y)
then ( match (x,y) with
| (Bool(u), Bool(w)) -> Bool(u && w)
| (_, _) -> failwith ("Not a valid type") )
else failwith ("type error")

let vel (x,y) = if typecheck("bool",x) && typecheck("bool",y)
then ( match (x,y) with
| (Bool(u), Bool(w)) -> Bool(u || w)
| (_, _) -> failwith ("Not a valid type") )
else failwith ("type error")

let non x = if typecheck("bool",x)
then ( match x with
| Bool(y) -> Bool(not y)
| (_) -> failwith ("Not a valid type") )
else failwith ("type error");;

(* Semantica *)

let rec sem ((e: exp), (r: env)) =
    match e with

Ide (ide) -> lookup(ide, r)

| Val (v) -> v

| Prod (a,b) -> prod (sem (a, r), sem (b, r))

| Somm (a,b) -> somm (sem (a, r), sem (b, r))

| Diff (a,b) -> diff (sem (a, r), sem (b, r))

| Ug (a,b) -> ug (sem (a, r) ,sem (b, r) )

| MinUg (a,b) -> ug (sem (a, r) ,sem (b, r) )

| And (a,b) -> et (sem (a, r), sem (b, r))

| Or (a,b) -> vel (sem (a, r), sem (b, r))

| Not (a) -> non (sem (a, r))

| Ifthenelse (a,b,c) -> let g = sem (a, r) in
                            if typecheck ("bool", g) then
                                (if g = Bool (true) then sem (b, r) else sem (c, r))
                            else failwith ("nonboolean guard")

| Let (i, e1, e2) -> sem (e2, (i, sem (e1, r)) :: r)

| Fun (x, a) -> Funval (e, r)

| Appl (e, param) -> (match sem (e, r) with
                              Funval ( Fun (x, code) , funDecEnv ) -> sem ( code, ( x, sem (param,r) ) :: funDecEnv )
                            | _ -> failwith ("Apply argument 1 must be a functional value"))

(* Operazioni su Tuple *)

(*  Espressione tupla   *)
(*
    Fa il pattern matching della tupla t in modo da costruirla:
    .) se t è una lista vuota dà errore;
    .) se t è un elemento singolo lo si valuta nella semantica, quindi si fa il pattern matching:
            ..) se è di tipo unbound si dà errore;
            ..) altrimenti si ritorna tale valore (di tipo E di tuple).
    .) se t è un elemento, più una lista di elementi si fa il pattern matching dell'elemento:
            ..) se è di tipo unbound si dà errore;
            ..) altrimenti si ritorna un valore di tipo tupla Elts composto da tale valore e dalla funzione applicata ricorsivamente alla lista di elementi successiva a quell'elemento.
*)
| ExpTup (t) -> let rec tupleBuild (t) = (
                    match t with
                          [] -> failwith "Empty expression"
                        | [e] -> let e1 = sem(e, r) in (
                                    match e1 with
                                          Unbound -> failwith "Unbound value"
                                        | _ -> E (e1))
                        | e :: elist -> let e1 = sem (e, r) in(
                                    match e1 with
                                          Unbound -> failwith "Unbound value"
                                        | _ -> Elts (e1, tupleBuild(elist))))
                in Tuple (tupleBuild t)

(*  Accesso elemento tupla   *)
(*
    Valuto l'espressione nella semantica, quindi controllo che sia di tipo tupla e a quel punto chiamo la funzione findT.
*)

| EatI (t, i) -> let t1 = sem(t, r) in (
                    match (t1) with
                          Tuple t1 -> findT (t1, i, 0)
                        | _ -> failwith "Type error")

(*
    Valuto l'espressione nella semantica, quindi controllo che sia di tipo tupla e a quel punto chiamo la funzione funfst, con cui mi ricavo una sotto-tupla di k valori e la restituisco come Tuple.
*)

| EfstI (t, k) -> let t1 = sem(t, r) in (
                    match (t1) with
                          Tuple t1 -> Tuple (funfst (t1, k, 1))
                        | _ -> failwith "Type error")

(*
    Valuto le espressioni nella semantica, quindi controllo che siano entrambe di tipo tupla e a quel punto chiamo la funzione ugT, con cui mi ricavo un booleano da restituire come Bool.
*)

| EugE (t1, t2) -> let new_t1 = sem(t1, r) in
                        let new_t2 = sem(t2, r) in (
                            match (new_t1, new_t2) with
                                  Tuple t1, Tuple t2 -> Bool (ugT (t1, t2))
                                | _ -> failwith "Type error")

| ApplT (t, f) -> let t1 = sem (t, r) in (
                    match t1 with
                          Tuple t -> let f1 = sem (f, r) in
                            let rec funAppl ((t: tuple), (f1: eval)) = (
                                match (t, f1) with
                                      E (e), Funval (Fun (x1, exp), r1) ->
                                        E (sem (exp, ((x1, e) :: r1)))
                                    | Elts (e, eList), Funval (Fun (x1, exp), r1) ->
                                        Elts (sem (exp, ((x1, e) :: r1)), funAppl (eList, f1))
                                    | _ -> failwith "Type Error"
                                ) in Tuple (funAppl (t, f1))
                        | _ -> failwith "Type Error")

(*
Funzione di ricerca dell'elemento i nella tupla t.
Sfruttando un contatore inizializzato a 0 dal chiamante, cerco l'i-esimo elemento della tupla facendo il pattern matching:
.) Se l'elemento in analisi è un elemento singolo (quindi un E di tipo tupla), se i (indice i-esimo) è uguale al valore del contatore ho finito, perché significa che il valore associato a quell'elemento è quello che stavo cercando, altrimenti do errore, in quanto la tupla è finita e l'indice non è stato ancora trovato;
.) Se, invece, ho un elemento Elts di tipo tupla (quindi formato da un valore e e da una sotto-tupla), se i (indice i-esimo) è uguale al valore del contatore ho finito, perché significa che il valore associato a quell'elemento è quello che stavo cercando, altrimenti proseguo la mia ricerca chiamando la funzione ricorsivamente e passandogli: come tupla, la sotto-tupla di t, come indice, lo stesso i e come contatore, il contatore attuale incrementato di uno.
*)

and findT ((t: tuple), (k: int), (count: int)) =
        match t with
              E (e) -> if k = count then e else failwith "Index too big"
            | Elts (e, eList) -> if k = count then e else findT (eList, k, count+1)

(*
Funzione di selezione di k elementi nella tupla t.
Sfruttando un contatore inizializzato a 1 dal chiamante, seleziono k elementi della tupla facendo il pattern matching:
.) Se l'elemento in analisi è un elemento singolo (quindi un E di tipo tupla), se k è uguale al valore del contatore ho finito, perché significa che sono arrivato a selezionare l'elemento k-esimo e posso restituire la sotto-tupla, altrimenti do errore, in quanto la tupla è finita e non è stato possibile selezionare k elementi;
.) Se, invece, ho un elemento Elts di tipo tupla (quindi formato da un valore e e da una sotto-tupla), se i (indice i-esimo) è uguale al valore del contatore ho finito, perché significa che sono arrivato a selezionare l'elemento k-esimo e posso restituire la sotto-tupla, altrimenti proseguo la mia ricerca chiamando la funzione ricorsivamente e passandogli: come tupla, la sotto-tupla di t, come indice, lo stesso k e come contatore, il contatore attuale incrementato di uno.
*)

and funfst ((t: tuple), (i: int), (count: int)) =
        match t with
              E (e) -> if i = count then E (e) else failwith "Index too big"
            | Elts (e, eList) -> if i = count then E (e) else Elts (e, funfst (eList, i, count+1))

(*
Funzione di confronzo fra la tupla t1 e la tupla t2.
Faccio un pattern matching con le due tuple:
    .) Se sono entrambe un elemento singolo (quindi E di tipo tupla), controllo semplicemente se i loro valori sono uguali, se lo sono restituisco true, altrimenti false;
    .) Se, invece, sono entrambe elementi Elts di tipo tupla (quindi formati da un valore e e da una sotto-tupla), se i valori sono uguali restituisco true in and con la chiamata ricorsiva alla funzione stessa passandole come argomenti le due sotto-tuple, altrimenti restituisco false;
    .) Se non valgono le prime due considerazioni (quindi se l'elemento raggiunto è di tipo diverso nelle due tuple - uno elemento singolo E e l'altro sottotupla Elts) restituisco false, perché avendo un numero diverso di elementi non possono essere uguali.
*)

and ugT ((t1: tuple), (t2: tuple)) =
        match (t1, t2) with
              E e1, E e2 -> if e1 = e2 then true else false
            | Elts (e1, eList1), Elts (e2, eList2) -> if e1 = e2 then true && ugT (eList1, eList2) else false
            | _ -> false
;;


(**************************  TESTS   **************************)

(*
    TEST 1:
    Test della funzione di valutazione delle tuple (Espressione Tupla).
*)

print_string ("TEST 1");;
let t = [Val (String "Ciao"); Val (Int 12); Val (Bool true)];;
let et = ExpTup t;;
sem (et, []);;

(*
    TEST 2,3,4:
    Test di accesso agli elementi.
*)

print_string ("TEST 2");;
let e = EatI (et, 0);;
sem (e, []);;
print_string ("TEST 3");;
let e = EatI (et, 1);;
sem (e, []);;
print_string ("TEST 4");;
let e = EatI (et, 2);;
sem (e, []);;

(*
    TEST 5,6:
    Test di selezione degli elementi e di accesso tali elementi.
*)

print_string ("TEST 5");;
let e1 = EfstI (et, 2);;
sem (e1, []);;
print_string ("TEST 6");;
let e2 = EatI (e1, 1);;
sem (e2, []);;

(*
    TEST 7,8:
    Test di uguaglianza fra tuple.
*)

print_string ("TEST 7");;
let e = EugE (et, e1);;
sem (e, []);;
print_string ("TEST 8");;
let e1 = EugE (et, et);;
sem (e1, []);;

let e = Let ("add5", Fun ("x", Somm(Ide "x", Val (Int 5))),
Let ("t", ExpTup ([Val (Int 5); Val (Int 6); Val (Bool true); Val (Int 7)]),
ApplT(EfstI (Ide "t", 2), Ide "add5")));;
sem (e, []);;
