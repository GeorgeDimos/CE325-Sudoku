# Sudoku
Μία εφαρμογή σε περιβάλλον Swing που υλοποιεί ένα παιχνίδι Sudoku. H εφαρμογή περιέχει ένα οριζόντιο μενού bar, με το μενού New Game με επιλογές
- Easy
- Intermediate
- Expert

Αμέσως μετά το οριζόντιο μενού ακολουθoύν τα 9x9 τετραγωνάκια του Sudoku (Sudoku Grid). 
Κάτω από το Sudoku Grid εμφανίζονται:
- τα κουμπιά των αριθμών 1-9
- ένα κουμπί που αναιρεί το περιεχόμενο ενός τετραγώνου
- ένα κουμπί αναίρεσης της ακολουθίας των ενεργειών του χρήστη (undo Action)
- ένα checkBox για την έλεγχο της εισόδου του χρήστη σε σχέση με την λύση (μπορεί να το επιλέξει ο χρήστης προαιρετικά)
- ένα κουμπί που εμφανίζει την λύση του puzzle

Επιλέγοντας ένα νέο παιχνίδι Sudoku θα πρέπει να εμφανίζονται οι κενές κυψέλες με λευκό χρώμα και οι κυψέλες που έχουν αρχικές τιμές με γκρι χρώμα. Οι κυψέλες με τις αρχικές τιμές θα πρέπει να διατηρούν το γκρι χρώμα σε όλη την εξέλιξη του παιχνιδιού.

To παιχνίδι παίζεται ως εξής:
1. Ο χρήστης επιλέγει ένα τετραγωνάκι από το sudoku grid. Το τετραγωνάκι που επέλεξε μαρκάρεται με ανοιχτό κίτρινο χρώμα (RGB: 255.255.200). Με το ίδιο χρώμα μαρκάρονται όλα τα τετραγωνάκια που έχουν την ίδια τιμή με το μαρκαρισμένο τετραγωνάκι ανεξάρτητα αν πρόκειται για τετραγωνάκια που έχουν δοθεί από το αρχικό puzzle ή τα έχει εισάγει ο χρήστης.
2. Εφόσον επιλέξει ένα τετραγωνάκι, στη συνέχεια μπορεί να πατήσει ένα κουμπί που αντιστοιχεί στο ψηφίο που θέλει να εισάγει στο τετραγωνάκι που επέλεξε.
3. Εάν ο χρήστης επιλέξει ένα τετραγωνάκι που έχει εισάγει προηγούμενα περιεχόμενο και πατήσει τοκουμπί της διαγραφής, το περιεχόμενο διαγράφεται. Εάν ο χρήστης επιλέξει ένα γκρι τετραγωνάκι που περιέχει αρχική τιμή του puzzle, αυτό δεν μπορεί να διαγραφεί πατώντας το κουμπί της διαγραφής.
4. Εάν ο χρήστης επιλέξει να εισάγει ένα ψηφίο που υπάρχει στην ίδια γραμμή, την ίδια στήλη ή το ίδιο τετράγωνο του Sudoku puzzle, τότε το τετραγωνάκι που υφίσταται ήδη και δημιουργεί “σύγκρουση” μαρκάρεται κόκκινο. 
5. Εάν ο χρήστης επιλέξει το κουμπί της αναίρεσης τότε κάθε μία από τις τιμές που έχει εισάγει θα πρέπει να αναιρείται με την αντίστροφη σειρά.
6. Εάν ο χρήστης επιλέξει το checkbox “Check against solution” , τότε κάθε ένα από τα τετραγωνάκια που έχουν διαφορετική τιμή από την λύση θα πρέπει να μαρκάρονται με μπλε. Εφόσον το checkbox παραμένει μαρκαρισμένο τα τετραγωνάκια θα πρέπει να παραμένουν μπλε μέχρι ο χρήστης να διορθώσει το puzzle.
7. Εάν ο χρήστης επιλέξει το κουμπί εμφάνισης της λύσης, τότε θα πρέπει να εμφανίζεται η λύση με παράλληλη απενεργοποίηση όλων των κουμπιών κάτω από το sudoku grid.
8. Το παιχνίδι τελειώνει με την ολοκλήρωση του sudoku puzzle. Σε αυτή την περίπτωση εμφανίζεται έναμήνυμα επιτυχίας με παράλληλη απενεργοποίηση όλων των κουμπιών κάτω από το sudoku grid.
