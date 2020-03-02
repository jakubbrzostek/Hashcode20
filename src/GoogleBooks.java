import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

public class GoogleBooks {

    public static void main(String[] args) {

        Map<String, String> dataSets = new HashMap<String, String>() {
            {
                put("a_example.txt", String.valueOf(getClass().getClassLoader().getResource("a_example.txt")));
                put("b_read_on.txt", String.valueOf(getClass().getClassLoader().getResource("b_read_on.txt")));
                put("c_incunabula.txt", String.valueOf(getClass().getClassLoader().getResource("c_incunabula.txt")));
                put("d_tough_choices.txt", String.valueOf(getClass().getClassLoader().getResource("d_tough_choices.txt")));
                put("e_so_many_books.txt", String.valueOf(getClass().getClassLoader().getResource("e_so_many_books.txt")));
                put("f_libraries_of_the_world.txt", String.valueOf(getClass().getClassLoader().getResource("f_libraries_of_the_world.txt")));
            }
        };

        for (Map.Entry<String, String> dataSet : dataSets.entrySet()) {
            String[] splitted = dataSet.getValue().split("\n");
            int books = Integer.parseInt(splitted[0].split(" ")[0]);
            int days = Integer.parseInt(splitted[0].split(" ")[2]);
            int[] scores = Arrays.stream(splitted[1].split(" ")).mapToInt(Integer::parseInt).toArray();

            Map<Integer, Integer> bookScores = new HashMap<Integer, Integer>();
            for (int i = 0; i < books; i++) {
                bookScores.put(i, scores[i]);
            }

            Map<Integer, Integer> orderedBookScores = bookScores
                    .entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(
                            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                    LinkedHashMap::new));


            List<Library> libraries = new ArrayList<>();
            int counter = 0;

            for (int i = 2; i < splitted.length - 2; i += 2) {
                libraries.add(new Library(
                                counter,
                                IntStream.of(Integer.parseInt(Arrays.toString(splitted[1].split(" ")))).boxed().collect(Collectors.toCollection(ArrayList::new)),
                                Integer.parseInt(splitted[i].split(" ")[1]),
                                Integer.parseInt(splitted[i].split(" ")[2])
                        )
                );
                libraries.get(libraries.size() - 1).createValues(orderedBookScores, days);
                counter++;
            }

            Scanning scanning = new Scanning();
            String resultPath = (Paths.get("").toAbsolutePath()).resolve(dataSet.getKey()).toString();
            scanning.process(libraries, days, orderedBookScores, resultPath);

        }
    }
}
