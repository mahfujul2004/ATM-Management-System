package com.mycompany.num1;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// ==========================================
// INTERNATIONALIZATION (I18N) SYSTEM
// ==========================================

enum Language {
    ENGLISH("EN", "English"),
    BANGLA("BN", "বাংলা"),
    HINDI("HI", "हिंदी");

    private final String code;
    private final String displayName;

    Language(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() { return code; }
    public String getDisplayName() { return displayName; }
}

class LanguageManager {
    private static Language currentLanguage = Language.ENGLISH;
    private static final Map<String, Map<Language, String>> translations = new HashMap<>();

    static {
        // App Headers & UI Labels
        addTranslation("AUTH_HEADER", "SECURE LOGIN", "নিরাপদ লগইন", "सुरक्षित लॉगिन");
        addTranslation("ENTER_CREDS", "ENTER YOUR PIN", "আপনার পিন লিখুন", "अपना पिन दर्ज करें");
        
        addTranslation("LOGIN_MAIN_TEXT", "PLEASE ENTER<br>YOUR ACCOUNT &<br>4 DIGIT PIN", "আপনার অ্যাকাউন্ট<br>এবং ৪ ডিজিটের পিন<br>প্রবেশ করান", "कृपया अपना खाता<br>और 4 अंकों का पिन<br>दर्ज करें");
        addTranslation("LOGIN_SUB_TEXT", "Your PIN is hidden while you type. The transaction menu opens after<br>the correct four digits.", "টাইপ করার সময় আপনার পিন লুকানো থাকে। সঠিক ৪টি সংখ্যা দিলে মেনু খুলবে।", "टाइप करते समय आपका पिन छिपा रहता है। सही चार अंकों के बाद मेनू खुल जाएगा।");
        
        // Menus
        addTranslation("MAIN_MENU", "MAIN MENU", "মূল মেনু", "मुख्य मेनू");
        addTranslation("SELECT_TRANSACTION", "SELECT THE TRANSACTION", "লেনদেন নির্বাচন করুন", "लेनदेन चुनें");
        addTranslation("MENU_SUBTEXT", "Choose a service. Your session remains active until you logout.", "একটি পরিষেবা নির্বাচন করুন। লগআউট না করা পর্যন্ত সেশন চালু থাকবে।", "एक सेवा चुनें। लॉगआउट होने तक आपका सत्र सक्रिय रहेगा।");
        
        // Buttons
        addTranslation("BALANCE_INQUIRY", "BALANCE INQUIRY", "ব্যালেন্স অনুসন্ধান", "बैलेंस पूछताछ");
        addTranslation("VIEW_BALANCE_SUB", "View available balance", "অবশিষ্ট ব্যালেন্স দেখুন", "उपलब्ध बैलेंस देखें");
        addTranslation("WITHDRAW", "WITHDRAW", "টাকা উত্তোলন", "कैश निकालें");
        addTranslation("WITHDRAW_SUB", "Withdraw cash from your account", "আপনার অ্যাকাউন্ট থেকে ক্যাশ তুলুন", "अपने खाते से नकद निकालें");
        addTranslation("DEPOSITS", "DEPOSIT", "টাকা জমা", "जमा");
        addTranslation("DEPOSIT_SUB", "Record a cash deposit", "ক্যাশ জমা রেকর্ড করুন", "नकद जमा रिकॉर्ड करें");
        addTranslation("PIN_CHANGE", "PASSWORD CHANGE", "পাসওয়ার্ড পরিবর্তন", "पासवर्ड बदलें");
        addTranslation("PIN_CHANGE_SUB", "Set a new password", "নতুন পাসওয়ার্ড সেট করুন", "नया पासवर्ड सेट करें");
        
        addTranslation("FUND_TRANSFER", "Fund Transfer", "ফান্ড ট্রান্সফার", "फंड ट्रांसफर");
        addTranslation("FUND_TRANSFER_SUB", "Send money to Bank, bKash, Rocket", "ব্যাংক, বিকাশ, রকেটে টাকা পাঠান", "बैंक, bKash, रॉकेट में पैसे भेजें");
        addTranslation("TRANSACTION_HISTORY", "Transaction History", "লেনদেনের ইতিহাস", "लेनदेन इतिहास");
        addTranslation("HISTORY_SUB", "View your past transactions", "আপনার অতীতের লেনদেন দেখুন", "अपने पिछले लेनदेन देखें");

        addTranslation("BACK", "BACK", "ফিরে যান", "वापस");
        addTranslation("CANCEL", "CANCEL", "বাতিল", "रद्द करें");
        addTranslation("LOGOUT", "LOGOUT", "লগআউট", "लॉगआउट");
        addTranslation("CONFIRM", "CONFIRM", "নিশ্চিত করুন", "पुष्टि करें");
        addTranslation("LOGIN_BTN", "LOGIN", "লগইন", "लॉग इन करें");
        
        // Operations
        addTranslation("AVAILABLE_BALANCE", "Available balance", "অবশিষ্ট ব্যালেন্স", "उपलब्ध बैलेंस");
        addTranslation("ACC_NUM_LABEL", "Account Number", "অ্যাকাউন্ট নম্বর", "खाता संख्या");
        addTranslation("PASSWORD_LABEL", "Password", "পাসওয়ার্ড", "पासवर्ड");
        addTranslation("RECIPIENT_LABEL", "Receiver Account Number", "প্রাপকের অ্যাকাউন্ট নম্বর", "प्राप्तकर्ता खाता संख्या");
        addTranslation("AMOUNT_LABEL", "Amount (৳)", "পরিমাণ (৳)", "राशि (৳)");
        addTranslation("OLD_PIN", "Current Password", "বর্তমান পাসওয়ার্ড", "वर्तमान पासवर्ड");
        addTranslation("NEW_PIN", "New Password", "নতুন পাসওয়ার্ড", "नया पासवर्ड");
        
        // PIN Change Interface specific texts
        addTranslation("PIN_CHANGE_TITLE", "CREATE NEW PIN", "নতুন পিন তৈরি করুন", "नया पिन बनाएं");
        addTranslation("PIN_CHANGE_HEADER_SUB", "PIN CHANGE", "পিন পরিবর্তন", "पिन बदलें");
        addTranslation("CHOOSE_NEW_PIN", "CHOOSE A NEW<br>SECURE PIN", "একটি নতুন<br>নিরাপদ পিন চয়ন করুন", "एक नया<br>सुरक्षित पिन चुनें");
        addTranslation("PIN_CHANGE_DESC", "Enter your current password, then your new password.", "আপনার বর্তমান পাসওয়ার্ড, তারপর আপনার নতুন পাসওয়ার্ড লিখুন।", "अपना वर्तमान पासवर्ड, फिर नया पासवर्ड दर्ज करें।");
        addTranslation("BTN_CHANGE_PIN", "CHANGE PIN", "পিন পরিবর্তন", "पिन बदलें");
        addTranslation("BTN_CHANGE_PIN_SUB", "Save the new PIN", "নতুন পিন সেভ করুন", "नया पिन सेव करें");
        addTranslation("BTN_BACK_SUB", "Return to transaction menu", "লেনদেন মেনুতে ফিরে যান", "लेनदेन मेनू पर लौटें");

        // Withdraw Interface specific texts
        addTranslation("WITHDRAW_TITLE", "ENTER AMOUNT", "পরিমাণ লিখুন", "राशि दर्ज करें");
        addTranslation("HOW_MUCH_WITHDRAW", "HOW MUCH<br>WOULD<br>YOU LIKE?", "আপনি কত<br>তুলতে<br>চান?", "आप कितना<br>निकालना<br>चाहेंगे?");
        addTranslation("WITHDRAWAL_AMOUNT", "WITHDRAWAL AMOUNT", "উত্তোলনের পরিমাণ", "निकासी राशि");
        addTranslation("QUICK_CASH", "Quick cash", "কুইক ক্যাশ", "क्विक कैश");
        addTranslation("BTN_CONFIRM_WITHDRAW", "<html>CONFIRM<br>WITHDRAW</html>", "<html>নিশ্চিত<br>করুন</html>", "<html>निकासी<br>पुष्टि करें</html>");

        // Deposit Interface specific texts
        addTranslation("DEPOSIT_TITLE", "ENTER DEPOSIT AMOUNT", "জমার পরিমাণ লিখুন", "जमा राशि दर्ज करें");
        addTranslation("PREPARE_DEPOSIT", "PREPARE<br>YOUR DEPOSIT", "আপনার জমা<br>প্রস্তুত করুন", "अपनी जमा<br>तैयार करें");
        addTranslation("DEPOSIT_DESC", "Enter the total cash or check amount before placing your envelope<br>in the deposit slot.", "স্লটে খাম রাখার আগে মোট ক্যাশ বা চেকের পরিমাণ লিখুন।", "स्लॉट में लिफाफा रखने से पहले कुल नकद या चेक राशि दर्ज करें।");
        addTranslation("DEPOSIT_AMOUNT_LABEL", "DEPOSIT AMOUNT", "জমার পরিমাণ", "जमा राशि");
        addTranslation("BTN_CONFIRM_DEPOSIT", "<html>CONFIRM<br>DEPOSIT</html>", "<html>জমা<br>নিশ্চিত করুন</html>", "<html>जमा<br>पुष्टि करें</html>");
        addTranslation("BTN_CONFIRM_DEPOSIT_SUB", "Queue envelope intake", "খাম গ্রহণের জন্য প্রস্তুত", "लिफाफा सेवन कतारबद्ध करें");

        // Transfer specific
        addTranslation("INTERNAL_TRANSFER", "Bank Transfer", "ব্যাংক ট্রান্সফার", "बैंक ट्रांसफर");
        addTranslation("BKASH_TRANSFER", "bKash Transfer", "বিকাশ ট্রান্সফার", "bKash ट्रांसफर");
        addTranslation("ROCKET_TRANSFER", "Rocket Transfer", "রকেট ট্রান্সফার", "रॉकेट ट्रांसफर");
        addTranslation("PAYPAL_TRANSFER", "PayPal Transfer", "পেপ্যাল ট্রান্সফার", "PayPal ट्रांसफर");
        
        // News Ticker Notice
        addTranslation("TICKER_NOTICE", 
                       "IMPORTANT NOTICE: Welcome to Sylhet Metropolitan Bank. Never share your password with anyone. Bank staff will never ask for your password.", 
                       "জরুরী নোটিশ: সিলেট মেট্রোপলিটন ব্যাংক-এ স্বাগতম। আপনার পাসওয়ার্ড কারও সাথে শেয়ার করবেন না। ব্যাংক কর্মীরা কখনো আপনার পাসওয়ার্ড জানতে চাইবে না।", 
                       "महत्वपूर्ण सूचना: सिलहट मेट्रोपॉलिटन बैंक में आपका स्वागत है। अपना पासवर्ड किसी के साथ साझा न करें।");

        // Voice
        addTranslation("VOICE_WELCOME", "Welcome to Sylhet Metropolitan Bank. Please login.", "সিলেট মেট্রোপলিটন ব্যাংক-এ স্বাগতম। লগইন করুন।", "सिलहट मेट्रोपॉलिटन बैंक में आपका स्वागत है।");
        addTranslation("VOICE_INCORRECT_PIN", "Invalid Account or Password.", "ভুল অ্যাকাউন্ট বা পাসওয়ার্ড।", "अमान्य खाता या पासवर्ड।");
        addTranslation("VOICE_LOGOUT", "Logged out successfully. Goodbye!", "সফলভাবে লগআউট হয়েছে। বিদায়!", "अलविदा!");
        addTranslation("VOICE_INSUFFICIENT", "Insufficient balance.", "পর্যাপ্ত ব্যালেন্স নেই।", "अपर्याप्त बैलेंस।");
        addTranslation("VOICE_SUCCESS", "Transaction Successful.", "লেনদেন সফল হয়েছে।", "लेनदेन सफल रहा।");
    }

