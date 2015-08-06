/* Esercizio 2 - Pieraccini Francesco 475944 */

import java.util.*;
import java.lang.*;


public interface ShareDoc {
    // Aggiunge l’utente con la relativa password alla repository.
    // Restituisce true se l'inserimento ha successo,
    // false se fallisce perche' esiste gia' un utente con il medesimo nome.
    public boolean addUser(String name, int password);
    
    // Elimina l’utente e tutti i suoi documenti digitali
    public void removeUser(String name);

    // Aggiunge al sistema il documento digitale identificato dal nome.
    // Restituisce true se l'inserimento ha successo,
    // false se fallisce perche' esiste gia' un documento con quel nome.
    /*
     Premessa: mi sono immaginato che in un sistema reale del genere, un'operazione come questa venga effettuata da un utente e, il fatto che siano necessari user e password, significa (in un certo senso) che l'utente debba fare un login per inserire un documento.
     Non essendo specificato niente sul fatto che può essere aggiunto un documento con un user e una password non registrati, ho aggiunto tale controllo con una eccezione all'interfaccia, in quanto mi sembrerebbe che dare la possibilità agli utenti di aggiungere documenti con nomi e password non presenti nel sistema sia un controsenso.
     Inoltre, tali controlli, sono presenti nella readDoc, quindi, in un sistema reale, non farli significherebbe che se un utente, ad esempio, sbagliasse ad inserire qualche dato, poi perderebbe il documento, in quanto non potrebbe più leggerlo non sapendo che aveva inserito male qualche dato.
    */
    public boolean addDoc(String user, String doc, int password) throws WrongIdException;
    
    // Rimuove dal sistema il documento digitale identificato dal nome.
    // Restituisce true se l’operazione ha successo,
    // false se fallisce perche' non esiste un documento con quel nome.
    /*
     Come per la add, aggiungo un controllo: da specifiche - è l'utente che rimuove i propri file, quindi dovrà fornire user e password correttamente.
     Poi, se i dati sono corretti, si agisce normalmente: ritorna true se l'operazione ha successo o false se il file non c'era
    */
    public boolean removeDoc(String user, String doc, int password) throws WrongIdException;
    
    // Legge il documento digitale identificato dal nome.
    // Lancia WrongIdException se user non e' il nome di un utente registrato
    // o se non esiste un documento con quel nome
    // o se la password non e’ corretta
    public void readDoc(String user, String doc, int password) throws WrongIdException;
    
    // Notifica una condivisione di documento
    // Lancia un'eccezione WrongIdException se fromName o toName non sono nomi
    // di utenti registrati o se non esiste un documento con quel nome
    // o se la password non e’ corretta
    public void shareDoc(String fromName, String toName, String doc,
                         int password) throws WrongIdException;
    
    // Restituisce il nome del documento condiviso cancellandolo dalla coda
    // delle notifiche di condivisione.
    // Lancia un'eccezione EmptyQueueException se non ci sono notifiche,
    // o WrongIdException se user non e' il nome di un utente registrato
    // o se la password non e’ corretta
    public String getNext(String user, int password) throws EmptyQueueException,
        WrongIdException;
}