import java.util.ArrayList;
import java.util.List;

/**
 * Fill in the implementation details of the class DecisionTree using this file. Any methods or
 * secondary classes that you want are fine but we will only interact with those methods in the
 * DecisionTree framework.
 */
public class DecisionTreeImpl {
  public DecTreeNode root;
  public List<List<Integer>> trainData;
  public int maxPerLeaf;
  public int maxDepth;
  public int numAttr;

  // Build a decision tree given a training set
  DecisionTreeImpl(List<List<Integer>> trainDataSet, int mPerLeaf,
      int mDepth) {
    this.trainData = trainDataSet;
    this.maxPerLeaf = mPerLeaf;
    this.maxDepth = mDepth;
    if (this.trainData.size() > 0)
      this.numAttr = trainDataSet.get(0).size() - 1;
    this.root = buildTree();
  }


  /**
   * Build the tree according to the "train" input file
   * 
   * @return DecTreeNode
   */
  private DecTreeNode buildTree() {
    int[] attributes = new int[numAttr];
    for (int i = 0; i < numAttr; i++) {
      attributes[i] = i;
    }
    return buildTreeHelper(trainData, attributes, 0);
  }

  /**
   * helper function to build the tree
   * 
   * @param examples
   * @param attributes
   * @param default_label: assume to be new DecTreeNode(-1, -1, -1)
   * @param depth: the current depth of the tree
   * @return
   */
  private DecTreeNode buildTreeHelper(List<List<Integer>> examples,
      int[] attributes, int depth) {

    // if examples is empty, then return default_label
    if (examples.isEmpty()) {
      return new DecTreeNode(1, -1, -1);
    }

    // count the number of instances in examples that belong to class0 or class1
    int class0 = 0;
    int class1 = 0;
    for (int i = 0; i < examples.size(); i++) {
      if (examples.get(i).get(numAttr) == 0) {
        class0++;
      } else {
        class1++;
      }
    }
    // find the best attribute and its corresponding best threshold value
    // we have 9 attributes labeled from 0 to 8
    // we have 10 thresholds labeled from 1 to 10
    // int bestAttribute = bestAttribute(examples, attributes);
    // int bestThreshold = bestThreshold(examples, bestAttribute);

    double maxInfoGain = -1;
    int bestAttribute = -1;
    int bestThreshold = -1;
    for (int i = 0; i < numAttr; i++) {
      for (int j = 0; j < 10; j++) {
        if (findInformationGain(examples, i, j + 1) > maxInfoGain) {
          maxInfoGain = findInformationGain(examples, i, j + 1);
          bestAttribute = i;
          bestThreshold = j + 1;
        }
      }
    }


    // 1. if the number of instances belonging to that node is less than or equal to the “maximum
    // instances
    // per leaf”,
    // 2. or if the depth is equal to the “maximum depth” (the root node has depth 0)
    // 3. or if attributes are empty, return the majority vote in examples
    // 4. or if the maximum information gain is 0
    // Then a leaf node must be created
    if (examples.size() <= maxPerLeaf || depth == maxDepth
        || numAttr == 0 || findInformationGain(examples,
            bestAttribute, bestThreshold) == 0) {
      return class0 > class1 ? new DecTreeNode(0, -1, -1)
          : new DecTreeNode(1, -1, -1);
    }

    // for here, we don't have the leaf node
    // assemble the first new list (leftSubtree)
    // when the value of bestAttribute less than or equal to bestThreshold
    List<List<Integer>> left = new ArrayList<List<Integer>>();
    for (int i = 0; i < examples.size(); i++) {
      if (examples.get(i).get(bestAttribute) <= bestThreshold) {
        left.add(examples.get(i));
      }
    }

    // assemble the second new list (leftSubtree)
    // when the value of bestAttribute larger than the bestThreshold
    List<List<Integer>> right = new ArrayList<List<Integer>>();
    for (int i = 0; i < examples.size(); i++) {
      if (examples.get(i).get(bestAttribute) > bestThreshold) {
        right.add(examples.get(i));
      }
    }


    DecTreeNode node =
        new DecTreeNode(-1, bestAttribute, bestThreshold);

    node.left = buildTreeHelper(left, attributes, depth + 1);
    node.right = buildTreeHelper(right, attributes, depth + 1);

    return node;
  }

  /**
   * 
   * @param attribute
   * @param threshold
   * @return
   */
  private double findInformationGain(List<List<Integer>> examples,
      int attribute, int threshold) {
    return findEntropy(examples)
        - findConditionalEntropy(examples, attribute, threshold);
  }

  /**
   * 
   * @param examples
   * @return
   */
  private double findEntropy(List<List<Integer>> examples) {
    // if example is empty, entropy is zero
    if (examples.size() == 0) {
      return 0;
    }

    // find the corresponding counts of class 0 and 1
    int class0 = 0;
    int class1 = 0;
    for (int i = 0; i < examples.size(); i++) {
      if (examples.get(i).get(numAttr) == 0) {
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
  private double findConditionalEntropy(List<List<Integer>> examples,
      int attribute, int threshold) {
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

    double leftProbability = (double) left.size() / examples.size();
    double righttProbability =
        (double) right.size() / examples.size();
    // find the specific conditional entropy for examples according to left and right
    return leftProbability * findEntropy(left)
        + righttProbability * findEntropy(right);
  }

  /**
   * 
   * @param instance
   * @return
   */
  public int classify(List<Integer> instance) {
    return classifyHelper(instance, root);
  }

  /**
   * 
   * @param instance
   * @param node
   * @return
   */
  private int classifyHelper(List<Integer> instance,
      DecTreeNode node) {
    // if the node is a leaf (a label/class node)
    if (node.attribute == -1 && node.threshold == -1) {
      return node.classLabel;
    }
    // if it is not a leaf (an attribute node)
    // find the node's left or right subTreeNode
    if (instance.get(node.attribute) <= node.threshold) {
      return classifyHelper(instance, node.left);
    } else {
      return classifyHelper(instance, node.right);
    }

  }

  // Print the decision tree in the specified format
  public void printTree() {
    printTreeNode("", this.root);
  }

  public void printTreeNode(String prefixStr, DecTreeNode node) {
    String printStr = prefixStr + "X_" + node.attribute;
    System.out.print(
        printStr + " <= " + String.format("%d", node.threshold));
    if (node.left.isLeaf()) {
      System.out
          .println(" : " + String.valueOf(node.left.classLabel));
    } else {
      System.out.println();
      printTreeNode(prefixStr + "|\t", node.left);
    }
    System.out.print(
        printStr + " > " + String.format("%d", node.threshold));
    if (node.right.isLeaf()) {
      System.out
          .println(" : " + String.valueOf(node.right.classLabel));
    } else {
      System.out.println();
      printTreeNode(prefixStr + "|\t", node.right);
    }
  }

  public double printTest(List<List<Integer>> testDataSet) {
    int numEqual = 0;
    int numTotal = 0;
    for (int i = 0; i < testDataSet.size(); i++) {
      int prediction = classify(testDataSet.get(i));
      int groundTruth =
          testDataSet.get(i).get(testDataSet.get(i).size() - 1);
      System.out.println(prediction);
      if (groundTruth == prediction) {
        numEqual++;
      }
      numTotal++;
    }
    double accuracy = numEqual * 100.0 / (double) numTotal;
    System.out.println(String.format("%.2f", accuracy) + "%");
    return accuracy;
  }
}