    private static void addTranslation(String key, String en, String bn, String hi) {
        Map<Language, String> map = new HashMap<>();
        map.put(Language.ENGLISH, en);
        map.put(Language.BANGLA, bn);
        map.put(Language.HINDI, hi);
        translations.put(key, map);
    }

    public static Language getCurrentLanguage() { return currentLanguage; }
    public static void setCurrentLanguage(Language lang) { currentLanguage = lang; }

    public static String get(String key) {
        if (translations.containsKey(key)) {
            return translations.get(key).get(currentLanguage);
        }
        return key;
    }
}

// ==========================================
// BACKEND LOGIC (ACCOUNTS & BANKING)
// ==========================================

class SoundEffectSystem {
    public static void playBeep() {
        Toolkit.getDefaultToolkit().beep();
    }

    public static void playCashSound() {
        new Thread(() -> {
            try {
                String command = "[console]::beep(800,100); [console]::beep(1200,150); [console]::beep(1600,200);";
                new ProcessBuilder("powershell.exe", "-Command", command).start().waitFor();
            } catch (Exception e) {
                Toolkit.getDefaultToolkit().beep();
            }
        }).start();
    }

    public static void speak(String textToSpeak) {
        new Thread(() -> {
            try {
                String text = LanguageManager.get(textToSpeak);
                if (text.equals(textToSpeak)) text = textToSpeak; 
                
                String cleanText = text.replace("'", "");
                String command = "Add-Type -AssemblyName System.Speech; " +
                                 "$synth = New-Object System.Speech.Synthesis.SpeechSynthesizer; " +
                                 "$synth.Speak('" + cleanText + "');";
                
                new ProcessBuilder("powershell.exe", "-Command", command).start().waitFor();
            } catch (Exception e) {
                System.err.println("Voice Error: " + e.getMessage());
            }
        }).start();
    }
}

class Account {
    private String name;
    private String accountNumber;
    private String password;
    private double balance;
    private List<String> historyLogs;
    
    // Security Mechanism Variables
    private int failedAttempts = 0;
    private long lockoutEndTime = 0;

    public Account(String name, String accountNumber, String password, double initialBalance) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.password = password;
        this.balance = initialBalance;
        this.historyLogs = new ArrayList<>();
        addLog("Account Created", "System", "Self", initialBalance, "Successful");
    }

    public String getName() { return name; }
    public String getAccountNumber() { return accountNumber; }
    
    public boolean checkPassword(String pwd) { return this.password.equals(pwd); }
    public void setPassword(String pwd) { this.password = pwd; }
    public double getBalance() { return balance; }

    // Security Methods
    public boolean isLocked() {
        if (lockoutEndTime > System.currentTimeMillis()) return true;
        if (lockoutEndTime > 0) { 
            failedAttempts = 0; 
            lockoutEndTime = 0;
        }
        return false;
    }

    public long getRemainingLockoutSeconds() {
        if (!isLocked()) return 0;
        return (lockoutEndTime - System.currentTimeMillis()) / 1000;
    }

    public void recordFailedAttempt() {
        failedAttempts++;
        if (failedAttempts >= 3) {
            // Block account for 2 minutes (120,000 milliseconds)
            lockoutEndTime = System.currentTimeMillis() + (120 * 1000);
        }
    }

    public void resetFailedAttempts() {
        failedAttempts = 0;
        lockoutEndTime = 0;
    }

    public int getFailedAttempts() { return failedAttempts; }

    // Transaction Methods
    public boolean deduct(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public void add(double amount) {
        if (amount > 0) balance += amount;
    }

    public void addLog(String type, String sender, String receiver, double amount, String status) {
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String formattedLog = String.format("[%s] %s → %s → ৳%.2f → %s", time, type, receiver, amount, status);
        historyLogs.add(0, formattedLog); // Add to top
    }

    public void addHistoryEntry(String entry) { historyLogs.add(entry); }

    public List<String> getHistory() { return new ArrayList<>(historyLogs); }
}

class BankSystem {
    private Map<String, Account> accounts = new HashMap<>();
    private Account loggedInAccount = null;

    public BankSystem() {
        loadAccountsFromDatabase();
    }

    private void loadAccountsFromDatabase() {
        String sql = "SELECT account_number, name, pin, balance FROM accounts";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) {
                System.out.println("Database connection unavailable.");
                return;
            }
            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String accNo = rs.getString("account_number");
                    Account account = new Account(
                        rs.getString("name"), accNo,
                        rs.getString("pin"), rs.getDouble("balance")
                    );
                    accounts.put(accNo, account);
                }
            }
            loadTransactionHistory(con);
            System.out.println("Accounts Loaded Successfully!");
        } catch (SQLException e) {
            System.out.println("Account Loading Failed!");
            e.printStackTrace();
        }
    }

    private void loadTransactionHistory(Connection con) throws SQLException {
        String sql = "SELECT account_number, transaction_type, amount, transaction_date "
                   + "FROM transactions ORDER BY transaction_date DESC, transaction_id DESC";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Account account = accounts.get(rs.getString("account_number"));
                if (account != null) {
                    String time = rs.getTimestamp("transaction_date").toString();
                    String type = rs.getString("transaction_type");
                    double amount = rs.getDouble("amount");
                    String entry = String.format("[%s] %s → ৳%.2f → Successful", time, type, amount);
                    account.addHistoryEntry(entry);
                }
            }
        }
    }

    private boolean saveBalanceAndTransaction(Account account, double newBalance,
                                               String type, double amount) {
        String update = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        String insert = "INSERT INTO transactions (account_number, transaction_type, amount) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return false;
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(update);
                 PreparedStatement tx = con.prepareStatement(insert)) {
                ps.setDouble(1, newBalance);
                ps.setString(2, account.getAccountNumber());
                if (ps.executeUpdate() != 1) throw new SQLException("Account balance was not updated.");

                tx.setString(1, account.getAccountNumber());
                tx.setString(2, type);
                tx.setDouble(3, amount);
                tx.executeUpdate();
                con.commit();
                return true;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.out.println("Transaction could not be saved.");
            e.printStackTrace();
            return false;
        }
    }

    public String createAccount(String name, String password) {
        if (name == null || name.trim().isEmpty() || password == null
                || !password.matches("\\d{4}")) return null;

        long nextNumber = 251115106L;
        String accountNumber = String.valueOf(nextNumber);
        while (accounts.containsKey(accountNumber)) accountNumber = String.valueOf(++nextNumber);

        String sql = "INSERT INTO accounts (account_number, name, pin, balance) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return null;
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, accountNumber);
                ps.setString(2, name.trim());
                ps.setString(3, password);
                ps.setDouble(4, 0.0);
                ps.executeUpdate();
            }
            accounts.put(accountNumber, new Account(name.trim(), accountNumber, password, 0.0));
            return accountNumber;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String login(String accNum, String pass) {
        Account acc = accounts.get(accNum);
        if (acc == null) return "INVALID_ACC";
        if (acc.isLocked()) return "LOCKED";
        if (acc.checkPassword(pass)) {
            acc.resetFailedAttempts();
            loggedInAccount = acc;
            return "SUCCESS";
        }
        acc.recordFailedAttempt();
        if (acc.isLocked()) return "LOCKED_NOW";
        return "INVALID_PASS_" + (3 - acc.getFailedAttempts());
    }

    public void logout() { loggedInAccount = null; }
    public Account getLoggedInAccount() { return loggedInAccount; }
    public Account getAccount(String accNum) { return accounts.get(accNum); }

    public String changePassword(String oldP, String newP) {
        if (!loggedInAccount.checkPassword(oldP)) return "Incorrect current password.";
        if (oldP.equals(newP)) return "New password must be different.";
        if (newP == null || newP.length() < 4) return "Password must be at least 4 characters.";

        String sql = "UPDATE accounts SET pin = ? WHERE account_number = ? AND pin = ?";
        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return "Database connection failed.";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, newP);
                ps.setString(2, loggedInAccount.getAccountNumber());
                ps.setString(3, oldP);
                if (ps.executeUpdate() != 1) return "Password update failed.";
            }
            loggedInAccount.setPassword(newP);
            return "Password successfully updated!";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Password update failed.";
        }
    }

    public String deposit(double amount) {
        if (amount <= 0) return "Invalid deposit amount.";
        double newBalance = loggedInAccount.getBalance() + amount;
        if (!saveBalanceAndTransaction(loggedInAccount, newBalance, "Deposit", amount))
            return "Database error. Deposit was not completed.";
        loggedInAccount.add(amount);
        loggedInAccount.addLog("Deposit", loggedInAccount.getName(), "Self", amount, "Successful");
        SoundEffectSystem.playCashSound();
        return String.format("Deposit Successful. New Balance: ৳%.2f", loggedInAccount.getBalance());
    }

    public String withdraw(double amount) {
        if (amount <= 0) return "Invalid withdraw amount.";
        if (loggedInAccount.getBalance() < amount) {
            SoundEffectSystem.speak("VOICE_INSUFFICIENT");
            return "Insufficient Balance.";
        }
        double newBalance = loggedInAccount.getBalance() - amount;
        if (!saveBalanceAndTransaction(loggedInAccount, newBalance, "Withdrawal", amount))
            return "Database error. Withdrawal was not completed.";
        loggedInAccount.deduct(amount);
        loggedInAccount.addLog("Withdrawal", "Self", "Cash", amount, "Successful");
        SoundEffectSystem.playCashSound();
        return String.format("Withdraw Successful. Balance: ৳%.2f", loggedInAccount.getBalance());
    }

    public String internalTransfer(String receiverAccNum, double amount) {
        if (amount <= 0) return "Invalid amount.";
        if (loggedInAccount.getAccountNumber().equals(receiverAccNum))
            return "Cannot transfer to your own account.";
        Account receiver = accounts.get(receiverAccNum);
        if (receiver == null) return "Receiver account not found.";
        if (loggedInAccount.getBalance() < amount) {
            SoundEffectSystem.speak("VOICE_INSUFFICIENT");
            return "Insufficient Balance.";
        }

        String update = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        String insert = "INSERT INTO transactions (account_number, transaction_type, amount) VALUES (?, ?, ?)";
        double senderBalance = loggedInAccount.getBalance() - amount;
        double receiverBalance = receiver.getBalance() + amount;

        try (Connection con = DBConnection.getConnection()) {
            if (con == null) return "Database connection failed.";
            con.setAutoCommit(false);
            try (PreparedStatement ps = con.prepareStatement(update);
                 PreparedStatement tx = con.prepareStatement(insert)) {
                ps.setDouble(1, senderBalance);
                ps.setString(2, loggedInAccount.getAccountNumber());
                ps.executeUpdate();
                ps.setDouble(1, receiverBalance);
                ps.setString(2, receiver.getAccountNumber());
                ps.executeUpdate();

                tx.setString(1, loggedInAccount.getAccountNumber());
                tx.setString(2, "Fund Transfer to " + receiverAccNum);
                tx.setDouble(3, amount);
                tx.executeUpdate();
                tx.setString(1, receiver.getAccountNumber());
                tx.setString(2, "Received from " + loggedInAccount.getAccountNumber());
                tx.setDouble(3, amount);
                tx.executeUpdate();
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
            loggedInAccount.deduct(amount);
            receiver.add(amount);
            loggedInAccount.addLog("Fund Transfer", loggedInAccount.getName(), receiver.getName(), amount, "Successful");
            receiver.addLog("Received Fund", loggedInAccount.getName(), "Self", amount, "Successful");
            SoundEffectSystem.playCashSound();
            SoundEffectSystem.speak("VOICE_SUCCESS");
            return String.format("Successfully sent ৳%.2f to %s.", amount, receiver.getName());
        } catch (SQLException e) {
            e.printStackTrace();
            return "Database error. Transfer was not completed.";
        }
    }

    public String externalTransfer(String platform, String identifier, double amount) {
        if (amount <= 0) return "Invalid amount.";
        if (identifier == null || identifier.trim().isEmpty()) return "Invalid details.";
        if (loggedInAccount.getBalance() < amount) {
            SoundEffectSystem.speak("VOICE_INSUFFICIENT");
            return "Insufficient Balance.";
        }
        double newBalance = loggedInAccount.getBalance() - amount;
        if (!saveBalanceAndTransaction(loggedInAccount, newBalance, platform + " Transfer to " + identifier, amount))
            return "Database error. Transfer was not completed.";
        loggedInAccount.deduct(amount);
        loggedInAccount.addLog(platform + " Transfer", loggedInAccount.getName(), identifier, amount, "Successful");
        SoundEffectSystem.playCashSound();
        SoundEffectSystem.speak("VOICE_SUCCESS");
        return String.format("Successfully sent ৳%.2f to %s via %s.", amount, identifier, platform);
    }
}

