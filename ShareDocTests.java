/* 
 I commenti a ciò che si sta testando sono nelle println così da rendere il risultato su terminale più leggibile.
*/

public class ShareDocTests {
    public static void main (String[] args) {
        ShareDocImpl tester = new ShareDocImpl();
        System.out.println("\n1. Test: addUser(String nick, int password).\n   Risultato atteso: true, false, false, true, true.\n");
        System.out.println(tester.addUser("Giorgio", 123));
        System.out.println(tester.addUser("Giorgio", 123));
        System.out.println(tester.addUser("Giorgio", 456));
        System.out.println(tester.addUser("Luca", 456));
        System.out.println(tester.addUser("Ugo", 78910));
        
        System.out.println("\n2. Test: verifico che non siano stati aggiunti né utenti con lo stesso nick né password senza utente.\n   Risultato atteso:  [Giorgio, Luca, Ugo]; [123, 456, 78910].\n");
        
        System.out.println(tester.getUsersU());
        System.out.println(tester.getPasswordsU());
        
        System.out.println("\n3. Test: removeUser(String nick).\n");
        tester.removeUser("Giorgio");
        
        System.out.println("\n4. Test: verifico che la rimozione abbia avuto successo.\n   Risultato atteso:  [Luca, Ugo]; [456, 78910].\n");
        System.out.println(tester.getUsersU());
        System.out.println(tester.getPasswordsU());
        
        System.out.println("\n5. Test: addDoc(String nick, String doc, int password).\n   Risultato atteso: true, false, true.\n");
        
        try {
            System.out.println(tester.addDoc("Luca", "Doc1", 456));
            System.out.println(tester.addDoc("Luca", "Doc1", 456));
            System.out.println(tester.addDoc("Ugo", "Doc2", 78910));
        } catch(WrongIdException e) {
            e.printStackTrace();
        }
        
        System.out.println("\n6. Test: verifico che non siano stati aggiunti documenti con lo stesso nome.\n   Risultato atteso:  [Luca, Ugo]; [Doc1, Doc2]; [456, 78910].\n");
        
        System.out.println(tester.getUsersD());
        System.out.println(tester.getDocsD());
        System.out.println(tester.getPasswordsD());
        
        System.out.println("\n7. Test: removeDoc(String nick, String doc, int password).\n   Risultato atteso: true, false.\n");
        
        try {
        System.out.println(tester.removeDoc("Luca", "Doc1", 456));
        System.out.println(tester.removeDoc("Luca", "Doc1", 456));
        } catch(WrongIdException e) {
            e.printStackTrace();
        }
        
        System.out.println("\n8. Test: verifico che la rimozione abbia avuto successo.\n   Risultato atteso:  [Ugo]; [Doc2]; [78910].\n");
        
        System.out.println(tester.getUsersD());
        System.out.println(tester.getDocsD());
        System.out.println(tester.getPasswordsD());
        
        System.out.println("\n9. Test: readDoc(String nick, String doc, int password).\n   Risultato atteso: \"Lettura eseguita con successo.\"\n");
        
        try {
        tester.readDoc("Ugo", "Doc2", 78910);
        } catch(WrongIdException e) {
            e.printStackTrace();
        }
        
        System.out.println("\n10. Test: shareDoc(String fromName, String toName, String doc, int password).\n    Risultato atteso: {Luca=Doc2}\n");
        
        try {
            tester.shareDoc("Ugo", "Luca", "Doc2", 78910);
            System.out.println(tester.getDocsShared());
        } catch(WrongIdException e) {
            e.printStackTrace();
        }
        
        System.out.println("\n11. Test: getNext(String user, int password).\n    Risultato atteso: Doc2\n");
        
        try {
            System.out.println(tester.getNext("Luca", 456));
        } catch(WrongIdException e) {
            e.printStackTrace();
        } catch(EmptyQueueException e) {
            e.printStackTrace();
        }
        
        System.out.println("\n12. Test: verifico che Doc2 sia stato rimosso dalla lista delle condivisioni.\n    Risultato atteso:  {}.\n");
        
        System.out.println(tester.getDocsShared());
    }
}