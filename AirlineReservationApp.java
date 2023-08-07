import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

class Flight {
    private String flightNumber;
    private String origin;
    private String destination;
    private int availableSeats;

    public Flight(String flightNumber, String origin, String destination, int availableSeats) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.availableSeats = availableSeats;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void bookSeats(int seatsToBook) {
        if (seatsToBook <= 0) {
            JOptionPane.showMessageDialog(null, "Please enter a valid number of seats to book.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } else if (seatsToBook <= availableSeats) {
            availableSeats -= seatsToBook;
            JOptionPane.showMessageDialog(null, "Seats booked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Not enough available seats!", "Booking Failed", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public String toString() {
        return "Flight Number: " + flightNumber + "\nOrigin: " + origin + "\nDestination: " + destination + "\nAvailable Seats: " + availableSeats + "\n";
    }
}

public class AirlineReservationApp extends JFrame {
    private Map<String, Flight> flights;
    private Map<String, Integer> bookedSeats;
    private JTextArea outputTextArea;
    private JTextField flightNumberTextField;
    private JTextField seatsToBookTextField;
    private JTable seatDetailsTable;

    public AirlineReservationApp() {
        flights = new HashMap<>();
        bookedSeats = new HashMap<>();
        initializeFlights();
        initializeBookedSeats();

        setTitle("Airline Reservation System");
        setPreferredSize(new Dimension(800, 500));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Font outputFont = new Font("Arial", Font.PLAIN, 16);
        outputTextArea = new JTextArea(10, 30);
        outputTextArea.setEditable(false);
        outputTextArea.setFont(outputFont);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Flight Reservation"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font textFieldFont = new Font("Arial", Font.PLAIN, 14);

        JLabel flightNumberLabel = new JLabel("Flight Number:");
        flightNumberLabel.setFont(labelFont);
        flightNumberTextField = new JTextField(10);
        flightNumberTextField.setFont(textFieldFont);

        JLabel seatsToBookLabel = new JLabel("Seats to Book:");
        seatsToBookLabel.setFont(labelFont);
        seatsToBookTextField = new JTextField(5);
        seatsToBookTextField.setFont(textFieldFont);

        JButton findAndBookButton = new JButton("Find and Book");
        findAndBookButton.setFont(labelFont);

        findAndBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String flightNumber = flightNumberTextField.getText();
                String seatsToBookStr = seatsToBookTextField.getText();
                try {
                    int seatsToBook = Integer.parseInt(seatsToBookStr);
                    Flight flight = flights.get(flightNumber);
                    if (flight != null) {
                        int bookedSeatsCount = bookedSeats.getOrDefault(flightNumber, 0);
                        if (seatsToBook + bookedSeatsCount <= flight.getAvailableSeats()) {
                            flight.bookSeats(seatsToBook);
                            bookedSeats.put(flightNumber, bookedSeatsCount + seatsToBook);
                            updateOutput();
                            updateSeatDetailsTable();
                        } else {
                            JOptionPane.showMessageDialog(null, "Not enough available seats!", "Booking Failed", JOptionPane.WARNING_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Flight not found!", "Not Found", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number of seats.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(flightNumberLabel, gbc);

        gbc.gridx = 1;
        inputPanel.add(flightNumberTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(seatsToBookLabel, gbc);

        gbc.gridx = 1;
        inputPanel.add(seatsToBookTextField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(findAndBookButton, gbc);

        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        String[] columnNames = {"Flight Number", "Origin", "Destination", "Available Seats", "Booked Seats"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        seatDetailsTable = new JTable(model);
        updateSeatDetailsTable();
        JScrollPane tableScrollPane = new JScrollPane(seatDetailsTable);
        mainPanel.add(tableScrollPane, BorderLayout.NORTH);

        add(mainPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        updateOutput();
    }

    private void initializeFlights() {
        flights.put("ABC123", new Flight("ABC123", "Bangalore", "Delhi", 150));
        flights.put("DEF456", new Flight("DEF456", "Bangalore", "Hyderabad", 100));
       flights.put("GHI789", new Flight("GHI789", "Bangalore", "Mumbai", 150));
       flights.put("JKL101", new Flight("JKL101", "Bangalore", "Chennai", 100));
       flights.put("MNO112", new Flight("MNO112", "Bangalore", "Goa", 100));
       flights.put("PQR131", new Flight("PQR131", "Bangalore", "Punjab", 150));
    }

    private void initializeBookedSeats() {
        bookedSeats.put("ABC123", 0);
        bookedSeats.put("DEF456", 0);
    }

    private void updateOutput() {
        StringBuilder sb = new StringBuilder("Flight Details:\n");
        for (Flight flight : flights.values()) {
            sb.append(flight.toString()).append("\n");
        }
        outputTextArea.setText(sb.toString());
    }

    private void updateSeatDetailsTable() {
        DefaultTableModel model = (DefaultTableModel) seatDetailsTable.getModel();
        model.setRowCount(0);

        for (Flight flight : flights.values()) {
            String flightNumber = flight.getFlightNumber();
            int availableSeats = flight.getAvailableSeats();
            int bookedSeatsCount = bookedSeats.getOrDefault(flightNumber, 0);

            Object[] row = {flightNumber, flight.getOrigin(), flight.getDestination(), availableSeats, bookedSeatsCount};
            model.addRow(row);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AirlineReservationApp app = new AirlineReservationApp();
            app.setVisible(true);
        });
    }
}
