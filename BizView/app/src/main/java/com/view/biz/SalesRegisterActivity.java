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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class SalesRegisterActivity extends AppCompatActivity {

    HashMap<String,String>Profitlosslist;
    ListView listview;
    ArrayList<HashMap<String, String>> arrarycashbalencelist;
    HashMap<String, String> arrarycashbalencelistdata;
    ArrayList<HashMap<String, String>> arraryamountlist;
    HashMap<String, String> arraryamountlistdata;
    ImageView img_calender;
    TextView txt_date;
    String Currentdate,Lastdate;
    LinearLayout len_toolbar;
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
        Lastdate = df.format(calendar.getTime());

        System.out.println("Last Day : " + lastDay);


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
            new SendProfitLoss().execute(CreateRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String CreateRequest()
    {
        String TXML = null;

        TXML = "<ENVELOPE>" +
                "<HEADER>" +
                "<TALLYREQUEST>Export Data</TALLYREQUEST>" +
                "</HEADER>" +
                "<BODY>" +
                "<EXPORTDATA>" +
                "<REQUESTDESC>" +
                "<STATICVARIABLES>" +
                "<VOUCHERTYPENAME>Sales</VOUCHERTYPENAME>" +
                "<SVEXPORTFORMAT>$$SysName:XML</SVEXPORTFORMAT>" +
                "<COLUMNARDAYBOOK>Yes</COLUMNARDAYBOOK>" +
                "<SVCOLUMNTYPE>$$SysName:AllItems</SVCOLUMNTYPE>" +
                "</STATICVARIABLES>" +
                "<REPORTNAME>Voucher Register</REPORTNAME>" +
                "</REQUESTDESC>" +
                "</EXPORTDATA>" +
                "</BODY>" +
                "</ENVELOPE>";




      /*  TXML = "<ENVELOPE>"
                + "<HEADER><TALLYREQUEST>Export Data</TALLYREQUEST></HEADER>"
                + "<BODY><EXPORTDATA>" + "<REQUESTDESC>" +
                "<STATICVARIABLES>" +
                "<SVEXPORTFORMAT>$$SysName:XML</SVEXPORTFORMAT>" +
                "</STATICVARIABLES>" +
                "<REPORTNAME>Balance Sheet</REPORTNAME>" +
                "</REQUESTDESC>" +
                "</EXPORTDATA>" +
                "</BODY>"
                + "</ENVELOPE>";
*/
        return TXML;
    }

    public class SendProfitLoss extends AsyncTask<String, Void, String> {

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
                    arrarycashbalencelistdata.put("DSPDISPNAME", jObj.getString("BILLPARTY"));
                    arrarycashbalencelistdata.put("BSMAINAMT", jObj.getString("BILLDATE"));
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

                QuestionsAdapter questionsAdapter=new QuestionsAdapter(SalesRegisterActivity.this,arrarycashbalencelist);
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



}
