import java.awt.font.NumericShaper;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Random;

/**
 * 
 */

/**
 * @author rajprateek
 *
 */
public class BloomFilter {

	private final int tableSize;
	//	private final int c;
	private final BitSet bitSet;
	private final int numHashes;
	private final int[] randomNumsForHash;
	private Random rand;


	/**
	 * 
	 * @param n the table size
	 * @param c where c = n/m
	 */
	public BloomFilter(int n, int c){
		rand = new Random();
		tableSize = n;
		//		this.c = c;
		bitSet = new BitSet(n);
		numHashes = (int)Math.ceil(c*  Math.log(2));
		randomNumsForHash = new int[numHashes];

		for(int i=0;i<numHashes; i++){
			randomNumsForHash[i] = rand.nextInt(Integer.MAX_VALUE/2);
		}
	}

	public void add(int element){
		for(int i=0;i<numHashes; i++){
			int indexToSet = getHashNdx(element,i);
			bitSet.set(indexToSet);
		}
	}


	private int getHashNdx(int element, int i) {
		return Math.abs((randomNumsForHash[i]^element)) % tableSize;
	}

	private boolean contains(int element){
		for(int i=0;i<numHashes;i++){
			int indexToCheck = getHashNdx(element, i);
			if(indexToCheck<0){
				System.out.println("ERRORRRRRR"+ element + " , "+i);
			}
			if(bitSet.get(indexToCheck)==false){
				return false;
			}
		}
		return true;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//		int c  = 1;

		for (int c=1; c<=50;c*=2){
			for(int n=10;n<10000; n*=10){
				System.out.println("Running for c="+c+" , n= "+n);
				//		int n  = 1;
				int m = n/c;

				HashSet<Integer> addedNums = new HashSet<Integer>();

				BloomFilter bloomFilter  = new BloomFilter(n,c);


				Random rand2 = new Random();
				for (int i = 0; i<m; i++){
					int numToAdd  = rand2.nextInt(Integer.MAX_VALUE/2);
					bloomFilter.add(numToAdd);
					addedNums.add(numToAdd);
				}

				//checking for any false negatives
				int falseNegs =0;
				for (int elem: addedNums){
					if(bloomFilter.contains(elem)==false){
						falseNegs++;
					}
				}
				System.out.println("	Number of False negatives: "+falseNegs);

				long numFalsePositives = 0;
				long  numOfElemetsNotInSet = (Integer.MAX_VALUE/2)-m;
				for (int numToCheck=0; numToCheck<Integer.MAX_VALUE/2;numToCheck++){
					if (bloomFilter.contains(numToCheck) == true){
						if(addedNums.contains(numToCheck) == false){
							numFalsePositives++;
							//System.out.println("Incremented");
						}
						
					}
					else{
//						System.out.println("not there");
					}
				}
				System.out.println("	Number of False Positives: "+numFalsePositives);
//				System.out.println("    Number of elements not in set: " + numOfElemetsNotInSet);
//				System.out.println("	False Postive Rate = "+ ((double)numFalsePositives)/(double)numOfElemetsNotInSet);




			}
		}
	}

}
