package algo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ClassicSymmetric {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String caesarEncrypt(String text, String shift) {
        int _shift = Integer.parseInt(shift);
        StringBuilder result = new StringBuilder();

        for (char character : text.toCharArray()) {
            if (ALPHABET.indexOf(character) != -1) {
                char base = Character.isLowerCase(character) ? 'a' : 'A';
                char shifted = (char) ((character - base + _shift) % 26 + base);
                result.append(shifted);
            } else {
                result.append(character);
            }
        }
        return result.toString();
    }

    public static String caesarDecrypt(String cipher, String shift) {
        int _shift = Integer.parseInt(shift);
        return caesarEncrypt(cipher, Integer.toString(26 - (_shift % 26)));
    }

    public static String substitutionEncrypt(String text, String key) {
        // Đảm bảo khóa có độ dài bằng độ dài bảng chữ cái
        StringBuilder fullKey = new StringBuilder();
        while (fullKey.length() < ALPHABET.length()) {
            fullKey.append(key);
        }
        key = fullKey.substring(0, ALPHABET.length()); // Cắt khóa đúng độ dài bảng chữ cái

        // Mã hóa
        StringBuilder result = new StringBuilder();
        for (char character : text.toCharArray()) {
            int index = ALPHABET.indexOf(character); // Tìm vị trí trong ALPHABET
            if (index != -1) { // Nếu ký tự có trong bảng chữ cái
                result.append(key.charAt(index)); // Thay thế bằng ký tự ở vị trí tương ứng trong key
            } else {
                result.append(character); // Giữ nguyên ký tự không phải chữ cái
            }
        }
        return result.toString();
    }

    public static String substitutionDecrypt(String cipher, String key) {
        // Đảm bảo khóa có độ dài bằng độ dài bảng chữ cái
        StringBuilder fullKey = new StringBuilder();
        while (fullKey.length() < ALPHABET.length()) {
            fullKey.append(key);
        }
        key = fullKey.substring(0, ALPHABET.length()); // Cắt khóa đúng độ dài bảng chữ cái

        // Giải mã
        StringBuilder result = new StringBuilder();
        for (char character : cipher.toCharArray()) {
            int index = key.indexOf(character); // Tìm vị trí trong key
            if (index != -1) { // Nếu ký tự có trong key
                result.append(ALPHABET.charAt(index)); // Thay thế bằng ký tự ở vị trí tương ứng trong ALPHABET
            } else {
                result.append(character); // Giữ nguyên ký tự không phải chữ cái
            }
        }
        return result.toString();
    }

    public static String vigenereEncrypt(String text, String key) {
        StringBuilder result = new StringBuilder();
        int keyIndex = 0;

        for (char character : text.toCharArray()) {
            if (ALPHABET.indexOf(character) != -1) {
                int originalIndex = ALPHABET.indexOf(character);
                int shiftedIndex = (originalIndex + ALPHABET.indexOf(key.charAt(keyIndex))) % ALPHABET.length();
                result.append(ALPHABET.charAt(shiftedIndex));
                keyIndex = (keyIndex + 1) % key.length();
            } else {
                result.append(character);
            }
        }
        return result.toString();
    }

    public static String vigenereDecrypt(String cipher, String key) {
        StringBuilder result = new StringBuilder();
        int keyIndex = 0;

        for (char character : cipher.toCharArray()) {
            if (ALPHABET.indexOf(character) != -1) {
                int originalIndex = ALPHABET.indexOf(character);
                int shiftedIndex = (originalIndex - ALPHABET.indexOf(key.charAt(keyIndex)) + ALPHABET.length())
                        % ALPHABET.length();
                result.append(ALPHABET.charAt(shiftedIndex));
                keyIndex = (keyIndex + 1) % key.length();
            } else {
                result.append(character);
            }
        }
        return result.toString();
    }

    public static String affineEncrypt(String text, String a, String b) {
        int _a = Integer.parseInt(a);
        int _b = Integer.parseInt(b);
        StringBuilder result = new StringBuilder();

        for (char character : text.toCharArray()) {
            if (ALPHABET.indexOf(character) != -1) {
                int originalIndex = ALPHABET.indexOf(character);
                int shiftedIndex = (_a * originalIndex + _b) % ALPHABET.length();
                result.append(ALPHABET.charAt(shiftedIndex));
            } else {
                result.append(character);
            }
        }
        return result.toString();
    }

    public static String affineDecrypt(String cipher, String a, String b) {
        int _a = Integer.parseInt(a);
        int _b = Integer.parseInt(b);
        int _aInv = 0;
        for (int i = 0; i < ALPHABET.length(); i++) {
            if ((_a * i) % ALPHABET.length() == 1) {
                _aInv = i;
                break;
            }
        }
        return affineEncrypt(cipher, Integer.toString(_aInv), Integer.toString(ALPHABET.length() - _b));
    }

    // Kiểm tra xem ma trận có khả năng nghịch đảo (tính định thức modulo 26 khác 0)
    private static boolean isInvertible(int[][] matrix, int mod) {
        return (modDeterminant(matrix, mod) != 0);
    }

    // Chuyển đổi ký tự thành chỉ số trong bảng chữ cái
    private static int charToIndex(char c) {
        return ALPHABET.indexOf(Character.toUpperCase(c));
    }

    // Tính định thức của ma trận modulo 26
    private static int modDeterminant(int[][] matrix, int mod) {
        int determinant = (matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]) % mod;
        if (determinant < 0)
            determinant += mod; // Đảm bảo giá trị dương
        return determinant;
    }

    // Tính nghịch đảo modulo m
    private static int modInverse(int a, int mod) {
        for (int i = 1; i < mod; i++) {
            if ((a * i) % mod == 1)
                return i;
        }
        return 1; // Trả về 1 nếu không tìm thấy nghịch đảo (điều này sẽ không xảy ra trong trường
                  // hợp khóa hợp lệ)
    }

    // Chuyển đổi chuỗi khóa thành ma trận
    private static int[][] parseKeyToMatrix(String key) {
        int[][] matrix = new int[2][2];
        matrix[0][0] = charToIndex(key.charAt(0));
        matrix[0][1] = charToIndex(key.charAt(1));
        matrix[1][0] = charToIndex(key.charAt(2));
        matrix[1][1] = charToIndex(key.charAt(3));
        return matrix;
    }

    public static String hillEncrypt(String text, String key) {
        // Đảm bảo rằng văn bản có độ dài chẵn, nếu không, thêm một ký tự mặc định (ví dụ: dấu cách)
        if (text.length() % 2 != 0) {
            text += " "; // Thêm dấu cách nếu độ dài lẻ
        }

        int[][] matrix = parseKeyToMatrix(key);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i += 2) {
            int[] vector = new int[] { ALPHABET.indexOf(text.charAt(i)), ALPHABET.indexOf(text.charAt(i + 1)) };

            // Kiểm tra nếu ký tự trong văn bản nằm trong bảng chữ cái
            if (vector[0] == -1 || vector[1] == -1) {
                throw new IllegalArgumentException("Text contains characters not in alphabet.");
            }

            int[] encryptedVector = multiplyMatrix(matrix, vector);
            result.append(ALPHABET.charAt(encryptedVector[0])).append(ALPHABET.charAt(encryptedVector[1]));
        }
        return result.toString();
    }

    // Hàm giải mã Hill
    public static String hillDecrypt(String cipher, String key) {
        int[][] matrix = parseKeyToMatrix(key);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < cipher.length(); i += 2) {
            int[] vector = new int[] { ALPHABET.indexOf(cipher.charAt(i)), ALPHABET.indexOf(cipher.charAt(i + 1)) };
            
            // Kiểm tra nếu ký tự trong cipher nằm trong bảng chữ cái
            if (vector[0] == -1 || vector[1] == -1) {
                throw new IllegalArgumentException("Cipher contains characters not in alphabet.");
            }

            int[] decryptedVector = multiplyMatrix(inverseMatrix(matrix), vector);
            result.append(ALPHABET.charAt(decryptedVector[0])).append(ALPHABET.charAt(decryptedVector[1]));
        }
        return result.toString();
    }

    // Hàm tính nghịch đảo ma trận
    private static int[][] inverseMatrix(int[][] matrix) {
        int det = matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        det = (det + 26) % 26;

        // Tính nghịch đảo của định thức mod 26
        int detInv = modInverse(det, 26);

        // Tính ma trận nghịch đảo
        int[][] inverse = new int[2][2];
        inverse[0][0] = matrix[1][1] * detInv % 26;
        inverse[0][1] = -matrix[0][1] * detInv % 26;
        inverse[1][0] = -matrix[1][0] * detInv % 26;
        inverse[1][1] = matrix[0][0] * detInv % 26;

        // Đảm bảo các giá trị không âm
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (inverse[i][j] < 0) inverse[i][j] += 26;
            }
        }
        return inverse;
    }

    private static int[] multiplyMatrix(int[][] matrix, int[] vector) {
        int[] result = new int[] { matrix[0][0] * vector[0] + matrix[0][1] * vector[1],
                matrix[1][0] * vector[0] + matrix[1][1] * vector[1] };
        return result;
    }

    public static String generateCaesarKey() {
        Random rd = new Random();
        return Integer.toString(rd.nextInt(27));
    }

    public static String generateSubstitutionKey() {
        List<Character> alphabetList = new ArrayList<>();
        for (char c : ALPHABET.toCharArray()) {
            alphabetList.add(c);
        }
        Collections.shuffle(alphabetList);
        StringBuilder key = new StringBuilder();
        for (char c : alphabetList) {
            key.append(c);
        }
        return key.toString();
    }

    public static String generateVigenereKey(int length) {
        Random random = new Random();
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(ALPHABET.length());
            key.append(ALPHABET.charAt(index));
        }
        return key.toString();
    }

    public static String generateAffineKey() {
        Random random = new Random();
        int a, b;
        do {
            a = random.nextInt(ALPHABET.length());
        } while (gcd(a, ALPHABET.length()) != 1); // Ensure 'a' is coprime with alphabet length
        b = random.nextInt(ALPHABET.length());
        return a + "," + b;
    }

    // Tạo khóa Hill Cipher hợp lệ (ma trận 2x2)
    public static String generateHillKey() {
        Random rand = new Random();
        int[][] matrix = new int[2][2];

        // Tiến hành tạo ma trận ngẫu nhiên cho tới khi ma trận khả nghịch
        while (true) {
            // Tạo các phần tử ma trận ngẫu nhiên trong phạm vi [0, 25]
            matrix[0][0] = rand.nextInt(26);
            matrix[0][1] = rand.nextInt(26);
            matrix[1][0] = rand.nextInt(26);
            matrix[1][1] = rand.nextInt(26);

            // Kiểm tra xem ma trận có khả nghịch không
            if (isInvertible(matrix, 26)) {
                break;
            }
        }

        // Chuyển ma trận thành khóa Hill
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                key.append(ALPHABET.charAt(matrix[i][j]));
            }
        }
        return key.toString();
    }

    private static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }
}
