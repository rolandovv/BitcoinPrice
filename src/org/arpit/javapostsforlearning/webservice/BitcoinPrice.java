package org.arpit.javapostsforlearning.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

public class BitcoinPrice extends TimerTask {

    public static void main(String args[]) {

        //command line arguments
        int duration = 0;
        int interval = 0;
        try {
            // Parse the string argument into an integer value.
            duration = Integer.parseInt(args[0]);
            interval = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException nfe) {
            System.out.println("The argument must be an integer.");
            System.exit(1);
        }

        TimerTask timerTask = new BitcoinPrice();
        //running timer task as daemon thread
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, interval*1000);
        System.out.println("BITCOIN PRICE CALCULATOR");

        //Finished task based on user inputs
        try {
            Thread.sleep(duration*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        timer.cancel();
        System.out.println("\nTask completed -----------------------\n");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        //System.out.println("Bitcoin price at:"+new Date());
        try {
            MyGETRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void MyGETRequest() throws IOException {

        URL urlForGetRequest = new URL("https://api.coindesk.com/v1/bpi/currentprice.json");
        String readLine = null;
        HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
        conection.setRequestMethod("GET");
        conection.setRequestProperty("userId", "a1bcdef"); // set userId its a sample here
        int responseCode = conection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while ((readLine = in .readLine()) != null) {
                response.append(readLine);
            } in .close();

            // print result
            System.out.println("\n--------------------------Bitcoin price at---------------------------");
            JSONObject myResponse = new JSONObject(response.toString());

            JSONObject time = myResponse.getJSONObject("time");
            String updated = time.getString("updated");
            String updateISO = time.getString("updatedISO");
            String updateduk = time.getString("updateduk");

            System.out.println("updated: "+updated);
            System.out.println("updatedISO: "+updateISO);
            System.out.println("updateduk: "+updateduk);

            System.out.println("---------------------------------------------------------------------");
            JSONObject bpi = myResponse.getJSONObject("bpi");

            JSONObject usd = bpi.getJSONObject("USD");
            String usd_code = usd.getString("code");
            String usd_symbol = usd.getString("symbol");
            String usd_rate = usd.getString("rate");
            String usd_description = usd.getString("description");
            double usd_rate_float = usd.getDouble("rate_float");

            JSONObject gbp = bpi.getJSONObject("GBP");
            String gbp_code = gbp.getString("code");
            String gbp_symbol = gbp.getString("symbol");
            String gbp_rate = gbp.getString("rate");
            String gbp_description = gbp.getString("description");
            double gbp_rate_float = gbp.getDouble("rate_float");

            JSONObject eur = bpi.getJSONObject("EUR");
            String eur_code = eur.getString("code");
            String eur_symbol = eur.getString("symbol");
            String eur_rate = eur.getString("rate");
            String eur_description = eur.getString("description");
            double eur_rate_float = eur.getDouble("rate_float");

            System.out.printf("%-8s%-8s%6s%22s%24s\n", "Code", "Symbol", "Rate","Description","Rate_float");
            System.out.printf("%-8s%-8s%13s%24s%13.2f\n", usd_code, "$", usd_rate, usd_description, usd_rate_float);
            System.out.printf("%-8s%-8s%13s%26s%11.2f\n", gbp_code, "£", gbp_rate, gbp_description, gbp_rate_float);
            System.out.printf("%-8s%-8s%13s%8s%29.2f\n", eur_code, "€", eur_rate, eur_description, eur_rate_float);

        } else {
            System.out.println("GET NOT WORKED");
        }
    }

}

