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

        // Ustawiamy biblioteki w kolejności od najwyższego pointera
        List<Library> orderedLibraries =
                libraryList.stream()
                        .sorted(Comparator.comparing(Library::getPointer))
                        .collect(Collectors.toList());

        /*

        Dla każdej biblioteki sprawdzamy czy książki do skanowania nie pojawiły się w zaagregowanej tabeli wszystkich książek (distinct)
         z poprzednich bibliotek. Jeśli jakaś książka z danej biblioteki pojawia się w aktualnej bibliotece to ją usuwamy - jeżeli wszystkie
          ksiązki z aktualnej biblioteki zostały usunięte to usuwamy bibliotekę, ponieważ skanowanie książek będzie bezpunktowe.

         */
//TODO - przemysleć czy nie zmienić na pętle  foreach
//        for (Library orderedLibrary : orderedLibraries) {
//            orderedLibrary.
//        }
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

        Dla każdej biblioteki sprawdzamy czy nie usuneliśmy jakiejś książki, a jeśli usunęliśmy i są jakieś książki w tablicy usuniętych
         z etapu pierwszego obliczania danych( Library.calculateValues() ) to dodajemy. Usuwamy wszystkie biblioteki, które nie mają książek
          do zeskanowania. Na nowo obliczamy wskaźnik - mógł się zmienić.

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
                        .sorted(Comparator.comparing(Library::getPointer))
                        .collect(Collectors.toList());

        /*

        Jeśli suma dni rejestracji bibliotek >= liczbie wszystkich dni, to wiemy, że ostatnia biblioteka nie zeskanuje
        żadnej książki, tak więc szukamy jakiejś co zdobyła by dla nas kilka dodatkowych punktów i podmieniamy, nawet
        jeśli miała by zeskanować jedną książke

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

//         //Przypisujemy pierwszy i będziemy zwiększać w pętli, żeby od razu nie iterować po wszystkich posortowanych bibliotekach
//        int orderedLibrariesCounter = orderedLibraries.size();
//        List<Library> copiedOrderedLibraries = new ArrayList<Library>();
//        copiedOrderedLibraries.add(orderedLibraries.get(0));
//        orderedLibrariesCounter--;

        //Proces skanowania
        for (int i = 0; i <= days; i++) {

            for (Library library : orderedLibraries) {

                Library actualSignUpLibrary = orderedLibraries.stream().filter(Library::isSignUpProcessing).findFirst().orElse(null);

                //Przypisujemy tylko niezeskanowane ksiażki
                library.setBooksByScore(
                        library.getBooksByScore()
                                .stream()
                                .filter(e -> !allScannedBooks.contains(e))
                                .collect(Collectors.toList()));

                if (library.getSignUpProcessDays() == 0 && library.getBooksByScore().size() > 0 ||
                        (actualSignUpLibrary == null || actualSignUpLibrary.equals(library)) && library.signUpProcess()
                ) {
                    //Skanujemy i przypisujemy zeskanowane do listy wszystkich zeskanowanych
                    allScannedBooks.addAll(library.scanningProcess(allScannedBooks));
                }

//                //Jeśli w ostatniej bibliotece trwa rejestracja to nie dodajemy kolejnych
//                if ( copiedOrderedLibraries.get(copiedOrderedLibraries.size() - 1).isSignUpProcessing()) {
//                    continue;
//                }
//
//                //Kolejny element do zainsertowania
//                Library firstElementToInsert = orderedLibraries.get(orderedLibraries.size() - orderedLibrariesCounter - 1);
//
//                //Jeśli nie ma pierwszego elementu to nie dodajemy
//                if (firstElementToInsert == null) { continue;}
//
//                //Insertujemy element do listy i podnosimy licznik
//                copiedOrderedLibraries.add(orderedLibraries.get(orderedLibraries.size() - orderedLibrariesCounter - 1));
//                orderedLibrariesCounter--;
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
