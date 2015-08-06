/* Esercizio 2 - Pieraccini Francesco 475944 */

import java.util.*;
import java.lang.*;

/*
 Nelle specifiche di addDoc e removeDoc sembra che la collezione dei documenti debba essere un'entità distinta rispetto agli utenti, ma vedendo il sistema nel complesso, ciò, mi pareva avere poco senso (ad esempio la specifica di removeUser dice di cancellare anche tutti i documenti ad esso associati, ma sarebbe stato impossibile, dato che per rimuovere i documenti serve anche la password dell'utente e, immaginandomi che la rimozione di un utente venga fatta da un operatore, la password non gli sarebbe stata nota e quindi i documenti sarebbero rimasti, non avendo un metodo di rimozione globale).
 Quindi la mia decisione implementativa è stata quella di legare i documenti agli utenti, aggiungendo delle eccezioni all'interfaccia per la add e remove dei documenti.
*/

public class ShareDocImpl implements ShareDoc {
    private Users users;
    private DigitalDocs dd;
    private Map<String, String> docsShared;
    /*
     Scelta implementativa: utilizzo una mappa per tener traccia delle condivisioni effettuate, la chiave è il documento (assumo un documento possa esser condiviso con solo un utente alla volta) e il valore è l'utente a cui questo documento è stato condiviso)
    */
    
    public ShareDocImpl() {
        users = new Users();
        dd = new DigitalDocs();
        docsShared = new HashMap<String, String>();
        
    }
    
    // Aggiunge l’utente con la relativa password alla repository.
    // Restituisce true se l'inserimento ha successo,
    // false se fallisce perche' esiste gia' un utente con il medesimo nome.
    public boolean addUser(String name, int password) {
        return users.addUser(name, password);
    }
    
    // Due funzioni di supporto ai test.
    // 1.
    public List<String> getUsersU() {
        return users.getUsers();
    }
    
    // 2.
    public List<Integer> getPasswordsU() {
        return users.getPasswords();
    }
    
    // Elimina l’utente e tutti i suoi documenti digitali
    public void removeUser(String name) {
        users.removeUser(name);
        dd.removeAll(name);
    }
    
    // Aggiunge al sistema il documento digitale identificato dal nome.
    // Restituisce true se l'inserimento ha successo,
    // false se fallisce perche' esiste gia' un documento con quel nome.
    /*
     Non essendo specificato niente sul fatto che può essere aggiunto un documento con un user e una password non registrati, ho aggiunto tale controllo con una eccezione all'interfaccia, in quanto mi sembrerebbe che dare la possibilità agli utenti di aggiungere documenti con nomi e password non registrati sia un controsenso.
     Inoltre, tali controlli, sono presenti nella readDoc, quindi, in un sistema reale, non fare tali controlli significa che se un utente, ad esempio, sbagli a inserire qualche dato, poi perde il documento, in quanto non potrà più leggerlo non sapendo che aveva inserito male qualche dato.
    */
    public boolean addDoc(String user, String doc, int password) throws WrongIdException {
        List<String> nicks = getUsersU();
        List<Integer> pass = getPasswordsU();
        int j = -1;
        for(int i = 0; i < nicks.size(); i++)
            if(nicks.get(i).compareTo(user) == 0)
                j = i;
        if(j == -1)
            throw new WrongIdException("Utente non registrato.");
        if(pass.get(j).compareTo(password) != 0)
            throw new WrongIdException("Password sbagliata.");
        return(dd.addDoc(user, doc, password));
    }
    
    // Tre funzioni di supporto ai test.
    // 1.
    public List<String> getUsersD() {
        return dd.getUsers();
    }
    
    // 2.
    public List<Integer> getPasswordsD() {
        return dd.getPasswords();
    }
    
    // 3.
    public List<String> getDocsD() {
        return dd.getDocs();
    }
    
    // Rimuove dal sistema il documento digitale identificato dal nome.
    // Restituisce true se l’operazione ha successo,
    // false se fallisce perche' non esiste un documento con quel nome.
    /*
     Come per la add, aggiungo un controllo: da specifiche - è l'utente che rimuove i propri file, quindi dovrà fornire user e password correttamente.
     Inoltre restituisco false anche se esiste un documento con quel nome, ma non appartiene a "user".
    */
    public boolean removeDoc(String user, String doc, int password) throws WrongIdException {
        List<String> nicks = getUsersU();
        List<Integer> pass = getPasswordsU();
        int j = -1;
        for(int i = 0; i < nicks.size(); i++)
            if(nicks.get(i).compareTo(user) == 0)
                j = i;
        if(j == -1)
            throw new WrongIdException("Utente non registrato.");
        if(pass.get(j).compareTo(password) != 0)
            throw new WrongIdException("Password sbagliata.");
        return(dd.removeDoc(user, doc, password));
    }
    
