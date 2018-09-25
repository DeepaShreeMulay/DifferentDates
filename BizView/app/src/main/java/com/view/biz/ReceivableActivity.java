package com.view.biz;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.view.biz.adapter.QuestionsAdapter;
import com.view.biz.utils.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.provider.Settings.System.DATE_FORMAT;


public class ReceivableActivity extends AppCompatActivity {

    HashMap<String,String>Profitlosslist;
    ListView listview;
    ArrayList<HashMap<String, String>> arrarycashbalencelist;
    HashMap<String, String> arrarycashbalencelistdata;
    ArrayList<HashMap<String, String>> arraryamountlist;
    HashMap<String, String> arraryamountlistdata;
    ImageView img_calender;
    TextView txt_date;
    String Currentdate,Lastdate,Calculatedate;
    LinearLayout len_toolbar;
    public static final String DATE_FORMAT = "dd-MMM-yyyy";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profiloss_lay);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Calendar calendar = Calendar.getInstance();
        int lastDate = calendar.getActualMaximum(Calendar.DATE);

        calendar.set(Calendar.DATE, lastDate);
        int lastDay = calendar.get(Calendar.DAY_OF_WEEK);

        System.out.println("Last Date: " + calendar.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yy");
        SimpleDateFormat df2 = new SimpleDateFormat("dd-MMM-yyyy");
        Lastdate = df.format(calendar.getTime());
        Calculatedate=df2.format(calendar.getTime());




        listview= (ListView) findViewById(R.id.listview);
        img_calender= (ImageView) findViewById(R.id.img_calender);
        txt_date= (TextView) findViewById(R.id.txt_date);
        len_toolbar= (LinearLayout) findViewById(R.id.len_toolbar);
        len_toolbar.setVisibility(View.VISIBLE);

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df1 = new SimpleDateFormat("dd MMM yy");
        Currentdate = df1.format(c.getTime());

        txt_date.setText(Currentdate + " to " + Lastdate);
        img_calender.setVisibility(View.VISIBLE);




        arrarycashbalencelist= new ArrayList<HashMap<String, String>>();

        arraryamountlistdata=new HashMap<>();
        arraryamountlist = new ArrayList<HashMap<String, String>>();

        try {
            new SendReceivable().execute(CreateRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    public String CreateRequest()
//    {
//        String TXML = null;
//
//        TXML = "<ENVELOPE>" +
//                "<HEADER>" +
//                "<TALLYREQUEST>Export Data</TALLYREQUEST>" +
//                "</HEADER>" +
//                "<BODY>" +
//                "<EXPORTDATA>" +
//                "<REQUESTDESC>" +
//                "<REPORTNAME>stock category summary</REPORTNAME>" +
//                "</REQUESTDESC>" +
//                "</EXPORTDATA>" +
//                "</BODY>" +
//                "</ENVELOPE>";
//
//        return TXML;
//    }

    public class SendReceivable extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String Url = Config.Url + "9000";

            String SOAPAction = "";
            String inputLine = "";
            String response = "";

            String Voucher = strings[0];

            try {

                URL url = new URL(Url);
                URLConnection connection = url.openConnection();
                HttpURLConnection httpConn = (HttpURLConnection) connection;

                ByteArrayInputStream bin = new ByteArrayInputStream(Voucher.getBytes());
                ByteArrayOutputStream bout = new ByteArrayOutputStream();

// Copy the SOAP file to the open connection.

                copy(bin, bout);

                byte[] b = bout.toByteArray();

// Set the appropriate HTTP parameters.
                httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
                httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
                httpConn.setRequestProperty("SOAPAction", SOAPAction);
                httpConn.setRequestMethod("POST");
                httpConn.setDoOutput(true);
                httpConn.setDoInput(true);

// Everything's set up; send the XML that was read in to b.
                OutputStream out = httpConn.getOutputStream();
                out.write(b);
                out.close();

// Read the response and write it to standard out.

                InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
                BufferedReader in = new BufferedReader(isr);

                while ((inputLine = in.readLine()) != null) {
                    //System.out.println(inputLine);
                    response = response + inputLine;
                }

                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            JSONObject jsonObj = null;
            try {
                jsonObj = XML.toJSONObject(s);
                arrarycashbalencelist.clear();
                JSONObject jsonObject=jsonObj.getJSONObject("ENVELOPE");
                JSONArray ja_data = jsonObject.getJSONArray("BILLFIXED");
                for(int i=0; i<ja_data.length(); i++) {
                    arrarycashbalencelistdata= new HashMap<String, String>();
                    JSONObject jObj = ja_data.getJSONObject(i);

                    String Billdate=jObj.getString("BILLDATE");
                    long Bill=getDaysBetweenDates(Calculatedate,Billdate);
                    String billcl = jsonObject.getJSONArray("BILLCL").getString(i);
                    String billdue = jsonObject.getJSONArray("BILLDUE").getString(i);
                    String billoverdue = jsonObject.getJSONArray("BILLOVERDUE").getString(i);
                    long Billdue=getDaysBetweenDates(Calculatedate,billdue);
                    arrarycashbalencelistdata.put("DSPDISPNAME", jObj.getString("BILLPARTY") + "\nCredit : " + Bill+" "+"days"+" | "+" ₹ "+billcl+"\nAvg Payment Days :" + Billdue );

//                    if(!rate.equals("")) {
//                        arrarycashbalencelistdata.put("BSMAINAMT", jsonObject.getJSONArray("DSPSTKINFO").getJSONObject(i).
//                                getJSONObject("DSPSTKCL").getString("DSPCLQTY")+ "\nRs. " + rate);
//                    } else {
                        if(!billdue.equals("")) {
                            //arrarycashbalencelistdata.put("BSMAINAMT", "₹" + billcl + "\n" + billdue + "\n₹" + billoverdue);
                           // arrarycashbalencelistdata.put("BSMAINAMT", "₹" + billcl + "\n" + billdue + "\n₹" + billoverdue);

                        } else {
                            arrarycashbalencelistdata.put("BSMAINAMT", "₹"+ billoverdue);
                        }
                    //  JSONArray array = jsonObject.getJSONArray("PLAMT");
                    /*for(int j=0; j<array.length(); j++) {
                       // arraryprofitlosslistdata3= new HashMap<String, String>();
                        JSONObject jObj1 = array.getJSONObject(j);
                        arraryprofitlosslistdata.put("PLSUBAMT", jObj1.getString("PLSUBAMT"));
                        arraryprofitlosslistdata.put("BSMAINAMT", jObj1.getString("BSMAINAMT"));
                        ///arraryprofitlosslist2.add(arraryprofitlosslistdata3);

                    }
*/
                    arrarycashbalencelist.add(arrarycashbalencelistdata);

                    // arraryprofitlosslist.addAll(arraryprofitlosslist2);
                }

                QuestionsAdapter questionsAdapter=new QuestionsAdapter(ReceivableActivity.this,arrarycashbalencelist);
                listview.setAdapter(questionsAdapter);


            } catch (JSONException e) {
                Log.e("JSON exception", e.getMessage());
                e.printStackTrace();
            }

        }
    }
    public static void copy(InputStream in, OutputStream out)
            throws IOException {

// do not allow other threads to read from the
// input or write to the output while copying is
// taking place

        synchronized (in) {
            synchronized (out) {

                byte[] buffer = new byte[256];
                while (true) {
                    int bytesRead = in.read(buffer);
                    if (bytesRead == -1) {
                        break;
                    }
                    out.write(buffer, 0, bytesRead);
                }
            }
        }
    }

    public String CreateRequest()
    {
        String TXML = null;

        TXML = "<ENVELOPE>"
                + "<HEADER><TALLYREQUEST>Export Data</TALLYREQUEST></HEADER>"
                + "<BODY><EXPORTDATA>" + "<REQUESTDESC>" +
                "<STATICVARIABLES>" +
                "<SVFROMDATE>20180401</SVFROMDATE>" +
                "<SVTODATE>20190331</SVTODATE>" +
                "<SVEXPORTFORMAT>$$SysName:XML</SVEXPORTFORMAT>" +
                "</STATICVARIABLES>" +
                "<REPORTNAME>Bills Receivable</REPORTNAME>" +
                "</REQUESTDESC>" +
                "</EXPORTDATA>" +
                "</BODY>"
                + "</ENVELOPE>";

        return TXML;
    }

//    public class SendTal extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... strings) {
//            String Url = "http://10.0.2.2:9000";
//
//            String SOAPAction = "";
//            String inputLine = "";
//            String response = "";
//
//            String Voucher = strings[0];
//
//            try {
//                URL url = new URL(Url);
//                URLConnection connection = url.openConnection();
//                HttpURLConnection httpConn = (HttpURLConnection) connection;
//
//                ByteArrayInputStream bin = new ByteArrayInputStream(Voucher.getBytes());
//                ByteArrayOutputStream bout = new ByteArrayOutputStream();
//
//// Copy the SOAP file to the open connection.
//
//                copy(bin, bout);
//
//                byte[] b = bout.toByteArray();
//
//// Set the appropriate HTTP parameters.
//                httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
//                httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
//                httpConn.setRequestProperty("SOAPAction", SOAPAction);
//                httpConn.setRequestMethod("POST");
//                httpConn.setDoOutput(true);
//                httpConn.setDoInput(true);
//
//// Everything's set up; send the XML that was read in to b.
//                OutputStream out = httpConn.getOutputStream();
//                out.write(b);
//                out.close();
//
//// Read the response and write it to standard out.
//
//                InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
//                BufferedReader in = new BufferedReader(isr);
//
//                while ((inputLine = in.readLine()) != null) {
//                    //System.out.println(inputLine);
//                    response = response + inputLine;
//                }
//
//                in.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return response;
//
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            bs_name.clear();
//            bs_amount.clear();
//
//            try {
//
//                tv1.setText("ANAND ENTERPRISES" + "\n" + "-276096.00 15-Dec-2015" + "\n\n"
//                        + "ANAND ENTERPRISES(BEBADOHAL)"  + "\n" + "-313355.00 26-Dec-2015" + "\n\n"
//                        + "ANAND ENTERPRISES(BEBADOHAL)"  + "\n" + "-19051.00 30-Dec-2015" + "\n\n"
//                        + "ANAND ENTERPRISES(BEBADOHAL)"  + "\n" + "-193346.00 11-Jan-2016" + "\n\n"
//                        + "ANAND ENTERPRISES(BEBADOHAL)"  + "\n" + "-171489.00 18-Jan-2016" + "\n\n"
//                        + "MAHALAXMI INDUSTRIES"  + "\n" + "-70000.00 9-Mar-2016" + "\n\n");
//
//                parser = new XMLParser();
//                String xml = s; // getting XML
//                Document doc = parser.getDomElement(xml); // getting DOM element
//
//                NodeList categoryList = doc.getElementsByTagName("BSNAME");
//                NodeList categoryList2 = doc.getElementsByTagName("BSAMT");
//                for (int categoryNo=0;categoryNo<categoryList.getLength();categoryNo++) {
//
//                    Element categoryNode = (Element)categoryList.item(categoryNo);
//                    Element categoryNode2 = (Element)categoryList2.item(categoryNo);
//                    String subamount = categoryNode2.getElementsByTagName("BSSUBAMT").item(0).getTextContent();
//                    String mainamount = categoryNode2.getElementsByTagName("BSMAINAMT").item(0).getTextContent();
//                    //System.out.println("Category No  " +categoryNo);
//                    //iterate through  <books> tags
//                    NodeList booksList = categoryNode.getElementsByTagName("DSPACCNAME");
//
//                    for (int booksNo=0;booksNo<booksList.getLength();booksNo++) {
//                        Element bookElement = (Element)booksList.item(0);
//                        tv1.setText(tv1.getText().toString() + bookElement.getElementsByTagName("DSPDISPNAME").item(0).getTextContent()
//                                + "\n Main Amount: " + mainamount + "\n Sub Amount: " + subamount + "\n\n");
//
//                    }
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }


    public static long getDaysBetweenDates(String start, String end) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        Date startDate, endDate;
        long numberOfDays = 0;
        try {
            startDate = dateFormat.parse(start);
            endDate = dateFormat.parse(end);
            numberOfDays = getUnitBetweenDates(startDate, endDate, TimeUnit.DAYS);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return numberOfDays;
    }
    private static long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }

}




/*




/*
 */

