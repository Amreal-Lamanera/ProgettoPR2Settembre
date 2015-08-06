/* Esercizio 2 - Pieraccini Francesco 475944 */

import java.util.*;
import java.lang.*;

public class DigitalDocs {
    /*
     OVERVIEW: classe che implementa una collezione di DigitalDoc, composta da una lista "docs".
    */
    private List<DigitalDoc> docs;      // Lista dei documenti degli utenti.
    
    public DigitalDocs() {
        /*
         MODIFIES: this.
         EFFECTS: inizializza this allocando "docs".
        */
        this.docs = new Vector<DigitalDoc>();
    }
    
    public boolean addDoc(String user, String doc, int password){
        /*
         MODIFIES: this.
         EFFECTS: aggiunge un documento, per far ciò, si controlla prima che non vi sia già un documento con lo stesso nome, quindi si aggiunge tale "doc" alla lista "docs" con i dati di chi ne è possessore.
        */
        
        // Controllo che non vi sia un documento con lo stesso nome.
        for(int i = 0; i < docs.size(); i++){
            if(docs.get(i).getDoc().compareTo(doc) == 0){
                return false;
            }
        }
        docs.add(new DigitalDoc(user, doc, password));
        return true;
    }
    
    public boolean removeDoc(String user, String doc, int password){
        /*
         MODIFIES: this.
         EFFECTS: elimina il documento Doc.
        */
        
        // Cerco il documento con quel nome e lo elimino.
        for(int i = 0; i < docs.size(); i++){
            // Il controllo sulla password, teoricamente, è inutile, ma lo faccio comunque.
            if(this.docs.get(i).getDoc().compareTo(doc) == 0 && this.docs.get(i).getName().compareTo(user) == 0 && this.docs.get(i).getPass() == password){
                docs.remove(i);
                return true;
            }
        }
        return false; // Documento "doc" non trovato.
    }
    
    public void removeAll(String name){
        /*
         MODIFIES: this.
         EFFECTS: elimina tutti i documenti dell'utente "name".
        */
        for(int i = 0; i < docs.size(); i++){
            if(this.docs.get(i).getName().compareTo(name) == 0){
                docs.remove(i);
            }
        }
    }
    
    public List<String> getUsers() {
        /*
         EFFECTS: restituisce una lista con i nickname dei possessori dei documenti presenti.
        */
        List<String> nicks = new Vector<String>();
        for(int i = 0; i < this.docs.size(); i++)
            nicks.add(this.docs.get(i).getName());
        return nicks;
    }
    
    public List<String> getDocs() {
        /*
         EFFECTS: restituisce una lista con i nomi dei documenti presenti.
        */
        List<String> docs = new Vector<String>();
        for(int i = 0; i < this.docs.size(); i++)
            docs.add(this.docs.get(i).getDoc());
        return docs;
    }
    
    public List<Integer> getPasswords() {
        /*
         EFFECTS: restituisce una lista con le password dei possessori dei documenti presenti.
        */
        List<Integer> pass = new Vector<Integer>();
        for(int i = 0; i < this.docs.size(); i++)
            pass.add(this.docs.get(i).getPass());
        return pass;
    }
}

class DigitalDoc {
    /*
     OVERVIEW: classe che implementa il tipo documento digitale, composta dal titolo del documento "doc", l'utente proprietario "name" e la sua password "pass".
    */
    
    private String doc;
    private String name;
    private int pass;
    
    public DigitalDoc(String name, String doc, int pass) {
        /*
         MODIFIES: this.
         EFFECTS: inizializza this assegnando i valori passati al costruttore alle variabili di istanza.
        */
        this.doc = doc;
        this.name = name;
        this.pass = pass;
    }
    
    public String getDoc(){
        /*
         EFFECTS: restituisce il titolo di this.
        */
        return this.doc;
    }
    
    public String getName(){
        /*
         EFFECTS: restituisce il nickname del proprietario di this.
        */
        return this.name;
    }
    
    public int getPass(){
        /*
         EFFECTS: restituisce la password del proprietario di this.
        */
        return this.pass;
    }
}