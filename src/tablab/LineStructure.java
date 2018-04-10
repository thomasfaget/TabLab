package tablab;

import java.util.*;

/** LineStructure contains the labels of the lines of the tab.
 * Prevents duplicate elements in the structure
 *
 */
public class LineStructure implements Iterable<String> {

    // Classic :
    public static String DO = "Do";
    public static String RE = "Ré";
    public static String MI = "Mi";
    public static String FA = "Fa";
    public static String SOL = "Sol";
    public static String LA = "La";
    public static String SI = "Si";
    public static String DO_DIESE = "Do#";
    public static String RE_DIESE = "Ré#";
    public static String FA_DIESE = "Fa#";
    public static String SOL_DIESE = "Sol#";
    public static String LA_DIESE = "La#";

    // Drum lines :
    public static String CRASH = "Crash";
    public static String RIDE = "Ride";
    public static String SPLASH = "Splash";
    public static String HIGH_TOM = "High Tom";
    public static String MID_TOM = "Middle Tom";
    public static String LOW_TOM = "Low Tom";
    public static String FLOOR_TOM = "Floor";
    public static String SNARE = "Snare";
    public static String BASS = "Bass Drum";
    public static String CLOSED_HIT_HAT = "Hit-Hat";
    public static String OPEN_HIT_HAT = "Open Hit-Hat";


    private List<String> structure = new ArrayList<>();


    public LineStructure(Collection<? extends String> c) {
        // Copy the list
        List<String> c2 = new ArrayList<>(c);

        // Remove duplicate elements in c2 and current list
        List<String> c3 = distinct(c2);
        for (String s : c2) {
            if (structure.contains(s)) {
                c3.remove(s);
            }
        }
        structure.addAll(c3);
    }

    /**
     * get the element at the given index
     * @param index the index
     * @return the element at the index
     */
    public String get(int index) {
        return structure.get(index);
    }

    /**
     * Check if the structure contains the given line
     * @param lineType the line
     * @return true if the structure contains the line
     */
    public boolean contains(String lineType) {
        return structure.contains(lineType);
    }

    /**
     * Get the number of line in the structure
     * @return the number of line
     */
    public int size() {
        return structure.size();
    }

    /**
     * Get the intersection between this structure and an other
     *
     * @param otherStructure the other structure to compare
     * @return the intersection of the structure
     */
    public LineStructure getIntersection(LineStructure otherStructure) {
        List<String> list = new ArrayList<>();

        for (String s : this.structure) {
            if (otherStructure.structure.contains(s)) {
                list.add(s);
            }
        }
        return new LineStructure(list);
    }

    /**
     * Get the union between this structure and an other
     *
     * @param otherStructure the other structure to compare
     * @return the union of the structure
     */
    public LineStructure getUnion(LineStructure otherStructure) {
        ArrayList<String> resultStructure = new ArrayList<>(this.structure);

        for (String s : otherStructure.structure) {
            if (!resultStructure.contains(s)) {
                resultStructure.add(s);
            }
        }

        return new LineStructure(resultStructure);
    }

    @Override
    public String toString() {
        return structure.toString();
    }

    /**
     * Distinct all the elements of a list
     *
     * @param structure the list to distinct
     * @return the distinct list
     */
    private static List<String> distinct(List<String> structure) {
        List<String> resultStructure = new ArrayList<>();
        for (String s : structure) {
            if (!resultStructure.contains(s)) {
                resultStructure.add(s);
            }
        }
        return resultStructure;
    }

    @Override
    public Iterator<String> iterator() {
        return new LineStructureIterator();
    }


    /** An iterator for the LineStructure
     */
    private class LineStructureIterator implements Iterator<String> {

        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < structure.size();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return structure.get(index++);
        }

        @Override
        public void remove() {
            if (index < 0) {
                throw  new IllegalStateException();
            }

            try {
                structure.remove(--index);
            }
            catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }
    }
}
