import java.util.*;
import java.util.stream.Collectors;

public class Library {

    private int libraryId;
    private ArrayList<Integer> books;
    private List<Integer> booksByScore;
    private int booksByScoreLength;
    private List<Integer> scannedBooks;
    private List<Integer> deletedBooks;
    private int signUpProcessDays;
    private int booksPerDay;
    private long possibleMaxScore;
    private double pointer;
    private boolean signUpProcessing;

    Library(int libraryId, ArrayList<Integer> books, int signUpProcessDays, int booksPerDay) {
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

        booksByScore =
                orderedBookScores.keySet()
                        .stream()
                        .filter(books::contains)
                        .collect(Collectors.toList());

        if (booksByScore.size() >= maxPossibleBooks) {

            deletedBooks = booksByScore.subList((int)maxPossibleBooks + 1, booksByScore.size());
            booksByScore = booksByScore.subList(0, (int) maxPossibleBooks);
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

        List<Integer> result = new ArrayList<>(
                booksByScore.subList(0, Math.min(booksPerDay,booksByScore.size()))
        ){};

        scannedBooks.addAll(booksByScore.subList(0, Math.min(booksPerDay,booksByScore.size())));

        booksByScore =
                booksByScore.stream()
                        .skip(Math.min(booksByScore.size(), booksPerDay))
                        .collect(Collectors.toList());

        return result;
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
