import java.util.*;

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

            Map<Integer,Integer> orderedBookScores = bookScores
                    .entrySet()
                    .stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(
                            toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                    LinkedHashMap::new));

            List<Library> libraries = new List<Library>() {

                @Override
                public int size() {
                    return 0;
                }

                @Override
                public boolean isEmpty() {
                    return false;
                }

                @Override
                public boolean contains(Object o) {
                    return false;
                }

                @Override
                public Iterator<Library> iterator() {
                    return null;
                }

                @Override
                public Object[] toArray() {
                    return new Object[0];
                }

                @Override
                public <T> T[] toArray(T[] a) {
                    return null;
                }

                @Override
                public boolean add(Library library) {
                    return false;
                }

                @Override
                public boolean remove(Object o) {
                    return false;
                }

                @Override
                public boolean containsAll(Collection<?> c) {
                    return false;
                }

                @Override
                public boolean addAll(Collection<? extends Library> c) {
                    return false;
                }

                @Override
                public boolean addAll(int index, Collection<? extends Library> c) {
                    return false;
                }

                @Override
                public boolean removeAll(Collection<?> c) {
                    return false;
                }

                @Override
                public boolean retainAll(Collection<?> c) {
                    return false;
                }

                @Override
                public void clear() {

                }

                @Override
                public Library get(int index) {
                    return null;
                }

                @Override
                public Library set(int index, Library element) {
                    return null;
                }

                @Override
                public void add(int index, Library element) {

                }

                @Override
                public Library remove(int index) {
                    return null;
                }

                @Override
                public int indexOf(Object o) {
                    return 0;
                }

                @Override
                public int lastIndexOf(Object o) {
                    return 0;
                }

                @Override
                public ListIterator<Library> listIterator() {
                    return null;
                }

                @Override
                public ListIterator<Library> listIterator(int index) {
                    return null;
                }

                @Override
                public List<Library> subList(int fromIndex, int toIndex) {
                    return null;
                }
            };
            int counter = 0;

            for (int i = 2; i < splitted.length - 2; i += 2) {
                libraries.add(new Library(
                                counter,
                                Arrays.stream(splitted[1].split(" ")).mapToInt(Integer::parseInt).toArray(),
                                Integer.parseInt(splitted[i].split(" ")[1]),
                                Integer.parseInt(splitted[i].split(" ")[2]),
                                new List<Integer>() {
                                    @Override
                                    public int size() {
                                        return 0;
                                    }

                                    @Override
                                    public boolean isEmpty() {
                                        return false;
                                    }

                                    @Override
                                    public boolean contains(Object o) {
                                        return false;
                                    }

                                    @Override
                                    public Iterator<Integer> iterator() {
                                        return null;
                                    }

                                    @Override
                                    public Object[] toArray() {
                                        return new Object[0];
                                    }

                                    @Override
                                    public <T> T[] toArray(T[] a) {
                                        return null;
                                    }

                                    @Override
                                    public boolean add(Integer integer) {
                                        return false;
                                    }

                                    @Override
                                    public boolean remove(Object o) {
                                        return false;
                                    }

                                    @Override
                                    public boolean containsAll(Collection<?> c) {
                                        return false;
                                    }

                                    @Override
                                    public boolean addAll(Collection<? extends Integer> c) {
                                        return false;
                                    }

                                    @Override
                                    public boolean addAll(int index, Collection<? extends Integer> c) {
                                        return false;
                                    }

                                    @Override
                                    public boolean removeAll(Collection<?> c) {
                                        return false;
                                    }

                                    @Override
                                    public boolean retainAll(Collection<?> c) {
                                        return false;
                                    }

                                    @Override
                                    public void clear() {

                                    }

                                    @Override
                                    public Integer get(int index) {
                                        return null;
                                    }

                                    @Override
                                    public Integer set(int index, Integer element) {
                                        return null;
                                    }

                                    @Override
                                    public void add(int index, Integer element) {

                                    }

                                    @Override
                                    public Integer remove(int index) {
                                        return null;
                                    }

                                    @Override
                                    public int indexOf(Object o) {
                                        return 0;
                                    }

                                    @Override
                                    public int lastIndexOf(Object o) {
                                        return 0;
                                    }

                                    @Override
                                    public ListIterator<Integer> listIterator() {
                                        return null;
                                    }

                                    @Override
                                    public ListIterator<Integer> listIterator(int index) {
                                        return null;
                                    }

                                    @Override
                                    public List<Integer> subList(int fromIndex, int toIndex) {
                                        return null;
                                    }
                                }
                        )
                );
                libraries.get(libraries.size() - 1).createValues(orderedBookScores, days);
                counter++;
            }

            Scanning scanning = new Scanning();
            String resultPath = "";
            scanning.process( libraries, days, orderedBookScores, resultPath);


        }
    }
}
