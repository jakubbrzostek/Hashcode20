import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Scanning {

    List<Integer> allScannedBooks = new ArrayList<>();

    public void process(List<Library> libraryList, int days, Map<Integer, Integer> orderedBookScores, String resultPath) throws IOException {

        // Sort libraries from highest pointer
        List<Library> orderedLibraries =
                libraryList.stream()
                        .sorted(Comparator.comparing(e -> -e.getPointer()))
                        .collect(Collectors.toList());

        /*
        For each library check if books to be scanned appeared in aggregated array of all scanned books (distinct)
        from previous libraries. If any appear in currently scanned library - we're removing this book because we won't
        gain any extra points for that. After all if library become empty - we're removing library.
         */

        List<Library> finalOrderedLibraries = orderedLibraries;
        orderedLibraries.forEach(library -> {
            List<Integer> tmpList = new ArrayList<>();
            finalOrderedLibraries.stream().limit(finalOrderedLibraries.indexOf(library)).forEach(library1 ->
                    tmpList.addAll(library1.getBooksByScore()
                            .stream()
                            .distinct()
                            .collect(Collectors.toList())));

            library.setBooksByScore(library.getBooksByScore()
                    .stream()
                    .filter(elem -> !tmpList.contains(elem))
                    .collect(Collectors.toList()));
        });

        /*
        For each library check if we removed any books - if so add them back from deletedbooks array at part 1 ( Library.calculateValues() )
        Remove all libraries that're not containing any books to be scanned. Count pointer one more time - it's possible that's been changed.
         */

        orderedLibraries.forEach(library -> {
            if (library.getBooksByScore().size() == library.getBooksByScoreLength() || library.getDeletedBooks() == null) {
                return;
            }

            int difference = library.getBooksByScoreLength() - library.getBooksByScore().size();
            List<Integer> tmpList = new ArrayList<>(library.getBooksByScore());
            int deletedBooksCount = difference >= (long) library.getDeletedBooks().size() ? library.getDeletedBooks().size() : difference;
            List<Integer> tmpDeletedBooks =
                    library.getDeletedBooks()
                            .stream()
                            .filter(integer -> integer >= deletedBooksCount)
                            .collect(Collectors.toList());

            tmpList.addAll(
                    library.getDeletedBooks()
                            .stream()
                            .limit(deletedBooksCount)
                            .collect(Collectors.toList()));

            library.setDeletedBooks(tmpDeletedBooks);
            library.setBooksByScore(tmpList);
            library.setBooksByScoreLength(library.getBooksByScore().size());
        });

        orderedLibraries =
                orderedLibraries
                        .stream()
                        .filter(library -> library.getBooksByScore().size() > 0)
                        .collect(Collectors.toList());

        orderedLibraries.forEach( library -> library.recalculateValues( orderedBookScores, days));
        orderedLibraries =
                orderedLibraries
                        .stream()
                        .sorted(Comparator.comparing(e-> -e.getPointer()))
                        .collect(Collectors.toList());

        /*
        Checking if sum of days for signing up all libraries >= days - if so, we know that last library won't scan any book
        so we're looking for some that would gain some extra points for us and we're switching them.
         */

        int daysSum = 0;
        for (Library library : orderedLibraries) {
            daysSum += library.getSignUpProcessDays();

            if (daysSum < days) {
                continue;
            }

            if (library.equals(orderedLibraries.get(orderedLibraries.size() - 1))) {
                break;
            }

            List<Library> tmpList = orderedLibraries.subList(0, orderedLibraries.indexOf(library) + 1);
            tmpList.remove(library);
            int daysLeft = library.getSignUpProcessDays() - daysSum + days;

            orderedLibraries.subList(orderedLibraries.indexOf(library) + 1, orderedLibraries.size());

            List<Library> validLibraries =
                    orderedLibraries
                            .stream().
                            filter(library1 -> library1.getSignUpProcessDays() < daysLeft)
                            .collect(Collectors.toList());

            if (validLibraries.size() > 0) {
                for (Library validLibrary : validLibraries) {
                    validLibrary.recalculateValues(orderedBookScores, daysLeft);
                }
            } else {
                orderedLibraries = tmpList;
                break;
            }

            Library validLibrariesFirst =
                    validLibraries
                            .stream()
                            .sorted(Comparator.comparing(Library::getPointer))
                            .collect(Collectors.toList())
                            .get(0);
            tmpList.add(validLibrariesFirst);
            orderedLibraries = tmpList;
            break;

        }

        //Scanning process
        for (int i = 0; i <= days; i++) {

            for (Library library : orderedLibraries) {

                Library actualSignUpLibrary = orderedLibraries.stream().filter(Library::isSignUpProcessing).findFirst().orElse(null);

                //Leaving only unscanned books
                library.setBooksByScore(
                        library.getBooksByScore()
                                .stream()
                                .filter(e -> !allScannedBooks.contains(e))
                                .collect(Collectors.toList()));

                if (library.getSignUpProcessDays() == 0 && library.getBooksByScore().size() > 0 ||
                        (actualSignUpLibrary == null || actualSignUpLibrary.equals(library)) && library.signUpProcess()
                ) {
                    //Scanning process and adding to all scanned books list
                    allScannedBooks.addAll(library.scanningProcess(allScannedBooks));
                }

            }
        }
            int countScannedLibraries =
                    (int) orderedLibraries
                            .stream()
                            .filter(library -> library.getScannedBooks().size() > 0)
                            .count();

            StringBuilder result = new StringBuilder(String.format("%s\n", countScannedLibraries));

            List<Library> librariesThatScannedBooks =
                    orderedLibraries
                            .stream()
                            .filter(library -> library.getScannedBooks().size() > 0)
                            .collect(Collectors.toList());

            for (Library libraryThatScannedBooks : librariesThatScannedBooks) {
                result.append(libraryThatScannedBooks.getLibraryId())
                        .append(" ")
                            .append(libraryThatScannedBooks.getScannedBooks().size())
                                .append("\n")
                                    .append(libraryThatScannedBooks.getScannedBooks()
                                            .stream()
                                            .map(Object::toString)
                                            .collect(Collectors.joining(" ")) )
                                                .append("\n");
            }

            Files.write(Paths.get(resultPath), Arrays.asList(result.toString().trim()), StandardCharsets.UTF_8);
        }

}
