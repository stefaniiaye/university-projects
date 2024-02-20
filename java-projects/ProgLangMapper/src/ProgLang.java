import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ProgLang {
    private Map<String, List<String>> data = new LinkedHashMap<>();

    public ProgLang(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> strings = Arrays.stream(line.split("\\t")).distinct().collect(Collectors.toList());
                data.put(strings.get(0), strings.subList(1, strings.size()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, List<String>> getLangsMap() {
        return new LinkedHashMap<>(data);
    }

    public Map<String, List<String>> getProgsMap() {
        Map<String, List<String>> progsMap = new LinkedHashMap<>();
        data.forEach((lang, programmers) ->
                programmers.forEach(programmer ->
                        progsMap.computeIfAbsent(programmer, v -> new ArrayList<>()).add(lang)
                )
        );
        return progsMap;
    }

    public Map<String, List<String>> getLangsMapSortedByNumOfProgs() {
        Map<String, List<String>> result = getLangsMap();
        result = sorted(result, (o1, o2) -> {
            int compareBySize = o2.getValue().size() - o1.getValue().size();
            if (compareBySize != 0) {
                return compareBySize;
            }
            return o1.getKey().compareTo(o2.getKey());
        });
        return result;
    }

    public Map<String, List<String>> getProgsMapSortedByNumOfLangs() {
        Map<String, List<String>> result = getProgsMap();
        result = sorted(result, (o1, o2) -> {
            int compareBySize = o2.getValue().size() - o1.getValue().size();
            if (compareBySize != 0) {
                return compareBySize;
            }
            return o1.getKey().compareTo(o2.getKey());
        });
        return result;
    }

    public Map<String, List<String>> getProgsMapForNumOfLangsGreaterThan(int n) {
        Map<String, List<String>> result = getProgsMap();
        return filtered(result, o -> ((List) o).size() > n);
    }

    private <K, V> Map<K, V> sorted(Map<K, V> map, Comparator<Map.Entry<K, V>> comparator) {
        Map<K, V> result = new LinkedHashMap<>();
        map.entrySet()
                .stream()
                .sorted(comparator)
                .forEach(entry -> result.put(entry.getKey(), entry.getValue()));
        return result;
    }

    private <K, V> Map<K, V> filtered(Map<K, V> map, Predicate<V> predicate) {
        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (predicate.test(entry.getValue())) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }
}
