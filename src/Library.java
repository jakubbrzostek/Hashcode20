import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class Library {

    private int libraryId;
    private int[] books;
    private int[] booksByScore;
    private List<Integer> scannedBooks;
    private int signUpProcessDays;
    private int booksPerDay;
    private long possibleMaxScore;
    private double pointer;
    private boolean signUpProcessing;

    Library(int libraryId, int[] books, int signUpProcessDays, int booksPerDay, List<Integer> scannedBooks) {
        this.libraryId = libraryId;
        this.books = books;
        this.signUpProcessDays = signUpProcessDays;
        this.booksPerDay = booksPerDay;
        this.scannedBooks = scannedBooks;
    }


    public void createValues (Map<Integer,Integer> bookScores) {
        Map<Integer,Integer> orderedBookScores = bookScores
                                        .entrySet()
                                        .stream()
                                        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                                        .collect(
                                                toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                                        LinkedHashMap::new));
        possibleMaxScore = orderedBookScores.
    }

    public boolean signUpProcess() {
        if ( signUpProcessDays == 0) {
            return false;
        }
        if (!isSignUpProcessing()) {
            setSignUpProcessing(true);
        }

        signUpProcessDays -= 1;
        if ( signUpProcessDays == 0) {
            setSignUpProcessing(false);
        }
        return false;
    }

    public List<Integer> scanningProcess (List<Integer> allScannedBooks) {
        //List<Integer> result = new List<Integer>( booksByScore.length >= booksPerDay ? booksByScore.) {}
        return null;
    }

    public int getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(int libraryId) {
        this.libraryId = libraryId;
    }

    public boolean isSignUpProcessing () {
        return this.signUpProcessing;
    }
    public void setSignUpProcessing (boolean signUpProcessing) {
        this.signUpProcessing = signUpProcessing;
    }
}
