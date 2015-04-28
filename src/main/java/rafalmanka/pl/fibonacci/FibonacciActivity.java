package rafalmanka.pl.fibonacci;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigInteger;


public class FibonacciActivity extends ActionBarActivity implements View.OnClickListener {

    private static final int TIMES = 100;
    private TextView txtOutput;
    private TextView txtErrorOutput;

    private static boolean mIsRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fibonacci);

        Button btnRunFibonacci = (Button) findViewById(R.id.btnRunFibonacci);
        btnRunFibonacci.setOnClickListener(this);

        txtOutput = (TextView) findViewById(R.id.txtOutput);
        txtErrorOutput = (TextView) findViewById(R.id.txtErrorOutput);
    }

    public static BigInteger fibonacciLoop(BigInteger number) {
        if (number.equals(new BigInteger("1")) || number.equals(new BigInteger("2"))) {
            return new BigInteger("1");
        }
        BigInteger fibo1 = new BigInteger("1"), fibo2 = new BigInteger("1"), fibonacci = new BigInteger("1");
        for (BigInteger i = new BigInteger("3"); i.compareTo(number) <= 0; i = i.add(new BigInteger("1"))) {
            fibonacci = fibo1.add(fibo2);
            fibo1 = fibo2;
            fibo2 = fibonacci;

        }
        return fibonacci;
    }

    private class MyAsyncTask extends AsyncTask<Integer, BigInteger, Integer> {

        private Exception exception;

        @Override
        protected void onProgressUpdate(BigInteger... values) {
            super.onProgressUpdate(values);
            txtOutput.setText(txtOutput.getText().toString() + values[0] + "\n");
        }

        @Override
        protected Integer doInBackground(Integer... times) {
            mIsRunning = true;
            try {
                for (int i = 1; i <= times[0]; i++) {
                    BigInteger out = fibonacciLoop(new BigInteger(String.valueOf(i)));
                    if ((i % 2) == 0) {
                        publishProgress(out);
                    }
                }
            } catch (Exception e) {
                this.exception = e;
                cancel(true);
            }

            return 0;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (exception != null) {
                txtErrorOutput.setText(exception.getMessage());
            }
            mIsRunning = false;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            mIsRunning = false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRunFibonacci:
                if (mIsRunning) {
                    return;
                }
                txtOutput.setText("");
                new MyAsyncTask().execute(TIMES);
                break;
        }
    }
}
