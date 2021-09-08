import java.util.ArrayList;
import java.util.List;

public class OwnTest {

  // public boolean test_1_find_entropy(List<List<Integer>> examples) {
  //
  // }

  public static void main(String[] args) {

    // create instances
    List<Integer> instance1 = new ArrayList<Integer>();
    instance1.add(1);
    instance1.add(5);
    instance1.add(2);
    instance1.add(4);
    instance1.add(0);

    List<Integer> instance2 = new ArrayList<Integer>();
    instance2.add(1);
    instance2.add(3);
    instance2.add(6);
    instance2.add(6);
    instance2.add(0);

    List<Integer> instance3 = new ArrayList<Integer>();
    instance3.add(2);
    instance3.add(7);
    instance3.add(5);
    instance3.add(8);
    instance3.add(0);

    List<Integer> instance4 = new ArrayList<Integer>();
    instance4.add(7);
    instance4.add(8);
    instance4.add(1);
    instance4.add(5);
    instance4.add(1);

    List<Integer> instance5 = new ArrayList<Integer>();
    instance5.add(9);
    instance5.add(9);
    instance5.add(7);
    instance5.add(7);
    instance5.add(1);

    List<Integer> instance6 = new ArrayList<Integer>();
    instance6.add(10);
    instance6.add(4);
    instance6.add(2);
    instance6.add(3);
    instance6.add(1);

    List<Integer> instance7 = new ArrayList<Integer>();
    instance7.add(8);
    instance7.add(5);
    instance7.add(5);
    instance7.add(4);
    instance7.add(1);

    // create examples
    List<List<Integer>> examples1 = new ArrayList<List<Integer>>();
    List<List<Integer>> examples2 = new ArrayList<List<Integer>>();

    examples1.add(instance1);
    examples1.add(instance2);
    examples1.add(instance3);
    examples1.add(instance4);
    examples1.add(instance5);
    examples1.add(instance6);
    examples1.add(instance7);

    examples2.add(instance1);
    examples2.add(instance2);
    examples2.add(instance3);
    examples2.add(instance4);
    examples2.add(instance5);
    examples2.add(instance6);


    // System.out.println(findEntropy(examples1));
    // System.out.println();
    // System.out.println(findEntropy(examples2));

    // System.out.println(findConditionalEntropy(examples1, 0, 2));
    // System.out.println();
    // System.out.println(findConditionalEntropy(examples2, 0, 2));
    //// System.out.println();
    // System.out.println(findConditionalEntropy(examples1, 0, 8));
    // System.out.println();
    // System.out.println(findConditionalEntropy(examples2, 0, 8));

    List<List<Double>> resultInfoGain1 = new ArrayList<List<Double>>();

    for(int i = 0;i<3;i++) {
      resultInfoGain1.add(new ArrayList<Double>());
      for(int j = 1;j<=10;j++) {
        resultInfoGain1.get(i).add(findInformationGain(examples1,i,j));
      }
    }
    
    List<List<Double>> resultInfoGain2 = new ArrayList<List<Double>>();

    for(int i = 0;i<3;i++) {
      resultInfoGain2.add(new ArrayList<Double>());
      for(int j = 1;j<=10;j++) {
        resultInfoGain2.get(i).add(findInformationGain(examples2,i,j));
      }
    }
    
    System.out.println(resultInfoGain1);
    System.out.println(resultInfoGain2);

  }


  private static double findEntropy(List<List<Integer>> examples) {
    // if example is empty, entropy is zero
    if (examples.size() == 0) {
      return 0;
    }

    // find the corresponding counts of class 0 and 1
    int class0 = 0;
    int class1 = 0;
    for (int i = 0; i < examples.size(); i++) {
      if (examples.get(i).get(4) == 0) {
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



  private static int bestAttribute(List<List<Integer>> examples,
      int[] attributes) {

    // initiate an array to record the information gain for corresponding attribute and threshold value
    double[] information_gain = new double[attributes.length];
    // for each attribute, find out corresponding best threshold value,
    // then find out corresponding information gain and record them
    for (int i = 0; i < attributes.length; i++) {
      int best_threshold = bestThreshold(examples, attributes[i]);
      information_gain[i] = findInformationGain(examples,
          attributes[i], best_threshold);
    }

    // find out the maximum information gain and the corresponding attribute
    int bestAttributePosition = -1;
    double maxInfoGain = -1;
    for (int i = 0; i < information_gain.length; i++) {
      if (information_gain[i] > maxInfoGain) {
        maxInfoGain = information_gain[i];
        bestAttributePosition = i;
      }
    }

    // the value of attribute is the same as its position in the list
    return bestAttributePosition;
  }

  /**
   * 
   * @param examples
   * @param attribute
   * @return
   */
  private static int bestThreshold(List<List<Integer>> examples,
      int attribute) {
    // There are 10 thresholds in total from 1 - 10
    // initiate a list to record information gains corresponding to different thresholds of an attribute
    List<Double> information_gains = new ArrayList<Double>();
    for (int i = 1; i <= 10; i++) {
      information_gains
          .add(findInformationGain(examples, attribute, i));
    }

    // find out the position of the maximum of information_gains
    int maxThresholdPosition = -1;
    double maxInfoGain = -1;
    for (int i = 0; i < information_gains.size(); i++) {
      // it should be > instead of >= here because we find out the earlist if same information gain
      if (information_gains.get(i) > maxInfoGain) {
        maxInfoGain = information_gains.get(i);
        maxThresholdPosition = i;
      }
    }
    return maxThresholdPosition + 1;
  }

  /**
   * 
   * @param attribute
   * @param threshold
   * @return
   */
  private static double findInformationGain(
      List<List<Integer>> examples, int attribute, int threshold) {
    return findEntropy(examples)
        - findConditionalEntropy(examples, attribute, threshold);
  }


}
