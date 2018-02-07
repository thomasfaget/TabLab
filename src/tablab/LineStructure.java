package tablab;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/** LineStructure contains the labels of the lines of the tab.
 * Prevents duplicate elements in the structure
 *
 */
public class LineStructure extends ArrayList<String> {

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


    // Override Add methods and constructor to prevent duplicate elements

    public LineStructure() {
        super();
    }

    public LineStructure(int initialCapacity) {
        super(initialCapacity);
    }

    public LineStructure(Collection<? extends String> c) {
        super();
        this.addAll(c);
    }

    @Override
    public boolean add(String e) {
        return !this.contains(e) && super.add(e);
    }

    @Override
    public void add(int index, String e) {
        if (!this.contains(e)) {
            super.add(index, e);
        }
    }


    @Override
    public boolean addAll(Collection<? extends String> c) {

        // Copy the list
        List<String> c2 = new ArrayList<>(c);

        // Remove duplicate elements in c2 and current list
        List<String> c3 = c2.stream().distinct().collect(Collectors.toList());
        for (String s : c2) {
            if (this.contains(s)) {
                c3.remove(s);
            }
        }
        return super.addAll(c3);
    }

    @Override
    public boolean addAll(int index, Collection<? extends String> c) {

        // Copy the list
        List<String> c2 = new ArrayList<>(c);

        // Remove duplicate elements in c2 and current list
        List<String> c3  = c2.stream().distinct().collect(Collectors.toList());
        for (String s : c2) {
            if (this.contains(s)) {
                c3.remove(s);
            }
        }
        return super.addAll(index, c3);
    }


    /**
     * Get the intersection between this structure and an other
     *
     * @param otherStructure the other structure to compare
     * @return the intersection of the structure
     */
    public LineStructure getIntersection(LineStructure otherStructure) {
        LineStructure resultStructure = new LineStructure();

        for (String line : this) {
            if (otherStructure.contains(line)) {
                resultStructure.add(line);
            }
        }
        return resultStructure;
    }

    /**
     * Get the union between this structure and an other
     *
     * @param otherStructure the other structure to compare
     * @return the uni of the structuonre
     */
    public LineStructure getUnion(LineStructure otherStructure) {
        LineStructure resultStructure = new LineStructure();

        resultStructure.addAll(this);

        for (String line : otherStructure) {
            if (!this.contains(line)) {
                resultStructure.add(line);
            }
        }
        return resultStructure;
    }


}
