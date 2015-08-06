/* Esercizio 2 - Pieraccini Francesco 475944 */

import java.util.*;
import java.lang.*;

public class Users {
    /*
     OVERVIEW: classe che implementa una collezione di utenti, composta da una lista di utenti "users" e una di password "pass".
    */
    
    private List<User> users;        // Lista ordinata degli utenti
    private List<Integer> pass;      // Lista ordinata di password con corrispondeza alla lista utenti.
    
    public Users() {
        /*
         MODIFIES: this.
         EFFECTS: inizializza this allocando "users" e "pass".
        */
        
        users = new Vector<User>();
        pass = new Vector<Integer>();
    }
    
    public boolean addUser(String nick, int pass){
        /*
         MODIFIES: this.
         EFFECTS: aggiunge un utente, per far ciò, si controlla prima che non vi sia già un utente con lo stesso nick, quindi si aggiunge tale "nick" alla lista degli utenti e si aggiunge una password alla lista delle password, dandole il valore passato alla funzione "pass". La funzione restituisce "true" se ha successo, "false" se è stato trovato un utente con lo stesso nickname.
        */
        for(int i = 0; i < users.size(); i++){
            if(users.get(i).getnick().compareTo(nick) == 0)
                return false;
        }
        this.users.add(new User(nick));
        this.pass.add(new Integer(pass));
        return true;
    }
    
    
    public List<String> getUsers() {
        /* 
         EFFECTS: restituisce una lista con i nickname degli utenti registrati.
        */
        List<String> nicks = new Vector<String>();
        for(int i = 0; i < users.size(); i++)
            nicks.add(users.get(i).getnick());
        return nicks;
    }
    
    public List<Integer> getPasswords() {
        /*
         EFFECTS: restituisce una lista con le password degli utenti registrati.
        */
        List<Integer> pass = new Vector<Integer>();
        for(int i = 0; i < users.size(); i++)
            pass.add(this.pass.get(i));
        return pass;
    }
    
    
    public void removeUser(String name) {
        /*
            MODIFIES: this.
            EFFECTS: elimina l’utente "name" e la corrispettiva password.
        */
        
        for(int i = 0; i < users.size(); i++){
            if(users.get(i).getnick().compareTo(name) == 0){
                users.remove(i);
                pass.remove(i);
            }
        }
    }
}

class User {
    
    /*
     OVERVIEW: classe che implementa il tipo utente, composta da un nickname "nick".
    */
    
    private String nick;                // Nick utente
    
    public User (String nick){
        /*
         MODIFIES: this.
         EFFECTS: inizializza this assegnando "nick" alla variabile d'istanza.
        */
        this.nick = nick;
    }
    
    public String getnick(){
        /*
         EFFECTS: restituisce il nick di this.
        */
        return this.nick;
    }
}