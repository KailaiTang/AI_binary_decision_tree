import java.util.ArrayList;
import java.util.List;

public class OwnClass2 {

  public static void main(String[] args) {
    // TODO Auto-generated method stub
    int[][] list1 = new int[][] {{1, 5, 8, 6, 3, 5, 4, 10, 9, 0},
        {10, 7, 1, 5, 5, 7, 3, 5, 8, 0},
        {9, 4, 3, 1, 6, 9, 6, 8, 7, 1},
        {8, 6, 5, 7, 2, 5, 6, 4, 7, 0},
        {8, 5, 1, 1, 8, 5, 4, 1, 6, 0},
        {3, 1, 3, 6, 5, 5, 9, 2, 6, 0},
        {2, 8, 1, 3, 5, 7, 10, 5, 2, 0},
        {6, 9, 4, 9, 10, 5, 5, 8, 3, 0},
        {3, 5, 7, 6, 4, 2, 7, 5, 7, 1},
        {6, 6, 1, 1, 9, 9, 9, 4, 5, 0},
        {9, 3, 2, 8, 5, 8, 6, 9, 9, 0},
        {3, 5, 7, 1, 2, 2, 9, 3, 7, 0},
        {7, 7, 4, 2, 10, 6, 9, 6, 6, 1},
        {6, 5, 5, 10, 8, 7, 9, 4, 9, 1},
        {2, 6, 2, 3, 6, 10, 6, 9, 4, 0},
        {6, 3, 10, 4, 6, 4, 7, 1, 2, 0},
        {9, 2, 1, 8, 3, 1, 1, 5, 7, 1},
        {4, 6, 7, 8, 9, 6, 5, 5, 6, 0},
        {6, 8, 1, 9, 2, 5, 5, 5, 9, 0},
        {4, 2, 9, 1, 7, 7, 2, 10, 1, 1},
        {6, 9, 1, 8, 1, 7, 6, 8, 2, 0},
        {2, 6, 6, 2, 5, 3, 1, 3, 1, 1},
        {5, 8, 4, 5, 7, 3, 3, 7, 5, 1}};


    List<List<Integer>> examples1 = new ArrayList<List<Integer>>();
    for (int i = 0; i < list1.length; i++) {
      examples1.add(new ArrayList<Integer>());
      for (int j = 0; j < list1[i].length; j++) {
        examples1.get(i).add(list1[i][j]);
      }
    }
    
    List<List<Double>> infoGain1 = new ArrayList<List<Double>>();
    for (int i = 0; i < list1[0].length-1; i++) { // attribute
      infoGain1.add(new ArrayList<Double>());
      for(int j = 1; j <= 10; j++) { // threshold
        infoGain1.get(i).add(findInformationGain(examples1,i,j));
        // System.out.print(String.format("%.2f",infoGain1.get(i).get(j-1))+", ");
      }
      // System.out.println();
    }
    
    for (int i = 0; i < list1[0].length-1; i++) {
      for(int j = 1; j <= 10; j++) {
        System.out.print(String.format("%.2f",infoGain1.get(i).get(j-1))+", ");
      }
      System.out.println();
    }
    
      

  }



  /**
   *
   * @param attribute
   * @param threshold
   * @return
   */
  private static double findInformationGain(
      List<List<Integer>> examples, int attribute, int threshold) {

    System.out.print("(entropy is " + findEntropy(examples)
        + "; conditional entropy is "
        + findConditionalEntropy(examples, attribute, threshold)
        + ") ");
    return findEntropy(examples)
        - findConditionalEntropy(examples, attribute, threshold);
  }

  /**
   *
   * @param examples
   * @return
   */
  private static double findEntropy(List<List<Integer>> examples) {
    // if example is empty, entropy is zero
    if (examples.size() == 0) {
      return 0;
    }

    // find the corresponding counts of class 0 and 1
    int class0 = 0;
    int class1 = 0;
    for (int i = 0; i < examples.size(); i++) {
      if (examples.get(i).get(9) == 0) {
        class0++;
      } else {
        class1++;
      }
    }

    // find the corresponding probability of class 0 and 1
    double class0Probability = (double) class0 / examples.size();
    double class1Probability = (double) class1 / examples.size();


    // calculate the entropy for left and right part
    // logxY = logY/logx
    double class0Entropy;
    double class1Entropy;
    // class 0 part:
    if (class0Probability == 0) {
      class0Entropy = 0;
    } else {
      class0Entropy = (-1) * class0Probability
          * Math.log(class0Probability) / Math.log(2);
    }
    // class 1 part:
    if (class1Probability == 0) {
      class1Entropy = 0;
    } else {
      class1Entropy = (-1) * class1Probability
          * Math.log(class1Probability) / Math.log(2);
    }
    return class0Entropy + class1Entropy;
  }

  /**
   *
   * @param examples
   * @param attribute
   * @param threshold
   * @return
   */
  private static double findConditionalEntropy(
      List<List<Integer>> examples, int attribute, int threshold) {
    // split the examples to left and right examples according to the attribute
    // if the attribute has value <= threshold, categorize to left example
    // if the attribute has value > threshold, categorize to right example
    List<List<Integer>> left = new ArrayList<List<Integer>>();
    List<List<Integer>> right = new ArrayList<List<Integer>>();
    
    
    for (int i = 0; i < examples.size(); i++) {

      if (examples.get(i).get(attribute) <= threshold) {
        left.add(examples.get(i));
      } else {
        right.add(examples.get(i));
      }
    }
    // find the specific conditional entropy for examples according to left and right
    return findEntropy(left) + findEntropy(right);
  }


}
