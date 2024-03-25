import java.util.Scanner;

class AVLNode {
    int data;
    AVLNode left;
    AVLNode right;
    int height;

    AVLNode(int data) {
        this.data = data;
        this.height = 1;
    }
}

class AVLTree {
    AVLNode root;

    // Function to get the height of a node
    int height(AVLNode node) {
        if (node == null)
            return 0;
        return node.height;
    }

    // Function to update the height of a node
    void updateHeight(AVLNode node) {
        if (node != null)
            node.height = Math.max(height(node.left), height(node.right)) + 1;
    }

    // Function to right rotate subtree rooted with y
    AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update heights
        updateHeight(y);
        updateHeight(x);

        // Return new root
        return x;
    }

    // Function to left rotate subtree rooted with x
    AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        // Update heights
        updateHeight(x);
        updateHeight(y);

        // Return new root
        return y;
    }

    // Function to insert a node in AVL Tree
    AVLNode insertNode(AVLNode node, int data, StringBuilder log) {
        // Perform normal BST insertion
        if (node == null) {
            if (root == null) {
                log.append(String.format("Insert %d:\n- AVL Tree is empty, %d becomes the root node\n", data, data));
            }
            return new AVLNode(data);
        }

        if (data < node.data) {
            if (node.left == null) {
                node.left = new AVLNode(data);
                log.append("");
            } else {
                node.left = insertNode(node.left, data, log);
                log.append("");
            }
        } else if (data > node.data) {
            if (node.right == null) {
                node.right = new AVLNode(data);
                log.append("");
            } else {
                node.right = insertNode(node.right, data, log);
                log.append("");
            }
        } else // Duplicate keys not allowed
            return node;

        // Update height of this ancestor node
        updateHeight(node);

        // Rebalance the tree if necessary
        return rebalance(node);
    }

    // Function to delete a node in AVL Tree using AVL Delete Algorithm
    AVLNode deleteNode(AVLNode root, int data, StringBuilder log) {
        // Perform standard BST delete
        if (root == null)
            return root;

        if (data < root.data) {
            root.left = deleteNode(root.left, data, log);
        } else if (data > root.data) {
            root.right = deleteNode(root.right, data, log);
        } else {
            // Node with only one child or no child
            if ((root.left == null) || (root.right == null)) {
                AVLNode temp = null;
                if (temp == root.left)
                    temp = root.right;
                else
                    temp = root.left;

                // No child case
                if (temp == null) {
                    temp = root;
                    root = null;
                } else // One child case
                    root = temp; // Copy the contents of the non-empty child

                temp = null;
            } else {
                // Node with two children: Get the inorder predecessor (greatest in the left subtree)
                AVLNode temp = maxValueNode(root.left);

                // Copy the inorder predecessor's data to this node
                root.data = temp.data;

                // Delete the inorder predecessor
                root.left = deleteNode(root.left, temp.data, log);
            }
        }

        // If the tree had only one node then return
        if (root == null)
            return root;

        // Update the height of the current node
        updateHeight(root);

        // Rebalance the tree if necessary
        return rebalance(root);
    }

    // Function to find the inorder predecessor
    AVLNode maxValueNode(AVLNode node) {
        AVLNode current = node;
        while (current.right != null)
            current = current.right;
        return current;
    }
