package com.example.myapplication;
import java.math.BigInteger;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText etNumber;
    private Spinner spinnerAlgorithm;
    private TextView tvResult, tvTime;
    static final int PRECOMPUTED_SIZE = 512;
    static BigInteger PRECOMPUTED[] = new BigInteger[PRECOMPUTED_SIZE];

    static {
        PRECOMPUTED[0] = BigInteger.ZERO;
        PRECOMPUTED[1] = BigInteger.ONE;
        for (int i = 2; i < PRECOMPUTED_SIZE; i++) {
            PRECOMPUTED[i] = PRECOMPUTED[i - 1].add(PRECOMPUTED[i - 2]);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNumber = findViewById(R.id.etNumber);
        spinnerAlgorithm = findViewById(R.id.spinnerAlgorithm);
        tvResult = findViewById(R.id.tvResult);
        tvTime = findViewById(R.id.tvTime);

        Button btnCalculate = findViewById(R.id.btnCalculate);

        // Set up the Spinner with algorithm options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.algorithm_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAlgorithm.setAdapter(adapter);

        // Button click listener to calculate Fibonacci
        btnCalculate.setOnClickListener(view -> {
            int n = Integer.parseInt(etNumber.getText().toString());
            String selectedAlgorithm = spinnerAlgorithm.getSelectedItem().toString();

            long startTime = System.nanoTime();
            String result = "";

            switch (selectedAlgorithm) {
                case "Đệ quy":
                    result = String.valueOf(computeRecursively(n));
                    break;
                case "Đệ quy với vòng lặp":
                    result = String.valueOf(computeRecursivelyWithLoop(n));
                    break;
                case "Duyệt lặp":
                    result = String.valueOf(computeIteratively(n));
                    break;
                case "Duyệt lặp nhanh hơn":
                    result = String.valueOf(computeIterativelyFaster(n));
                    break;
                case "Lặp nhanh bằng BigInteger":
                    result = String.valueOf(computeIterativelyFasterUsingBigInteger(n));
                    break;
                case "Đệ quy nhanh bằng BigInteger":
                    result = String.valueOf(computeRecursivelyFasterUsingBigInteger(n));
                    break;
                case "Phân bổ với BigInteger":
                    result = String.valueOf(computeRecursivelyFasterUsingBigIntegerAllocations(n));
                    break;
                case "Đệ quy nhanh với BigInteger và nguyên thuỷ":
                    result = String.valueOf(computeRecursivelyFasterUsingBigIntegerAndPrimitive(n));
                    break;
                case "Đệ quy với BigInteger và dùng bảng tra cứu":
                    result = String.valueOf(computeRecursivelyFasterUsingBigIntegerAndTable(n));
                    break;
            }

            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1_000_000;

            tvResult.setText("Kết quả: " + result);
            tvTime.setText("Thời gian thực thi: " + duration + " ms");

        });
    }

    // Recursive Fibonacci calculation
    private long computeRecursively(int n) {
        if (n <= 1) return n;
        return computeRecursively(n - 1) + computeRecursively(n - 2);
    }

    // Recursive with loop Fibonacci calculation
    public static long computeRecursivelyWithLoop (int n) {
        if (n > 1) {
            long result = 1;
            do {
                result += computeRecursivelyWithLoop(n-2);
                n--;
            } while (n > 1);
            return result;
        }
        return n;
    }


    // Iterative Fibonacci calculation
    public static long computeIteratively (int n) {
        if (n > 1) {
            long a = 0, b = 1;
            do {
                long tmp = b;
                b += a;
                a = tmp;
            } while (--n > 1);
            return b;
        }
        return n;
    }


    // Faster iterative Fibonacci calculation
    public static long computeIterativelyFaster (int n) {
        if (n > 1) {
            long a, b = 1;
            n--;
            a = n & 1;
            n /= 2;
            while (n-- > 0) {
                a += b;
                b += a;
            }
            return b;
        }
        return n;
    }

    public static BigInteger computeIterativelyFasterUsingBigInteger (int n)
    {
        if (n > 1) {
            BigInteger a, b = BigInteger.ONE;
            n--;
            a = BigInteger.valueOf(n & 1);
            n /= 2;
            while (n-- > 0) {
                a = a.add(b);
                b = b.add(a);
            }
            return b;
        }
        return (n == 0) ? BigInteger.ZERO : BigInteger.ONE;
    }

    public static BigInteger computeRecursivelyFasterUsingBigInteger(int n) {
        if (n > 1) {
            int m = (n / 2) + (n & 1);
            BigInteger fM = computeRecursivelyFasterUsingBigInteger(m);
            BigInteger fM_1 = computeRecursivelyFasterUsingBigInteger(m - 1);
            if ((n & 1) == 1) {
                // F(m)^2 + F(m-1)^2
                return fM.pow(2).add(fM_1.pow(2));
            } else {
                // (2*F(m-1) + F(m)) * F(m)
                return fM_1.shiftLeft(1).add(fM).multiply(fM);
            }
        }
        return (n == 0) ? BigInteger.ZERO : BigInteger.ONE;
    }

    public static long computeRecursivelyFasterUsingBigIntegerAllocations(int n) {
        long allocations = 0;
        if (n > 1) {
            int m = (n / 2) + (n & 1);
            allocations += computeRecursivelyFasterUsingBigIntegerAllocations(m);
            allocations += computeRecursivelyFasterUsingBigIntegerAllocations(m - 1);
            allocations += 3;
        }
        return allocations;
    }

    public static BigInteger computeRecursivelyFasterUsingBigIntegerAndPrimitive(int n) {
        if (n > 92) {
            int m = (n / 2) + (n & 1);
            BigInteger fM = computeRecursivelyFasterUsingBigIntegerAndPrimitive(m);
            BigInteger fM_1 = computeRecursivelyFasterUsingBigIntegerAndPrimitive(m -1);
            if ((n & 1) == 1) {
                return fM.pow(2).add(fM_1.pow(2));
            } else {
                return fM_1.shiftLeft(1).add(fM).multiply(fM); // shiftLeft(1) to multiply by 2
            }
        }
        return BigInteger.valueOf(computeIterativelyFaster(n));
    }
    public static BigInteger computeRecursivelyFasterUsingBigIntegerAndTable(int n) {
        if (n > PRECOMPUTED_SIZE - 1) {
            int m = (n / 2) + (n & 1);
            BigInteger fM = computeRecursivelyFasterUsingBigIntegerAndTable(m);
            BigInteger fM_1 = computeRecursivelyFasterUsingBigIntegerAndTable(m - 1);
            if ((n & 1) == 1) {
                return fM.pow(2).add(fM_1.pow(2));
            } else {
                return fM_1.shiftLeft(1).add(fM).multiply(fM);
            }
        }
        return PRECOMPUTED[n];
    }



}
