package JavaProj.com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;






public class ProductManagement extends JFrame {
    private static final long serialVersionUID = 1L;
    private JMenuBar menuBar;
    private JPanel mainPanel;
    private JMenu fileMenu;
    private JMenu productMenu;
    private JMenuItem exitMenuItem;
    private JMenuItem addUpdateMenuItem;
    private JMenuItem findDisplayMenuItem;
    private JButton addButton;
    private JButton firstButton;
    private JButton previousButton;
    private JButton nextButton;
    private JButton lastButton;
    private JButton updateButton;
    private JTextArea displayArea;

    private List<Product> products;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private int currentRecordIndex;

    private JTextField productNameField;
    private JTextField productIdField;
    private JTextField quantityField;
    private JTextField unitPriceField;
    private JButton saveButton;

    public ProductManagement() {
        // Initialize and set up the GUI components
        setTitle("Product Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the menu bar
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        productMenu = new JMenu("Product");
        exitMenuItem = new JMenuItem("Exit");
        addUpdateMenuItem = new JMenuItem("Add/Update");
        findDisplayMenuItem = new JMenuItem("Find/Display");
        fileMenu.add(exitMenuItem);
        productMenu.add(addUpdateMenuItem);
        productMenu.add(findDisplayMenuItem);
        menuBar.add(fileMenu);
        menuBar.add(productMenu);
        setJMenuBar(menuBar);

        // Initialize mainPanel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        // Initialize the addButton
        addButton = new JButton("Add Product");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });

        firstButton = new JButton("First");
        previousButton = new JButton("Previous");
        nextButton = new JButton("Next");
        lastButton = new JButton("Last");
        updateButton = new JButton("Update");

        firstButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showFirstProduct();
            }
        });

        previousButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showPreviousProduct();
            }
        });

        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showNextProduct();
            }
        });

        lastButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLastProduct();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateCurrentProduct();
            }
        });

        // Create a panel for the buttons at the bottom
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(firstButton);
        buttonPanel.add(previousButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(lastButton);
        buttonPanel.add(updateButton);

        // Create a panel for the input fields
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Product Name:"));
        productNameField = new JTextField(20);
        inputPanel.add(productNameField);
        inputPanel.add(new JLabel("Product ID:"));
        productIdField = new JTextField(20);
        inputPanel.add(productIdField);
        inputPanel.add(new JLabel("Quantity:"));
        quantityField = new JTextField(20);
        inputPanel.add(quantityField);
        inputPanel.add(new JLabel("Unit Price:"));
        unitPriceField = new JTextField(20);
        inputPanel.add(unitPriceField);
        saveButton = new JButton("Save");
        inputPanel.add(saveButton);

        // Create a panel for the display area
        displayArea = new JTextArea();
        JScrollPane displayScrollPane = new JScrollPane(displayArea);

        // Add the components to the main panel
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(inputPanel, BorderLayout.WEST);
        mainPanel.add(displayScrollPane, BorderLayout.CENTER);

        // Implement action listeners for menu items and buttons
        exitMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        addUpdateMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAddUpdateDialog();
            }
        });

        findDisplayMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showFindDisplayDialog();
            }
        });

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addProduct();
            }
        });
    



        firstButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showFirstProduct();
            }
        });

        previousButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showPreviousProduct();
            }
        });

        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showNextProduct();
            }
        });

        lastButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLastProduct();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateCurrentProduct();
            }
        });

        // Initialize the product list and set up file I/O
        products = new ArrayList<>();
        currentRecordIndex = -1;
        try {
            dataOutputStream = new DataOutputStream(new FileOutputStream("products.dat"));
            dataInputStream = new DataInputStream(new FileInputStream("products.dat"));
            loadProducts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAddUpdateDialog() {
        // Implement code to display the Add/Update dialog
    	String productName = productNameField.getText();
        if (productName != null) {
            // Prompt the user for other product details
            String productIdStr = productIdField.getText();
            String quantityStr = quantityField.getText();
            String unitPriceStr = unitPriceField.getText();

            try {
                // Convert input to appropriate data types
                int productId = Integer.parseInt(productIdStr);
                int quantity = Integer.parseInt(quantityStr);
                double unitPrice = Double.parseDouble(unitPriceStr);

                // Perform data validation
                if (productId < 0) {
                    JOptionPane.showMessageDialog(null, "Product ID must be a non-negative integer.");
                    return; // Exit the method if validation fails
                }
                if (quantity < 0) {
                    JOptionPane.showMessageDialog(null, "Quantity must be a non-negative integer.");
                    return; // Exit the method if validation fails
                }
                if (unitPrice <= 0) {
                    JOptionPane.showMessageDialog(null, "Unit Price must be greater than 0.");
                    return; // Exit the method if validation fails
                }

                // Check if the Product ID already exists (for update)
                boolean isUpdate = false;
                Product existingProduct = null;
                for (Product product : products) {
                    if (product.getProductId() == productId) {
                        isUpdate = true;
                        existingProduct = product;
                        break;
                    }
                }

                if (isUpdate) {
                    // Handle the update operation
                    updateProduct(existingProduct, productName, quantity, unitPrice);
                } else {
                    // Handle the add operation for a new product
                    addNewProduct(productId, productName, quantity, unitPrice);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid numeric values.");
            }
        }
    }

    private void updateProduct(Product product, String newName, int newQuantity, double newUnitPrice) {
        // Perform the update for the existing product
        product.setName(newName);
        product.setQuantity(newQuantity);
        product.setUnitPrice(newUnitPrice);
        saveProducts(); // Save the updated product to the file
        JOptionPane.showMessageDialog(null, "Product updated successfully.");
    }

    private void addNewProduct(int productId, String productName, int quantity, double unitPrice) {
        // Create a new Product object and add it to the list
        Product newProduct = new Product(productId, productName, quantity, unitPrice);
        products.add(newProduct);
        saveProducts(); // Save the new product to the file
        JOptionPane.showMessageDialog(null, "Product added successfully.");
    }

    private void showFindDisplayDialog() {
    	String[] options = {"All", "Keyword", "Price Range"};
        int choice = JOptionPane.showOptionDialog(null, "Select search criteria:", "Find/Display", JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            findAllProducts();
        } else if (choice == 1) {
            String keyword = JOptionPane.showInputDialog("Enter Keyword:");
            if (keyword != null) {
                findProductsByKeyword(keyword);
            }
        } else if (choice == 2) {
            double from = Double.parseDouble(JOptionPane.showInputDialog("Enter Price From:"));
            double to = Double.parseDouble(JOptionPane.showInputDialog("Enter Price To:"));
            findProductsByPriceRange(from, to);
        }
        }

    private void addProduct() {
        // Implement code to add a product to the list and file
    	// Prompt the user to enter new product details
        String productName = JOptionPane.showInputDialog("Enter Product Name:");
        if (productName == null) {
            // User canceled the input, so return without adding the product.
            return;
        }

        // Prompt the user for other product details
        String productIdStr = JOptionPane.showInputDialog("Enter Product ID:");
        String quantityStr = JOptionPane.showInputDialog("Enter Quantity:");
        String unitPriceStr = JOptionPane.showInputDialog("Enter Unit Price:");

        try {
            // Convert input to appropriate data types
            int productId = Integer.parseInt(productIdStr);
            int quantity = Integer.parseInt(quantityStr);
            double unitPrice = Double.parseDouble(unitPriceStr);

            // Perform data validation
            if (productId < 0) {
                JOptionPane.showMessageDialog(null, "Product ID must be a non-negative integer.");
                return; // Exit the method if validation fails
            }
            if (quantity < 0) {
                JOptionPane.showMessageDialog(null, "Quantity must be a non-negative integer.");
                return; // Exit the method if validation fails
            }
            if (unitPrice <= 0) {
                JOptionPane.showMessageDialog(null, "Unit Price must be greater than 0.");
                return; // Exit the method if validation fails
            }

            // Create a new Product object
            Product newProduct = new Product(productId, productName, quantity, unitPrice);

            // Check if the Product ID is unique
            boolean isUnique = isProductIdUnique(productId);
            if (!isUnique) {
                JOptionPane.showMessageDialog(null, "Product ID must be unique.");
                return; // Exit the method if the Product ID is not unique
            }

            
            // Add the new product to the list
            products.add(newProduct);

            // Save the new product to the file
            saveProducts();

            JOptionPane.showMessageDialog(null, "Product added successfully.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid numeric values.");
        }
    }

    private boolean isProductIdUnique(int productId) {
        for (Product product : products) {
            if (product.getProductId() == productId) {
                return false;
            }
        }
        return true;
    }

    private void showFirstProduct() {
        // Implement code to show the first product
    	if (!products.isEmpty()) {
            currentRecordIndex = 0;
            displayProduct(products.get(currentRecordIndex));
        }
    }
    

    private void showPreviousProduct() {
        // Implement code to show the previous product
    	if (!products.isEmpty() && currentRecordIndex > 0) {
            currentRecordIndex--;
            displayProduct(products.get(currentRecordIndex));
        }
    }

    private void showNextProduct() {
        // Implement code to show the next product
    	if (!products.isEmpty() && currentRecordIndex < products.size() - 1) {
            currentRecordIndex++;
            displayProduct(products.get(currentRecordIndex));
        }
    }

    private void showLastProduct() {
        // Implement code to show the last product
    	if (!products.isEmpty()) {
            currentRecordIndex = products.size() - 1;
            displayProduct(products.get(currentRecordIndex));
        }
    }

    private void updateCurrentProduct() {
        // Implement code to update the current product
    	if (!products.isEmpty() && currentRecordIndex >= 0 && currentRecordIndex < products.size()) {
            // Get the product at the current index
            Product currentProduct = products.get(currentRecordIndex);

            // Prompt the user to update product details
            String name = JOptionPane.showInputDialog("Enter Updated Product Name:", currentProduct.getName());
            String quantityStr = JOptionPane.showInputDialog("Enter Updated Quantity:", currentProduct.getQuantity());
            String unitPriceStr = JOptionPane.showInputDialog("Enter Updated Unit Price:", currentProduct.getUnitPrice());

            // Perform data validation and update the product
            try {
                // Validate and update the product name
                if (name != null) {
                    currentProduct.setName(name);
                }

                // Validate and update the quantity
                int updatedQuantity = Integer.parseInt(quantityStr);
                if (updatedQuantity >= 0) {
                    currentProduct.setQuantity(updatedQuantity);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid quantity. Quantity must be a non-negative integer.");
                    return; // Exit the method if validation fails
                }

                // Validate and update the unit price
                double updatedUnitPrice = Double.parseDouble(unitPriceStr);
                if (updatedUnitPrice > 0) {
                    currentProduct.setUnitPrice(updatedUnitPrice);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid unit price. Price must be greater than 0.");
                    return; // Exit the method if validation fails
                }

                // Update the product in the list
                products.set(currentRecordIndex, currentProduct);

                // Save the updated product to the file
                saveProducts();

                JOptionPane.showMessageDialog(null, "Product updated successfully.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter valid numeric values.");
            }
        }
    }

    private void loadProducts() {
    	try {
            int numProducts = dataInputStream.readInt(); // Read the number of products

            for (int i = 0; i < numProducts; i++) {
                int productId = dataInputStream.readInt();
                String name = dataInputStream.readUTF();
                int quantity = dataInputStream.readInt();
                double unitPrice = dataInputStream.readDouble();
                
                // Create a Product object using the parameterized constructor
                Product product = new Product(productId, name, quantity, unitPrice);
                
                products.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveProducts() {
        // Implement code to save products from the list to the file
    	try {
            dataOutputStream.writeInt(products.size()); // Write the number of products

            for (Product product : products) {
                product.writeToDataOutputStream(dataOutputStream);
            }
            dataOutputStream.flush(); // Flush the data to the file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayProduct(Product product) {
        // Implement code to display a product in the UI
    	 // Loop through the 'products' list and call 'displayProduct()' for each product.
//        for (Product product : products) {
//            displayProduct(product);
//        }
    	 if (displayArea == null) {
    	        displayArea = new JTextArea();
    	    }
    	//JTextArea displayArea = new JTextArea();
    	displayArea.setText(""); // Clear the text area before adding the product details
        displayArea.append("Product ID: " + product.getProductId() + "\n");
        displayArea.append("Product Name: " + product.getName() + "\n");
        displayArea.append("Quantity: " + product.getQuantity() + "\n");
        displayArea.append("Unit Price: $" + product.getUnitPrice() + "\n");
    }

    private void findAllProducts() {
//    	JTextArea displayArea = new JTextArea();

        // Clear the display area before adding products
        displayArea.setText("");

        // Loop through the 'products' list and display each product
        for (Product product : products) {
        	displayProduct(product);
//            displayArea.append("Product ID: " + product.getProductId() + "\n");
//            displayArea.append("Product Name: " + product.getName() + "\n");
//            displayArea.append("Quantity: " + product.getQuantity() + "\n");
//            displayArea.append("Unit Price: $" + product.getUnitPrice() + "\n");
//            displayArea.append("\n"); // Add some spacing between products
        }

        // Create a new JFrame to display all products
        JFrame allProductsFrame = new JFrame("All Products");
        allProductsFrame.setSize(400, 300);

        // Assuming you have a JPanel named 'mainPanel' in the allProductsFrame
        allProductsFrame.add(mainPanel);

        // Create a JScrollPane to hold the JTextArea
        JScrollPane scrollPane = new JScrollPane(displayArea);

        // Add the scroll pane to the panel
        mainPanel.add(scrollPane);

        // Make the frame visible
        allProductsFrame.setVisible(true);
    }

    private void findProductsByKeyword(String keyword) {
        // Implement code to display products by keyword in the UI
    	// Loop through the 'products' list and check if 'name' contains the 'keyword'.
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(keyword.toLowerCase())) {
                displayProduct(product);
            }
        }
    }

    private void findProductsByPriceRange(double from, double to) {
        // Implement code to display products by price range in the UI
    	// Loop through the 'products' list and check if 'unitPrice' is within the specified range.
        displayArea.setText(""); // Clear the text area before adding products

    	for (Product product : products) {
            if (product.getUnitPrice() >= from && product.getUnitPrice() <= to) {
                displayProduct(product);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ProductManagement app = new ProductManagement();
            app.setVisible(true);
        });
    }
}

class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private int productId;
    private String name;
    private int quantity;
    private double unitPrice;

    public Product(int productId, String name, int quantity, double unitPrice) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
    public void writeToDataOutputStream(DataOutputStream dataOutputStream) throws IOException {
        // Write the product data to the DataOutputStream
        dataOutputStream.writeInt(productId);
        dataOutputStream.writeUTF(name);
        dataOutputStream.writeInt(quantity);
        dataOutputStream.writeDouble(unitPrice);
    }

    public void readFromDataInputStream(DataInputStream dataInputStream) throws IOException {
        // Read the product data from the DataInputStream
        productId = dataInputStream.readInt();
        name = dataInputStream.readUTF();
        quantity = dataInputStream.readInt();
        unitPrice = dataInputStream.readDouble();
    }
}