    // Legge il documento digitale identificato dal nome.
    // Lancia WrongIdException se user non e' il nome di un utente registrato
    // o se non esiste un documento con quel nome
    // o se la password non e’ corretta
    public void readDoc(String user, String doc, int password) throws WrongIdException{
        List<String> nicks = getUsersU();
        List<Integer> pass = getPasswordsU();
        List<String> docs = dd.getDocs();
        
        // Prima controllo che l'utente sia registrato e che la password sia giusta.
        int j = -1;
        for(int i = 0; i < nicks.size(); i++)
            if(nicks.get(i).compareTo(user) == 0)
                j = i;
        if(j == -1)
            throw new WrongIdException("Utente non registrato.");
        if(pass.get(j).compareTo(password) != 0)
            throw new WrongIdException("Password sbagliata.");
        
        // Adesso cerco il documento chiamato "doc" e verifico se appartiene ad "user"
        nicks = getUsersD();
        pass = getPasswordsD();
        j = -1;
        for(int i = 0; i < docs.size(); i++)
            if(docs.get(i).compareTo(doc) == 0 && nicks.get(i).compareTo(user) == 0 && pass.get(i) == password)
                j = i;
        if(j == -1)
            throw new WrongIdException("Non hai nessun documento chiamato " + doc + ".");
        System.out.println("Lettura eseguita con successo.");
    }
    
    public void shareDoc(String fromName, String toName, String doc,
                         int password) throws WrongIdException {
        List<String> nicks = getUsersU();
        List<Integer> pass = getPasswordsU();
        List<String> docs = dd.getDocs();
        
        // Prima controllo che l'utente che condivide sia registrato e che la password sia giusta.
        int j = -1;
        for(int i = 0; i < nicks.size(); i++)
            if(nicks.get(i).compareTo(fromName) == 0)
                j = i;
        if(j == -1)
            throw new WrongIdException("Utente non registrato.");
        if(pass.get(j).compareTo(password) != 0)
            throw new WrongIdException("Password sbagliata.");
        
        // Poi che l'utente che riceve la condivisione sia registrato.
        j = -1;
        for(int i = 0; i < nicks.size(); i++)
            if(nicks.get(i).compareTo(toName) == 0)
                j = i;
        if(j == -1)
            throw new WrongIdException("Non puoi condivire con un utente non registrato.");
        
        // Adesso cerco il documento chiamato "doc" e verifico se appartiene ad "fromName".
        nicks = getUsersD();
        pass = getPasswordsD();
        j = -1;
        for(int i = 0; i < docs.size(); i++)
            if(docs.get(i).compareTo(doc) == 0 && nicks.get(i).compareTo(fromName) == 0 && pass.get(i) == password)
                j = i;
        if(j == -1)
            throw new WrongIdException("Non hai nessun documento chiamato " + doc + ".");
        
        // Adesso devo inserire i dati nella mappa delle condivisioni.
        docsShared.put(doc, toName);
    }
    
    // Funzione di supporto ai test.
    public Map<String, String> getDocsShared() {
        return docsShared;
    }
    
    // Restituisce il nome del documento condiviso cancellandolo dalla coda
    // delle notifiche di condivisione.
    // Lancia un'eccezione EmptyQueueException se non ci sono notifiche,
    // o WrongIdException se user non e' il nome di un utente registrato
    // o se la password non e’ corretta
    /*
     Non è specificato, quindi la mia assunzione è stata che: user e password siano di un utente che vuole vedere un documento che gli è stato condiviso: la funzione prima verifica che sia un utente registrato e che la password sia giusta, poi vede se ci sono file condivisi con lui. Se ci sono restituisce il nome del documento, se non ci sono lancia EmptyQueueException, lo stesso se la mappa è vuota.
    */
    public String getNext(String user, int password) throws EmptyQueueException,
    WrongIdException {
        List<String> nicks = getUsersU();
        List<Integer> pass = getPasswordsU();
        
        // Prima controllo che l'utente sia registrato e che la password sia giusta.
        int j = -1;
        for(int i = 0; i < nicks.size(); i++)
            if(nicks.get(i).compareTo(user) == 0)
                j = i;
        if(j == -1)
            throw new WrongIdException("Utente non registrato.");
        if(pass.get(j).compareTo(password) != 0)
            throw new WrongIdException("Password sbagliata.");
        
        // Adesso che ci sia qualche file condiviso.
        if(docsShared.isEmpty())
            throw new EmptyQueueException("Al momento non ci sono documenti condivisi.");
        
        // Adesso cerco se l'utente "user" ha qualche documento condiviso.
        Iterator<String> keys = docsShared.keySet().iterator();  // Iteratore delle chiavi della mappa => i documenti presenti.
        String tmp;
        String myDoc = null;
         while(keys.hasNext()) {
            tmp = keys.next();
            if(docsShared.get(tmp).compareTo(user) == 0)
               myDoc = tmp;
        }
        if(myDoc == null)
            throw new EmptyQueueException("Non hai nessun documento condiviso.");
        
        // Se ne ho trovato almeno uno, rimuovo l'ultimo trovato e restituisco il titolo di tale documento.
        docsShared.remove(myDoc);
        return myDoc;
    }
}

// Definizioni delle classi eccezioni.

class WrongIdException extends Exception {
    public WrongIdException() {
        super();
    }
    public WrongIdException(String s) {
        super(s);
    }
}

class EmptyQueueException extends Exception {
    public EmptyQueueException() {
        super();
    }
    public EmptyQueueException(String s) {
        super(s);
    }
}
