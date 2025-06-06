import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.border.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.*;

public class BookingPage {
    // Color palette and Fonts (same as original)
    private static final Color PRIMARY_COLOR = new Color(54, 116, 181);     // #3674B5
    private static final Color SECONDARY_COLOR = new Color(87, 143, 202);   // #578FCA
    private static final Color ACCENT_COLOR = new Color(161, 227, 249);     // #A1E3F9
    private static final Color BACKGROUND_COLOR = new Color(209, 248, 239); // #D1F8EF
    private static final Color TEXT_COLOR = new Color(33, 33, 33);
    
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    
    private JFrame frame;
    private UserHomePage homePageRef; // Reference to home page to return back
    
    public BookingPage(String carModel, String price, String year, String location, UserHomePage homePage) {
        this.homePageRef = homePage;
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        frame = new JFrame("RiderProvider - Book Your Ride");
        frame.setSize(900, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        
        // Booking form panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        // Car details summary panel
        JPanel carSummaryPanel = createCarSummaryPanel(carModel, price, year, location);
        
        // Booking form
        JPanel bookingFormPanel = createBookingFormPanel();
        
        // Add panels to content
        contentPanel.add(carSummaryPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(bookingFormPanel);
        
        // Add all to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(contentPanel), BorderLayout.CENTER);
        
        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        header.setPreferredSize(new Dimension(900, 80));
        
        // Panel for logo and company name
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        logoPanel.setOpaque(false);
        
        // Create placeholder logo
        ImageIcon originalIcon = new ImageIcon("logo.png");
        Image scaledImage = originalIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        JLabel logo = new JLabel(scaledIcon);
        
        // Company name label
        JLabel companyName = new JLabel("RiderProvider");
        companyName.setFont(new Font("Segoe UI", Font.BOLD, 24));
        companyName.setForeground(Color.WHITE);
        
        logoPanel.add(logo);
        logoPanel.add(companyName);
        header.add(logoPanel, BorderLayout.WEST);
        
        // Page title
        JLabel pageTitle = new JLabel("Book Your Ride", SwingConstants.CENTER);
        pageTitle.setFont(TITLE_FONT);
        pageTitle.setForeground(Color.WHITE);
        header.add(pageTitle, BorderLayout.CENTER);
        
        // Back button
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        navPanel.setOpaque(false);
        JButton backButton = new JButton("Back to Listings");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        backButton.setBackground(SECONDARY_COLOR);
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add action to return to UserHomePage
        backButton.addActionListener(e -> {
            frame.dispose(); // Close current frame
            if (homePageRef != null) {
                homePageRef.showFrame(); // Show the home page frame
            }
        });
        
        navPanel.add(backButton);
        header.add(navPanel, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createCarSummaryPanel(String carModel, String price, String year, String location) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(ACCENT_COLOR, 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Title for the summary
        JLabel summaryTitle = new JLabel("Booking Summary");
        summaryTitle.setFont(SUBTITLE_FONT);
        summaryTitle.setForeground(PRIMARY_COLOR);
        summaryTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Car info panel with image and details
        JPanel carInfoPanel = new JPanel(new BorderLayout(20, 0));
        carInfoPanel.setBackground(Color.WHITE);
        carInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Car image
        JLabel carImage = new JLabel(createCarPlaceholderIcon(120, 90));
        carImage.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        
        // Car details panel
        JPanel detailsPanel = new JPanel(new GridLayout(4, 1, 0, 5));
        detailsPanel.setBackground(Color.WHITE);
        
        JLabel modelLabel = new JLabel("Model: " + carModel);
        modelLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        modelLabel.setForeground(TEXT_COLOR);
        
        JLabel priceLabel = new JLabel("Rental Price: " + price);
        priceLabel.setFont(REGULAR_FONT);
        priceLabel.setForeground(TEXT_COLOR);
        
        JLabel yearLabel = new JLabel("Year: " + year);
        yearLabel.setFont(REGULAR_FONT);
        yearLabel.setForeground(TEXT_COLOR);
        
        JLabel locationLabel = new JLabel("Pickup Location: " + location);
        locationLabel.setFont(REGULAR_FONT);
        locationLabel.setForeground(TEXT_COLOR);
        
        detailsPanel.add(modelLabel);
        detailsPanel.add(priceLabel);
        detailsPanel.add(yearLabel);
        detailsPanel.add(locationLabel);
        
        carInfoPanel.add(carImage, BorderLayout.WEST);
        carInfoPanel.add(detailsPanel, BorderLayout.CENTER);
        
        panel.add(summaryTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(carInfoPanel);
        
        return panel;
    }

    private JPanel createBookingFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(ACCENT_COLOR, 1, true),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        formPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Form title
        JLabel formTitle = new JLabel("Booking Details");
        formTitle.setFont(SUBTITLE_FONT);
        formTitle.setForeground(PRIMARY_COLOR);
        formTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Date selection panel
        JPanel datePanel = new JPanel(new GridLayout(2, 2, 15, 15));
        datePanel.setBackground(Color.WHITE);
        datePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Date labels
        JLabel pickupLabel = new JLabel("Pickup Date:");
        pickupLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pickupLabel.setForeground(TEXT_COLOR);
        
        JLabel returnLabel = new JLabel("Return Date:");
        returnLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        returnLabel.setForeground(TEXT_COLOR);
        
        // Date pickers - using JTextField as placeholder
        JTextField pickupDate = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        pickupDate.setFont(REGULAR_FONT);
        
        JTextField returnDate = new JTextField(LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        returnDate.setFont(REGULAR_FONT);
        
        datePanel.add(pickupLabel);
        datePanel.add(pickupDate);
        datePanel.add(returnLabel);
        datePanel.add(returnDate);
        
        // Driver details panel
        JPanel driverPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        driverPanel.setBackground(Color.WHITE);
        driverPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Labels
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLabel.setForeground(TEXT_COLOR);
        
        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        phoneLabel.setForeground(TEXT_COLOR);
        
        JLabel licenseLabel = new JLabel("Driver's License #:");
        licenseLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        licenseLabel.setForeground(TEXT_COLOR);
        
        // Text fields
        JTextField nameField = new JTextField();
        nameField.setFont(REGULAR_FONT);
        
        JTextField phoneField = new JTextField();
        phoneField.setFont(REGULAR_FONT);
        
        JTextField licenseField = new JTextField();
        licenseField.setFont(REGULAR_FONT);
        
        driverPanel.add(nameLabel);
        driverPanel.add(nameField);
        driverPanel.add(phoneLabel);
        driverPanel.add(phoneField);
        driverPanel.add(licenseLabel);
        driverPanel.add(licenseField);
        
        // Payment section
        JPanel paymentPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        paymentPanel.setBackground(Color.WHITE);
        paymentPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel cardLabel = new JLabel("Card Number:");
        cardLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cardLabel.setForeground(TEXT_COLOR);
        
        JLabel expiryLabel = new JLabel("Expiry (MM/YY):");
        expiryLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        expiryLabel.setForeground(TEXT_COLOR);
        
        JTextField cardField = new JTextField();
        cardField.setFont(REGULAR_FONT);
        
        JTextField expiryField = new JTextField();
        expiryField.setFont(REGULAR_FONT);
        
        paymentPanel.add(cardLabel);
        paymentPanel.add(cardField);
        paymentPanel.add(expiryLabel);
        paymentPanel.add(expiryField);
        
        // Terms and conditions checkbox
        JPanel termsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        termsPanel.setBackground(Color.WHITE);
        termsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JCheckBox termsCheckbox = new JCheckBox("I agree to the terms and conditions");
        termsCheckbox.setFont(REGULAR_FONT);
        termsCheckbox.setBackground(Color.WHITE);
        
        termsPanel.add(termsCheckbox);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cancelButton.setForeground(TEXT_COLOR);
        cancelButton.setBackground(Color.WHITE);
        cancelButton.setBorder(new LineBorder(SECONDARY_COLOR, 1));
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setFocusPainted(false);
        
        // Add action to return to UserHomePage
        cancelButton.addActionListener(e -> {
            frame.dispose(); // Close current frame
            if (homePageRef != null) {
                homePageRef.showFrame(); // Show the home page frame
            }
        });
        
        JButton confirmButton = new JButton("Confirm Booking");
        confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setBackground(PRIMARY_COLOR);
        confirmButton.setBorderPainted(false);
        confirmButton.setPreferredSize(new Dimension(150, 40));
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmButton.setFocusPainted(false);
        
        confirmButton.addActionListener(e -> {
            if (!termsCheckbox.isSelected()) {
                JOptionPane.showMessageDialog(frame, 
                    "Please agree to the terms and conditions to proceed.",
                    "Terms Required", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (nameField.getText().trim().isEmpty() || 
                phoneField.getText().trim().isEmpty() || 
                licenseField.getText().trim().isEmpty() ||
                cardField.getText().trim().isEmpty() ||
                expiryField.getText().trim().isEmpty()) 
                {
                    JOptionPane.showMessageDialog(frame, "Please fill in all the fields before confirming.", "Incomplete Form", JOptionPane.WARNING_MESSAGE) ;
                    return;
                }
                frame.dispose(); // Close current frame
                new ConfirmationPage(
                    nameField.getText(),
                    phoneField.getText(),
                    pickupDate.getText(),
                    returnDate.getText(),
                    licenseField.getText(),
                    cardField.getText(),
                    expiryField.getText()
                );
            });      
            buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(confirmButton);
        
        // Add everything to the form panel
        formPanel.add(formTitle);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(new JLabel("Please select your rental dates:"));
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(datePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(new JLabel("Driver Information:"));
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(driverPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(new JLabel("Payment Details:"));
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(paymentPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(termsPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(buttonPanel);
        
        return formPanel;
    }  
            
            // Utility methods for placeholder images
    private ImageIcon createPlaceholderIcon(int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        
        g2d.setColor(SECONDARY_COLOR);
        g2d.fillRoundRect(0, 0, width, height, 10, 10);
        
        g2d.setColor(PRIMARY_COLOR);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 24));
        FontMetrics fm = g2d.getFontMetrics();
        String text = "RP";
        int textWidth = fm.stringWidth(text);
        int textHeight = fm.getHeight();
        g2d.drawString(text, (width - textWidth) / 2, height / 2 + textHeight / 4);
        
        g2d.dispose();
        return new ImageIcon(img);
    }
    
    private ImageIcon createCarPlaceholderIcon(int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRect(0, 0, width, height);
        
        g2d.setColor(SECONDARY_COLOR);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(10, 5, width - 20, height - 10, 10, 10);
        
        // Draw simple car shape
        int carWidth = width - 40;
        int carHeight = height - 30;
        int startX = 20;
        int startY = 15;
        
        g2d.setColor(PRIMARY_COLOR);
        // Car body
        g2d.fillRoundRect(startX, startY + carHeight/3, carWidth, carHeight/2, 10, 10);
        // Car top
        g2d.fillRoundRect(startX + carWidth/4, startY, carWidth/2, carHeight/3, 8, 8);
        // Wheels
        g2d.setColor(new Color(60, 60, 60));
        g2d.fillOval(startX + carWidth/5, startY + carHeight*2/3, carWidth/5, carHeight/5);
        g2d.fillOval(startX + carWidth*3/5, startY + carHeight*2/3, carWidth/5, carHeight/5);
        
        g2d.dispose();
        return new ImageIcon(img);
    }

    // Function to confirm booking and insert into the database
    private void confirmBooking(String name, String phone, String license, 
        String pickupDate, String returnDate, String cardNumber, String expiry) {
        
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/car_rental_db", "root", "password")) {
            
            String query = "INSERT INTO bookings (name, phone, license, pickup_date, return_date, card_number, expiry_date) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            try (PreparedStatement pst = connection.prepareStatement(query)) {
                pst.setString(1, name);
                pst.setString(2, phone);
                pst.setString(3, license);
                pst.setString(4, pickupDate);
                pst.setString(5, returnDate);
                pst.setString(6, cardNumber);
                pst.setString(7, expiry);
                
                int rowsAffected = pst.executeUpdate();
                
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(frame, "Booking Confirmed!");
                    frame.dispose();
                    if (homePageRef != null) {
                        homePageRef.showFrame();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Booking failed. Please try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, 
                "An error occurred while processing your booking. Please try again.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
}
