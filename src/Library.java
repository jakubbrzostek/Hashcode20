import javax.swing.*;
import java.security.KeyStore;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

public class Library {

    private int libraryId;
    private ArrayList books;
    private ArrayList booksByScore;
    private int booksByScoreLength;
    private List<Integer> scannedBooks;
    private List<Integer> deletedBooks;
    private int signUpProcessDays;
    private int booksPerDay;
    private long possibleMaxScore;
    private double pointer;
    private boolean signUpProcessing;

    Library(int libraryId, ArrayList books, int signUpProcessDays, int booksPerDay) {
        this.libraryId = libraryId;
        this.books = books;
        this.signUpProcessDays = signUpProcessDays;
        this.booksPerDay = booksPerDay;
    }

    public void createValues(Map<Integer, Integer> orderedBookScores, int days) {

        long maxPossibleBooks = days * (long) booksPerDay - signUpProcessDays * (long) booksPerDay;
        if (maxPossibleBooks > books.size()) {
            maxPossibleBooks = books.size();
        }

        Map<Integer, Integer> matchedBooks =
                orderedBookScores.entrySet()
                        .stream()
                        .filter(pair -> books.contains(pair.getKey()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List keyValuePairs =
                matchedBooks.entrySet()
                        .stream()
                        .sorted(Comparator.comparing(e -> -e.getValue()))
                        .map(e -> new Pair<>(e.getKey(), e.getValue()))
                        .collect(Collectors.toList());

        possibleMaxScore =
                matchedBooks.entrySet()
                        .stream()
                        .sorted(Comparator.comparing(e -> -e.getValue()))
                        .map(e -> new Pair<>(e.getKey(), e.getValue()))
                        .limit(Math.min(keyValuePairs.size(), (int) maxPossibleBooks))
                        .mapToInt(Pair::getRight).sum();
//TODO
        booksByScore =
                orderedBookScores.entrySet()
                        .stream()
                        .filter(pair -> books.contains(pair.getKey()))
                        .collect(Collectors.toConcurrentMap(Map.Entry::getKey, Map.Entry::getValue));

        if (booksByScore.size() >= maxPossibleBooks) {
            long finalMaxPossibleBooks = maxPossibleBooks;
            //deletedBooks = Arrays.stream(booksByScore).filter((i, i1) -> i1 >= finalMaxPossibleBooks).collect(Collectors.toList());
            //booksByScore = booksByScore.stream().limit(maxPossibleBooks).toArray();
        }
        booksByScoreLength = booksByScore.size();

        pointer = possibleMaxScore / (double) signUpProcessDays;
    }

    public void recalculateValues( Map<Integer, Integer> orderedBookScores, int daysLeft ) {
        long maxPossibleBooks = daysLeft * (long)booksPerDay - signUpProcessDays * (long) booksPerDay;
        if (maxPossibleBooks > books.size()) {
            maxPossibleBooks = books.size();
        }

        Map<Integer, Integer> matchedBooks =
                orderedBookScores.entrySet()
                        .stream()
                        .filter(pair -> booksByScore.contains(pair.getKey()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List keyValuePairs =
                matchedBooks.entrySet()
                        .stream()
                        .sorted(Comparator.comparing(e -> -e.getValue()))
                        .map(e -> new Pair<>(e.getKey(), e.getValue()))
                        .collect(Collectors.toList());

        possibleMaxScore =
                matchedBooks.entrySet()
                        .stream()
                        .sorted(Comparator.comparing(e -> -e.getValue()))
                        .map(e -> new Pair<>(e.getKey(), e.getValue()))
                        .limit(Math.min(keyValuePairs.size(), (int) maxPossibleBooks))
                        .mapToInt(Pair::getRight).sum();

        pointer = possibleMaxScore / (double) signUpProcessDays;
    }

    public boolean signUpProcess() {
        if (signUpProcessDays == 0) {
            return false;
        }
        if (!isSignUpProcessing()) {
            setSignUpProcessing(true);
        }

        signUpProcessDays -= 1;
        if (signUpProcessDays == 0) {
            setSignUpProcessing(false);
        }
        return false;
    }

    public List<Integer> scanningProcess(List<Integer> allScannedBooks) {
        //List<Integer> result = new ArrayList<>( booksByScore.length >= booksPerDay ? booksByScore.) {}
        return null;
    }

    public int getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(int libraryId) {
        this.libraryId = libraryId;
    }

    public boolean isSignUpProcessing() {
        return this.signUpProcessing;
    }

    public void setSignUpProcessing(boolean signUpProcessing) {
        this.signUpProcessing = signUpProcessing;
    }
}