// ==========================================
// PRESENTATION LAYER (UI)
// ==========================================

public class Num1 {
    private JFrame frame;
    private AnimatedCardsPanel cards; 
    private CardLayout cardLayout;
    private NewsTickerPanel tickerPanel;
    private BankSystem bank;
    private Timer loginLockoutTimer;

    private String currentView = "LoginPanel";

    // Palette Colors
    private static final Color BG_GRADIENT_TOP = new Color(42, 69, 54);
    private static final Color BG_GRADIENT_BOT = new Color(11, 23, 16);
    private static final Color HEADER_BG = new Color(243, 244, 240);
    private static final Color LIME_BTN = new Color(217, 249, 157);
    private static final Color DARK_BTN = new Color(55, 75, 65);
    private static final Color DARK_PANEL = new Color(45, 65, 55);
    private static final Color KEYPAD_LIGHT = new Color(245, 246, 242);
    private static final Color TEXT_WHITE = new Color(248, 250, 252);
    private static final Color TEXT_MUTED = new Color(180, 200, 190);
    private static final Color TICKER_BG = new Color(20, 30, 25);
    
    // Enum for Button Colors
    enum BtnType { PRIMARY, SECONDARY, DANGER }

    public Num1() {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        bank = new BankSystem();

        frame = new JFrame("SYLHET METROPOLITAN BANK");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1100, 720);
        frame.setMinimumSize(new Dimension(900, 620));
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);
        frame.setLayout(new BorderLayout()); 

        cardLayout = new CardLayout();
        cards = new AnimatedCardsPanel(cardLayout);
        cards.setOpaque(false);

        tickerPanel = new NewsTickerPanel();

        rebuildUI();

        frame.add(cards, BorderLayout.CENTER);
        frame.add(tickerPanel, BorderLayout.SOUTH);
        frame.setVisible(true);

        SoundEffectSystem.speak("VOICE_WELCOME");
    }

    // --- ANIMATION HELPER COMPONENTS ---

    class NewsTickerPanel extends JPanel {
        private String message = "";
        private int xPos;
        private Timer tickerTimer;

        public NewsTickerPanel() {
            setOpaque(true);
            setBackground(TICKER_BG);
            setPreferredSize(new Dimension(950, 35));
            xPos = 950;

            tickerTimer = new Timer(15, e -> {
                xPos -= 2; 
                repaint();
            });
            tickerTimer.start();
        }

        public void updateMessage() {
            this.message = LanguageManager.get("TICKER_NOTICE");
            xPos = getWidth();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            Font font = new Font("Nirmala UI", Font.BOLD, 14);
            g2.setFont(font);
            g2.setColor(LIME_BTN);
            
            FontMetrics fm = g2.getFontMetrics();
            int msgWidth = fm.stringWidth(message);
            
            if (xPos + msgWidth < 0) xPos = getWidth();
            
            int yPos = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(message, xPos, yPos);
            g2.dispose();
        }
    }
    
    class AnimatedCardsPanel extends JPanel {
        private float alpha = 1.0f;
        private Timer fadeTimer;

        public AnimatedCardsPanel(LayoutManager layout) { super(layout); }

        public void transitionTo(String viewName, CardLayout layout) {
            if (fadeTimer != null && fadeTimer.isRunning()) fadeTimer.stop();
            alpha = 0.0f;
            layout.show(this, viewName);
            
            fadeTimer = new Timer(15, e -> {
                alpha += 0.08f; 
                if (alpha >= 1.0f) {
                    alpha = 1.0f;
                    fadeTimer.stop();
                }
                repaint();
            });
            fadeTimer.start();
        }

        @Override
        public void paint(Graphics g) {
            if (alpha >= 1.0f) super.paint(g);
            else {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                super.paint(g2);
                g2.dispose();
            }
        }
    }

    class ShakePanel extends JPanel {
        private int shakeOffset = 0;
        private Timer shakeTimer;
        public ShakePanel(LayoutManager lm) { super(lm); }
        public void shake() {
            if (shakeTimer != null && shakeTimer.isRunning()) shakeTimer.stop();
            long startTime = System.currentTimeMillis();
            shakeTimer = new Timer(15, e -> {
                long elapsed = System.currentTimeMillis() - startTime;
                if (elapsed > 320) {
                    shakeOffset = 0;
                    shakeTimer.stop();
                } else {
                    double progress = elapsed / 320.0;
                    double damp = 1.0 - progress;
                    shakeOffset = (int)(Math.sin(progress * Math.PI * 6) * 12 * damp);
                }
                repaint();
            });
            shakeTimer.start();
        }
        @Override
        public void paint(Graphics g) {
            if (shakeOffset != 0) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.translate(shakeOffset, 0);
                super.paint(g2);
                g2.dispose();
            } else {
                super.paint(g);
            }
        }
    }

    private void attachSmoothAnimation(JButton btn, Color baseColor, Color hoverColor, Color pressColor) {
        btn.setBackground(baseColor);
        btn.addMouseListener(new MouseAdapter() {
            Timer fader; float progress = 0.0f;
            Color currentBg = baseColor, targetBg = baseColor, startBg = baseColor;
            private void animateTo(Color target) {
                if (target.equals(targetBg)) return;
                if (fader != null && fader.isRunning()) fader.stop();
                targetBg = target; startBg = currentBg; progress = 0.0f;
                fader = new Timer(15, evt -> {
                    progress += 0.15f; 
                    if (progress >= 1.0f) { progress = 1.0f; fader.stop(); }
                    float p = 1.0f - (1.0f - progress) * (1.0f - progress);
                    int r = (int) (startBg.getRed() + p * (targetBg.getRed() - startBg.getRed()));
                    int g = (int) (startBg.getGreen() + p * (targetBg.getGreen() - startBg.getGreen()));
                    int b = (int) (startBg.getBlue() + p * (targetBg.getBlue() - startBg.getBlue()));
                    currentBg = new Color(r, g, b);
                    btn.setBackground(currentBg);
                    btn.repaint();
                });
                fader.start();
            }
            @Override public void mouseEntered(MouseEvent e) { if(!btn.getModel().isPressed()) animateTo(hoverColor); }
            @Override public void mouseExited(MouseEvent e) { animateTo(baseColor); }
            @Override public void mousePressed(MouseEvent e) { animateTo(pressColor); }
            @Override public void mouseReleased(MouseEvent e) { 
                if(btn.contains(e.getPoint())) animateTo(hoverColor); else animateTo(baseColor);
            }
        });
    }

    private void navigateTo(String viewName) {
        this.currentView = viewName;
        cards.transitionTo(viewName, cardLayout);
    }

    private void rebuildUI() {
        if (loginLockoutTimer != null) loginLockoutTimer.stop();
        
        cards.removeAll();
        cards.add(createLoginPanel(), "LoginPanel");
        cards.add(createAccountPanel(), "CreateAccountPanel");
        cards.add(createWelcomePanel(), "WelcomePanel");
        cards.add(createMenuPanel(), "MenuPanel");
        cards.add(createBalancePanel(), "BalancePanel");
        cards.add(createTransferMenuPanel(), "TransferMenuPanel");
        
        cards.add(createDepositPanel(), "DepositPanel");
        cards.add(createWithdrawPanel(), "WithdrawPanel");
        
        cards.add(createInternalTransferPanel(), "InternalTransferPanel");
        cards.add(createExternalTransferPanel("bKash", "bKash Number", "BKASH_TRANSFER"), "BkashPanel");
        cards.add(createExternalTransferPanel("Rocket", "Rocket Number", "ROCKET_TRANSFER"), "RocketPanel");
        cards.add(createExternalTransferPanel("PayPal", "PayPal Email", "PAYPAL_TRANSFER"), "PaypalPanel");
        
        cards.add(createPinChangePanel(), "PinChangePanel");
        cards.add(createHistoryPanel(), "HistoryPanel");
        
        if (tickerPanel != null) tickerPanel.updateMessage();

        cardLayout.show(cards, currentView);
        cards.revalidate();
        cards.repaint();
    }

    private JPanel createAccountPanel() {
        GradientPanel bg = new GradientPanel();
        bg.setLayout(new BorderLayout());
        bg.add(new HeaderPanel("AUTH_HEADER", "CREATE ACCOUNT"), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(25, 25, 25, 25));

        RoundedPanel form = new RoundedPanel(new Color(35, 50, 40), 25);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(30, 35, 30, 35));
        form.setPreferredSize(new Dimension(480, 430));

        JLabel title = new JLabel("CREATE YOUR BANK ACCOUNT");
        title.setFont(new Font("Nirmala UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel description = new JLabel("Enter your name and create a 4-digit PIN.");
        description.setFont(new Font("Nirmala UI", Font.PLAIN, 12));
        description.setForeground(TEXT_MUTED);
        description.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField nameField = createStyledTextField();
        JPasswordField pinField = createStyledPasswordField();
        JPasswordField confirmPinField = createStyledPasswordField();

        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        pinField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        confirmPinField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));

        JLabel nameLabel = createFormLabel("FULL NAME");
        JLabel pinLabel = createFormLabel("CREATE 4-DIGIT PIN");
        JLabel confirmLabel = createFormLabel("CONFIRM PIN");
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        pinLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        confirmLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("Nirmala UI", Font.BOLD, 12));
        errorLabel.setForeground(new Color(255, 130, 130));
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(title);
        form.add(Box.createRigidArea(new Dimension(0, 8)));
        form.add(description);
        form.add(Box.createRigidArea(new Dimension(0, 22)));
        form.add(nameLabel);
        form.add(Box.createRigidArea(new Dimension(0, 5)));
        form.add(nameField);
        form.add(Box.createRigidArea(new Dimension(0, 14)));
        form.add(pinLabel);
        form.add(Box.createRigidArea(new Dimension(0, 5)));
        form.add(pinField);
        form.add(Box.createRigidArea(new Dimension(0, 14)));
        form.add(confirmLabel);
        form.add(Box.createRigidArea(new Dimension(0, 5)));
        form.add(confirmPinField);
        form.add(Box.createRigidArea(new Dimension(0, 10)));
        form.add(errorLabel);
        form.add(Box.createVerticalGlue());

        JPanel buttons = new JPanel(new GridLayout(1, 2, 12, 0));
        buttons.setOpaque(false);
        buttons.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JButton registerButton = createMenuButton(
            "REGISTER",
            "Create account",
            BtnType.PRIMARY,
            e -> {
                String name = nameField.getText().trim();
                String pin = new String(pinField.getPassword());
                String confirmPin = new String(confirmPinField.getPassword());

                if (name.isEmpty()) {
                    errorLabel.setText("Please enter your full name.");
                    return;
                }
                if (!pin.matches("\\d{4}")) {
                    errorLabel.setText("PIN must contain exactly 4 digits.");
                    return;
                }
                if (!pin.equals(confirmPin)) {
                    errorLabel.setText("PINs do not match.");
                    return;
                }

                String accountNumber = bank.createAccount(name, pin);
                if (accountNumber == null) {
                    errorLabel.setText("Unable to create account. Check your details.");
                    return;
                }

                JOptionPane.showMessageDialog(
                    frame,
                    "Account created successfully!\nYour account number is: " + accountNumber,
                    "Account Created",
                    JOptionPane.INFORMATION_MESSAGE
                );

                nameField.setText("");
                pinField.setText("");
                confirmPinField.setText("");
                errorLabel.setText(" ");
                navigateTo("LoginPanel");
            }
        );

        JButton backButton = createMenuButton(
            "BACK",
            "Return to login",
            BtnType.SECONDARY,
            e -> navigateTo("LoginPanel")
        );

        buttons.add(registerButton);
        buttons.add(backButton);
        form.add(buttons);
        center.add(form);
        bg.add(center, BorderLayout.CENTER);
        return bg;
    }

    // --- UI BASE COMPONENTS ---

    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(0, 0, BG_GRADIENT_TOP, 0, getHeight(), BG_GRADIENT_BOT);
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    class HeaderPanel extends JPanel {
        public HeaderPanel(String subtitleKey, String titleKey) {
            setLayout(new BorderLayout());
            setBackground(HEADER_BG);
            setBorder(new EmptyBorder(15, 30, 15, 30));

            JPanel leftSide = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
            leftSide.setOpaque(false);
            
            try {
                File logoFile = new File("atm 2_2.jpeg");
                if (logoFile.exists()) {
                    ImageIcon logoIcon = new ImageIcon(logoFile.getAbsolutePath());
                    Image img = logoIcon.getImage().getScaledInstance(65, -1, Image.SCALE_SMOOTH); 
                    JLabel logoLabel = new JLabel(new ImageIcon(img));
                    leftSide.add(logoLabel);
                }
            } catch (Exception e) {}

            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setOpaque(false);

            JLabel subLbl = new JLabel(LanguageManager.get(subtitleKey));
            subLbl.setFont(new Font("Nirmala UI", Font.BOLD, 12));
            subLbl.setForeground(new Color(100, 110, 105));

            JLabel titleLbl = new JLabel(LanguageManager.get(titleKey));
            titleLbl.setFont(new Font("Nirmala UI", Font.BOLD, 22));
            titleLbl.setForeground(new Color(25, 35, 30));

            textPanel.add(subLbl);
            textPanel.add(titleLbl);
            
            leftSide.add(textPanel);

            JPanel langPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
            langPanel.setOpaque(false);

            for (Language lang : Language.values()) {
                JButton langBtn = new JButton(lang.getCode());
                langBtn.setFont(new Font("Nirmala UI", Font.BOLD, 11));
                langBtn.setFocusPainted(false);
                
                if (lang == LanguageManager.getCurrentLanguage()) {
                    langBtn.setBackground(BG_GRADIENT_TOP);
                    langBtn.setForeground(Color.WHITE);
                } else {
                    langBtn.setBackground(Color.WHITE);
                    langBtn.setForeground(Color.BLACK);
                }

                langBtn.addActionListener(e -> {
                    LanguageManager.setCurrentLanguage(lang);
                    rebuildUI();
                });
                langPanel.add(langBtn);
            }

            add(leftSide, BorderLayout.WEST);
            add(langPanel, BorderLayout.EAST);
        }
    }

    class RoundedPanel extends JPanel {
        private Color bgColor; private int cornerRadius;
        public RoundedPanel(Color bgColor, int radius) {
            this.bgColor = bgColor; this.cornerRadius = radius; setOpaque(false);
        }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(bgColor);
            g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
        }
    }
    
    // Core logic class supporting native key inputs and visual circle rendering
    class PinInputBox extends RoundedPanel {
        private StringBuilder pinBuffer;
        private boolean isActive;
        private String title;
        private Runnable onComplete;

        public PinInputBox(String title, StringBuilder pinBuffer, Runnable onComplete) {
            super(new Color(25, 35, 30), 20); 
            this.title = title;
            this.pinBuffer = pinBuffer;
            this.onComplete = onComplete;
            
            setPreferredSize(new Dimension(220, 80));
            setMinimumSize(new Dimension(200, 80));
            setMaximumSize(new Dimension(240, 80));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setFocusable(true);

            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) { requestFocusInWindow(); }
            });

            addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) { setActiveBox(true); }
                public void focusLost(FocusEvent e) { setActiveBox(false); }
            });

            addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();
                    if (Character.isDigit(c) && pinBuffer.length() < 4) {
                        SoundEffectSystem.playBeep();
                        pinBuffer.append(c);
                        repaint();
                        if (pinBuffer.length() == 4 && onComplete != null) {
                            SwingUtilities.invokeLater(onComplete);
                        }
                    } else if (c == '\b' && pinBuffer.length() > 0) {
                        SoundEffectSystem.playBeep();
                        pinBuffer.setLength(pinBuffer.length() - 1);
                        repaint();
                    }
                }
            });
        }

        public boolean isActiveBox() { return isActive; }
        public void setActiveBox(boolean active) { 
            this.isActive = active; 
            if(active) requestFocusInWindow();
            repaint(); 
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if(title != null && !title.isEmpty()) {
                g2.setFont(new Font("Nirmala UI", Font.BOLD, 10));
                g2.setColor(new Color(180, 200, 190));
                g2.drawString(title, 20, 25);
            }
            
            int len = pinBuffer.length();
            int x = 20;
            int y = title != null && !title.isEmpty() ? getHeight() - 35 : (getHeight() / 2) - 9; 
            int diameter = 18;
            for (int i = 0; i < 4; i++) {
                g2.setColor(LIME_BTN);
                if (i < len) {
                    g2.fillOval(x, y, diameter, diameter); 
                } else {
                    g2.setStroke(new BasicStroke(2));
                    g2.drawOval(x, y, diameter, diameter); 
                }
                x += 32;
            }
            
            if (isActive) {
                g2.setColor(new Color(255, 255, 255, 40));
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 20, 20);
            }
            g2.dispose();
        }
    }

    private JButton createMenuButton(String mainTextKey, String subTextKey, BtnType type, java.awt.event.ActionListener action) {
        String mainText = LanguageManager.get(mainTextKey);
        String subText = LanguageManager.get(subTextKey);
        
        Color base, hover, press, titleFg, descFg;
        
        if (type == BtnType.PRIMARY) {
            base = LIME_BTN; hover = new Color(230, 255, 175); press = new Color(195, 230, 130);
            titleFg = Color.BLACK; descFg = new Color(60, 80, 50);
        } else if (type == BtnType.DANGER) {
            base = new Color(220, 53, 69); hover = new Color(235, 70, 85); press = new Color(200, 35, 51);
            titleFg = Color.WHITE; descFg = new Color(255, 200, 200);
        } else { 
            base = new Color(55, 70, 60); hover = new Color(75, 95, 85); press = new Color(35, 55, 45);
            titleFg = TEXT_WHITE; descFg = TEXT_MUTED;
        }

        JButton btn = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) g2.translate(0, 1);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g2);
                g2.dispose();
            }
        };

        btn.setLayout(new BorderLayout());
        btn.setFocusPainted(false); btn.setBorderPainted(false); btn.setContentAreaFilled(false); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel title = new JLabel(mainText);
        title.setFont(new Font("Nirmala UI", Font.BOLD, 14));
        JLabel desc = new JLabel(subText);
        desc.setFont(new Font("Nirmala UI", Font.PLAIN, 10));

        title.setForeground(titleFg); desc.setForeground(descFg);

        JPanel textHolder = new JPanel();
        textHolder.setLayout(new BoxLayout(textHolder, BoxLayout.Y_AXIS));
        textHolder.setOpaque(false);
        textHolder.add(title); textHolder.add(desc);

        btn.add(textHolder, BorderLayout.WEST);
        btn.addActionListener(action);
        attachSmoothAnimation(btn, base, hover, press);
        return btn;
    }

    private JButton createCustomWithdrawBtn(String mainText, String subText, Color baseColor, Color fgColor, String actionCommand, java.awt.event.ActionListener action) {
        Color hover = new Color(Math.min(255, baseColor.getRed() + 20), Math.min(255, baseColor.getGreen() + 20), Math.min(255, baseColor.getBlue() + 20));
        Color press = new Color(Math.max(0, baseColor.getRed() - 20), Math.max(0, baseColor.getGreen() - 20), Math.max(0, baseColor.getBlue() - 20));

        JButton btn = new JButton() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) g2.translate(0, 1);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g2);
                g2.dispose();
            }
        };

        btn.setLayout(new BorderLayout());
        btn.setFocusPainted(false); btn.setBorderPainted(false); btn.setContentAreaFilled(false); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(5, 10, 5, 10));

        JPanel textHolder = new JPanel();
        textHolder.setLayout(new BoxLayout(textHolder, BoxLayout.Y_AXIS));
        textHolder.setOpaque(false);

        JLabel title = new JLabel(mainText);
        title.setFont(new Font("Nirmala UI", Font.BOLD, 15));
        title.setForeground(fgColor);
        textHolder.add(title);

        if (subText != null && !subText.isEmpty()) {
            JLabel desc = new JLabel(subText);
            desc.setFont(new Font("Nirmala UI", Font.PLAIN, 10));
            desc.setForeground(fgColor.equals(Color.WHITE) ? TEXT_MUTED : new Color(60, 80, 50));
            textHolder.add(desc);
        }

        btn.add(textHolder, BorderLayout.WEST);
        btn.setActionCommand(actionCommand);
        btn.addActionListener(action);
        attachSmoothAnimation(btn, baseColor, hover, press);
        return btn;
    }

    private JButton createPhotoStyleKeypadBtn(String text, boolean isNumber) {
        Color base = isNumber ? new Color(245, 246, 240) : new Color(60, 75, 65);
        Color hover = isNumber ? Color.WHITE : new Color(80, 95, 85);
        Color press = isNumber ? new Color(220, 225, 220) : new Color(45, 60, 50);
        Color fg = isNumber ? Color.BLACK : Color.WHITE;

        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) g2.translate(0, 1);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btn.setFont(new Font("Nirmala UI", Font.BOLD, 18));
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        attachSmoothAnimation(btn, base, hover, press);
        return btn;
    }

    // --- REDESIGNED LOGIN INTERFACE ---

    private JPanel createLoginPanel() {
        GradientPanel bg = new GradientPanel();
        bg.setLayout(new BorderLayout());
        bg.add(new HeaderPanel("AUTH_HEADER", "ENTER_CREDS"), BorderLayout.NORTH);

        ShakePanel centerPanel = new ShakePanel(new BorderLayout(40, 0));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(24, 45, 20, 45));

        // LEFT SIDE UI
        JPanel leftSide = new JPanel();
        leftSide.setLayout(new BoxLayout(leftSide, BoxLayout.Y_AXIS));
        leftSide.setOpaque(false);

        JLabel mainTitle = new JLabel("<html><b style='font-size:27px; font-family:\"Nirmala UI\";'>" + LanguageManager.get("LOGIN_MAIN_TEXT") + "</b></html>");
        mainTitle.setForeground(Color.WHITE);
        mainTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel descLabel = new JLabel("<html>" + LanguageManager.get("LOGIN_SUB_TEXT") + "</html>");
        descLabel.setFont(new Font("Nirmala UI", Font.PLAIN, 12));
        descLabel.setForeground(TEXT_MUTED);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("Nirmala UI", Font.BOLD, 13));
        errorLabel.setForeground(new Color(248, 113, 113));
        errorLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftSide.add(mainTitle);
        leftSide.add(Box.createRigidArea(new Dimension(0, 10)));
        leftSide.add(descLabel);
        leftSide.add(errorLabel);

        // Account Field Styled Box
        RoundedPanel accBox = new RoundedPanel(new Color(25, 35, 30), 20);
        accBox.setLayout(new BorderLayout());
        accBox.setBorder(new EmptyBorder(10, 20, 10, 20));
        accBox.setMaximumSize(new Dimension(360, 55));
        accBox.setPreferredSize(new Dimension(360, 55));
        accBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel accLabel = new JLabel(LanguageManager.get("ACC_NUM_LABEL").toUpperCase());
        accLabel.setForeground(TEXT_MUTED);
        accLabel.setFont(new Font("Nirmala UI", Font.BOLD, 10));
        
        JTextField accField = new JTextField();
        accField.setOpaque(false);
        accField.setForeground(Color.WHITE);
        accField.setFont(new Font("Nirmala UI", Font.BOLD, 18));
        accField.setBorder(null);
        accField.setCaretColor(Color.WHITE);

        accBox.add(accLabel, BorderLayout.NORTH);
        accBox.add(accField, BorderLayout.CENTER);

        // PIN Box (using the 4 custom circles logic)
        StringBuilder pinStr = new StringBuilder();
        
        Runnable processLogin = () -> {
            String accNum = accField.getText().trim();
            String pass = pinStr.toString();
            if(pass.length() < 4) return;
            
            String result = bank.login(accNum, pass);
            if (result.equals("SUCCESS")) {
                accField.setText(""); pinStr.setLength(0); errorLabel.setText(" ");
                navigateTo("WelcomePanel");
            } else if (result.equals("INVALID_ACC")) {
                errorLabel.setText(LanguageManager.get("VOICE_INCORRECT_PIN"));
                SoundEffectSystem.speak("VOICE_INCORRECT_PIN");
                pinStr.setLength(0);
                centerPanel.shake();
            } else if (result.startsWith("INVALID_PASS_")) {
                String left = result.split("_")[2];
                errorLabel.setText("Invalid Password. " + left + " attempts left.");
                SoundEffectSystem.speak("VOICE_INCORRECT_PIN");
                pinStr.setLength(0);
                centerPanel.shake();
            } else if (result.startsWith("LOCKED")) {
                SoundEffectSystem.speak("Too many incorrect attempts. Account blocked.");
                pinStr.setLength(0);
                centerPanel.shake();
            }
        };

        PinInputBox pinBox = new PinInputBox("", pinStr, processLogin);
        pinBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        pinBox.setMaximumSize(new Dimension(360, 55));

        // Focus management styling (to let user know which field is active)
        accField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { 
                accBox.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(255, 255, 255, 40), 2),
                    new EmptyBorder(8, 18, 8, 18)
                ));
            }
            public void focusLost(FocusEvent e) {
                accBox.setBorder(new EmptyBorder(10, 20, 10, 20));
            }
        });
        
        // Keep demo credentials private: never display account numbers or passwords
        // on the public login screen.
        leftSide.add(accBox);
        leftSide.add(Box.createRigidArea(new Dimension(0, 8)));
        leftSide.add(pinBox);
        leftSide.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton createAccountButton = createMenuButton(
            "CREATE ACCOUNT",
            "Open a new bank account",
            BtnType.PRIMARY,
            e -> navigateTo("CreateAccountPanel")
        );
        createAccountButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        createAccountButton.setMaximumSize(new Dimension(360, 65));
        leftSide.add(createAccountButton);
        leftSide.add(Box.createVerticalGlue());

        // RIGHT SIDE KEYPAD
        JPanel rightSide = new JPanel(new GridBagLayout());
        rightSide.setOpaque(false);
        
        RoundedPanel keypadWrapper = new RoundedPanel(new Color(35, 50, 40, 200), 25);
        keypadWrapper.setLayout(new BorderLayout());
        keypadWrapper.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        JPanel keypadGrid = new JPanel(new GridLayout(4, 3, 10, 10));
        keypadGrid.setOpaque(false);
        keypadGrid.setPreferredSize(new Dimension(300, 260));
        
        String[] keys = {"1","2","3","4","5","6","7","8","9","DEL","0","CLR"};
        for(String k : keys) {
            JButton kb = createPhotoStyleKeypadBtn(k, !k.equals("CLR") && !k.equals("DEL"));
            kb.addActionListener(e -> {
                SoundEffectSystem.playBeep();
                
                // Routes inputs to whichever box is active (Defaults to AccField if neither)
                if (pinBox.isActiveBox()) {
                    if (k.equals("CLR")) {
                        pinStr.setLength(0);
                        pinBox.repaint();
                    } else if (k.equals("DEL")) {
                        if (pinStr.length() > 0) pinStr.setLength(pinStr.length() - 1);
                        pinBox.repaint();
                    } else {
                        if (pinStr.length() < 4) {
                            pinStr.append(k);
                            pinBox.repaint();
                            if (pinStr.length() == 4) SwingUtilities.invokeLater(processLogin);
                        }
                    }
                } else {
                    if(!accField.isFocusOwner()) accField.requestFocusInWindow();
                    
                    if (k.equals("CLR")) accField.setText("");
                    else if (k.equals("DEL")) {
                        String t = accField.getText();
                        if(t.length()>0) accField.setText(t.substring(0, t.length()-1));
                    } else {
                        accField.replaceSelection(k);
                    }
                }
            });
            keypadGrid.add(kb);
        }
        
        keypadWrapper.add(keypadGrid, BorderLayout.CENTER);
        rightSide.add(keypadWrapper);

        // Security Timer Lockout Mechanism
        loginLockoutTimer = new Timer(1000, e -> {
            String accNum = accField.getText().trim();
            Account acc = bank.getAccount(accNum);
            if (acc != null && acc.isLocked()) {
                errorLabel.setText("Account blocked. Wait " + acc.getRemainingLockoutSeconds() + "s.");
            } else {
                if (errorLabel.getText().startsWith("Account blocked")) {
                    errorLabel.setText(" ");
                }
            }
        });
        loginLockoutTimer.start();

        centerPanel.add(leftSide, BorderLayout.CENTER);
        centerPanel.add(rightSide, BorderLayout.EAST);
        bg.add(centerPanel, BorderLayout.CENTER);

        // Auto-focus account field on load
        bg.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                accField.requestFocusInWindow();
            }
        });

        return bg;
    }

    private JPanel createWelcomePanel() {
        GradientPanel bg = new GradientPanel();
        bg.setLayout(new BorderLayout());

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(150, 0, 0, 0));

        JLabel welcomeLbl = new JLabel("Welcome");
        welcomeLbl.setFont(new Font("Nirmala UI", Font.PLAIN, 24));
        welcomeLbl.setForeground(TEXT_MUTED);
        welcomeLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel nameLbl = new JLabel("");
        nameLbl.setFont(new Font("Nirmala UI", Font.BOLD, 42));
        nameLbl.setForeground(LIME_BTN);
        nameLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        center.add(welcomeLbl);
        center.add(Box.createRigidArea(new Dimension(0, 10)));
        center.add(nameLbl);
        
        bg.add(center, BorderLayout.CENTER);

        bg.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                if(bank.getLoggedInAccount() != null) {
                    String name = bank.getLoggedInAccount().getName();
                    nameLbl.setText(name + "!");
                    SoundEffectSystem.speak("Welcome, " + name);
                    
                    Timer t = new Timer(3500, e -> navigateTo("MenuPanel"));
                    t.setRepeats(false);
                    t.start();
                }
            }
        });
        return bg;
    }

    private JPanel createMenuPanel() {
        GradientPanel bg = new GradientPanel();
        bg.setLayout(new BorderLayout());
        bg.add(new HeaderPanel("MAIN_MENU", "SELECT_TRANSACTION"), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(30, 60, 60, 60));

        JLabel subText = new JLabel(LanguageManager.get("MENU_SUBTEXT"));
        subText.setFont(new Font("Nirmala UI", Font.PLAIN, 13));
        subText.setForeground(TEXT_MUTED);
        subText.setBorder(new EmptyBorder(0, 0, 20, 0));
        centerPanel.add(subText, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        gridPanel.setOpaque(false);

        gridPanel.add(createMenuButton("BALANCE_INQUIRY", "VIEW_BALANCE_SUB", BtnType.PRIMARY, e -> navigateTo("BalancePanel")));
        gridPanel.add(createMenuButton("WITHDRAW", "WITHDRAW_SUB", BtnType.PRIMARY, e -> navigateTo("WithdrawPanel")));
        gridPanel.add(createMenuButton("FUND_TRANSFER", "FUND_TRANSFER_SUB", BtnType.PRIMARY, e -> navigateTo("TransferMenuPanel")));
        gridPanel.add(createMenuButton("DEPOSITS", "DEPOSIT_SUB", BtnType.SECONDARY, e -> navigateTo("DepositPanel")));
        gridPanel.add(createMenuButton("TRANSACTION_HISTORY", "HISTORY_SUB", BtnType.SECONDARY, e -> navigateTo("HistoryPanel")));
        
        JPanel extraControls = new JPanel(new GridLayout(1, 2, 10, 0));
        extraControls.setOpaque(false);
        extraControls.add(createMenuButton("PIN_CHANGE", "PIN_CHANGE_SUB", BtnType.SECONDARY, e -> navigateTo("PinChangePanel")));
        extraControls.add(createMenuButton("LOGOUT", "LOGOUT", BtnType.DANGER, e -> {
            bank.logout();
            SoundEffectSystem.speak("VOICE_LOGOUT");
            navigateTo("LoginPanel");
        }));
        
        gridPanel.add(extraControls);

        centerPanel.add(gridPanel, BorderLayout.CENTER);
        bg.add(centerPanel, BorderLayout.CENTER);
        return bg;
    }

    private JPanel createBalancePanel() {
        GradientPanel bg = new GradientPanel();
        bg.setLayout(new BorderLayout());
        bg.add(new HeaderPanel("BALANCE_INQUIRY", "AVAILABLE_BALANCE"), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(40, 60, 40, 60));

        JLabel nameLabel = new JLabel("");
        nameLabel.setFont(new Font("Nirmala UI", Font.BOLD, 14));
        nameLabel.setForeground(LIME_BTN);
        nameLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        centerPanel.add(nameLabel, BorderLayout.NORTH);

        RoundedPanel displayCard = new RoundedPanel(DARK_PANEL, 20);
        displayCard.setLayout(new BoxLayout(displayCard, BoxLayout.Y_AXIS));
        displayCard.setBorder(new EmptyBorder(40, 40, 40, 40));

        JLabel availLbl = new JLabel(LanguageManager.get("AVAILABLE_BALANCE"));
        availLbl.setFont(new Font("Nirmala UI", Font.PLAIN, 14));
        availLbl.setForeground(TEXT_MUTED);
        
        // FIX: Replaced "Arial Black" with "Nirmala UI" to perfectly render the ৳ character
        JLabel balanceAmt = new JLabel("৳0.00");
        balanceAmt.setFont(new Font("Nirmala UI", Font.BOLD, 54));
        balanceAmt.setForeground(TEXT_WHITE);

        displayCard.add(availLbl);
        displayCard.add(Box.createRigidArea(new Dimension(0, 10)));
        displayCard.add(balanceAmt);
        
        centerPanel.add(displayCard, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(new EmptyBorder(30, 0, 0, 0));
        bottomPanel.add(createMenuButton("BACK", "MENU_SUBTEXT", BtnType.DANGER, e -> navigateTo("MenuPanel")));
        
        centerPanel.add(bottomPanel, BorderLayout.SOUTH);
        bg.add(centerPanel, BorderLayout.CENTER);

        bg.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                if(bank.getLoggedInAccount() != null) {
                    nameLabel.setText(bank.getLoggedInAccount().getName() + " | A/C: " + bank.getLoggedInAccount().getAccountNumber());
                    balanceAmt.setText(String.format("৳%.2f", bank.getLoggedInAccount().getBalance()));
                }
            }
        });
        return bg;
    }

    private JPanel createTransferMenuPanel() {
        GradientPanel bg = new GradientPanel();
        bg.setLayout(new BorderLayout());
        bg.add(new HeaderPanel("FUND_TRANSFER", "SELECT_TRANSACTION"), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(40, 60, 60, 60));

        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        gridPanel.setOpaque(false);

        gridPanel.add(createMenuButton("INTERNAL_TRANSFER", "FUND_TRANSFER_SUB", BtnType.PRIMARY, e -> navigateTo("InternalTransferPanel")));
        gridPanel.add(createMenuButton("BKASH_TRANSFER", "FUND_TRANSFER_SUB", BtnType.PRIMARY, e -> navigateTo("BkashPanel")));
        gridPanel.add(createMenuButton("ROCKET_TRANSFER", "FUND_TRANSFER_SUB", BtnType.PRIMARY, e -> navigateTo("RocketPanel")));
        gridPanel.add(createMenuButton("PAYPAL_TRANSFER", "FUND_TRANSFER_SUB", BtnType.PRIMARY, e -> navigateTo("PaypalPanel")));

        centerPanel.add(gridPanel, BorderLayout.CENTER);
        
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottom.setOpaque(false);
        bottom.add(createMenuButton("BACK", "BACK", BtnType.DANGER, e -> navigateTo("MenuPanel")));
        centerPanel.add(bottom, BorderLayout.SOUTH);

        bg.add(centerPanel, BorderLayout.CENTER);
        return bg;
    }

    // --- REDESIGNED DEPOSIT INTERFACE ---
    
    private JPanel createDepositPanel() {
        GradientPanel bg = new GradientPanel();
        bg.setLayout(new BorderLayout());
        bg.add(new HeaderPanel("DEPOSITS", "DEPOSIT_TITLE"), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(40, 0));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(20, 60, 20, 60));

        // LEFT SIDE UI
        JPanel leftSide = new JPanel();
        leftSide.setLayout(new BoxLayout(leftSide, BoxLayout.Y_AXIS));
        leftSide.setOpaque(false);

        JLabel mainTitle = new JLabel("<html><b style='font-size:36px; font-family:\"Nirmala UI\";'>" + LanguageManager.get("PREPARE_DEPOSIT") + "</b></html>");
        mainTitle.setForeground(Color.WHITE);
        mainTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel descLabel = new JLabel("<html>" + LanguageManager.get("DEPOSIT_DESC") + "</html>");
        descLabel.setFont(new Font("Nirmala UI", Font.PLAIN, 12));
        descLabel.setForeground(TEXT_MUTED);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        leftSide.add(mainTitle);
        leftSide.add(Box.createRigidArea(new Dimension(0, 10)));
        leftSide.add(descLabel);
        leftSide.add(Box.createRigidArea(new Dimension(0, 20)));

        // Amount Display Box
        RoundedPanel amountBox = new RoundedPanel(DARK_PANEL, 20);
        amountBox.setLayout(new BoxLayout(amountBox, BoxLayout.Y_AXIS));
        amountBox.setBorder(new EmptyBorder(15, 20, 15, 20));
        amountBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        amountBox.setMaximumSize(new Dimension(450, 90));
        amountBox.setPreferredSize(new Dimension(450, 90));

        JLabel amtLabelTitle = new JLabel(LanguageManager.get("DEPOSIT_AMOUNT_LABEL"));
        amtLabelTitle.setFont(new Font("Nirmala UI", Font.BOLD, 12));
        amtLabelTitle.setForeground(TEXT_MUTED);
        
        JLabel amtValue = new JLabel("৳0"); 
        amtValue.setFont(new Font("Nirmala UI", Font.BOLD, 42));
        amtValue.setForeground(Color.WHITE);

        amountBox.add(amtLabelTitle);
        amountBox.add(Box.createRigidArea(new Dimension(0, 5)));
        amountBox.add(amtValue);

        leftSide.add(amountBox);
        leftSide.add(Box.createRigidArea(new Dimension(0, 30)));

        // Action Buttons Row (Confirm / Back)
        JPanel actionRow = new JPanel(new GridLayout(1, 2, 20, 0));
        actionRow.setOpaque(false);
        actionRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionRow.setMaximumSize(new Dimension(450, 65));
        actionRow.setPreferredSize(new Dimension(450, 65));

        Color confirmColor = new Color(130, 150, 85);
        JButton confirmBtn = createCustomWithdrawBtn(LanguageManager.get("BTN_CONFIRM_DEPOSIT"), LanguageManager.get("BTN_CONFIRM_DEPOSIT_SUB"), confirmColor, Color.BLACK, "CONFIRM", e -> {
            try {
                String currentStr = amtValue.getText().replace("৳", "");
                double amt = Double.parseDouble(currentStr);
                String res = bank.deposit(amt);
                JOptionPane.showMessageDialog(frame, res);
                if (res.contains("Successful")) {
                    amtValue.setText("৳0");
                    navigateTo("MenuPanel");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid amount.");
            }
        });
        
        Color secondaryColor = new Color(55, 70, 60);
        JButton backBtn = createCustomWithdrawBtn("BACK", LanguageManager.get("BTN_BACK_SUB"), secondaryColor, Color.WHITE, "BACK", e -> {
            amtValue.setText("৳0");
            navigateTo("MenuPanel");
        });

        actionRow.add(confirmBtn);
        actionRow.add(backBtn);

        leftSide.add(actionRow);
        leftSide.add(Box.createVerticalGlue()); // Push elements upwards 

        // RIGHT SIDE KEYPAD
        JPanel rightSide = new JPanel(new GridBagLayout());
        rightSide.setOpaque(false);

        RoundedPanel keypadWrapper = new RoundedPanel(new Color(35, 50, 40, 200), 25);
        keypadWrapper.setLayout(new BorderLayout());
        keypadWrapper.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel keypadGrid = new JPanel(new GridLayout(4, 3, 10, 10));
        keypadGrid.setOpaque(false);
        keypadGrid.setPreferredSize(new Dimension(300, 260)); 

        String[] keys = {"1","2","3","4","5","6","7","8","9","DEL","0","CLR"};
        for(String k : keys) {
            JButton kb = createPhotoStyleKeypadBtn(k, !k.equals("CLR") && !k.equals("DEL"));
            kb.addActionListener(e -> {
                SoundEffectSystem.playBeep();
                String current = amtValue.getText().replace("৳", "");
                if (current.equals("0")) current = "";

                if (k.equals("CLR")) {
                    amtValue.setText("৳0");
                } else if (k.equals("DEL")) {
                    if (current.length() > 1) {
                        amtValue.setText("৳" + current.substring(0, current.length() - 1));
                    } else {
                        amtValue.setText("৳0");
                    }
                } else {
                    if (current.length() < 7) { 
                        amtValue.setText("৳" + current + k);
                    }
                }
            });
            keypadGrid.add(kb);
        }

        keypadWrapper.add(keypadGrid, BorderLayout.CENTER);
        rightSide.add(keypadWrapper);

        centerPanel.add(leftSide, BorderLayout.CENTER);
        centerPanel.add(rightSide, BorderLayout.EAST);
        bg.add(centerPanel, BorderLayout.CENTER);

        return bg;
    }

    // --- REDESIGNED WITHDRAW INTERFACE ---

    private JPanel createWithdrawPanel() {
        GradientPanel bg = new GradientPanel();
        bg.setLayout(new BorderLayout());
        bg.add(new HeaderPanel("WITHDRAW", "WITHDRAW_TITLE"), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(40, 0));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(20, 60, 10, 60)); 

        JPanel leftSide = new JPanel();
        leftSide.setLayout(new BoxLayout(leftSide, BoxLayout.Y_AXIS));
        leftSide.setOpaque(false);

        JLabel mainTitle = new JLabel("<html><b style='font-size:30px; font-family:\"Nirmala UI\";'>" + LanguageManager.get("HOW_MUCH_WITHDRAW") + "</b></html>");
        mainTitle.setForeground(Color.WHITE);
        mainTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        leftSide.add(mainTitle);
        leftSide.add(Box.createRigidArea(new Dimension(0, 15)));

        RoundedPanel amountBox = new RoundedPanel(DARK_PANEL, 20);
        amountBox.setLayout(new BoxLayout(amountBox, BoxLayout.Y_AXIS));
        amountBox.setBorder(new EmptyBorder(15, 20, 15, 20));
        amountBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        amountBox.setMaximumSize(new Dimension(450, 90));
        amountBox.setPreferredSize(new Dimension(450, 90));

        JLabel amtLabelTitle = new JLabel(LanguageManager.get("WITHDRAWAL_AMOUNT"));
        amtLabelTitle.setFont(new Font("Nirmala UI", Font.BOLD, 12));
        amtLabelTitle.setForeground(TEXT_MUTED);
        
        JLabel amtValue = new JLabel("৳0");
        amtValue.setFont(new Font("Nirmala UI", Font.BOLD, 42)); 
        amtValue.setForeground(Color.WHITE);

        amountBox.add(amtLabelTitle);
        amountBox.add(Box.createRigidArea(new Dimension(0, 5)));
        amountBox.add(amtValue);

        leftSide.add(amountBox);
        leftSide.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel quickCashRow = new JPanel(new GridLayout(1, 3, 15, 0));
        quickCashRow.setOpaque(false);
        quickCashRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        quickCashRow.setMaximumSize(new Dimension(450, 65));
        quickCashRow.setPreferredSize(new Dimension(450, 65));

        ActionListener quickCashAction = e -> {
            JButton btn = (JButton) e.getSource();
            String val = btn.getActionCommand();
            amtValue.setText("৳" + val);
            SoundEffectSystem.playBeep();
        };

        Color quickCashColor = new Color(210, 245, 100); 
        quickCashRow.add(createCustomWithdrawBtn("৳500", LanguageManager.get("QUICK_CASH"), quickCashColor, Color.BLACK, "500", quickCashAction));
        quickCashRow.add(createCustomWithdrawBtn("৳1000", LanguageManager.get("QUICK_CASH"), quickCashColor, Color.BLACK, "1000", quickCashAction));
        quickCashRow.add(createCustomWithdrawBtn("৳5000", LanguageManager.get("QUICK_CASH"), quickCashColor, Color.BLACK, "5000", quickCashAction));

        leftSide.add(quickCashRow);
        leftSide.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel actionRow = new JPanel(new GridLayout(1, 2, 20, 0));
        actionRow.setOpaque(false);
        actionRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionRow.setMaximumSize(new Dimension(450, 65));
        actionRow.setPreferredSize(new Dimension(450, 65));

        Color confirmColor = new Color(130, 150, 85);
        JButton confirmBtn = createCustomWithdrawBtn(LanguageManager.get("BTN_CONFIRM_WITHDRAW"), "", confirmColor, Color.BLACK, "CONFIRM", e -> {
            try {
                String currentStr = amtValue.getText().replace("৳", "");
                double amt = Double.parseDouble(currentStr);
                String res = bank.withdraw(amt);
                JOptionPane.showMessageDialog(frame, res);
                if (res.contains("Successful")) {
                    amtValue.setText("৳0");
                    navigateTo("MenuPanel");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid amount.");
            }
        });
        
        Color secondaryColor = new Color(55, 70, 60);
        JButton backBtn = createCustomWithdrawBtn("BACK", LanguageManager.get("BTN_BACK_SUB"), secondaryColor, Color.WHITE, "BACK", e -> {
            amtValue.setText("৳0");
            navigateTo("MenuPanel");
        });

        actionRow.add(confirmBtn);
        actionRow.add(backBtn);

        leftSide.add(actionRow);
        leftSide.add(Box.createVerticalGlue()); 

        JPanel rightSide = new JPanel(new GridBagLayout());
        rightSide.setOpaque(false);

        RoundedPanel keypadWrapper = new RoundedPanel(new Color(35, 50, 40, 200), 25);
        keypadWrapper.setLayout(new BorderLayout());
        keypadWrapper.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel keypadGrid = new JPanel(new GridLayout(4, 3, 10, 10));
        keypadGrid.setOpaque(false);
        keypadGrid.setPreferredSize(new Dimension(300, 260)); 

        String[] keys = {"1","2","3","4","5","6","7","8","9","DEL","0","CLR"};
        for(String k : keys) {
            JButton kb = createPhotoStyleKeypadBtn(k, !k.equals("CLR") && !k.equals("DEL"));
            kb.addActionListener(e -> {
                SoundEffectSystem.playBeep();
                String current = amtValue.getText().replace("৳", "");
                if (current.equals("0")) current = "";

                if (k.equals("CLR")) {
                    amtValue.setText("৳0");
                } else if (k.equals("DEL")) {
                    if (current.length() > 1) {
                        amtValue.setText("৳" + current.substring(0, current.length() - 1));
                    } else {
                        amtValue.setText("৳0");
                    }
                } else {
                    if (current.length() < 7) { 
                        amtValue.setText("৳" + current + k);
                    }
                }
            });
            keypadGrid.add(kb);
        }

        keypadWrapper.add(keypadGrid, BorderLayout.CENTER);
        rightSide.add(keypadWrapper);

        centerPanel.add(leftSide, BorderLayout.CENTER);
        centerPanel.add(rightSide, BorderLayout.EAST);
        bg.add(centerPanel, BorderLayout.CENTER);

        return bg;
    }

    private JPanel createInternalTransferPanel() {
        JTextField recipientField = createStyledTextField();
        JLabel receiverNameDisplay = new JLabel(" ");
        receiverNameDisplay.setFont(new Font("Nirmala UI", Font.BOLD, 14));
        receiverNameDisplay.setForeground(LIME_BTN);
        
        JTextField amountField = createStyledTextField();

        recipientField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { updateName(); }
            public void removeUpdate(DocumentEvent e) { updateName(); }
            public void insertUpdate(DocumentEvent e) { updateName(); }
            private void updateName() {
                String typed = recipientField.getText().trim();
                Account a = bank.getAccount(typed);
                if (a != null) {
                    if(bank.getLoggedInAccount() != null && a.getAccountNumber().equals(bank.getLoggedInAccount().getAccountNumber())) {
                        receiverNameDisplay.setForeground(new Color(248, 113, 113));
                        receiverNameDisplay.setText("Cannot transfer to yourself.");
                    } else {
                        receiverNameDisplay.setForeground(LIME_BTN);
                        receiverNameDisplay.setText("Receiver: " + a.getName());
                    }
                } else {
                    receiverNameDisplay.setForeground(new Color(248, 113, 113));
                    receiverNameDisplay.setText(typed.length() > 0 ? "Account not found." : " ");
                }
            }
        });

        JPanel wrapName = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        wrapName.setOpaque(false); wrapName.add(receiverNameDisplay);

        return createFormPanel("FUND_TRANSFER", "INTERNAL_TRANSFER", "CONFIRM", () -> {
            try {
                String recipient = recipientField.getText().trim();
                double amount = Double.parseDouble(amountField.getText().trim());
                String result = bank.internalTransfer(recipient, amount);
                JOptionPane.showMessageDialog(frame, result);
                if (result.contains("Successfully")) {
                    recipientField.setText(""); amountField.setText("");
                    navigateTo("MenuPanel");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid amount.");
            }
        }, 
        createFormLabel(LanguageManager.get("RECIPIENT_LABEL")), recipientField, wrapName,
        createFormLabel(LanguageManager.get("AMOUNT_LABEL")), amountField);
    }

    private JPanel createExternalTransferPanel(String platform, String identifierLabel, String titleKey) {
        JTextField identifierField = createStyledTextField();
        JTextField amountField = createStyledTextField();
        
        return createFormPanel("FUND_TRANSFER", titleKey, "CONFIRM", () -> {
            try {
                String id = identifierField.getText().trim();
                double amount = Double.parseDouble(amountField.getText().trim());
                String result = bank.externalTransfer(platform, id, amount);
                JOptionPane.showMessageDialog(frame, result);
                if (result.contains("Successfully")) {
                    identifierField.setText(""); amountField.setText("");
                    navigateTo("MenuPanel");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid amount.");
            }
        }, 
        createFormLabel(identifierLabel), identifierField, 
        createFormLabel(LanguageManager.get("AMOUNT_LABEL")), amountField);
    }

    private JPanel createPinChangePanel() {
        GradientPanel bg = new GradientPanel();
        bg.setLayout(new BorderLayout());
        bg.add(new HeaderPanel("PIN_CHANGE_HEADER_SUB", "PIN_CHANGE_TITLE"), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(40, 0));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(24, 45, 20, 45));

        JPanel leftSide = new JPanel();
        leftSide.setLayout(new BoxLayout(leftSide, BoxLayout.Y_AXIS));
        leftSide.setOpaque(false);

        JLabel mainTitle = new JLabel("<html><b style='font-size:36px; font-family:\"Nirmala UI\";'>" + LanguageManager.get("CHOOSE_NEW_PIN") + "</b></html>");
        mainTitle.setForeground(Color.WHITE);
        mainTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel descLabel = new JLabel("<html>" + LanguageManager.get("PIN_CHANGE_DESC") + "</html>");
        descLabel.setFont(new Font("Nirmala UI", Font.PLAIN, 12));
        descLabel.setForeground(TEXT_MUTED);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        leftSide.add(mainTitle);
        leftSide.add(Box.createRigidArea(new Dimension(0, 10)));
        leftSide.add(descLabel);
        leftSide.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel inputRow = new JPanel(new GridLayout(1, 2, 20, 0));
        inputRow.setOpaque(false);
        inputRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputRow.setMaximumSize(new Dimension(450, 90));
        inputRow.setPreferredSize(new Dimension(450, 90));
        
        StringBuilder oldPinStr = new StringBuilder();
        StringBuilder newPinStr = new StringBuilder();
        boolean[] isOldActive = {true}; 

        PinInputBox oldPinBox = new PinInputBox(LanguageManager.get("OLD_PIN"), oldPinStr, () -> {
            // Nothing specific on old pin complete, focus just shifts
        });
        PinInputBox newPinBox = new PinInputBox(LanguageManager.get("NEW_PIN"), newPinStr, () -> {
            // Nothing specific on new pin complete
        });
        
        oldPinBox.setActiveBox(true);
        newPinBox.setActiveBox(false);

        // Keep both PIN input boxes in an array for keypad handling.
        PinInputBox[] boxes = {oldPinBox, newPinBox};

        MouseAdapter switcher = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getSource() == oldPinBox) {
                    isOldActive[0] = true;
                    oldPinBox.setActiveBox(true); newPinBox.setActiveBox(false);
                } else {
                    isOldActive[0] = false;
                    oldPinBox.setActiveBox(false); newPinBox.setActiveBox(true);
                }
            }
        };
        oldPinBox.addMouseListener(switcher);
        newPinBox.addMouseListener(switcher);

        inputRow.add(oldPinBox);
        inputRow.add(newPinBox);
        leftSide.add(inputRow);
        leftSide.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel buttonRow = new JPanel(new GridLayout(1, 2, 20, 0));
        buttonRow.setOpaque(false);
        buttonRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonRow.setMaximumSize(new Dimension(450, 60));
        buttonRow.setPreferredSize(new Dimension(450, 60));
        
        JButton changePinBtn = createMenuButton("BTN_CHANGE_PIN", "BTN_CHANGE_PIN_SUB", BtnType.PRIMARY, e -> {
            if(oldPinStr.length() < 4 || newPinStr.length() < 4) {
                JOptionPane.showMessageDialog(frame, "Please enter 4 digits for both fields.");
                return;
            }
            String result = bank.changePassword(oldPinStr.toString(), newPinStr.toString());
            JOptionPane.showMessageDialog(frame, result);
            if (result.contains("successfully")) {
                oldPinStr.setLength(0); newPinStr.setLength(0);
                navigateTo("MenuPanel");
            } else {
                oldPinStr.setLength(0); newPinStr.setLength(0);
                isOldActive[0] = true;
                oldPinBox.setActiveBox(true); newPinBox.setActiveBox(false);
                oldPinBox.repaint(); newPinBox.repaint();
            }
        });
        
        JButton backBtn = createMenuButton("BACK", "BTN_BACK_SUB", BtnType.SECONDARY, e -> {
            oldPinStr.setLength(0); newPinStr.setLength(0);
            navigateTo("MenuPanel");
        });
        
        buttonRow.add(changePinBtn);
        buttonRow.add(backBtn);
        leftSide.add(buttonRow);
        leftSide.add(Box.createVerticalGlue()); 

        JPanel rightSide = new JPanel(new GridBagLayout());
        rightSide.setOpaque(false);
        
        RoundedPanel keypadWrapper = new RoundedPanel(new Color(35, 50, 40, 200), 25);
        keypadWrapper.setLayout(new BorderLayout());
        keypadWrapper.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        JPanel keypadGrid = new JPanel(new GridLayout(4, 3, 10, 10));
        keypadGrid.setOpaque(false);
        keypadGrid.setPreferredSize(new Dimension(300, 280));
        
        String[] keys = {"1","2","3","4","5","6","7","8","9","DEL","0","CLR"};
        for(String k : keys) {
            JButton kb = createPhotoStyleKeypadBtn(k, !k.equals("CLR") && !k.equals("DEL"));
            kb.addActionListener(e -> {
                PinInputBox activeBox = boxes[0].isActiveBox() ? boxes[0] : boxes[1];
                StringBuilder activeBuffer = boxes[0].isActiveBox() ? oldPinStr : newPinStr;
                
                SoundEffectSystem.playBeep();
                
                if (k.equals("CLR")) {
                    activeBuffer.setLength(0);
                } else if (k.equals("DEL")) {
                    if (activeBuffer.length() > 0) activeBuffer.setLength(activeBuffer.length() - 1);
                } else {
                    if (activeBuffer.length() < 4) {
                        activeBuffer.append(k);
                        if (activeBuffer.length() == 4 && boxes[0].isActiveBox()) {
                            boxes[0].setActiveBox(false);
                            boxes[1].setActiveBox(true);
                        }
                    }
                }
                activeBox.repaint();
            });
            keypadGrid.add(kb);
        }
        
        keypadWrapper.add(keypadGrid, BorderLayout.CENTER);
        rightSide.add(keypadWrapper);

        centerPanel.add(leftSide, BorderLayout.CENTER);
        centerPanel.add(rightSide, BorderLayout.EAST);
        bg.add(centerPanel, BorderLayout.CENTER);

        return bg;
    }

    private JPanel createHistoryPanel() {
        GradientPanel bg = new GradientPanel();
        bg.setLayout(new BorderLayout());
        bg.add(new HeaderPanel("TRANSACTION_HISTORY", "TRANSACTION_HISTORY"), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(30, 60, 30, 60));

        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> historyList = new JList<>(listModel);
        historyList.setFont(new Font("Monospaced", Font.PLAIN, 14));
        historyList.setBackground(DARK_PANEL);
        historyList.setForeground(TEXT_WHITE);

        JScrollPane scrollPane = new JScrollPane(historyList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        centerPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 15));
        bottomPanel.setOpaque(false);
        bottomPanel.add(createMenuButton("BACK", "BACK", BtnType.DANGER, e -> navigateTo("MenuPanel")));

        centerPanel.add(bottomPanel, BorderLayout.SOUTH);
        bg.add(centerPanel, BorderLayout.CENTER);

        bg.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                listModel.clear();
                if(bank.getLoggedInAccount() != null) {
                    for (String log : bank.getLoggedInAccount().getHistory()) {
                        listModel.addElement(log);
                    }
                    if(listModel.isEmpty()) listModel.addElement("No recent transactions.");
                }
            }
        });
        return bg;
    }

    private JPanel createFormPanel(String subtitleKey, String titleKey, String actionBtnTxtKey, Runnable onAction, JComponent... inputs) {
        GradientPanel bg = new GradientPanel();
        bg.setLayout(new BorderLayout());
        bg.add(new HeaderPanel(subtitleKey, titleKey), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        
        RoundedPanel formCard = new RoundedPanel(DARK_PANEL, 20);
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(new EmptyBorder(40, 40, 40, 40));
        formCard.setPreferredSize(new Dimension(500, 400));

        for (JComponent comp : inputs) {
            comp.setAlignmentX(Component.LEFT_ALIGNMENT);
            formCard.add(comp);
            formCard.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        
        formCard.add(Box.createVerticalGlue());
        
        JPanel btnRow = new JPanel(new GridLayout(1, 2, 10, 0));
        btnRow.setOpaque(false);
        btnRow.add(createMenuButton(actionBtnTxtKey, "CONFIRM", BtnType.PRIMARY, e -> onAction.run()));
        btnRow.add(createMenuButton("CANCEL", "CANCEL", BtnType.DANGER, e -> navigateTo("MenuPanel")));
        btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        formCard.add(btnRow);

        centerPanel.add(formCard);
        bg.add(centerPanel, BorderLayout.CENTER);
        return bg;
    }

    private JLabel createFormLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Nirmala UI", Font.BOLD, 13));
        lbl.setForeground(TEXT_MUTED);
        return lbl;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(400, 40));
        field.setFont(new Font("Nirmala UI", Font.BOLD, 16));
        field.setBackground(KEYPAD_LIGHT);
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setMaximumSize(new Dimension(400, 40));
        field.setFont(new Font("Nirmala UI", Font.BOLD, 16));
        field.setBackground(KEYPAD_LIGHT);
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return field;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Num1::new);
    }
}