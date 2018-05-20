import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 競技プログラミング用スタートアップクラス.
 */
public class StartUp {

    /**
     * 競技プログラミング用エントリーポイント.
     *
     * @param args 未使用
     */
    public static void main(String[] args) {
        File testDirectory = new File("TestData");
        File[] testFiles = testDirectory.listFiles();

        if (testFiles == null) {
            return
                    ;
        }

        for (File testFile : testFiles) {
            List<String> inputDataList = new ArrayList<>();
            List<String> expectDataList = new ArrayList<>();

            try {
                readTestData(testFile, inputDataList, expectDataList);
            } catch (IOException exception) {
                System.out.println("Failed to read test data. FileName=" + testFile.getName());
                continue;
            }

            execute(testFile.getName(), inputDataList, expectDataList);
        }

    }

    /**
     * テストデータを読み込む.
     *
     * @param testFile   テストファイル
     * @param inputList  入力値リスト
     * @param expectList 期待値リスト
     */
    private static void readTestData(File testFile, List<String> inputList, List<String> expectList) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(testFile));

        List<String> targetList = null;
        String line;
        while ((line = reader.readLine()) != null) {
            switch (line) {
                case "[Input]":
                    targetList = inputList;
                    break;
                case "[Expect]":
                    targetList = expectList;
                    break;
                default:
                    targetList.add(line);
                    break;
            }
        }
    }

    /**
     * 実行する.
     *
     * @param testName       テスト名
     * @param inputDataList  入力値リスト.
     * @param expectDataList 期待値リスト.
     */
    private static void execute(String testName, List<String> inputDataList, List<String> expectDataList) {
        InputStream defaultIn = System.in;
        PrintStream defaultOut = System.out;

        // 初期化処理
        InputStreamInterrupter in = new InputStreamInterrupter();
        OutputStreamInterrupter out = new OutputStreamInterrupter();

        System.setIn(in);
        System.setOut(out);

        // 入力データ設定
        for (String inputData : inputDataList) {
            in.addLine(inputData);
        }

        // コード実行
        try {
            Main.main(null);
        } catch (Exception exception) {
            System.out.println(testName + " NG : Exception occurred : " + exception.getMessage());
        }

        // 終了処理
        System.setIn(defaultIn);
        System.setOut(defaultOut);

        // 出力データ
        boolean result = true;
        for (String outputData : expectDataList) {
            String resultData = out.readLine();
            if (!outputData.equals(resultData)) {
                System.out.println(testName + " NG : " + outputData + " != " + resultData);
                result = false;
            }
        }

        if (result) {
            System.out.println(testName + " OK");
        }
    }

    /**
     * 標準入力割り込み用クラス.
     */
    private static class InputStreamInterrupter extends InputStream {
        private StringBuilder inputDataBuilder;

        /*package*/ InputStreamInterrupter() {
            inputDataBuilder = new StringBuilder();
        }

        public int read() {
            int result = -1;
            if (inputDataBuilder.length() > 0) {
                result = inputDataBuilder.charAt(0);
                inputDataBuilder.deleteCharAt(0);
            }
            return result;
        }

        /*package*/ void addLine(String lineData) {
            inputDataBuilder.append(lineData);
            inputDataBuilder.append("\n");
        }
    }

    /**
     * 標準出力割り込み用クラス.
     */
    private static class OutputStreamInterrupter extends PrintStream {
        private BufferedReader buffer = new BufferedReader(new StringReader(""));

        /*package*/ OutputStreamInterrupter() {
            super(new ByteArrayOutputStream());
        }

        /*package*/ String readLine() {
            try {
                String line = "";
                if ((line = buffer.readLine()) != null) {
                    return line;
                } else {
                    buffer = new BufferedReader(new StringReader(out.toString()));
                    ((ByteArrayOutputStream) out).reset();
                    return buffer.readLine();
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
