import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CryptoApp extends JFrame {
    private JComboBox<String> cipherSelection;
    private JTextArea inputText, outputText, infoText;
    private JTextField keyField;
    private JSpinner railsSpinner, cesarShiftSpinner;
    private JButton encryptBtn, decryptBtn;
    private JPanel paramPanel;

    public CryptoApp() {
        setTitle("Cifrados Clásicos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        String[] ciphers = {"Cesar", "Atbash", "Vigenere", "Rail Fence", "Playfair"};
        cipherSelection = new JComboBox<>(ciphers);
        cipherSelection.addActionListener(e -> updateParamPanel());

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Cifrado:"));
        topPanel.add(cipherSelection);

        paramPanel = new JPanel();
        updateParamPanel();

        JPanel centerPanel = new JPanel(new GridLayout(1, 2));
        
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Texto Original"));
        inputText = new JTextArea();
        inputText.setLineWrap(true);
        inputText.setWrapStyleWord(true);
        leftPanel.add(new JScrollPane(inputText), BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Texto Cifrado"));
        outputText = new JTextArea();
        outputText.setLineWrap(true);
        outputText.setWrapStyleWord(true);
        outputText.setEditable(false);
        rightPanel.add(new JScrollPane(outputText), BorderLayout.CENTER);

        centerPanel.add(leftPanel);
        centerPanel.add(rightPanel);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        JPanel buttonPanel = new JPanel();
        encryptBtn = new JButton("Cifrar");
        decryptBtn = new JButton("Descifrar");
        encryptBtn.addActionListener(e -> encrypt());
        decryptBtn.addActionListener(e -> decrypt());
        buttonPanel.add(encryptBtn);
        buttonPanel.add(decryptBtn);
        
        infoText = new JTextArea(3, 40);
        infoText.setEditable(false);
        infoText.setLineWrap(true);
        infoText.setWrapStyleWord(true);
        infoText.setFont(new Font("Monospaced", Font.PLAIN, 10));
        
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(new JScrollPane(infoText), BorderLayout.CENTER);

        Container cp = getContentPane();
        cp.add(topPanel, BorderLayout.NORTH);
        cp.add(createParamSection(), BorderLayout.WEST);
        cp.add(centerPanel, BorderLayout.CENTER);
        cp.add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createParamSection() {
        JPanel panel = new JPanel(new BorderLayout());
        paramPanel = new JPanel();
        panel.add(paramPanel, BorderLayout.NORTH);
        return panel;
    }

    private void updateParamPanel() {
        paramPanel.removeAll();
        String selected = (String) cipherSelection.getSelectedItem();

        if (selected.equals("Cesar")) {
            JPanel p = new JPanel();
            p.add(new JLabel("Desplazamiento:"));
            cesarShiftSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 25, 1));
            p.add(cesarShiftSpinner);
            paramPanel.add(p);
        } else if (selected.equals("Atbash")) {
            JPanel p = new JPanel();
            p.add(new JLabel("(Sin clave necesaria)"));
            paramPanel.add(p);
        } else if (selected.equals("Vigenere")) {
            JPanel p = new JPanel();
            p.add(new JLabel("Clave:"));
            keyField = new JTextField(10);
            p.add(keyField);
            paramPanel.add(p);
        } else if (selected.equals("Rail Fence")) {
            JPanel p = new JPanel();
            p.add(new JLabel("Rieles:"));
            railsSpinner = new JSpinner(new SpinnerNumberModel(3, 2, 10, 1));
            p.add(railsSpinner);
            paramPanel.add(p);
        } else if (selected.equals("Playfair")) {
            JPanel p = new JPanel();
            p.add(new JLabel("Clave:"));
            keyField = new JTextField(10);
            p.add(keyField);
            paramPanel.add(p);
        }

        paramPanel.revalidate();
        paramPanel.repaint();
    }

    private void encrypt() {
        String plaintext = inputText.getText();
        String selected = (String) cipherSelection.getSelectedItem();
        String result = "";
        String info = "";

        try {
            if (selected.equals("Cesar")) {
                int shift = (Integer) cesarShiftSpinner.getValue();
                result = CesarCipher.encrypt(plaintext, shift);
                info = "Cifrado: César - Desplazamiento: " + shift;
            } else if (selected.equals("Atbash")) {
                result = AtbashCipher.encrypt(plaintext);
                info = "Cifrado: Atbash - Invierte alfabeto";
            } else if (selected.equals("Vigenere")) {
                String key = extraerKeyField();
                if (key.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Ingrese una clave");
                    return;
                }
                result = VigenereCipher.encrypt(plaintext, key);
                info = "Cifrado: Vigenere - Clave: " + key;
            } else if (selected.equals("Rail Fence")) {
                int rails = (Integer) railsSpinner.getValue();
                result = RailFenceCipher.encrypt(plaintext, rails);
                info = "Cifrado: Rail Fence - Rieles: " + rails + "\n\n" + 
                       RailFenceCipher.visualize(plaintext, rails);
            } else if (selected.equals("Playfair")) {
                String key = extraerKeyField();
                if (key.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Ingrese una clave");
                    return;
                }
                PlayfairCipher pf = new PlayfairCipher(key);
                result = pf.encrypt(plaintext);
                info = "Cifrado: Playfair - Clave: " + key + "\n\n" + pf.getKeyTableDisplay();
            }

            outputText.setText(result);
            infoText.setText(info);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void decrypt() {
        String ciphertext = outputText.getText();
        String selected = (String) cipherSelection.getSelectedItem();
        String result = "";
        String info = "";

        try {
            if (selected.equals("Cesar")) {
                int shift = (Integer) cesarShiftSpinner.getValue();
                result = CesarCipher.decrypt(ciphertext, shift);
                info = "Descifrado: César - Desplazamiento: " + shift;
            } else if (selected.equals("Atbash")) {
                result = AtbashCipher.decrypt(ciphertext);
                info = "Descifrado: Atbash";
            } else if (selected.equals("Vigenere")) {
                String key = extraerKeyField();
                if (key.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Ingrese una clave");
                    return;
                }
                result = VigenereCipher.decrypt(ciphertext, key);
                info = "Descifrado: Vigenere - Clave: " + key;
            } else if (selected.equals("Rail Fence")) {
                int rails = (Integer) railsSpinner.getValue();
                result = RailFenceCipher.decrypt(ciphertext, rails);
                info = "Descifrado: Rail Fence - Rieles: " + rails;
            } else if (selected.equals("Playfair")) {
                String key = extraerKeyField();
                if (key.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Ingrese una clave");
                    return;
                }
                PlayfairCipher pf = new PlayfairCipher(key);
                result = pf.decrypt(ciphertext);
                info = "Descifrado: Playfair - Clave: " + key + "\n\n" + pf.getKeyTableDisplay();
            }

            inputText.setText(result);
            infoText.setText(info);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private String extraerKeyField() {
        if (keyField != null) {
            return keyField.getText().trim();
        }
        return "";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CryptoApp());
    }
}
