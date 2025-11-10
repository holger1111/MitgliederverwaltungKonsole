package New.Manager;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseManager<T> {

    protected List<T> items = new ArrayList<>();

    // Fügt ein Element zur Liste hinzu.
    public void add(T item) {
        items.add(item);
    }

    // Entfernt ein Element aus der Liste.
    public void remove(T item) {
        items.remove(item);
    }

    // Gibt die Liste aller Elemente zurück.
    public List<T> getAll() {
        return new ArrayList<>(items);
    }

    // Löscht alle gespeicherten Elemente.
    public void clear() {
        items.clear();
    }

    // Abstrakte Methode, die spezialisierte Manager überschreiben können/müssen.
    public abstract void process();
}