// Function to rebalance the tree to ensure the root is a specific value
AVLNode rebalanceRoot(AVLNode node, int rootValue) {
    if (node == null)
        return null;

    if (node.data == rootValue)
        return node;

    if (rootValue < node.data) {
        node.left = rebalanceRoot(node.left, rootValue);
        return rightRotate(node);
    } else {
        node.right = rebalanceRoot(node.right, rootValue);
        return leftRotate(node);
    }
}

    // Function to rebalance the AVL Tree if necessary
    AVLNode rebalance(AVLNode node) {
        int balance = balanceFactor(node);

        // If the node is unbalanced, then perform rotations and rebalance
        if (balance > 1) {
            if (balanceFactor(node.left) >= 0)
                return rightRotate(node);
            else {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
        } else if (balance < -1) {
            if (balanceFactor(node.right) <= 0)
                return leftRotate(node);
            else {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
        }

        // Return the unchanged or balanced node pointer
        return node;
    }

    // Function to find the inorder successor
    AVLNode minValueNode(AVLNode node) {
        AVLNode current = node;
        while (current.left != null)
            current = current.left;
        return current;
    }

    // Function to get the balance factor of a node
    int balanceFactor(AVLNode node) {
        if (node == null)
            return 0;
        return height(node.left) - height(node.right);
    }

    // Function to print preorder traversal of AVL Tree
    void preorderTraversal(AVLNode node) {
        if (node != null) {
            System.out.print(node.data + " ");
            preorderTraversal(node.left);
            preorderTraversal(node.right);
        }
    }

    // Function to print inorder traversal of AVL Tree
    void inorderTraversal(AVLNode node) {
        if (node != null) {
            inorderTraversal(node.left);
            System.out.print(node.data + " ");
            inorderTraversal(node.right);
        }
    }

    // Function to print postorder traversal of AVL Tree
    void postorderTraversal(AVLNode node) {
        if (node != null) {
            postorderTraversal(node.left);
            postorderTraversal(node.right);
            System.out.print(node.data + " ");
        }
    }

    // Function to display AVL Tree as 1-D array
    void updateTreeRepresentation() {
        int maxDepth = maxDepth(root);
        int arraySize = (int) Math.pow(2, maxDepth) - 1;
        int[] arr = new int[arraySize];
        treeToArray(root, arr, 0);
        System.out.println("\n\n1-D Array Representation:");
        System.out.printf("Size of array = 2^k - 1 = 2^%d - 1 = %d nodes\n", maxDepth, arraySize);

        for (int i = 0; i < arraySize; i++) {
            if (i > 0) {
                System.out.print(" | ");
            }
            System.out.printf("%4d", i);
        }
        System.out.println();

        for (int i = 0; i < arraySize; i++) {
            if (i > 0) {
                System.out.print(" | ");
            }
            System.out.printf("%4s", arr[i] == 0 ? "" : arr[i]);
        }
    }

    // Function to convert AVL Tree to array representation
    void treeToArray(AVLNode node, int[] arr, int index) {
        if (node != null) {
            arr[index] = node.data;
            treeToArray(node.left, arr, 2 * index + 1);
            treeToArray(node.right, arr, 2 * index + 2);
        }
    }

    // Function to get the maximum depth of the AVL Tree
    int maxDepth(AVLNode node) {
        if (node == null)
            return 0;
        int leftDepth = maxDepth(node.left);
        int rightDepth = maxDepth(node.right);
        return Math.max(leftDepth, rightDepth) + 1;
    }
}

class AVLTrees {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AVLTree tree = new AVLTree();
        StringBuilder log = new StringBuilder();
    
        boolean done = false;
        while (!done) {
            System.out.print("Enter an integer to insert into the AVL Tree, 'delete' to delete a node, or 'done' to finish: ");
            String choice = scanner.next();
            if (choice.equals("done")) {
                done = true;
            } else if (choice.equals("delete")) {
                System.out.print("Enter the integer to delete from the AVL Tree: ");
                int deleteVal = scanner.nextInt();
                tree.root = tree.deleteNode(tree.root, deleteVal, log);
                System.out.println(log.toString());
                log.setLength(0); // Clear the log for the next deletion
            } else {
                try {
                    int num = Integer.parseInt(choice);
                    tree.root = tree.insertNode(tree.root, num, log);
                    System.out.println(log.toString());
                    log.setLength(0); // Clear the log for the next insertion
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter an integer or 'done' to finish.");
                }
            }
        }
    
        // Display updated 1-D array representation
        tree.updateTreeRepresentation();
    
        // Display the three tree traversals
        System.out.println("\nPreorder Traversal:");
        tree.preorderTraversal(tree.root);
        System.out.println("\nInorder Traversal:");
        tree.inorderTraversal(tree.root);
        System.out.println("\nPostorder Traversal:");
        tree.postorderTraversal(tree.root);
    
        // Ask if the user wants to try again
        System.out.print("\nDo you want to try again? (yes/no): ");
        String tryAgain = scanner.next();
        if (tryAgain.equals("yes")) {
            main(args); // Restart the program
        }
    
        scanner.close();
    }
    
}
