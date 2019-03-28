package com.assignment3;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    //directory for reading and writing tree's
    private static String treeDir = "C:\\Users\\Cian\\IdeaProjects\\DecisionTree\\tmp\\";

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        System.out.println("Constructing decision tree..");
        BinaryTree<String> decisionTree = new BinaryTree<>();
        createTree(decisionTree);
        printTree(decisionTree);// for testing
        System.out.println("Enter the answer: ");
        String answer = in.nextLine();
        playGame(decisionTree,answer);
    }

    //all "game" logic handled here
    public static  void playGame(BinaryTree<String> tree,String answer){
        BinaryNodeInterface<String> currentNode = tree.getRootNode();
        Scanner in = new Scanner(System.in);

        outer: while(true)
        {
            while(!currentNode.isLeaf()){
                //print the question
                System.out.println(currentNode.getData());
                String input = in.nextLine();
                input = input.toUpperCase();

                if(!input.equals("Y")&&!input.equals("N"))
                {
                    System.out.println("Please only enter 'Y' or 'N' to select your answer!");
                    continue;
                }

                if(input.equals("Y"))
                    currentNode = currentNode.getLeftChild();

                if(input.equals("N"))
                    currentNode = currentNode.getRightChild();
            }
            System.out.println("Is it: "+currentNode.getData());
            String input = in.nextLine();
            //ensure user input is valid
            input = input.toUpperCase();

            if(input.equals("Y"))
            {
              System.out.println("I guessed correctly! Answer is: "+answer);

              switchChoice: while(true){
                  System.out.println("1. Play Again? \n 2. Store the tree \n 3. Load a tree \n 4. Quit");
                  int choice = Integer.parseInt(in.nextLine());
                  switch (choice){
                      case 1:
                          playGame(tree,answer);
                          break switchChoice;
                      case 2:
                          //TODO store tree
                          System.out.println("Enter name to store tree: ");
                          String treeName = in.nextLine();
                          serializeTree(tree,treeName);
                          break;
                      case 3:
                          //TODO load tree
                            tree = loadTree();
                            currentNode = tree.getRootNode();
                          continue outer;//start game after loading tree
                      case 4:
                          return;
                  }
              }
            }
            if(input.equals("N")){
              System.out.println("Oops my guess was wrong, please help me improve by adding a node to my tree!");
              System.out.println("New Question: ");
              String newQ = in.nextLine();
              System.out.println("Now the left answer: ");
              String newLeftAnswer = in.nextLine();
              System.out.println("Now the right answer: ");
              String newRightAnswer = in.nextLine();

              BinaryNode<String> newLeft = new BinaryNode<>(newLeftAnswer);
              BinaryNode<String> newRight = new BinaryNode<>(newRightAnswer);

              currentNode.setData(newQ);
              currentNode.setLeftChild(newLeft);
              currentNode.setRightChild(newRight);
              continue;
            }
        }
    }

    public static  void createTree(BinaryTree<String> tree){
        //Bottom layer
        BinaryTree<String> cheetah = new BinaryTree<>("cheetah");
        BinaryTree<String> sloth = new BinaryTree<>("sloth");
        BinaryTree<String> whale = new BinaryTree<>("whale");
        BinaryTree<String> monkey = new BinaryTree<>("monkey");
        BinaryTree<String> hawk = new BinaryTree<>("hawk");
        BinaryTree<String> penguin = new BinaryTree<>("penguin");
        BinaryTree<String> komodo = new BinaryTree<>("komodo dragon");
        BinaryTree<String> iguana = new BinaryTree<>("iguana");
        BinaryTree<String> lego = new BinaryTree<>("lego");
        BinaryTree<String> buzz = new BinaryTree<>("buzz lightyear");
        BinaryTree<String> barbie = new BinaryTree<>("barbie");
        BinaryTree<String> rcCar = new BinaryTree<>("RC Car");
        BinaryTree<String> apple2 = new BinaryTree<>("Apple 2");
        BinaryTree<String> macbook = new BinaryTree<>("Macbook Pro");
        BinaryTree<String> deadpool = new BinaryTree<>("Deadpool");
        BinaryTree<String> football = new BinaryTree<>("football");

        //next layer
        BinaryTree<String> fast = new BinaryTree<>("Is it fast?",cheetah,sloth);
        BinaryTree<String> ocean = new BinaryTree<>("Does it live in the ocean?",whale,monkey);
        BinaryTree<String> fly = new BinaryTree<>("Does it fly?",hawk,penguin);
        BinaryTree<String> dangerous = new BinaryTree<>("Is it dangerous",komodo,iguana);
        BinaryTree<String> build = new BinaryTree<>("Can you build things with it?",lego,buzz);
        BinaryTree<String> doll = new BinaryTree<>("Is it a doll?",barbie,rcCar);
        BinaryTree<String> old = new BinaryTree<>("Is it old?",apple2,macbook);
        BinaryTree<String> movie = new BinaryTree<>("Is it a movie?", deadpool,football);

        //next layer...
        BinaryTree<String> fourLegs = new BinaryTree<>("Does it have 4 legs?",fast,ocean);
        BinaryTree<String> bird = new BinaryTree<>("Is it a bird?",fly,dangerous);
        BinaryTree<String> inMovie = new BinaryTree<>("Is it in a movie?",build,doll);
        BinaryTree<String> computer = new BinaryTree<>("Is it a computer?",old,movie);

        //nearly there...
        BinaryTree<String> mammal = new BinaryTree<>("Is it a mammal?",fourLegs,bird);
        BinaryTree<String> toy = new BinaryTree<>("Is it a toy?",inMovie,computer);

        //finally...
        tree.setTree("Are you thinking of an animal?",mammal,toy);
    }

    private static  void serializeTree(BinaryTree<String> tree, String treeName)
    {
        try {
            //create the necessary output streams and write our tree object
            FileOutputStream treeOut = new FileOutputStream(treeDir+treeName+".ser");
            ObjectOutputStream output = new ObjectOutputStream(treeOut);
            output.writeObject(tree);
            output.close();
            treeOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static  BinaryTree<String> loadTree(){
        Scanner in = new Scanner(System.in);
        File currFolder = new File(treeDir);
        File[] files = currFolder.listFiles();

        BinaryTree<String> tree = null;

        for(int i=0;i<files.length;i++)
        {
            System.out.println((i+1)+". "+files[i].getName());
        }
        System.out.println("\n Please select a file");
        int selection = Integer.parseInt(in.nextLine());
        try{
            //create the necessary input streams and read tree object
            FileInputStream treeIn = new FileInputStream(treeDir+files[selection-1].getName());
            ObjectInputStream input = new ObjectInputStream(treeIn);
            tree = (BinaryTree<String>)input.readObject();
            input.close();
            treeIn.close();
        }catch (IOException i){
            i.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return  tree;
    }
//print tree level by level
    private static void printTree(BinaryTree<String> tree)
    {
        int height = tree.getHeight();
        String[] levels = new String[height];
        Arrays.fill(levels,"");
        BinaryNodeInterface<String> currNode = tree.getRootNode();
        traverseTree(currNode,levels);
        for(int i=height-1;i>=0;i--)
        {
            System.out.println(levels[i]);
        }
    }
    //recursively do an in order tranverse of the tree and add data to an array based on nodes level
    private static void traverseTree(BinaryNodeInterface<String> node, String[] levels){
        if(node!=null){
            traverseTree(node.getLeftChild(),levels);
            int level = node.getHeight()-1;
            levels[level] += " "+node.getData();
            traverseTree(node.getRightChild(),levels);
        }
    }
}
