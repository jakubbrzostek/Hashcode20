import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

        long maxPossibleBooks = days * (long) booksPerDay - getSignUpProcessDays() * (long) booksPerDay;
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
            long finalMaxPossibleBooks = maxPossibleBooks;

            if (getDeletedBooks() == null) {
                deletedBooks = new ArrayList<>();
            } else {
                getDeletedBooks().addAll(
                        IntStream
                                .range(0, booksByScore.size())
                                .filter(index -> index >= finalMaxPossibleBooks)
                                .mapToObj(index -> booksByScore.get(index))
                                .collect(Collectors.toList()));
            }

            booksByScore =
                    booksByScore
                            .stream()
                            .limit(maxPossibleBooks)
                            .collect(Collectors.toList());
                    //booksByScore.subList(0, (int) maxPossibleBooks);
        }

        setBooksByScoreLength(booksByScore.size());
        setPointer(possibleMaxScore / (double) getSignUpProcessDays());
    }

    public void recalculateValues(Map<Integer, Integer> orderedBookScores, int daysLeft) {
        long maxPossibleBooks = daysLeft * (long) booksPerDay - getSignUpProcessDays() * (long) booksPerDay;
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

        pointer = possibleMaxScore / (double) getSignUpProcessDays();
    }

    public boolean signUpProcess() {
        if (getSignUpProcessDays() == 0) {
            return false;
        }
        if (!isSignUpProcessing()) {
            setSignUpProcessing(true);
        }

        setSignUpProcessDays(getSignUpProcessDays() -1);
        if (getSignUpProcessDays() == 0) {
            setSignUpProcessing(false);
        }
        return false;
    }

    public List<Integer> scanningProcess(List<Integer> allScannedBooks) {

        List<Integer> result = new ArrayList<>(
                booksByScore.subList(0, Math.min(booksPerDay, booksByScore.size()))
        ) {};

        if (getScannedBooks() == null) {
            scannedBooks = new ArrayList<>();
        } else {

            getScannedBooks().addAll(booksByScore.subList(0, Math.min(booksPerDay, booksByScore.size() ) ) );
        }

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

    public double getPointer() {
        return pointer;
    }

    public void setPointer(double pointer) {
        this.pointer = pointer;
    }

    public List<Integer> getBooksByScore() {
        return booksByScore;
    }

    public void setBooksByScore(List<Integer> booksByScore) {
        this.booksByScore = booksByScore;
    }

    public boolean isSignUpProcessing() {
        return this.signUpProcessing;
    }

    public void setSignUpProcessing(boolean signUpProcessing) {
        this.signUpProcessing = signUpProcessing;
    }

    public void setBooksByScoreLength(int booksByScoreLength) {
        this.booksByScoreLength = booksByScoreLength;
    }

    public int getBooksByScoreLength() {
        return booksByScoreLength;
    }

    public List<Integer> getDeletedBooks() {
        return deletedBooks;
    }

    public void setDeletedBooks(List<Integer> deletedBooks) {
        this.deletedBooks = deletedBooks;
    }

    public List<Integer> getScannedBooks() {
        return scannedBooks;
    }

    public void setScannedBooks(List<Integer> scannedBooks) {
        this.scannedBooks = scannedBooks;
    }

    public int getSignUpProcessDays() {
        return signUpProcessDays;
    }

    public void setSignUpProcessDays(int signUpProcessDays) {
        this.signUpProcessDays = signUpProcessDays;
    }

}
