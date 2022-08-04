import java.util.*;
import java.util.Collection;
import java.util.stream.Collectors;

class Program {
	
	public static List<Integer[]> fourNumberSum(int[] array, int targetSum) {
        // If no four numbers sum up to the target sum, the function should return an empty array.
        if(array.length < 4) return new ArrayList();
        
	    List<Integer> arraylist = Arrays.stream(array).boxed().collect(Collectors.toList());
        
        // find all quadruplets in the array
        List<Integer[]> combinations = getCombinations(arraylist,  4)
            .stream()
            // that sum up to the target sum
            .filter(tuple -> tuple.stream().reduce(0, Integer::sum).equals(targetSum))
            // sort to be able to remove duplicates later
            .map(tuple -> { Collections.sort(tuple); return tuple; })
            .map(t -> toIntegerArray(t))
            .collect(Collectors.toList());

        return removeDuplicates(combinations);
    }
    
    private static List<Integer[]> removeDuplicates(List<Integer[]> lists) {
        List<Integer[]> withoutDuplicates = new ArrayList<>();
        lists.forEach(list -> {
            if(!withoutDuplicates.stream().anyMatch(wd -> Arrays.equals(list, wd))) {
                withoutDuplicates.add(list);
            }
        });
        return withoutDuplicates;
    }

    private static List<Integer> cloneAndRemove(List<Integer> array, Integer number) {
        List<Integer> arrayWithoutNumber = new ArrayList(array);
        arrayWithoutNumber.remove(number);
        return arrayWithoutNumber;
    }

    private static List<Integer> cloneAndAdd(List<Integer> array, Integer number) {
        List<Integer> arrayWithNumber = new ArrayList<>(array); 
        arrayWithNumber.add(number); 
        return arrayWithNumber;
    }
		
	// Get all combinations of tuples(n)
    private static List<List<Integer>> getCombinations(List<Integer> array, int n) {
        // base case: just convert List<Integer> to List<List<Integer>>
        // eg: [1, 2] to [[1], [2]]
        if(n == 1) {
            return array.stream()
                    .map(number -> Arrays.asList(number))
                    .collect(Collectors.toList());
        }
        
        // recursive case
        // for each number get combinations in the following way:
        //      get combinations n-1 without that number
        //      add the number to all those combinations
        // join combinations
        return array.stream()
                .map(number -> {
                    return getCombinations(cloneAndRemove(array, number), n - 1)
                            .stream()
                            .map(list -> cloneAndAdd(list, number))
                            .collect(Collectors.toList());
                })
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
    
    private static Integer[] toIntegerArray(List<Integer> list) {
        int[] intList = list.stream().mapToInt(Integer::valueOf).toArray();
        return  Arrays.stream(intList).boxed().toArray(Integer[]::new);
    }
}
