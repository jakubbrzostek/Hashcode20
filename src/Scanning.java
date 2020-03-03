import java.util.*;
import java.util.stream.Collectors;

public class Scanning {

    private List<Integer> allScannedBooks;

    //allScannedBooks = new ArrayList<>();

    public void process(List<Library> libraryList, int days, Map<Integer, Integer> orderedBookScores, String resultPath) {

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
        orderedLibraries.forEach( library -> {
            List<Integer> tmpList = new ArrayList<>();
            orderedLibraries.stream().limit(orderedLibraries.indexOf(library)).forEach( library1 ->
                    tmpList.addAll( library1.getBooksByScore()
                            .stream()
                            .distinct()
                            .collect(Collectors.toList()) ) );

            library.setBooksByScore(library.getBooksByScore()
                    .stream()
                    .filter( elem -> !tmpList.contains( elem))
                    .collect(Collectors.toList()) );
        } );

        /*

        Dla każdej biblioteki sprawdzamy czy nie usuneliśmy jakiejś książki, a jeśli usunęliśmy i są jakieś książki w tablicy usuniętych
         z etapu pierwszego obliczania danych( Library.calculateValues() ) to dodajemy. Usuwamy wszystkie biblioteki, które nie mają książek
          do zeskanowania. Na nowo obliczamy wskaźnik - mógł się zmienić.

         */
        orderedLibraries.forEach( library -> {
            if ( library.getBooksByScore().size() == library.getBooksByScoreLength() || library.getDeletedBooks() == null) {return;};
            int difference = library.getBooksByScoreLength() - library.getBooksByScore().size();


        });

    };
        String result;

        //List<Library> orderedLibraries = libraryList.sort( new Library() => new Library().);

        };
