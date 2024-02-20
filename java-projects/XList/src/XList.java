import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

class XList<T> extends ArrayList<T> {
    public XList() {
        super();
    }
    public XList(Collection<? extends T> c) {
        super(c);
    }
    public XList(T... items) {
        super(Arrays.asList(items));
    }
    public static <T> XList<T> of(Collection<? extends T> c) {
        return new XList<>(c);
    }
    public static <T> XList<T> of(T... items) {
        return new XList<>(items);
    }

    public static XList<String> charsOf(String string) {
        return new XList<>(string.split(""));
    }

    public static XList<String> tokensOf(String string, String sep) {
        if (sep == null) {
            sep = "\\s+";
        }
        return new XList<>(string.split(sep));
    }

    public XList<T> union(Collection<? extends T> c) {
        XList<T> unionList = new XList<>(this);
        unionList.addAll(c);
        return unionList;
    }

    public XList<T> diff(Collection<? extends T> c) {
        XList<T> diffList = new XList<>(this);
        diffList.removeAll(c);
        return diffList;
    }

    public XList unique() {
        XList list = new XList<>();
        this.forEach((x) -> {
            if (!list.contains(x)) {
                list.add(x);
            }
        });
        return list;
    }

    public XList<XList<T>> combine() {
        int n = this.size();
        int[] indices = new int[n];
        XList<XList<T>> result = new XList<>();

        while (true) {
            XList<T> combination = new XList<>();
            for (int i = 0; i < n; i++) {
                combination.add(((List<T>) this.get(i)).get(indices[i]));
            }
            result.add(combination);

            int j = 0;
            while (j < n && indices[j] == ((List<T>) this.get(j)).size() - 1) {
                indices[j] = 0;
                j++;
            }
            if (j == n) {
                break;
            }
            indices[j]++;
        }

        return result;
    }

    public <R> XList collect(Function<XList<R>, R> func) {
        XList list = new XList();
        for (XList<R> element : (XList<XList<R>>) this) {
            list.add(func.apply(element));
        }
        return list;
    }

    public String join(String sep) {
        StringBuilder result = new StringBuilder();
        for (T element : this) {
            result.append(element).append(sep);
        }
        if (result.length() > 0) {
            result.setLength(result.length() - sep.length());
        }
        return result.toString();
    }

    public String join() {
        return join("");
    }

    public void forEachWithIndex(BiConsumer<T, Integer> cons) {
        for (int i = 0; i < this.size(); i++) {
            cons.accept(this.get(i), i);
        }
    }
}